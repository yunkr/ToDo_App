package com.todo_app.todo.service;

import com.todo_app.todo.entity.ToDo;
import com.todo_app.todo.repository.ToDoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

// @Service : Service 레이어 클래스들에 사용되는 어노테이션
@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;

    // 생성자(Constructor)
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    // Post
    public ToDo createToDo(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    // Patch
    public ToDo updateToDo(ToDo toDo) {

        // 조회하려는 ToDo가 검증된 ToDo인지 확인(존재하는 ToDo인지 확인 등)
        ToDo findToDo = this.findToDo(toDo.getTodoId());


        // ifPresent()는 Optional 객체가 값을 가지고 있으면 실행 값이 없으면 넘어감, ifPresent() 메소드 = 값을 가지고 있는지 확인 후 예외처리

        Optional.ofNullable(toDo.getTitle())
                .ifPresent(title -> findToDo.setTitle(title));                  // title 확인
        Optional.ofNullable(toDo.getTodoOrder())
                .ifPresent(todoOrder -> findToDo.setTodoOrder(todoOrder));      // todoOrder 확인
        Optional.ofNullable(toDo.getCompleted())
                .ifPresent(completed -> findToDo.setCompleted(completed));      // completed 확인


        return toDoRepository.save(findToDo);
    }

    // Get
    public ToDo findToDo(long todoId) {
        return this.toDoRepository.findById(todoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ToDo> findAllToDo(){
        return this.toDoRepository.findAll();
    }


    // Delete
    public void deleteToDo(long todoId) {
        this.toDoRepository.deleteById(todoId);
    }

    public void deleteAllToDo(){
        this.toDoRepository.deleteAll();
    }

}
