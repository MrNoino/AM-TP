package com.example.canteen;

public class MainResponse {

    private int Code;

    private String Erro;

    private int id_prato;

    private String nome;

    private String tipo;

    public MainResponse(int code, String erro, int id_prato, String nome, String tipo) {
        Code = code;
        Erro = erro;
        this.id_prato = id_prato;
        this.nome = nome;
        this.tipo = tipo;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_prato() {
        return id_prato;
    }

    public void setId_prato(int id_prato) {
        this.id_prato = id_prato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
