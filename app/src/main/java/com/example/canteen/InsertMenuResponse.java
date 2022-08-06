package com.example.canteen;

public class InsertMenuResponse {


    private  int Code;
    private String Erro;
    private int id_ementa;

    public InsertMenuResponse(int code, String erro, int id_ementa) {
        Code = code;
        Erro = erro;
        this.id_ementa = id_ementa;
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

    public int getId_ementa() {
        return id_ementa;
    }

    public void setId_ementa(int id_ementa) {
        this.id_ementa = id_ementa;
    }
}
