/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Bootstrap;

import client.controller.AuthController;
import client.controller.DashboardController;
import client.controller.PeticionController;
import client.controller.TurnoController;
import client.controller.UserController;
import client.gateway.ServerGateway;
import client.gateway.SocketServerGateway;
import client.network.ClientGateway;

public class ClientBootstrap {
    
    //Comunicador con  el server
    private static final ClientGateway client = new ClientGateway("localhost", 5000);
    
    //Puerta de acceso
    private static final ServerGateway gateway = new SocketServerGateway(client);

    //Metodos para inicializar los controladores del cliente con su socket y puerta
    
    /**
     * Metodo para inicializar el auntentificador
     * @return 
     */
    public static AuthController buildAuthController() {
        return new AuthController(gateway);
    }
    
    /**
     * Metodo para inicializar el dashboard de bienvenida
     * @return 
     */
    public static DashboardController buildDashboardController() {
        return new DashboardController(gateway);
    }
    
    /**
     * Metodo para inicializar el controlador del usuario
     * @return 
     */
    public static UserController buildUserController() {
        return new UserController(gateway);
    }
    
    /**
     * Metodo para inicializar el controlador del tuno
     * @return 
     */
    public static TurnoController buildTurnoController() {
        return new TurnoController(gateway);
    }
    
    /**
     * Metodo para iniciaizar el controlador de peticiones
     * @return 
     */
    public static PeticionController buildPeticionController() {
        return new PeticionController(gateway);
    }
}
