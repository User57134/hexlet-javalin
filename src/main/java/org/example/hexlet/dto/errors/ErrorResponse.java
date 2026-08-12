package org.example.hexlet.dto.errors;

import lombok.Getter;

@Getter
public class ErrorResponse {
  private String title;
  private int status;
  private String description;
  private long timestamp;

  public ErrorResponse(String title, int status, String description) {
      this.title = title;
      this.status = status;
      this.description = description;
      timestamp = System.currentTimeMillis();
  }
}
