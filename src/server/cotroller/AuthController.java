/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.cotroller;

import common.dto.Result;
import server.service.AuthService;

/**
 *
 * @author Fabian
 */
public class AuthController {

    /**
     * Metodo registrar 
     * Para delegar reglas de negocio y manejar errores
     * @param email
     * @param password
     * @param number
     * @param name
     * @return 
     */
    public Result<Void> register(String email, String password, String number, String name) {
        try {
            // Delegar al service (negocio + seguridad + BD)
            return AuthService.register(email, password, number, name);
        } catch (Exception e) {
            return Result.fail("messge:Error inesperado\nError:500");
        }
        // Mensaje controlado para el cliente

    }
    /**
     * Metodo login
     * Para delegar reglas de negocio y manejar errores
     * @param email
     * @param password
     * @return 
     */
    public Result<Void> login(String email, String password) {
        try {
            return AuthService.login(email,password);

        } catch (Exception e) {
            return Result.fail("messge:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo forget
     * Para delegar reglas de negocio y manejar errores
     * @param user
     * @param name
     * @param number
     * @return 
     */
    public Result<Void> forget(String user, String name, String number) {
        try {
            return AuthService.forget(user,name,number);
        } catch (Exception e) {
            return Result.fail("messge:Error inesperado\nError:500");
        }
    }
}
