/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Bootstrap;

/**
 *
 * @author Fabian
 */

import client.controller.ForgetController;
import client.controller.RegisterUserController;
import client.controller.LoginController;
import client.gateway.LocalServerGateway;
import client.gateway.ServerGateway;

import server.cotroller.AuthController;

public class ClientBootstrap {
    
    /**
     * Metodo para inicializar la puerta de comunicación para cada registro
     * @return 
     */
    public static RegisterUserController buildRegisterController() {
        AuthController authController = new AuthController();
        ServerGateway gateway = new LocalServerGateway(authController);
        return new RegisterUserController(gateway);
    }
    /**
     * Metodo para inicializar la puerta de comunicación en cada login
     * @return 
     */
    public static LoginController builLoginController(){
        AuthController authController=new AuthController();
        ServerGateway gateway= new LocalServerGateway(authController);
        return new LoginController(gateway);
    }
    /**
     * Metodo para inicializar la puert de comunicación cada ves que se olvide una contraseña
     * @return 
     */
    public static ForgetController builLoginControllerForget(){
        AuthController authController=new AuthController();
        ServerGateway gateway= new LocalServerGateway(authController);
        return new ForgetController(gateway);
    }
}
