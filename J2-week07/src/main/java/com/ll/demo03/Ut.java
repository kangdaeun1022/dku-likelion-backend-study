package com.ll.demo03;

public final class Ut {
    private Ut() {}
    public static final class Str {
        private Str() {}
        public static boolean isBlank(String value) { return value == null || value.isBlank(); }
        public static boolean hasLength(String value) { return value != null && !value.isEmpty(); }
    }
}
