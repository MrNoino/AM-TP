package com.example.canteen;

public class DefaultResponse {

    private int Code;
    private String Erro;

    public DefaultResponse(int code, String erro) {
        Code = code;
        Erro = erro;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getErro() {
        return Erro;
    }

    public void setErro(String erro) {
        Erro = erro;
    }
}
