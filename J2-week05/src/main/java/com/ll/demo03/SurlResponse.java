package com.ll.demo03;

import java.time.LocalDateTime;

public record SurlResponse(
        Long id, String body, String url, long count,
        String author, LocalDateTime createDate
) {
    public static SurlResponse from(Surl surl) {
        return new SurlResponse(surl.getId(), surl.getBody(), surl.getUrl(),
                surl.getCount(), surl.getAuthor().getUsername(), surl.getCreateDate());
    }
}
