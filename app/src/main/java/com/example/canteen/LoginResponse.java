package com.example.canteen;

public class LoginResponse {

    private int Code;
    private String Token;
    private String Erro;

    public int getCode() {
        return Code;
    }

    public String getToken() {
        return Token;
    }

    public String getErro() {
        return Erro;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public void setErro(String Erro) {
        this.Erro = Erro;
    }

    public String toString() {
        return "[ StatusCode: " + Code + ", Token: " + Token + " ]";
    }

}
