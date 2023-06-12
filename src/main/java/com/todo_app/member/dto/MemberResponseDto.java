package com.todo_app.member.dto;

import com.todo_app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor     // 테스트를 위해 추가됨
public class MemberResponseDto {

    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;

    public String getMemberStatus() {
        return memberStatus.getStatus();
    }

}
