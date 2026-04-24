/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientManager {

    private static final List<ObjectOutputStream> clients
            = new CopyOnWriteArrayList<>();

    public static void add(ObjectOutputStream out) {
        clients.add(out);
    }

    public static void remove(ObjectOutputStream out) {
        clients.remove(out);
    }

    public static void broadcast(Object obj) {
        for (ObjectOutputStream out : clients) {
            try {
                out.writeObject(obj);
                out.flush();
            } catch (Exception e) {
                remove(out);
            }
        }
    }
}
