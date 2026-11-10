package com.ll.demo03;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Surl extends BaseTime {
    @Column(nullable = false)
    private String body;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(nullable = false)
    private long count;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    protected Surl() {
    }

    public Surl(String body, String url, Member author) {
        this.body = body;
        this.url = url;
        this.author = author;
    }

    public void increaseCount() {
        count++;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    public long getCount() {
        return count;
    }

    public Member getAuthor() {
        return author;
    }
}
