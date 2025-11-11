package com.bajaj.qualifier.service;

import com.bajaj.qualifier.config.AppProperties;
import com.bajaj.qualifier.dto.FinalQueryPayload;
import com.bajaj.qualifier.dto.GenerateWebhookRequest;
import com.bajaj.qualifier.dto.GenerateWebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QualifierRunner implements ApplicationRunner {
	private static final Logger log = LoggerFactory.getLogger(QualifierRunner.class);

	private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
	private static final String DEFAULT_TEST_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

	private final AppProperties props;
	private final WebClient webClient;
	private final ResourceLoader resourceLoader;

	public QualifierRunner(AppProperties props, ResourceLoader resourceLoader, WebClient.Builder webClientBuilder) {
		this.props = props;
		this.resourceLoader = resourceLoader;
		this.webClient = webClientBuilder.build();
	}

	@Override
	public void run(ApplicationArguments args) {
		log.info("Starting Bajaj Finserv Health Qualifier flow");
		try {
			GenerateWebhookRequest request = new GenerateWebhookRequest(props.getName(), props.getRegNo(), props.getEmail());

			GenerateWebhookResponse response = webClient.post()
				.uri(GENERATE_WEBHOOK_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(request))
				.retrieve()
				.bodyToMono(GenerateWebhookResponse.class)
				.block();

			if (response == null) {
				throw new IllegalStateException("Empty response from generateWebhook");
			}

			String webhookUrl = Optional.ofNullable(response.getWebhook()).orElse(DEFAULT_TEST_WEBHOOK_URL);
			String accessToken = response.getAccessToken();
			if (accessToken == null || accessToken.isBlank()) {
				throw new IllegalStateException("accessToken is missing in response");
			}

			boolean isOdd = isRegNoOdd(props.getRegNo());
			String finalQuery = resolveFinalQuery(isOdd);
			if (finalQuery == null || finalQuery.isBlank()) {
				throw new IllegalStateException("finalQuery is empty. Provide it via properties or resource files.");
			}

			log.info("Submitting finalQuery to webhook. Odd?={} webhook={}", isOdd, webhookUrl);

			FinalQueryPayload payload = new FinalQueryPayload(finalQuery.trim());
			String result = webClient.post()
				.uri(webhookUrl)
				.header(HttpHeaders.AUTHORIZATION, accessToken) // Spec shows plain token, no Bearer
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(payload))
				.retrieve()
				.bodyToMono(String.class)
				.onErrorResume(e -> {
					log.error("Submission failed: {}", e.getMessage(), e);
					return Mono.just("Submission failed: " + e.getMessage());
				})
				.block();

			log.info("Submission response: {}", result);
		} catch (Exception e) {
			log.error("Qualifier flow failed: {}", e.getMessage(), e);
		}
	}

	private boolean isRegNoOdd(String regNo) {
		if (regNo == null) return false;
		Matcher m = Pattern.compile("(\\d{2})$").matcher(regNo.replaceAll("\\s+", ""));
		if (m.find()) {
			int lastTwo = Integer.parseInt(m.group(1));
			return (lastTwo % 2) == 1;
		}
		// Fallback: use last digit
		char lastDigit = regNo.charAt(regNo.length() - 1);
		if (Character.isDigit(lastDigit)) {
			return ((lastDigit - '0') % 2) == 1;
		}
		return false;
	}

	private String resolveFinalQuery(boolean isOdd) {
		// Prefer inline property if provided
		if (isOdd && props.getFinalQueryOdd() != null && !props.getFinalQueryOdd().isBlank()) {
			return props.getFinalQueryOdd();
		}
		if (!isOdd && props.getFinalQueryEven() != null && !props.getFinalQueryEven().isBlank()) {
			return props.getFinalQueryEven();
		}
		// Otherwise load from resource path
		String path = isOdd ? props.getFinalQueryOddPath() : props.getFinalQueryEvenPath();
		try {
			Resource resource = resourceLoader.getResource(path);
			if (!resource.exists()) {
				log.warn("Resource not found at {}", path);
				return null;
			}
			try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
				return FileCopyUtils.copyToString(reader);
			}
		} catch (Exception e) {
			log.error("Failed to read SQL from {}: {}", path, e.getMessage());
			return null;
		}
	}
}


