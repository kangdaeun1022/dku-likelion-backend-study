package com.ll.demo03;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {
    private final MemberService memberService;
    private final Rq rq;

    public SessionController(MemberService memberService, Rq rq) {
        this.memberService = memberService;
        this.rq = rq;
    }

    // 학습용: 샘플 회원 선택. 실제 인증 기능은 아닙니다.
    @PostMapping("/session/login")
    public RsData<String> login(@RequestParam String username) {
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new GlobalException(404, "회원을 찾을 수 없습니다."));
        rq.login(member);
        return RsData.success("샘플 회원을 선택했습니다.", member.getUsername());
    }

    @PostMapping("/session/logout")
    public RsData<Void> logout() {
        rq.logout();
        return RsData.success("세션을 종료했습니다.", null);
    }
}
