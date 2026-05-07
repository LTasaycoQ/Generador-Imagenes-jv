package pe.edu.vallegrande.imagen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ImageGenerationService {

    @Value("${api.endpoint}")
    private String apiEndpoint;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.host}")
    private String apiHost;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateImage3D(String inputText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-rapidapi-key", apiKey);
        headers.set("x-rapidapi-host", apiHost);

        try {
            // Safe JSON building — no manual string formatting
            Map<String, Object> requestBodyMap = Map.of(
                "messages", List.of(Map.of("role", "user", "content", inputText)),
                "system_prompt", "",
                "temperature", 0.9,
                "top_k", 5,
                "top_p", 0.9,
                "max_tokens", 256,
                "web_access", false
            );

            String requestBody = objectMapper.writeValueAsString(requestBodyMap);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                apiEndpoint,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException(
                    "Unexpected status: " + response.getStatusCode() + " - " + response.getBody()
                );
            }

        } catch (RuntimeException e) {
            throw e; // re-throw as-is
        } catch (Exception e) {
            throw new RuntimeException("Error calling external API: " + e.getMessage(), e);
        }
    }
}