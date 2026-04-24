/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

/**
 *
 * @author Fabian
 */
import common.dto.Request;
import common.dto.Result;
import java.util.ArrayList;
import server.model.Turno;

import server.service.TurnoService;

public class TurnoController {

    /**
     * Metodo que carga todos los turnos registrados. Crea una lista vacia y le
     * pide al servicio que cargue los datos.
     *
     * @param request solicitud recibida desde el cliente
     * @return lista con los turnos cargados
     */
    public Result<ArrayList<Turno>> loadTurno(Request<?> request) {
        try {
            // Se crea la lista donde se van a guardar los turnos
            ArrayList<Turno> lista = new ArrayList<>();

            // Se le pide al servicio que cargue los turnos en la lista
            lista = TurnoService.loadTurno(lista);

            // Se devuelve la lista cargada correctamente
            return Result.ok("Datos cargados", lista);
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que carga los turnos de un usuario especifico. Usa el id recibido
     * en el request para buscar sus turnos.
     *
     * @param request solicitud con el id del usuario
     * @return lista con los turnos del usuario
     */
    public Result<ArrayList<Turno>> loadGetTurnos(Request<Integer> request) {
        try {
            // Se carga la lista de turnos usando el id recibido
            ArrayList<Turno> lista = TurnoService.loadGetTurnos(request.getData());

            // Se devuelve la lista cargada correctamente
            return Result.ok("Datos cargados", lista);
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que crea un nuevo turno. Toma el turno recibido en el request,
     * valida que exista y luego lo envia al servicio para guardarlo.
     *
     * @param request solicitud con los datos del turno
     * @return resultado de la creacion del turno
     */
    public Result<?> createTurn(Request<?> request) {
        try {
            // Se obtiene el turno enviado dentro del request
            Turno t = (Turno) request.getData();

            // Se valida que el turno no venga vacio
            if (t == null) {
                return Result.fail("message:Datos de turno inválidos\nError:400");
            }

            // Se envia el turno al servicio para guardarlo
            Result<Void> result = TurnoService.insertTurn(t);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que el turno fue creado
            return Result.ok("Turno creado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que actualiza la informacion de un turno. Primero valida que el
     * turno y sus datos principales sean correctos, luego lo envia al servicio
     * para actualizarlo.
     *
     * @param request solicitud con el turno actualizado
     * @return resultado de la actualizacion del turno
     */
    public Result<?> updateTurno(Request<Turno> request) {
        try {
            // Se obtiene el turno que se desea actualizar
            Turno t = request.getData();

            // Se valida que el turno no venga vacio
            if (t == null) {
                return Result.fail("message:Datos de turno inválidos\nError:400");
            }

            // Se valida que el id del turno sea correcto
            if (t.getIdTurno() <= 0) {
                return Result.fail("message:ID de turno inválido\nError:400");
            }

            // Se valida que el id del cliente sea correcto
            if (t.getIdCliente() <= 0) {
                return Result.fail("message:ID de cliente inválido\nError:400");
            }

            // Se valida que el id del empleado sea correcto
            if (t.getIdEmpleado() <= 0) {
                return Result.fail("message:ID de empleado inválido\nError:400");
            }

            // Se valida que la fecha no venga vacia
            if (t.getFecha() == null) {
                return Result.fail("message:Fecha inválida\nError:400");
            }

            // Se valida que la hora no venga vacia
            if (t.getHora() == null) {
                return Result.fail("message:Hora inválida\nError:400");
            }

            // Se valida que el estado no venga vacio
            if (t.getEstado() == null) {
                return Result.fail("message:Estado inválido\nError:400");
            }

            // Se envia el turno al servicio para actualizarlo
            Result<Void> result = TurnoService.updateTurno(t);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que el turno fue actualizado
            return Result.ok("Turno actualizado correctamente");

        } catch (Exception e) {
            // Se imprime el error en consola para poder revisar el problema
            e.printStackTrace();

            // Se devuelve un mensaje general de error
            return Result.fail("message:Error inesperado al actualizar turno\nError:500");
        }
    }

    /**
     * Metodo que elimina un turno. Toma el id recibido en el request, valida
     * que sea correcto y luego lo envia al servicio para eliminarlo.
     *
     * @param request solicitud con el id del turno
     * @return resultado de la eliminacion del turno
     */
    public Result<?> deleteTurno(Request<Integer> request) {
        try {
            // Se obtiene el id del turno enviado en el request
            Integer idTurno = request.getData();

            // Se valida que el id exista y sea mayor a cero
            if (idTurno == null || idTurno <= 0) {
                return Result.fail("message:ID de turno inválido\nError:400");
            }

            // Se envia el id al servicio para eliminar el turno
            Result<Void> result = TurnoService.deleteTurno(idTurno);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que el turno fue eliminado
            return Result.ok("Turno eliminado correctamente");

        } catch (Exception e) {
            // Se imprime el error en consola para poder revisar el problema
            e.printStackTrace();

            // Se devuelve un mensaje general de error
            return Result.fail("message:Error inesperado al eliminar turno\nError:500");
        }
    }
}
