package com.example.canteen;

public class SoupResponse {

    private int Code;

    private String Erro;

    private int id_sopa;

    private String nome;

    public SoupResponse(int code, String erro, int id_sopa, String nome) {
        Code = code;
        Erro = erro;
        this.id_sopa = id_sopa;
        this.nome = nome;
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

    public int getId_sopa() {
        return id_sopa;
    }

    public void setId_sopa(int id_sopa) {
        this.id_sopa = id_sopa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
