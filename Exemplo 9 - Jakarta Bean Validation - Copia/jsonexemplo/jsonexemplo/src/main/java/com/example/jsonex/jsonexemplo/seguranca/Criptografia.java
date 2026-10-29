package com.example.jsonex.jsonexemplo.seguranca;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public final class Criptografia {
private Criptografia() {
}
public static String gerarSHA256(String texto) {
try {
MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
byte[] hash = algoritmo.digest(
texto.getBytes(StandardCharsets.UTF_8)
);
StringBuilder resultado = new StringBuilder();
for (byte item : hash) {
resultado.append(String.format("%02x", item));
}
return resultado.toString();
} catch (NoSuchAlgorithmException e) {
throw new IllegalStateException(
"O algoritmo SHA-256 não está disponível.",
e
);
}
}
}
