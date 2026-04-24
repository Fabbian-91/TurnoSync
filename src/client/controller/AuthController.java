/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

import client.gateway.ServerGateway;
import client.sesion.AppSession;
import common.dto.LoginDTO;
import common.dto.Result;

public class AuthController {

    // Puerta que permite comunicarse con el servidor
    private final ServerGateway gateway;

    /**
     * Constructor que recibe la puerta de comunicación con el servidor
     *
     * @param gateway puerta de comunicación
     */
    public AuthController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Metodo que se encarga de iniciar sesion. Primero valida el correo y la
     * contraseña, luego envia los datos al servidor. Si el login es correcto,
     * guarda la sesion del usuario.
     *
     * @param email correo del usuario
     * @param password contraseña del usuario
     * @return resultado del login
     */
    public Result<LoginDTO> login(String email, String password) {
        Result<String> emailValid = validarEmail(email);
        if (!emailValid.ok) {
            return Result.fail(emailValid.message);
        }

        Result<String> passValid = validarPasswordSimple(password);
        if (!passValid.ok) {
            return Result.fail(passValid.message);
        }

        Result<LoginDTO> result = gateway.login(emailValid.data, password);

        // Si el servidor responde bien, se guarda la sesion actual
        if (result.ok && result.data != null) {
            AppSession.setSession(result.data);
        }

        return result;
    }

    /**
     * Metodo para registrar un nuevo usuario. Valida cada campo antes de
     * enviarlo al servidor.
     *
     * @param email correo del usuario
     * @param password contraseña
     * @param confirmPassword confirmacion de la contraseña
     * @param telefono telefono del usuario
     * @param nombre nombre del usuario
     * @return resultado del registro
     */
    public Result<Void> register(String email, String password, String confirmPassword, String telefono, String nombre) {
        Result<String> emailValid = validarEmail(email);
        if (!emailValid.ok) {
            return Result.fail(emailValid.message);
        }

        Result<Void> passValid = validarPasswordConfirmacion(password, confirmPassword);
        if (!passValid.ok) {
            return passValid;
        }

        Result<String> phoneValid = validarTelefono(telefono);
        if (!phoneValid.ok) {
            return Result.fail(phoneValid.message);
        }

        Result<String> nameValid = validarNombre(nombre);
        if (!nameValid.ok) {
            return Result.fail(nameValid.message);
        }

        // Cuando todos los datos son correctos, se envia el registro al servidor
        return gateway.register(emailValid.data, password, phoneValid.data, nameValid.data);
    }

    /**
     * Metodo para recuperar o actualizar la contraseña olvidada. Valida el
     * usuario y el nombre antes de enviar la solicitud al servidor.
     *
     * @param username usuario o correo
     * @param nombre nombre del usuario
     * @return resultado de la actualizacion
     */
    public Result<Void> recoverPassword(String username, String nombre) {
        Result<String> userValid = validarUsername(username);
        if (!userValid.ok) {
            return Result.fail(userValid.message);
        }

        Result<String> nameValid = validarNombre(nombre);
        if (!nameValid.ok) {
            return Result.fail(nameValid.message);
        }

        // Se envia la informacion validada al servidor
        return gateway.updateForget(userValid.data, nameValid.data);
    }

    /**
     * Metodo para validar el correo del usuario. Revisa que no venga vacio, que
     * tenga un tamaño correcto y que tenga un formato basico de correo.
     *
     * @param value correo ingresado
     * @return correo validado
     */
    private Result<String> validarEmail(String value) {
        if (value == null) {
            return Result.fail("El usuario no puede ser null.");
        }

        String email = value.trim().toLowerCase();

        if (email.isEmpty()) {
            return Result.fail("El usuario no puede estar vacío.");
        }

        if (email.length() < 5 || email.length() > 50) {
            return Result.fail("El usuario debe tener entre 5 y 50 caracteres.");
        }

        if (email.contains(" ")) {
            return Result.fail("El usuario no puede contener espacios.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            return Result.fail("El correo debe tener formato válido.");
        }

        return Result.ok("OK", email);
    }

    /**
     * Metodo para validar el nombre de usuario. Permite letras, numeros, guion
     * bajo, arroba y punto.
     *
     * @param value usuario ingresado
     * @return usuario validado
     */
    private Result<String> validarUsername(String value) {
        if (value == null) {
            return Result.fail("El usuario no puede ser null.");
        }

        String user = value.trim().toLowerCase();

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

        return Result.ok("OK", user);
    }

    /**
     * Metodo para validar una contraseña simple. Se usa principalmente en el
     * inicio de sesion.
     *
     * @param password contraseña ingresada
     * @return contraseña validada
     */
    private Result<String> validarPasswordSimple(String password) {
        if (password == null) {
            return Result.fail("La contraseña no puede ser null.");
        }

        if (password.isEmpty()) {
            return Result.fail("La contraseña no puede estar vacía.");
        }

        if (password.length() < 6) {
            return Result.fail("La contraseña debe tener mínimo 6 caracteres.");
        }

        return Result.ok("OK", password);
    }

    /**
     * Metodo para validar que la contraseña y su confirmacion sean correctas.
     * Revisa que no esten vacias, que tengan el tamaño minimo y que ambas sean
     * iguales.
     *
     * @param password contraseña ingresada
     * @param confirmPassword confirmacion de la contraseña
     * @return resultado de la validacion
     */
    private Result<Void> validarPasswordConfirmacion(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return Result.fail("La contraseña no puede ser null.");
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return Result.fail("Los campos de contraseña no pueden estar vacíos.");
        }

        if (password.length() < 6) {
            return Result.fail("La contraseña debe tener mínimo 6 caracteres.");
        }

        if (!password.equals(confirmPassword)) {
            return Result.fail("Las contraseñas no coinciden.");
        }

        return Result.ok("OK", null);
    }

    /**
     * Metodo para validar el numero de telefono. Limpia los guiones y verifica
     * que solo tenga numeros.
     *
     * @param value telefono ingresado
     * @return telefono validado
     */
    private Result<String> validarTelefono(String value) {
        if (value == null) {
            return Result.fail("El número no puede ser null.");
        }

        String telefono = value.trim().replace("-", "");

        if (telefono.isEmpty()) {
            return Result.fail("El número no puede estar vacío.");
        }

        if (!telefono.matches("^[0-9]{8,15}$")) {
            return Result.fail("El teléfono debe tener solo números (8 a 15 dígitos).");
        }

        return Result.ok("OK", telefono);
    }

    /**
     * Metodo para validar el nombre del usuario. Revisa que no este vacio, que
     * tenga un tamaño valido y que solo contenga letras.
     *
     * @param value nombre ingresado
     * @return nombre validado
     */
    private Result<String> validarNombre(String value) {
        if (value == null) {
            return Result.fail("El nombre no puede ser null.");
        }

        String nombre = value.trim();

        if (nombre.isEmpty()) {
            return Result.fail("El nombre no puede estar vacío.");
        }

        if (nombre.length() < 2 || nombre.length() > 50) {
            return Result.fail("El nombre debe tener entre 2 y 50 caracteres.");
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            return Result.fail("El nombre solo puede contener letras.");
        }

        return Result.ok("OK", nombre);
    }
}
