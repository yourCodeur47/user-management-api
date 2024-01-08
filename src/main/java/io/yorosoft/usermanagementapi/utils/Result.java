package io.yorosoft.usermanagementapi.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * This class defines the schema of the response. It is used to encapsulate data prepared by
 * the server side, this object will be serialized to JSON before sent back to the client end.
 */
public class Result {

    private final boolean flag; // Two values: true means success, false means not success

    HttpStatus code;

    private final String message; // Response message

    @Setter
    @Getter
    private Object data; // The response payload


    public Result(boolean flag, HttpStatus code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, HttpStatus code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
