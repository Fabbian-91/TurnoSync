/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.network;

import common.dto.Request;
import common.dto.Result;
import java.io.IOException;

public class SocketClient {

    // Conexion que se usa para comunicarse con el servidor
    private final ClientConnection connection;

    /**
     * Constructor que recibe la conexion del cliente.
     *
     * @param connection conexion con el servidor
     */
    public SocketClient(ClientConnection connection) {
        // Se guarda la conexion para usarla al enviar solicitudes
        this.connection = connection;
    }

    /**
     * Metodo encargado de enviar una solicitud al servidor. Si no hay conexion,
     * primero se conecta. Luego envia la solicitud y espera una respuesta
     * valida.
     *
     * @param request solicitud que se desea enviar al servidor
     * @return resultado recibido desde el servidor
     * @throws IOException error al enviar o recibir datos
     * @throws ClassNotFoundException error si no se reconoce la clase recibida
     */
    public Result<?> send(Request<?> request) throws IOException, ClassNotFoundException {
        // Si el cliente no esta conectado, se abre la conexion con el servidor
        if (!connection.isConnected()) {
            connection.connect();
        }

        // Se sincroniza la conexion para evitar que varias solicitudes se envien al mismo tiempo
        synchronized (connection) {

            // Se envia la solicitud al servidor por medio del flujo de salida
            connection.getOut().writeObject(request);

            // Se limpia el flujo para asegurar que la solicitud salga de inmediato
            connection.getOut().flush();

            // Se queda esperando hasta recibir una respuesta del servidor
            while (true) {
                // Se toma una respuesta de la cola de respuestas
                Object response = connection.takeResponse();

                // Si la respuesta recibida es un Result, se devuelve como respuesta final
                if (response instanceof Result<?> result) {
                    return result;
                }
            }
        }
    }

    /**
     * Metodo que inicia el listener de eventos. Este listener queda atento a
     * mensajes enviados por el servidor.
     */
    public void startEventListener() {
        // Se inicia la escucha de eventos desde la conexion
        connection.startListener();
    }

    /**
     * Metodo para desconectar el cliente del servidor.
     */
    public void disconnect() {
        // Se cierra la conexion actual
        connection.close();
    }
}
