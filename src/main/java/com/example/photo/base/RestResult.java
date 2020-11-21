package com.example.photo.base;

import com.google.gson.Gson;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonBuilderUtils;

public class RestResult<Data> {
    private String message;
    private int code;
    private Data data;
    public static RestResult<String> NOT_FOUND = new RestResult<String>(404,"Not Found");
    public RestResult(int code, String message, Data data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestResult(int code, String message) {
        this(code,message,null);
    }

    public RestResult(int code) {
        this(code,"");
    }

    public Data getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <Data> RestResult<Data> success() {
        return new RestResult<Data>(200,"成功");
    }
    public static <Data> RestResult<Data> success(String message) { return new RestResult<Data>(200,message); }
    public static <Data> RestResult<Data> success(Data data) {
        return new RestResult<Data>(200,"成功",data);
    }

    public static <Data> RestResult<Data> notFound() {
        return new RestResult<Data>(404,"未找到");
    }

    public static <Data> RestResult<Data> noAccess() {
        return new RestResult<Data>(403,"没有权限");
    }

    public static <Data> RestResult<Data> failed() {
        return failed("未知错误");
    }
    public static <Data> RestResult<Data> failed(String message) {
        return failed(message,null);
    }
    public static <Data> RestResult<Data> failed(String message, Data data) {
        return new RestResult<Data>(199,message,data);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
