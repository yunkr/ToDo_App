package com.todo_app.slice;

import com.google.gson.Gson;
import com.todo_app.member.dto.MemberPatchDto;
import com.todo_app.member.dto.MemberPostDto;
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
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;


    // Post test
    @Test
    void postMemberTest() throws Exception {

        // given
        MemberPostDto post = new MemberPostDto("hgd@gmail.com", "홍길동", "010-1234-5678");

        String content = gson.toJson(post);        // JSON 변환 라이브러리를 이용해서 객체를 JSON 포맷으로 변환


        // when
        ResultActions resultActions = mockMvc.perform(

                // HTTP request에 대한 정보
                post("/v1/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        // then
        // andExpect() 메서드를 통해 파라미터로 입력한 매처(Matcher)로 예상되는 기대 결과를 검증
        resultActions
                .andExpect(status().isCreated())        // response status가 201(Created)인지 매치, 백엔드 측에 리소스 정보가 잘 생성(저장)되었는지를 검증
                .andExpect(header().string("Location", is(startsWith("/v1/members"))));     // HTTP header에 추가된 Location의 문자열 값이 “/v1/members/”로 시작하는지 검증

    }


    // Patch test
    @Test
    void patchMemberTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작

        // given
        MemberPostDto post = new MemberPostDto("hsd@gmail.com","홍삼동","010-1212-1212");
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝

        long memberId;
        String location = postActions.andReturn().getResponse().getHeader("Location");      // "/v1/members/1"
        memberId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        MemberPatchDto patch = new MemberPatchDto("박코딩", "010-1234-5678");
        patch.setMemberId(memberId);
        String patchContent = gson.toJson(patch);

        // when / then
        mockMvc.perform(
                patch("/v1/members/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchContent)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patch.getName()))
                .andExpect(jsonPath("$.phone").value(patch.getPhone()));

    }


    // Get test
    @Test
    void getMemberTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작

        // given: MemberController의 getMember()를 테스트하기 위해서 postMember()를 이용해 테스트 데이터를 생성 후, DB에 저장
        MemberPostDto post = new MemberPostDto("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝

        long memberId;
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v1/members/1"
        memberId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        // when / then
        mockMvc.perform(
                        get("/v1/members/" + memberId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.name").value(post.getName()))
                .andExpect(jsonPath("$.phone").value(post.getPhone()));

    }

    @Test
    void getMembersTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작

        // given
        MemberPostDto post1 = new MemberPostDto("ksd@gmail.com","김삼동","010-8282-8282");
        String getContent1 = gson.toJson(post1);

        ResultActions postActions1 =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent1)
                );

        MemberPostDto post2 = new MemberPostDto("gbs@gmail.com","정보석","010-2323-2323");
        String getContent2 = gson.toJson(post2);

        ResultActions postActions2 =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent2)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝


        // when / then
        mockMvc.perform(
                        get("/v1/members")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

    }

    // Delete test
    @Test
    void deleteMemberTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작
        // given
        MemberPostDto post = new MemberPostDto("hgd@gmail.com","홍길동","010-1111-1111");
        String postContent = gson.toJson(post);

        ResultActions postActions =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(postContent)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝

        long memberId;
        String location = postActions.andReturn().getResponse().getHeader("Location"); // "/v1/members/1"
        memberId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        // when / then
        mockMvc.perform(
                        delete("/v1/members/" + memberId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteMembersTest() throws Exception {
        // =================================== postMember()를 이용한 테스트 데이터 생성 시작

        // given
        MemberPostDto post1 = new MemberPostDto("ksd@gmail.com","김삼동","010-8282-8282");
        String getContent1 = gson.toJson(post1);

        ResultActions postActions1 =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent1)
                );

        MemberPostDto post2 = new MemberPostDto("gbs@gmail.com","정보석","010-2323-2323");
        String getContent2 = gson.toJson(post2);

        ResultActions postActions2 =
                mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(getContent2)
                );

        // =================================== postMember()를 이용한 테스트 데이터 생성 끝

        // when / then
        mockMvc.perform(
                        delete("/v1/members")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

    }

}
