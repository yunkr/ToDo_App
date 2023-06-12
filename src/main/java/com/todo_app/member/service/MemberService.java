package com.todo_app.member.service;

import com.todo_app.exception.BusinessLogicException;
import com.todo_app.exception.ExceptionCode;
import com.todo_app.member.entity.Member;
import com.todo_app.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


// @Transactional : 수행하는 작업에 대해 트랜잭션 원칙이 지켜지도록 보장해주는 것으로 직접 객체를 만들지 않고, 선언만 해도 이 과정이 적용되어서 선언적 트랜잭션
// @Service : Service 레이어 클래스들에 사용되는 어노테이션
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;


    // 생성자(Constructor)
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // Post
    public Member createMember(Member member) {

        verifyExistsEmail(member.getEmail());
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    // Patch
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());

        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> findMember.setPhone(phone));
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));

        return memberRepository.save(findMember);
    }

    // Get
    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return this.memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Member> findAllMember() {
        return this.memberRepository.findAll();
    }

    // Delete
    public void deleteMember(long memberId) {
        this.memberRepository.deleteById(memberId);
    }

    public void deleteAllMember() {
        this.memberRepository.deleteAll();
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

}
