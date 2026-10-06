package com.ll.demo03;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Rq {
    private final HttpSession session;

    public Rq(HttpSession session) {
        this.session = session;
    }

    public long getCurrentMemberId() {
        Object value = session.getAttribute("memberId");
        if (!(value instanceof Long id)) {
            throw new GlobalException(401, "먼저 로그인해 주세요.");
        }
        return id;
    }

    public void login(Member member) {
        session.setAttribute("memberId", member.getId());
    }

    public void logout() {
        session.invalidate();
    }
}
