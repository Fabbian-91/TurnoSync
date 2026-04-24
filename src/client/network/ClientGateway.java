/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.network;

import common.dto.Request;
import common.dto.Result;
import java.util.UUID;
import server.security.ClsTokenGenerator;

public class ClientGateway {

    // Puerta de conexion con el servidor por medio del socket
    private final SocketClient socketClient;

    /**
     * Constructor de la puerta de conexion. Aqui se crea la conexion con el
     * servidor usando el host y el puerto.
     *
     * @param host direccion del servidor
     * @param port puerto del servidor
     */
    public ClientGateway(String host, int port) {
        // Se crea la conexion con el servidor
        ClientConnection connection = new ClientConnection(host, port);

        // Se crea el cliente socket usando la conexion anterior
        this.socketClient = new SocketClient(connection);
    }

    /**
     * Metodo encargado de enviar una solicitud al servidor. Antes de enviarla,
     * revisa si la solicitud tiene un identificador. Si no lo tiene, le genera
     * uno automaticamente.
     *
     * @param request solicitud que se desea enviar al servidor
     * @return resultado recibido desde el servidor
     */
    public Result<?> sendRequest(Request<?> request) {
        try {
            // Se valida que el id de la solicitud no sea nulo ni este vacio
            if (request.getRequestId() == null || request.getRequestId().isBlank()) {

                // Si no tiene id, se genera un token para identificar la solicitud
                request.setRequestId(ClsTokenGenerator.generateToken());
            }

            // Se envia la solicitud por medio del socket y se retorna la respuesta del servidor
            return socketClient.send(request);

        } catch (Exception e) {
            // Si ocurre algun error de comunicacion, se devuelve un resultado fallido
            return Result.fail("Error de comunicación con el servidor: " + e.getMessage());
        }
    }

    /**
     * Metodo para desconectar el socket del cliente. Cierra la comunicacion con
     * el servidor.
     */
    public void disconnect() {
        // Se desconecta el cliente socket
        socketClient.disconnect();
    }
}
