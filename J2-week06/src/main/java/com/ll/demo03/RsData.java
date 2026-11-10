package com.ll.demo03;

public record RsData<T>(int statusCode, String message, T data) {
    public static <T> RsData<T> success(String message, T data) {
        return new RsData<>(200, message, data);
    }

    public static <T> RsData<T> failure(int statusCode, String message) {
        return new RsData<>(statusCode, message, null);
    }
}
