package com.example.canteen;

public class BalanceResponse {

    private int Code;
    private String Erro;
    private double Saldo;

    public BalanceResponse(int code, String erro, double saldo) {
        Code = code;
        Erro = erro;
        Saldo = saldo;
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

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        Saldo = saldo;
    }

}
