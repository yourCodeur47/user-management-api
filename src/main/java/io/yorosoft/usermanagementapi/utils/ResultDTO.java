package io.yorosoft.usermanagementapi.utils;

public record ResultDTO(
        boolean flag,
        int code,
        String message,
        Object data
) { }
