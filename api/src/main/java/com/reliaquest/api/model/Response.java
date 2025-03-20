package com.reliaquest.api.model;

import lombok.Builder;

@Builder
public record Response<T>(T data) {
    public static <T> Response<T> from(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> handled() {
        return new Response<>(null);
    }
}
