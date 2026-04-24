/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import server.network.SocketServer;
import server.network.WorkerThread;

public class ServerMain {

    public static void main(String[] args) {
        //Puerto de escucha
        int port = 5000;

        // iniciar workers, procesa unicamente 3 peticiones
        for (int i = 0; i < 3; i++) {
            new WorkerThread().start();
        }

        // iniciar servidor
        new SocketServer(port).start();
    }
}
