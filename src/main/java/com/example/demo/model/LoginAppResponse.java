package com.example.demo.model;

public class LoginAppResponse<T>
{
    int code;
    String description;
    T data;
    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
