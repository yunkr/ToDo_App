package com.todo_app.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor     // 테스트를 위해 추가됨
public class ToDoPostDto {      // Create

    @NotEmpty(message = "빈칸이 아니어야 합니다.")
    private String title;

    private Long todoOrder;

    private Boolean completed;

}
