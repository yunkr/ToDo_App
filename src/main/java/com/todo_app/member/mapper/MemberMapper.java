package com.todo_app.member.mapper;

import com.todo_app.member.dto.MemberPatchDto;
import com.todo_app.member.dto.MemberPostDto;
import com.todo_app.member.dto.MemberResponseDto;
import com.todo_app.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)       // mapper : SQL을 호출하기 위한 인터페이스
public interface MemberMapper {

    Member memberPostDtoToMember(MemberPostDto requestBody);
    Member memberPatchDtoToMember(MemberPatchDto requestBody);
    MemberResponseDto memberToMemberResponseDto(Member member);
    List<MemberResponseDto> membersToMemberResponseDtos(List<Member> members);

}

