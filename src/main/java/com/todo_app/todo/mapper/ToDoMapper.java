package com.todo_app.todo.mapper;

import com.todo_app.todo.dto.ToDoPatchDto;
import com.todo_app.todo.dto.ToDoPostDto;
import com.todo_app.todo.dto.ToDoResponseDto;
import com.todo_app.todo.entity.ToDo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")      // mapper : SQL을 호출하기 위한 인터페이스
public interface ToDoMapper {
    ToDo ToDoPostDto_ToDo(ToDoPostDto toDoPostDto);

    ToDo ToDoPatchDto_ToDo(ToDoPatchDto toDoPatchDto);

    ToDoResponseDto ToDo_ToDoResponseDto(ToDo toDo);

    List<ToDoResponseDto> ToDos_ToDoResponseDtos(List<ToDo> todos);
}
