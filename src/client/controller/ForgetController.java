/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

import client.gateway.ServerGateway;
import common.dto.Result;

/**
 *
 * @author Fabian
 */
public class ForgetController {

    //Atributos de comunicación
    private final ServerGateway gateway;
    
    /**
     * Metodo constructor de la clase
     * @param gateway 
     */
    public ForgetController(ServerGateway gateway) {
        this.gateway = gateway;//Generar puerta de comunicación
    }
    
    /**
     * Metodo para actualizar una contraseña
     * @param u
     * @param na
     * @param n
     * @return 
     */
    public Result<Void> update(String u, String na, String n) {
        
        //Validar el campo de usuario 
        Result<Void> userValid = validarUser(u);
        if (!userValid.ok) {
            return userValid;
        }
        //Validar el campo del nombre
        Result<Void> nameValid = validarName(na);
        if (!nameValid.ok) {
            return nameValid;
        }
        
        //Validar campo del número
        Result<Void> numberValid = validarNumber(n);
        if (!numberValid.ok) {
            return numberValid;
        }
        
        //Retornamos la el resultado del server
        return gateway.updateForget(u, na, n);
    }


    /**
     * Metodo parra validar el usuario
     * @param u
     * @return 
     */
    private Result<Void> validarUser(String u) {
        if (u == null) {
            return Result.fail("El usuario no puede ser null.");
        }

        String user = u.trim().toLowerCase();

        if (user.isEmpty()) {
            return Result.fail("El usuario no puede estar vacío.");
        }

        if (user.length() < 4 || user.length() > 30) {
            return Result.fail("El usuario debe tener entre 4 y 30 caracteres.");
        }

        if (user.contains(" ")) {
            return Result.fail("El usuario no puede contener espacios.");
        }

        if (!user.matches("[a-zA-Z0-9_@.]+")) {
            return Result.fail("El usuario contiene caracteres inválidos.");
        }

        return Result.ok("OK", null);
    }

  
    /**
     * Metodo para validar el campo de nombre
     * @param na
     * @return 
     */
    private Result<Void> validarName(String na) {
           
        if (na == null) {
            return Result.fail("El nombre no puede ser null.");
        }

        String name = na.trim();

        if (name.isEmpty()) {
            return Result.fail("El nombre no puede estar vacío.");
        }

        if (name.length() < 2 || name.length() > 50) {
            return Result.fail("El nombre debe tener entre 2 y 50 caracteres.");
        }

        if (!name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            return Result.fail("El nombre solo puede contener letras.");
        }

        return Result.ok("OK", null);
    }
    
    /**
     * Metodo para validar el campo del número
     * @param n
     * @return 
     */
    private Result<Void> validarNumber(String n) {

        if (n == null) {
            return Result.fail("El número no puede ser null.");
        }

        String number = n.trim();

        if (number.isEmpty()) {
            return Result.fail("El número no puede estar vacío.");
        }

        if (!number.matches("\\d+")) {
            return Result.fail("El número solo puede contener dígitos.");
        }

        if (number.length() < 7 || number.length() > 15) {
            return Result.fail("El número debe tener entre 7 y 15 dígitos.");
        }

        return Result.ok("OK", null);
    }
}
