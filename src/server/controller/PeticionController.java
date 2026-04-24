/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

/**
 *
 * @author Fabian
 */
import client.gateway.ServerGateway;
import common.dto.Request;
import common.dto.Result;
import java.util.ArrayList;
import server.model.Peticion;
import server.service.PeticionService;

public class PeticionController {

    /**
     * Metodo que carga el historial de peticiones. Crea una lista vacia y le
     * pide al servicio que la llene con las peticiones que ya forman parte del
     * historial.
     *
     * @param request solicitud recibida desde el cliente
     * @return lista con las peticiones del historial
     */
    public Result<ArrayList<Peticion>> loadHistorial(Request<?> request) {
        try {
            // Se crea la lista donde se van a guardar las peticiones del historial
            ArrayList<Peticion> lista = new ArrayList<>();

            // Se le pide al servicio que cargue el historial en la lista
            lista = PeticionService.loadHistorial(lista);

            // Se devuelve la lista cargada correctamente
            return Result.ok("Datos cargados", lista);

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que carga las peticiones pendientes por atender. Crea una lista
     * vacia y le pide al servicio que cargue las peticiones que aun deben ser
     * atendidas.
     *
     * @param request solicitud recibida desde el cliente
     * @return lista con las peticiones pendientes
     */
    public Result<ArrayList<Peticion>> loadAtender(Request<?> request) {
        try {
            // Se crea la lista donde se van a guardar las peticiones pendientes
            ArrayList<Peticion> lista = new ArrayList<>();

            // Se le pide al servicio que cargue las peticiones por atender
            lista = PeticionService.loadAtender(lista);

            // Se devuelve la lista cargada correctamente
            return Result.ok("Datos cargados", lista);

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que registra una nueva peticion. Toma la peticion enviada en el
     * request, valida que no venga nula y luego la envia al servicio para
     * guardarla.
     *
     * @param request solicitud con los datos de la peticion
     * @return resultado del registro de la peticion
     */
    public Result<?> postPeticion(Request<?> request) {
        try {
            // Se obtiene la peticion enviada dentro del request
            Peticion p = (Peticion) request.getData();

            // Se valida que la peticion no venga vacia o incorrecta
            if (p == null) {
                return Result.fail("message:Datos de petición inválidos\nError:400");
            }

            // Se envia la peticion al servicio para guardarla
            Result<Void> result = PeticionService.insertPeticion(p);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que la solicitud fue enviada
            return Result.ok("Solicitud enviada correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que actualiza el estado de una peticion. Primero valida que la
     * peticion exista, que tenga un id valido y que tenga un estado
     * seleccionado.
     *
     * @param request solicitud con la peticion que se desea actualizar
     * @return resultado de la actualizacion del estado
     */
    public Result<?> updateEstadoPeticion(Request<Peticion> request) {
        try {
            // Se obtiene la peticion enviada en el request
            Peticion p = request.getData();

            // Se valida que la peticion no sea nula
            if (p == null) {
                return Result.fail("message:Datos de petición inválidos\nError:400");
            }

            // Se valida que el id de la peticion sea correcto
            if (p.getIdPeticion() <= 0) {
                return Result.fail("message:ID de petición inválido\nError:400");
            }

            // Se valida que el estado de la peticion no venga vacio
            if (p.getEstado() == null) {
                return Result.fail("message:Estado de petición inválido\nError:400");
            }

            // Se envia la peticion al servicio para actualizar su estado
            Result<Void> result = PeticionService.updateEstadoPeticion(p);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que la peticion fue actualizada
            return Result.ok("Petición actualizada correctamente");

        } catch (Exception e) {
            // Se imprime el error en consola para poder revisar el problema
            e.printStackTrace();

            // Se devuelve un mensaje general de error
            return Result.fail("message:Error inesperado al actualizar petición\nError:500");
        }
    }
}
