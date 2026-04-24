/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.network;

import common.dto.EventMessage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

public class ClientConnection {

    private final String host;
    private final int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private volatile boolean connected = false;
    private volatile boolean listening = false;

    private final BlockingQueue<Object> responseQueue = new LinkedBlockingQueue<>();

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public synchronized void connect() {
        try {
            if (connected) {
                return;
            }

            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            connected = true;

            startListener();

        } catch (Exception e) {
            throw new RuntimeException("Error conectando al servidor", e);
        }
    }

    public synchronized void startListener() {
        if (!connected || listening) {
            return;
        }

        listening = true;

        Thread listenerThread = new Thread(() -> {
            try {
                while (connected && !socket.isClosed()) {
                    Object obj = in.readObject();

                    if (obj instanceof EventMessage event) {
                        SwingUtilities.invokeLater(() -> {
                            ClientEventDispatcher.dispatch(event);
                        });
                    } else {
                        responseQueue.put(obj);
                    }
                }
            } catch (Exception e) {
                if (connected) {
                    e.printStackTrace();
                }
            } finally {
                listening = false;
                close();
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.setName("client-event-listener");
        listenerThread.start();
    }

    public Object takeResponse() {
        try {
            return responseQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Hilo interrumpido esperando respuesta", e);
        }
    }

    public synchronized void close() {
        connected = false;

        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
        }

        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
    }

    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }
}
