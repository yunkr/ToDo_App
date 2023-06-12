package com.todo_app.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ToDoPatchDto {     // Update

    private long todoId;

    @NotEmpty(message = "빈칸이 아니어야 합니다.")
    private String title;

    private Long todoOrder;

    private Boolean completed;

    public ToDoPatchDto(String title, Boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }
}
