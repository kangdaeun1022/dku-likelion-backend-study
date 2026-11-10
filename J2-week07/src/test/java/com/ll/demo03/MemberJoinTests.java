package com.ll.demo03;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberJoinTests {
    @Autowired MockMvc mvc;
    @Autowired MemberService members;

    @Test void joinsAndHashesPasswordWithoutExposingIt() throws Exception {
        String body = """
            {"username":"new_member","password":"StrongPass12!","nickname":"새 회원"}
            """;
        String response = mvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.statusCode").value(201))
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.data.username").value("new_member"))
            .andExpect(jsonPath("$.data.passwordHash").doesNotExist())
            .andReturn().getResponse().getContentAsString();
        Member saved = members.findByUsername("new_member").orElseThrow();
        assertThat(new BCryptPasswordEncoder().matches("StrongPass12!", saved.getPasswordHash())).isTrue();
        assertThat(response).doesNotContain("StrongPass12!").doesNotContain(saved.getPasswordHash());
        mvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.statusCode").value(409));
    }

    @Test void rejectsMissingFieldsAndMalformedJson() throws Exception {
        long before = members.findByUsername("bad_member").stream().count();
        mvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"bad_member\",\"password\":\" \"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.resultCode").value("400-1"));
        assertThat(members.findByUsername("bad_member").stream().count()).isEqualTo(before);
        mvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON).content("{"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.msg").exists());
    }
}
