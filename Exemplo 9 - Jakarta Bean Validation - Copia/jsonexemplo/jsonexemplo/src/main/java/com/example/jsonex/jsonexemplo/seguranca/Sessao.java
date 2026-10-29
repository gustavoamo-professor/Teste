package com.example.jsonex.jsonexemplo.seguranca;

import com.example.jsonex.jsonexemplo.model.Usuario;
public final class Sessao {
private static Usuario usuarioLogado;
private Sessao() {
}
public static void iniciar(Usuario usuario) {
usuarioLogado = usuario;
}
public static void encerrar() {
usuarioLogado = null;
}
public static Usuario getUsuarioLogado() {
return usuarioLogado;
}
public static boolean estaLogado() {
return usuarioLogado != null;
}
}

