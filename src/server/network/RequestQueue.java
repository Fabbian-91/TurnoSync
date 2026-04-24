/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Fabian
 */
public class RequestQueue {
    private static final BlockingQueue<RequestContext> queue = new LinkedBlockingQueue<>();

    public static void enqueue(RequestContext context) throws InterruptedException {
        queue.put(context);
    }

    public static RequestContext take() throws InterruptedException {
        return queue.take();
    }
}
