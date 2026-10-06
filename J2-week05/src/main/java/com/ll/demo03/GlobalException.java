package com.ll.demo03;

public class GlobalException extends RuntimeException {
    private final RsData<Void> rsData;

    public GlobalException(int statusCode, String message) {
        super(message);
        this.rsData = RsData.failure(statusCode, message);
    }

    public RsData<Void> getRsData() {
        return rsData;
    }
}
