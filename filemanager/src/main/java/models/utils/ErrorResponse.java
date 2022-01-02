package models.utils;

import com.google.gson.Gson;
import models.Model;

/**
 * Represent an error object, with a message, a constant boolean error field and an
 * optional code to identify a particular error.
 */
public class ErrorResponse extends Model<ErrorResponse> {
    public static final int MISSING_RESOURCE = 1000;
    public static final int INVALID_RESOURCE = 1001;

    private String message;
    private final boolean error = true;
    private Integer code = null;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorResponse fromJson(String json) {
        return new Gson().fromJson(json, ErrorResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isError() {
        return error;
    }

    public Integer getCode() {
        return code;
    }

    public ErrorResponse setCode(Integer code) {
        this.code = code;
        return this;
    }
}