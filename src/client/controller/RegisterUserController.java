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
//Clase controladora de cliente para validar núevo registros
public class RegisterUserController {

    //Atributos puerta de comunicación
    private final ServerGateway gateway;//Generamos la núeva puerta para comunicar con el server

    /**
     * Costructor de la clase controladora Le pasamos la núeva puerta generada
     *
     * @param gateway
     */
    public RegisterUserController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Petodo para validar un registro
     *
     * @param u
     * @param c
     * @param cc
     * @return retoramos el resultado si es fallido o su data y contraseña si es
     * valido
     */
    public Result<Void> registrar(String u, String c, String cc, String n, String na) {
        //Generamos un núevo resultado para validar el email
        Result<String> emailValid = validarEmail(u);
        //Validamos si su estado no es ok
        if (!emailValid.ok) {
            return Result.fail(emailValid.message);//Retornamos el resultado fallido
        }

        //Generamos núevo resultado para validar la contraseña
        Result<Void> passValid = ValidarPassword(c, cc);
        //Validamos si el estado de su contraseña es diferente a ok y retornamos resultado fallido
        if (!passValid.ok) {
            return passValid;
        }

        //Geneamos núevo resultado para validar el numero
        Result<String> numberValid = validarNumber(n);
        //Validamos si el estado del numero no es ok y retornamos resultado fallido
        if (!numberValid.ok) {
            return Result.fail(numberValid.message);
        }

        //Generamos núevo resultado para validar el nombre
        Result<String> nameValid = validarName(na);
        //Validamos si el estado del nombre no es ok y retornamos resultado fallido
        if (!nameValid.ok) {
            return Result.fail(nameValid.message);
        }

        
        return gateway.register(emailValid.data, c,n,na);
    }

    /**
     * Metodo para validar los email que retorne un estado de resultado
     *
     * @param u
     * @return
     */
    private Result<String> validarEmail(String u) {
        //validar que el usario no sea null
        if (u == null) {
            return Result.fail("El usuario no puede ser null.");
        }

        //Generamos strim con el imail en minuscula y sin espacios
        String email = u.trim().toLowerCase();

        //validamos que no este vacío
        if (email.isEmpty()) {
            return Result.fail("El usuario no puede estar vacío.");
        }

        //Validamos la longitud de email
        if (email.length() < 5 || email.length() > 50) {
            return Result.fail("El usuario debe tener entre 5 y 50 caracteres.");
        }

        //Validamos si contiene espacios vacíos
        if (email.contains(" ")) {
            return Result.fail("El usuario no puede contener espacios.");
        }

        //Validamos si tiene formato de correo
        if (!email.contains("@") || !email.contains(".")) {
            return Result.fail("El correo debe tener formato de correo.");
        }

        //Retornamos el estado ok del resultado
        return Result.ok("OK", email);
    }

    /**
     * Metodo para validar contraseña y confirmar contraseña
     * @param c
     * @param cc
     * @return 
     */
    private Result<Void> ValidarPassword(String c, String cc) {
        if (c == null || cc == null) {
            return Result.fail("La contraseña no puede ser null.");
        }

        if (c.isEmpty() || cc.isEmpty()) {
            return Result.fail("Los campos de contraseña no pueden estar vacíos.");
        }

        if (c.length() < 6) {
            return Result.fail("La contraseña debe tener mínimo 6 caracteres.");
        }
        if (!c.equals(cc)) {
            return Result.fail("Las contraseñas no coinciden.");
        }

        return Result.ok("OK", null);
    }
    
    /**
     * Metodo para validar el número de telefono
     * @param n
     * @return 
     */
    private Result<String> validarNumber(String n) {

        if (n == null) {
            return Result.fail("El número no puede ser null.");
        }

        String number = n.trim();

        if (number.isEmpty()) {
            return Result.fail("El número no puede estar vacío.");
        }

        // Solo dígitos
        if (!number.matches("\\d+")) {
            return Result.fail("El número solo puede contener dígitos.");
        }

        // Validar longitud
        if (number.length() < 7 || number.length() > 15) {
            return Result.fail("El número debe tener entre 7 y 15 dígitos.");
        }

        return Result.ok("OK", number);
    }
    
    /**
     * Metodo para validar el nombre
     * @param na
     * @return 
     */
    private Result<String> validarName(String na) {

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

        // Solo letras y espacios
        if (!name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            return Result.fail("El nombre solo puede contener letras.");
        }

        return Result.ok("OK", name);
    }

}
