package com.bajaj.qualifier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bfhl")
public class AppProperties {
	private String name;
	private String regNo;
	private String email;

	// Option A: inline SQLs in properties
	private String finalQueryOdd;
	private String finalQueryEven;

	// Option B: load SQLs from resource files (classpath:/sql/odd.sql etc.)
	private String finalQueryOddPath = "classpath:sql/odd.sql";
	private String finalQueryEvenPath = "classpath:sql/even.sql";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFinalQueryOdd() {
		return finalQueryOdd;
	}

	public void setFinalQueryOdd(String finalQueryOdd) {
		this.finalQueryOdd = finalQueryOdd;
	}

	public String getFinalQueryEven() {
		return finalQueryEven;
	}

	public void setFinalQueryEven(String finalQueryEven) {
		this.finalQueryEven = finalQueryEven;
	}

	public String getFinalQueryOddPath() {
		return finalQueryOddPath;
	}

	public void setFinalQueryOddPath(String finalQueryOddPath) {
		this.finalQueryOddPath = finalQueryOddPath;
	}

	public String getFinalQueryEvenPath() {
		return finalQueryEvenPath;
	}

	public void setFinalQueryEvenPath(String finalQueryEvenPath) {
		this.finalQueryEvenPath = finalQueryEvenPath;
	}
}


