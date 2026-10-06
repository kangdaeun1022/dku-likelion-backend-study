package com.ll.demo03;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleData {
    @Bean
    CommandLineRunner initializeSamples(MemberService memberService, ArticleService articleService) {
        return args -> {
            Member member1 = memberService.findByUsername("user1")
                    .orElseGet(() -> memberService.join("user1"));
            memberService.findByUsername("user2")
                    .orElseGet(() -> memberService.join("user2"));

            articleService.seedIfEmpty(member1);
        };
    }
}
