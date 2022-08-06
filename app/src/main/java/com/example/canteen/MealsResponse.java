package com.example.canteen;

import com.google.gson.annotations.SerializedName;

public class MealsResponse {

    @SerializedName("Code")
    private int Code;
    @SerializedName("Erro")
    private String Erro;
    @SerializedName("Comprado")
    public boolean comprado;
    @SerializedName("Data da Refeição")
    public String datadaRefeicao;
    @SerializedName("Id")
    public int id;
    @SerializedName("Id_reserva")
    public int id_reserva;
    @SerializedName("Prato")
    public String prato;
    @SerializedName("Preço")
    public double preco;
    @SerializedName("Sobremesa")
    public String sobremesa;
    @SerializedName("Sopa")
    public String sopa;
    @SerializedName("Tipo")
    public String tipo;
    @SerializedName("Tipo de Refeição")
    public String tipodeRefeicao;

    public MealsResponse(int code, String erro, boolean comprado, String datadaRefeicao, int id, int id_reserva, String prato, double preco, String sobremesa, String sopa, String tipo, String tipodeRefeicao) {
        Code = code;
        Erro = erro;
        this.comprado = comprado;
        this.datadaRefeicao = datadaRefeicao;
        this.id = id;
        this.id_reserva = id_reserva;
        this.prato = prato;
        this.preco = preco;
        this.sobremesa = sobremesa;
        this.sopa = sopa;
        this.tipo = tipo;
        this.tipodeRefeicao = tipodeRefeicao;
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

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public String getDatadaRefeicao() {
        return datadaRefeicao;
    }

    public void setDatadaRefeicao(String datadaRefeicao) {
        this.datadaRefeicao = datadaRefeicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getSobremesa() {
        return sobremesa;
    }

    public void setSobremesa(String sobremesa) {
        this.sobremesa = sobremesa;
    }

    public String getSopa() {
        return sopa;
    }

    public void setSopa(String sopa) {
        this.sopa = sopa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipodeRefeicao() {
        return tipodeRefeicao;
    }

    public void setTipodeRefeicao(String tipodeRefeicao) {
        this.tipodeRefeicao = tipodeRefeicao;
    }
}
