package com.example.canteen;

public class DessertResponse {

    private int Code;

    private String Erro;

    private int id_sobremesa;

    private String nome;

    public DessertResponse(int code, String erro, int id_sobremesa, String nome) {
        Code = code;
        Erro = erro;
        this.id_sobremesa = id_sobremesa;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_sobremesa() {
        return id_sobremesa;
    }

    public void setId_sobremesa(int id_sobremesa) {
        this.id_sobremesa = id_sobremesa;
    }
}
