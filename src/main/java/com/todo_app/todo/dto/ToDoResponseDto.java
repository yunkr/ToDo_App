package com.todo_app.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor     // 테스트를 위해 추가됨
public class ToDoResponseDto {      // Read

    private Long todoId;

    private String title;

    private Long todoOrder;

    private Boolean completed;

}
