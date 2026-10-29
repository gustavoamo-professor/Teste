package com.example.jsonex.jsonexemplo.model;

public class Usuario {
private int id;
private String nome;
private String login;
private String perfil;
private boolean ativo;
public Usuario() {
}
public Usuario(
int id,
String nome,
String login,
String perfil,
boolean ativo
) {
this.id = id;
this.nome = nome;
this.login = login;
this.perfil = perfil;
this.ativo = ativo;
}
public int getId() {
return id;
}
public void setId(int id) {
this.id = id;
}
public String getNome() {
return nome;
}
public void setNome(String nome) {
this.nome = nome;
}
public String getLogin() {
return login;
}
public void setLogin(String login) {
this.login = login;
}
public String getPerfil() {
return perfil;
}
public void setPerfil(String perfil) {
this.perfil = perfil;
}
public boolean isAtivo() {
return ativo;
}
public void setAtivo(boolean ativo) {
this.ativo = ativo;
}
public boolean isAdministrador() {
return "Administrador do Sistema".equalsIgnoreCase(perfil);
}
public boolean isOperador() {
return "Operador de Omoxarifado".equalsIgnoreCase(perfil);
}
@Override
public String toString() {
return nome + " - " + perfil;
}
}

