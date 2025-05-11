package com.example.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookAppApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("App started. Running logic...");
		RestTemplate restTemplate = new RestTemplate();

		String registrationUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Amisha Pandit"); 
		requestBody.put("regNo", "REG12347");     
		requestBody.put("email", "amishapandit220378@acropolis.in"); 
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<Map> response = restTemplate.exchange(
					registrationUrl,
					HttpMethod.POST,
					request,
					Map.class
			);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				String webhookUrl = (String) response.getBody().get("webhook");
				String accessToken = (String) response.getBody().get("accessToken");

				// Solve your SQL here
				String finalSqlQuery = "SELECT * FROM your_table;"; // replace with actual query

				// Send to webhook
				submitSqlQuery(webhookUrl, accessToken, finalSqlQuery, restTemplate);
			} else {
				System.out.println("Webhook generation failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void submitSqlQuery(String webhookUrl, String accessToken, String query, RestTemplate restTemplate) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
	
		Map<String, String> body = new HashMap<>();
		body.put("finalQuery", query);
	
		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
	
		try {
			ResponseEntity<String> response = restTemplate.exchange(
					webhookUrl,
					HttpMethod.POST,
					request,
					String.class
			);
	
			System.out.println("Submission Status: " + response.getStatusCode());
			System.out.println("Response Body: " + response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
