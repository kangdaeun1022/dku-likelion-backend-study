package com.ll.demo03;

public record RsData<T>(String resultCode, int statusCode, String msg, T data) {
    public static <T> RsData<T> success(String msg, T data) {
        return new RsData<>("200-1", 200, msg, data);
    }
    public static <T> RsData<T> failure(int status, String msg) {
        return new RsData<>(status + "-1", status, msg, null);
    }
}
