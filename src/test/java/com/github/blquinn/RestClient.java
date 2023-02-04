package com.github.blquinn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.Assertions;

public class RestClient {
  private final String baseUrl;
  private final ObjectMapper objectMapper;
  OkHttpClient client = new OkHttpClient();

  public RestClient(String baseUrl, ObjectMapper objectMapper) {
    this.baseUrl = baseUrl;
    this.objectMapper = objectMapper;
  }

  <T> T request(String path, String method, @Nullable Object requestBody, TypeReference<T> responseClass, int expectedStatus) {
    try {
      RequestBody body = null;
      if (requestBody != null) {
        body = RequestBody.create(
          MediaType.parse("application/json"),
          objectMapper.writeValueAsString(requestBody)
        );
      }

      var request = new Request.Builder()
          .url(baseUrl + path)
          .method(method, body)
          .build();

      try (var res = client.newCall(request).execute()) {
        Assertions.assertEquals(expectedStatus, res.code());
        if (responseClass == null || responseClass.getType() == Void.class) {
          return null;
        }
        return objectMapper.readValue(res.body().bytes(), responseClass);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  <T> T get(String path, TypeReference<T> responseClass) {
    return get(path, responseClass, 200);
  }

  <T> T get(String path, TypeReference<T> responseClass, int expectedStatus) {
    return request(path, "GET", null, responseClass, expectedStatus);
  }

  <T> T post(String path, Object requestBody, TypeReference<T> responseClass) {
    return post(path, requestBody, responseClass, 200);
  }

  <T> T post(String path, Object requestBody, TypeReference<T> responseClass, int expectedStatus) {
    return request(path, "POST", requestBody, responseClass, expectedStatus);
  }
}
