package me.ztiany.simple.http.example;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MockErrorResult {

    @SerializedName("message")
    private String msg;

    @SerializedName("status")
    private int code;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @NonNull
    @Override
    public String toString() {
        return "ErrorResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }

}