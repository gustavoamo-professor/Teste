package com.example.jsonex.jsonexemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.jsonex.jsonexemplo.conexao.Conexao;
import com.example.jsonex.jsonexemplo.model.Usuario;
import com.example.jsonex.jsonexemplo.seguranca.Criptografia;

public class UsuarioDAO {
public Usuario autenticar(String login, String senha)
throws SQLException {
String sql = """
SELECT
id,
nome,
login,
perfil,
ativo
FROM usuarios
WHERE login = ?
AND senha_hash = ?
AND ativo = TRUE
""";
String senhaCriptografada =
Criptografia.gerarSHA256(senha);
try (
Connection connection = Conexao.conectar();
PreparedStatement statement =
connection.prepareStatement(sql)
) {
statement.setString(1, login);
statement.setString(2, senhaCriptografada);
try (ResultSet resultado = statement.executeQuery()) {
if (resultado.next()) {
return new Usuario(
resultado.getInt("id"),
resultado.getString("nome"),
resultado.getString("login"),
resultado.getString("perfil"),
resultado.getBoolean("ativo")
);
}
}
}
return null;
}
}