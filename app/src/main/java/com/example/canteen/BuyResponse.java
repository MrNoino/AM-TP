package com.example.canteen;

public class BuyResponse {

    private int Code;
    private String Erro;
    private int id_reserva;

    public BuyResponse(int code, String erro, int id_reserva) {
        Code = code;
        Erro = erro;
        this.id_reserva = id_reserva;
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

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }
}
