package com.todo_app.slice;

import com.google.gson.Gson;
import com.todo_app.todo.dto.ToDoPatchDto;
import com.todo_app.todo.dto.ToDoPostDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;


    // Post test
    @Test
    void postToDoTest() throws Exception {

        // given
        ToDoPostDto post = new ToDoPostDto("운동하기", 1L, true);

        String content = gson.toJson(post);        // JSON 변환 라이브러리를 이용해서 객체를 JSON 포맷으로 변환


        // when
        ResultActions actions = mockMvc.perform(

                // HTTP request에 대한 정보
                post("/v1/todos")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        // then
        // andExpect() 메서드를 통해 파라미터로 입력한 매처(Matcher)로 예상되는 기대 결과를 검증
        actions
                .andExpect(status().isCreated())        // response status가 201(Created)인지 매치, 백엔드 측에 리소스 정보가 잘 생성(저장)되었는지를 검증
                .andExpect(header().string("Location", is(startsWith("/v1/todos"))));     // HTTP header에 추가된 Location의 문자열 값이 “/v1/todo”로 시작하는지 검증

    }


    // Patch test
    @Test
    void patchToDoTest() throws Exception {
        // =================================== postToDo()를 이용한 테스트 데이터 생성 시작

        // given
        ToDoPostDto post = new ToDoPostDto("운동하기",1L,false);
        String postContent = gson.toJson(post);         // JSON 변환 라이브러리를 이용해서 객체를 JSON 포맷으로 변환

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/todos")       // HTTP request에 대한 정보
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postToDo()를 이용한 테스트 데이터 생성 끝

        long todoId;
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        todoId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));


        ToDoPatchDto patch = new ToDoPatchDto("개발하기", true);
        patch.setTodoId(todoId);
        String patchContent = gson.toJson(patch);       // JSON 변환 라이브러리를 이용해서 객체를 JSON 포맷으로 변환

        // when / then
        mockMvc.perform(
                        patch("/v1/todos/" + todoId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patchContent)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.completed").value(patch.getCompleted()));


    }


    // Get test
    @Test
    void getToDoTest() throws Exception {
        // =================================== postToDo()를 이용한 테스트 데이터 생성 시작

        // given: ToDoController의 getTitle()를 테스트하기 위해서 postToDo()를 이용해 테스트 데이터를 생성 후, DB에 저장
        ToDoPostDto post = new ToDoPostDto("운동하기",1L,false);
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/todos")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postToDo()를 이용한 테스트 데이터 생성 끝

        long todoId;
        String location = postActions.andReturn().getResponse().getHeader("Location");  // "/v1/todos/1"
        todoId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        // when / then
        mockMvc.perform(
                        get("/v1/members/" + todoId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(post.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(post.getCompleted()));

    }

    // Delete test
    @Test
    void deleteToDoTest() throws Exception {
        // =================================== postToDo()를 이용한 테스트 데이터 생성 시작
        // given
        ToDoPostDto post = new ToDoPostDto("운동하기",1L,false);
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postToDo()를 이용한 테스트 데이터 생성 끝

        long todoId;
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v1/todos/1"
        todoId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        // when / then
        mockMvc.perform(
                        delete("/v1/todos/" + todoId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteToDosTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작

        // given
        ToDoPostDto post1 = new ToDoPostDto("운동하기",1L,false);
        String getContent1 = gson.toJson(post1);

        ResultActions postActions1 =
                mockMvc.perform(
                        post("/v1/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent1)
                );

        ToDoPostDto post2 = new ToDoPostDto("개발하기",2L,false);
        String getContent2 = gson.toJson(post2);

        ResultActions postActions2 =
                mockMvc.perform(
                        post("/v1/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent2)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝

        // when / then
        mockMvc.perform(
                        delete("/v1/todos")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

    }

}
