package com.example.canteen;

import com.google.gson.annotations.SerializedName;

public class ExtractResponse {


    @SerializedName("Code")
    private int Code;
    @SerializedName("Erro")
    private String Erro;
    @SerializedName("Data da Compra")
    private String dataCompra;
    @SerializedName("Data da Refeição")
    private String datadaRefeicao;
    @SerializedName("Prato")
    private String prato;
    @SerializedName("Preço")
    private double preco;
    @SerializedName("Sobremesa")
    private String sobremesa;
    @SerializedName("Sopa")
    private String sopa;
    @SerializedName("Tipo")
    private String tipo;
    @SerializedName("Tipo de Refeição")
    private String tipodeRefeicao;

    public ExtractResponse(int code, String erro, String dataCompra, String datadaRefeição, String prato, double preco, String sobremesa, String sopa, String tipo, String tipodeRefeicao) {

        Code = code;
        Erro = erro;
        this.dataCompra = dataCompra;
        this.datadaRefeicao = datadaRefeição;
        this.prato = prato;
        this.preco = preco;
        this.sobremesa = sobremesa;
        this.sopa = sopa;
        this.tipo = tipo;
        this.tipodeRefeicao = tipodeRefeicao;

    }

    public String getDataCompra() {
        return dataCompra;
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

    public String isDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getDatadaRefeicao() {
        return datadaRefeicao;
    }

    public void setDatadaRefeicao(String datadaRefeicao) {
        this.datadaRefeicao = datadaRefeicao;
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
