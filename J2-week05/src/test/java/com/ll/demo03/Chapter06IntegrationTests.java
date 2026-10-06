package com.ll.demo03;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Chapter06IntegrationTests {
    @Autowired MemberService memberService;
    @Autowired ArticleService articleService;
    @Autowired SurlService surlService;
    @Autowired MockMvc mockMvc;

    @Test
    void articleCrudAndDerivedQueriesUseTheDatabase() {
        Member author = memberService.findByUsername("user1").orElseThrow();
        long before = articleService.count();
        Article created = articleService.write("JPA 실습", "저장 테스트", author).data();

        assertThat(created.getId()).isNotNull();
        assertThat(created.getCreateDate()).isNotNull();
        assertThat(articleService.count()).isEqualTo(before + 1);
        assertThat(articleService.findByIds(List.of(created.getId()))).hasSize(1);
        assertThat(articleService.findByTitleContaining("JPA 실습")).hasSize(1);
        assertThat(articleService.findByTitleAndBody("JPA 실습", "저장 테스트")).hasSize(1);

        articleService.modify(created.getId(), "수정된 제목", "수정된 내용");
        assertThat(articleService.getById(created.getId()).getTitle()).isEqualTo("수정된 제목");
        assertThat(articleService.getById(created.getId()).getModifyDate()).isNotNull();

        articleService.delete(created.getId());
        assertThat(articleService.count()).isEqualTo(before);
        assertThatThrownBy(() -> articleService.getById(created.getId()))
                .isInstanceOf(GlobalException.class);
    }

    @Test
    void sampleMembersAreUniqueAndSurlsKeepTheirAuthorAndCount() {
        Member author = memberService.findByUsername("user2").orElseThrow();
        assertThatThrownBy(() -> memberService.join("user2"))
                .isInstanceOf(GlobalException.class);

        SurlResponse created = surlService.add("강의", "https://example.com/course", author.getId());
        assertThat(created.author()).isEqualTo("user2");
        assertThat(created.createDate()).isNotNull();
        assertThat(surlService.visit(created.id())).isEqualTo("https://example.com/course");
        SurlResponse saved = surlService.findAll().stream()
                .filter(surl -> surl.id().equals(created.id()))
                .findFirst().orElseThrow();
        assertThat(saved.count()).isEqualTo(1);
        assertThat(saved.author()).isEqualTo("user2");
    }

    @Test
    void sessionMemberIsUsedWhenCreatingAShortUrl() throws Exception {
        mockMvc.perform(get("/add").param("body", "설명").param("url", "https://example.com"))
                .andExpect(status().isUnauthorized());

        MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/session/login").session(session).param("username", "user1"))
                .andExpect(status().isOk());
        String response = mockMvc.perform(get("/add").session(session)
                        .param("body", "학습 링크").param("url", "https://example.com/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("user1"))
                .andReturn().getResponse().getContentAsString();

        long id = ((Number) JsonPath.read(response, "$.id")).longValue();
        mockMvc.perform(get("/g/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("https://example.com/page"));

        mockMvc.perform(get("/s/note/https://example.com/extra").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("user1"));
    }
}
