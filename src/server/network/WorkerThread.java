/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import common.dto.EventMessage;
import common.dto.Result;
import common.enums.TypeEvent;

public class WorkerThread extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                RequestContext context = RequestQueue.take();

                Result<?> response = ProtocolDispatcher.dispatch(context.getRequest());

                // Respuesta al cliente que hizo la petición
                context.getOutput().writeObject(response);
                context.getOutput().flush();

                // Broadcast solo para acciones que modifican datos
                if (response.ok) {
                    switch (context.getRequest().getAction()) {

                        case REGISTER:
                        case CREAR_USUARIO:
                        case CHANGE_PASSWORD:
                        case UPDATE_USER:
                        case DELETE_USER:
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_USUARIOS, null)
                            );
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_BIENVENIDA, null)
                            );
                            break;

                        case SOLICITAR_TURNO:
                        case CANCELAR_TURNO:
                        case CREAR_TURNO:
                        case CAMBIAR_ESTADO_TURNO:
                        case UPDATE_TURNO:
                        case DELETE_TURNO:
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_TURNOS, null)
                            );
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_BIENVENIDA, null)
                            );
                            break;

                        case CREAR_PETICION:
                        case UPDATE_ESTADO_PETICION:
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_SOLICITUDES, null)
                            );
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_BIENVENIDA, null)
                            );
                            ClientManager.broadcast(
                                    new EventMessage(TypeEvent.RECARGAR_HISTORIAL, null)
                            );
                            break;

                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
