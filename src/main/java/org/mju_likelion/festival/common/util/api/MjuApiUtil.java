package org.mju_likelion.festival.common.util.api;

import static org.mju_likelion.festival.common.exception.type.ErrorType.API_ERROR;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MjuApiUtil {

  private final RestTemplate restTemplate;

  @Value("${mju-user-check-api-uri}")
  private String mjuUserCheckApiUri;

  public boolean doUserCheck(final String studentId, final String password) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("id", studentId);
      body.add("passwrd", password);

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

      ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(mjuUserCheckApiUri,
          request, ApiResponse.class);

      ApiResponse response = responseEntity.getBody();
      if (response != null) {
        return "0000".equals(response.getError());
      }

      throw new InternalServerException(API_ERROR, "Response body is null");
    } catch (Exception e) {
      throw new InternalServerException(API_ERROR, e.getMessage());
    }
  }
}
