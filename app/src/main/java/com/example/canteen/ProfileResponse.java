package com.example.canteen;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    private int Code;

    private String Erro;

    private String Id;

    private String cargo;

    @SerializedName("e-mail")
    private String email;

    private String nome;

    public ProfileResponse(int code, String erro, String id, String cargo, String email, String nome) {

        Code = code;
        Erro = erro;
        Id = id;
        this.cargo = cargo;
        this.email = email;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
