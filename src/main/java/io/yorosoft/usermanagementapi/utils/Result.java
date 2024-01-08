package io.yorosoft.usermanagementapi.utils;

import org.springframework.http.HttpStatus;

/**
 * This class defines the schema of the response. It is used to encapsulate data prepared by
 * the server side, this object will be serialized to JSON before sent back to the client end.
 */
public class Result {

    private boolean flag; // Two values: true means success, false means not success

    HttpStatus code;

    private String message; // Response message

    private Object data; // The response payload


    public Result() {
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
