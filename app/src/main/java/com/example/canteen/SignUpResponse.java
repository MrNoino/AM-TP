package com.example.canteen;

public class SignUpResponse {

    private int Code;
    private String Erro;

    public int getCode() {
        return Code;
    }

    public String getErro() {
        return Erro;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public void setErro(String Erro) {
        this.Erro = Erro;
    }

    public String toString() {
        return "[ StatusCode: " + Code + " ]";
    }
}
