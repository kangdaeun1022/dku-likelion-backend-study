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
        return join(username, "Sample-password-only", username);
    }

    @Transactional
    public Member join(String username, String password, String nickname) {
        if (Ut.Str.isBlank(username) || Ut.Str.isBlank(password) || Ut.Str.isBlank(nickname)) {
            throw new GlobalException(400, "사용자 이름을 입력해 주세요.");
        }
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new GlobalException(409, "이미 사용 중인 사용자 이름입니다.");
        }
        return memberRepository.saveAndFlush(new Member(username, new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(password), nickname));
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member getById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new GlobalException(404, "회원을 찾을 수 없습니다."));
    }
}
