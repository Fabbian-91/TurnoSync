/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.security;

import java.util.UUID;

/**
 *
 * @author Fabian
 */
public class ClsTokenGenerator {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
