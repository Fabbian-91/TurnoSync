/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

/**
 *
 * @author Fabian
 */
import client.gateway.ServerGateway;
import common.dto.Result;
import common.enums.EstadoPeticion;
import java.util.ArrayList;

import server.model.Peticion;

public class PeticionController {

    // Puerta de comunicacion con el servidor
    private final ServerGateway gateway;

    /**
     * Constructor que inicializa la puerta de comunicacion.
     *
     * @param gateway puerta para comunicarse con el servidor
     */
    public PeticionController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Metodo para enviar una peticion al servidor. Primero valida que los datos
     * de la peticion esten correctos. Si todo esta bien, la envia por medio del
     * gateway.
     *
     * @param peticion peticion que se desea enviar
     * @return resultado del envio de la peticion
     */
    public Result<Void> sendPeticion(Peticion peticion) {
        try {
            validarPeticion(peticion);
            return gateway.eniviarPeticion(peticion);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al enviar la petición: " + e.getMessage());
        }
    }

    /**
     * Metodo que carga las peticiones pendientes por atender. La informacion se
     * solicita al servidor usando el gateway.
     *
     * @return lista de peticiones pendientes
     */
    public Result<ArrayList<Peticion>> loadPendientes() {
        return gateway.loadAtender();
    }

    /**
     * Metodo que carga el historial de peticiones. Obtiene las peticiones ya
     * procesadas desde el servidor.
     *
     * @return lista con el historial de peticiones
     */
    public Result<ArrayList<Peticion>> loadHistorial() {
        return gateway.loadHistorial();
    }

    /**
     * Metodo que valida los datos principales de una peticion. Revisa que la
     * peticion exista, que tenga usuario, tipo de solicitud y una descripcion.
     *
     * @param peticion peticion que se va a validar
     */
    private void validarPeticion(Peticion peticion) {
        if (peticion == null) {
            throw new IllegalArgumentException("La petición no puede ser nula");
        }

        if (peticion.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (peticion.getTipo() == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de solicitud");
        }

        if (peticion.getDetalle() == null || peticion.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe escribir una descripción");
        }
    }

    /**
     * Metodo para actualizar el estado de una peticion. Primero valida el id y
     * el estado antes de enviarlos al servidor.
     *
     * @param idPeticion id de la peticion que se desea actualizar
     * @param estado nuevo estado de la peticion
     * @return resultado de la actualizacion
     */
    public Result<Void> updateEstadoPeticion(int idPeticion, EstadoPeticion estado) {
        try {
            validarIdPeticion(idPeticion);
            validarEstadoPeticion(estado);

            return gateway.updateEstadoPeticion(idPeticion, estado);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al actualizar la petición: " + e.getMessage());
        }
    }

    /**
     * Metodo que valida que el id de la peticion sea correcto.
     *
     * @param idPeticion id de la peticion
     */
    private void validarIdPeticion(int idPeticion) {
        if (idPeticion <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una petición válida");
        }
    }

    /**
     * Metodo que valida que el estado de la peticion no sea nulo.
     *
     * @param estado estado de la peticion
     */
    private void validarEstadoPeticion(EstadoPeticion estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Debe seleccionar un estado válido");
        }
    }
}
