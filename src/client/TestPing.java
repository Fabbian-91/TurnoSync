/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import client.network.ClientGateway;
import common.dto.Request;
import common.dto.Result;
import common.enums.ActionType;
import server.dao.ConexionBD;

public class TestPing {

    public static void main(String[] args) {
        
        //Geneamos un nuevo cominicador con el server
        ClientGateway gateway = new ClientGateway("localhost", 5000);
        
        //Generamos el Reques con su tipo de acción que va ir a hacer al server
        Request<Void> request = new Request<>();
        request.setAction(ActionType.PING);
        request.setToken(null);
        request.setData(null);
        
        //Esperamos resultados del server
        Result<?> result = gateway.sendRequest(request);
        
        //Validamos resultado
        if (result != null) {
            System.out.println("OK: " + result.ok);
            System.out.println("Mensaje: " + result.message);
        } else {
            System.out.println("Sin respuesta del servidor");
        }
        //Probar Conexión a la bd
        ConexionBD.getConnection();
        
        //Cerramos comunicador con el server
        gateway.disconnect();
    }
}
