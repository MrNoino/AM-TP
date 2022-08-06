package com.example.canteen;

public class CancelResponse {

    private int Code;
    private String Erro;

    public CancelResponse(int code, String erro) {
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
