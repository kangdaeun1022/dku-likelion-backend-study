package com.ll.demo03;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "site_member")
public class Member extends BaseTime {
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
