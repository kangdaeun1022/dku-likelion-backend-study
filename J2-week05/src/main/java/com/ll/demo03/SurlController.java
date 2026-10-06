package com.ll.demo03;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SurlController {
    private final SurlService surlService;
    private final Rq rq;

    public SurlController(SurlService surlService, Rq rq) {
        this.surlService = surlService;
        this.rq = rq;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<SurlResponse> getAll() {
        return surlService.findAll();
    }

    // 기존 챕터 03의 /add 주소를 유지합니다.
    @GetMapping("/add")
    @ResponseBody
    public SurlResponse add(@RequestParam String body, @RequestParam String url) {
        return surlService.add(body, url, rq.getCurrentMemberId());
    }

    @GetMapping("/s/{body}/**")
    @ResponseBody
    public SurlResponse addFromPath(@PathVariable String body, HttpServletRequest request) {
        String[] parts = request.getRequestURI().split("/", 4);
        if (parts.length < 4) {
            throw new GlobalException(400, "올바른 단축 URL 경로가 아닙니다.");
        }
        String url = parts[3];
        // Servlet 경로 정규화가 https://를 https:/로 바꾼 경우 복원합니다.
        if (url.startsWith("https:/") && !url.startsWith("https://")) {
            url = "https://" + url.substring("https:/".length());
        } else if (url.startsWith("http:/") && !url.startsWith("http://")) {
            url = "http://" + url.substring("http:/".length());
        }
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }
        return surlService.add(body, url, rq.getCurrentMemberId());
    }

    @GetMapping("/g/{id}")
    public String go(@PathVariable long id) {
        return "redirect:" + surlService.visit(id);
    }
}
