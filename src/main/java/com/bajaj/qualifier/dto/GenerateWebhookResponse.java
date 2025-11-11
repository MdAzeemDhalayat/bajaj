package com.bajaj.qualifier.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateWebhookResponse {
	// Be liberal with possible field names
	@JsonProperty("webhook")
	@JsonAlias({"webhookUrl", "webhookURL", "url"})
	private String webhook;

	@JsonProperty("accessToken")
	@JsonAlias({"token", "jwt"})
	private String accessToken;

	public String getWebhook() {
		return webhook;
	}

	public void setWebhook(String webhook) {
		this.webhook = webhook;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}


