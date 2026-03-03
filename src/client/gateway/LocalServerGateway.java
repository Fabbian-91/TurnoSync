/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.gateway;

import common.dto.Result;
import server.cotroller.AuthController;

/**
 *
 * @author Fabian
 */
//Clase para manejar las puertas al server
public class LocalServerGateway implements ServerGateway {
    
    //Llamamos al controlador del server
    private final AuthController authController;
    
    /**
     * Para poder interactua con la capa de controlador
     * @param authController 
     */
    public LocalServerGateway(AuthController authController) {
        this.authController = authController;
    }

    /**
     * Actuar como metodo de entrada al server para hacer núevos registros de usuarios
     * @param email
     * @param password
     * @return 
     */
    @Override
    public Result<Void> register(String email, String password, String number, String name) {
        return authController.register(email, password,number,name);
    }

    /**
     * Actuar como metodo de entrada al server para hacer un núevo login
     * @param email
     * @param password
     * @return 
     */
    @Override
    public Result<Void> login(String email, String password) {
        return authController.login(email,password);
    }
    
    /**
     * Actuar como metodo de entrada al server para hacer una núeva actualización de contraseña
     * @param user
     * @param name
     * @param number
     * @return 
     */
    @Override
    public Result<Void> updateForget(String user, String name, String number) {
        return authController.forget(user,name,number);
    }

}
