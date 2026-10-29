package com.example.jsonex.jsonexemplo.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Conexao {
private static final String URL = "jdbc:mysql://localhost:3307/empresa";
private static final String USUARIO = "root";
private static final String SENHA = "senai123";
private Conexao() {
}
public static Connection conectar() throws SQLException {
return DriverManager.getConnection(URL, USUARIO, SENHA);
}
}
