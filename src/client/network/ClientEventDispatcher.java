/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.network;

import client.view.JIFAtender;
import client.view.JIFBienvenida;
import client.view.JIFGestionTurnos;
import client.view.JIFGestionUsers;
import client.view.JIFHistorial;
import client.view.JIFMisTurnos;
import common.dto.EventMessage;
import common.enums.TypeEvent;
import javax.swing.SwingUtilities;

public class ClientEventDispatcher {

    /**
     * Metodo encargado de recibir un evento del servidor y actualizar las
     * ventanas correspondientes segun el tipo de evento.
     *
     * @param event evento recibido desde el servidor
     */
    public static void dispatch(EventMessage event) {
        // Se obtiene el tipo de evento para saber que parte de la interfaz se debe actualizar
        TypeEvent type = event.getType();

        // Se revisa el tipo de evento recibido y se ejecuta la accion correspondiente
        switch (type) {
            case RECARGAR_USUARIOS:
                // Se ejecuta en el hilo de Swing para actualizar la interfaz de forma segura
                SwingUtilities.invokeLater(() -> {

                    // Si la ventana de gestion de usuarios esta abierta, se recarga la lista de usuarios
                    if (JIFGestionUsers.instancia != null) {
                        JIFGestionUsers.instancia.cargarUsuarios();
                    }

                    // Si la ventana de bienvenida esta abierta, se actualizan sus tarjetas
                    if (JIFBienvenida.instancia != null) {
                        JIFBienvenida.instancia.cargarTarjetas();
                    }
                });
                break;

            case RECARGAR_TURNOS:
                // Se ejecuta en el hilo de Swing para actualizar las ventanas relacionadas con turnos
                SwingUtilities.invokeLater(() -> {

                    // Si la ventana de gestion de turnos esta abierta, se recarga la tabla de turnos
                    if (JIFGestionTurnos.instancia != null) {
                        JIFGestionTurnos.instancia.cargarTurnos();
                    }

                    // Si la ventana de mis turnos esta abierta, se actualiza su tabla
                    if (JIFMisTurnos.instancia != null) {
                        JIFMisTurnos.instancia.cargarTabla();
                    }

                    // Tambien se actualizan las tarjetas de la pantalla de bienvenida si esta abierta
                    if (JIFBienvenida.instancia != null) {
                        JIFBienvenida.instancia.cargarTarjetas();
                    }
                });
                break;

            case RECARGAR_SOLICITUDES:
            case RECARGAR_BIENVENIDA:
            case RECARGAR_HISTORIAL:
                // Estos eventos actualizan varias partes relacionadas con solicitudes e historial
                SwingUtilities.invokeLater(() -> {

                    // Si la bienvenida esta abierta, se recarga la actividad reciente
                    if (JIFBienvenida.instancia != null) {
                        JIFBienvenida.instancia.cargarActividadReciente();
                    }

                    // Si el historial esta abierto, se recarga la tabla del historial
                    if (JIFHistorial.instancia != null) {
                        JIFHistorial.instancia.cargarTablaHistorial();
                    }

                    // Si la ventana de atender solicitudes esta abierta, se recargan las peticiones
                    if (JIFAtender.instancia != null) {
                        JIFAtender.instancia.cargarPeticiones();
                    }

                    // Tambien se actualizan las tarjetas de la bienvenida
                    if (JIFBienvenida.instancia != null) {
                        JIFBienvenida.instancia.cargarTarjetas();
                    }
                });
                break;

            default:
                // Si llega un evento que no se esta manejando, no se realiza ninguna accion
                break;
        }
    }
}
