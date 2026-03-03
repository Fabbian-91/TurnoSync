/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Fabian
 */
public class ClsEncriptar {
    // Genera un salt aleatorio compuesto por caracteres alfanuméricos.
    // length: longitud del salt (si tu tabla tiene salt VARCHAR(5) usa 5, pero es recomendable aumentarlo)
    public static String generarSalt() {
        final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int index = rnd.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }
        return sb.toString();
    }

    // Hash SHA-256 de (password + salt). Devuelve el hash en hexadecimal.
    public static String encriptaSHA256(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = password + salt; // concatenar password y salt
            byte[] hash = digest.digest(combined.getBytes("UTF-8"));
            return bytesAHex(hash);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException ex) {
            throw new RuntimeException("Error generando hash SHA-256", ex);
        }
    }

 
    // Convierte array de bytes a String hex (p. ej. "a3f4...")
    private static String bytesAHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
