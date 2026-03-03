package client.controller;

import client.gateway.ServerGateway;
import common.dto.Result;

public class LoginController {
    //Atributo puerta de comunicación
    private final ServerGateway gateway;

    /**
     * Metodo contructor del login
     * Inicializamos un puerta de comunicación
     * @param gateway 
     */
    public LoginController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Metodo para hacer login aplicando validaciones en cliente
     *
     * @param email
     * @param pass
     * @return Retorna Result con ok/fail y mensaje
     */
    public Result<Void> login(String email, String pass) {

        //Validamos email
        Result<String> emailValid = validarEmail(email);
        if (!emailValid.ok) {
            return Result.fail(emailValid.message);
        }

        //Validamos contraseña
        Result<Void> passValid = validarPassword(pass);
        if (!passValid.ok) {
            return passValid;
        }

        //Llamamos al server (por ahora LocalGateway, luego SocketGateway)
        return gateway.login(emailValid.data, pass);
    }

    /**
     * Metodo para validar el email del login
     *
     * @param u
     * @return
     */
    private Result<String> validarEmail(String u) {

        //Validar que no sea null
        if (u == null) {
            return Result.fail("El usuario no puede ser null.");
        }

        //Quitamos espacios y lo pasamos a minuscula
        String email = u.trim().toLowerCase();

        //Validamos que no esté vacío
        if (email.isEmpty()) {
            return Result.fail("El usuario no puede estar vacío.");
        }

        //Validamos longitud
        if (email.length() < 5 || email.length() > 50) {
            return Result.fail("El usuario debe tener entre 5 y 50 caracteres.");
        }

        //Validamos que no tenga espacios internos
        if (email.contains(" ")) {
            return Result.fail("El usuario no puede contener espacios.");
        }

        //Validamos formato básico
        if (!email.contains("@") || !email.contains(".")) {
            return Result.fail("El correo debe tener formato válido.");
        }

        return Result.ok("OK", email);
    }

    /**
     * Metodo para validar la contraseña del login
     *
     * @param pass
     * @return
     */
    private Result<Void> validarPassword(String pass) {

        //Validar null
        if (pass == null) {
            return Result.fail("La contraseña no puede ser null.");
        }

        //Validar vacía
        if (pass.isEmpty()) {
            return Result.fail("La contraseña no puede estar vacía.");
        }

        //Validación mínima
        if (pass.length() < 6) {
            return Result.fail("La contraseña debe tener mínimo 6 caracteres.");
        }

        return Result.ok("OK", null);
    }
}
