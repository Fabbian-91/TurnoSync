/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import common.dto.Request;
import java.io.EOFException;
import java.io.IOException;

public class SocketServer {

    // Puerto donde el servidor va a escuchar conexiones
    private final int port;

    /**
     * Metodo constructor que recibe el puerto del servidor.
     *
     * @param port puerto donde se va a iniciar el servidor
     */
    public SocketServer(int port) {
        this.port = port;
    }

    /**
     * Metodo que inicia el servidor. Crea el ServerSocket y queda esperando
     * clientes.
     */
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("Servidor activo en puerto " + port);

            // El servidor queda activo esperando nuevas conexiones
            while (true) {
                // Se acepta la conexion de un cliente
                Socket client = server.accept();
                System.out.println("Cliente conectado");

                // Cada cliente se maneja en un hilo aparte
                new Thread(() -> handleClient(client)).start();
            }

        } catch (Exception e) {
            // Si ocurre un error al iniciar o ejecutar el servidor, se muestra en consola
            e.printStackTrace();
        }
    }

    /**
     * Metodo para recibir el socket del cliente y procesar sus peticiones. Crea
     * los flujos de entrada y salida, registra el cliente y envia las
     * solicitudes recibidas a la cola.
     *
     * @param client socket del cliente conectado
     */
    private void handleClient(Socket client) {
        ObjectOutputStream out = null;

        try (
                Socket socket = client; ObjectOutputStream tempOut = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out = tempOut;

            // Se prepara el flujo de salida para enviar respuestas al cliente
            out.flush();

            // Se guarda este cliente en el administrador para poder enviarle eventos
            ClientManager.add(out);

            // Mientras el cliente este conectado, se siguen leyendo solicitudes
            while (true) {
                try {
                    // Se recibe una solicitud enviada por el cliente
                    Request<?> request = (Request<?>) in.readObject();

                    // Se crea el contexto con la solicitud, el socket y la salida del cliente
                    RequestContext context = new RequestContext(request, socket, out);

                    // Se agrega la solicitud a la cola para que sea procesada
                    RequestQueue.enqueue(context);

                } catch (InterruptedException e) {
                    // Si el hilo se interrumpe, se cierra la conexion de forma controlada
                    System.out.println("Hilo interrumpido, cerrando conexión...");
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (EOFException | java.net.SocketException e) {
            // Esto ocurre cuando el cliente se desconecta
            System.out.println("Cliente desconectado.");
        } catch (IOException | ClassNotFoundException e) {
            // Si ocurre un error leyendo objetos o con la conexion, se muestra en consola
            e.printStackTrace();
        } finally {
            // Al salir, se elimina el cliente de la lista de conexiones activas
            if (out != null) {
                ClientManager.remove(out);
            }
        }
    }
}
