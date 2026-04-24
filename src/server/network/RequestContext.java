/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import common.dto.Request;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Fabian
 */
public class RequestContext {

    // Atributos que guardan la solicitud, el socket y la salida del cliente
    private final Request<?> request;
    private final Socket socket;

    // Flujo de salida que permite responderle al mismo cliente que hizo la solicitud
    private final ObjectOutputStream output;

    /**
     * Metodo constructor que recibe los datos necesarios de la solicitud.
     * Guarda el request, el socket y el flujo de salida del cliente.
     *
     * @param request solicitud recibida del cliente
     * @param socket conexion del cliente
     * @param output flujo para enviar la respuesta al cliente
     */
    public RequestContext(Request<?> request, Socket socket, ObjectOutputStream output) {
        this.request = request;
        this.socket = socket;
        this.output = output;
    }

    /**
     * Metodo para obtener la solicitud recibida.
     *
     * @return request del cliente
     */
    public Request<?> getRequest() {
        return request;
    }

    /**
     * Metodo para obtener el socket del cliente.
     *
     * @return socket del cliente
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Metodo para obtener el flujo de salida.
     *
     * @return salida usada para responderle al cliente
     */
    public ObjectOutputStream getOutput() {
        return output;
    }
}
