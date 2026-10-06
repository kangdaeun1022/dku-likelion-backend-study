package com.ll.demo03;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member join(String username) {
        if (username == null || username.isBlank()) {
            throw new GlobalException(400, "사용자 이름을 입력해 주세요.");
        }
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new GlobalException(409, "이미 사용 중인 사용자 이름입니다.");
        }
        return memberRepository.save(new Member(username));
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(404, "회원을 찾을 수 없습니다."));
    }
}
