package com.todo_app.todo.controller;

import com.todo_app.todo.dto.ToDoPatchDto;
import com.todo_app.todo.dto.ToDoPostDto;
import com.todo_app.todo.dto.ToDoResponseDto;
import com.todo_app.utils.UriCreator;
import com.todo_app.todo.entity.ToDo;
import com.todo_app.todo.mapper.ToDoMapper;
import com.todo_app.todo.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin        // Todo_backend 사이트에서 확인하기 위함
@RestController
@RequestMapping("/v1/todos")
@Validated
public class ToDoController {
    private final static String TODO_DEFAULT_URL = "/v1/todos";
    private ToDoService toDoService;
    private ToDoMapper toDoMapper;


    // Constructor(생성자)
    public ToDoController(ToDoService toDoService, ToDoMapper toDoMapper) {
        this.toDoService = toDoService;
        this.toDoMapper = toDoMapper;
    }

    // Post
    @PostMapping
    public ResponseEntity postToDo(@Valid @RequestBody ToDoPostDto toDoPostDto) {

        ToDo todo = toDoService.createToDo(toDoMapper.ToDoPostDto_ToDo(toDoPostDto));
        URI location = UriCreator.createUri(TODO_DEFAULT_URL, todo.getTodoId());

        return ResponseEntity.created(location).build();
    }

    // Patch
    @PatchMapping("/{todo-id}")
    public ResponseEntity patchToDo(@PathVariable("todo-id") @Positive long todoId,
                                    @Valid @RequestBody ToDoPatchDto toDoPatchDto) {
        toDoPatchDto.setTodoId(todoId);
        ToDo todo = toDoService.updateToDo(toDoMapper.ToDoPatchDto_ToDo(toDoPatchDto));

        return new ResponseEntity<>(toDoMapper.ToDo_ToDoResponseDto(todo), HttpStatus.OK);
    }

    // Get
    @GetMapping("/{todo-id}")
    public ResponseEntity getToDo(@PathVariable("todo-id") @Positive long todoId) {

        ToDo todo = toDoService.findToDo(todoId);

        return new ResponseEntity<>(toDoMapper.ToDo_ToDoResponseDto(todo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllToDo() {

        List<ToDo> todos = toDoService.findAllToDo();

        List<ToDoResponseDto> response = toDoMapper.ToDos_ToDoResponseDtos(todos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{todo-id}")
    public ResponseEntity deleteToDo(@PathVariable("todo-id") @Positive long todoId) {

        toDoService.deleteToDo(todoId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity deleteAllToDo() {

        this.toDoService.deleteAllToDo();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
