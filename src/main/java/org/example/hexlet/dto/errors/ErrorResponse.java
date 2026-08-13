package org.example.hexlet.dto.errors;

import io.javalin.http.Context;
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

  public static void sendErrors(Context ctx, String title, int status, String description) {
      ErrorResponse er = new ErrorResponse(title, status, description);

      ctx.status(status);
      ctx.json(er);
  }
}
