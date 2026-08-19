package org.example.hexlet.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public final class Course {
  private Long id;

  @ToString.Include
  private String name;
  private String description;
  private LocalDateTime createdAt;

  public Course (String name, String description) {
      this.name = name;
      this.description = description;
      createdAt = LocalDateTime.now();
  }

  public Course (Long id, String name, String description) {
      this.id = id;
      this.name = name;
      this.description = description;
      createdAt = LocalDateTime.now();
  }
}


