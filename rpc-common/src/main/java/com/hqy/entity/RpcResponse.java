package com.hqy.entity;

import java.io.Serializable;

public class RpcResponse<T> implements Serializable {
    private int code;
    private boolean success;
    private T data;
    private String errorMessage;


    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.success = true;
        response.data = data;
        response.code = 200;
        return response;
    }

    public static <T> RpcResponse<T> fail(String errorMessage) {
        RpcResponse<T> response = new RpcResponse<>();
        response.success = false;
        response.errorMessage = errorMessage;
        response.code = 500;
        return response;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return success ? "SUCCESS:" + data : "FAIL:" + errorMessage;
    }
}
