package com.ll.demo03;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class ApiV1MemberController {
    private final MemberService memberService;
    public ApiV1MemberController(MemberService memberService) { this.memberService = memberService; }

    public record MemberJoinReqBody(
        @NotBlank @Size(min = 3, max = 30) @Pattern(regexp = "[A-Za-z0-9_]+") String username,
        @NotBlank @Size(min = 8, max = 64) @Pattern(regexp = "[\\x20-\\x7E]+") String password,
        @NotBlank @Size(max = 100) String nickname
    ) {}
    public record MemberJoinRespBody(Long id, String username, String nickname) {}

    @PostMapping
    public RsData<MemberJoinRespBody> join(@Valid @RequestBody MemberJoinReqBody request) {
        Member member = memberService.join(request.username(), request.password(), request.nickname());
        return new RsData<>("201-1", 201, "회원가입이 완료되었습니다.",
            new MemberJoinRespBody(member.getId(), member.getUsername(), member.getNickname()));
    }
}
