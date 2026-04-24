/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

/**
 *
 * @author Fabian
 */
import common.dto.Result;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import server.dao.TurnoDAO;
import server.model.Turno;
import java.sql.*;

public class TurnoService {

    /**
     * Metodo que carga todos los turnos. Obtiene los turnos desde el DAO y los
     * agrega a la lista recibida.
     *
     * @param lista lista donde se guardaran los turnos
     * @return lista con los turnos cargados
     */
    public static ArrayList<Turno> loadTurno(ArrayList<Turno> lista) {
        try {
            // Se obtienen todos los turnos activos desde la base de datos
            ArrayList<Turno> turnos = (ArrayList<Turno>) TurnoDAO.obtenerTodos();

            // Se agregan los turnos obtenidos a la lista recibida
            for (Turno t : turnos) {
                lista.add(t);
            }

            return lista;

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que carga los turnos relacionados con un id. Primero intenta
     * buscar turnos como cliente y si no encuentra, busca turnos como empleado.
     *
     * @param id id del cliente o empleado
     * @return lista de turnos encontrados
     */
    public static ArrayList<Turno> loadGetTurnos(int id) {
        try {
            // Se buscan primero los turnos asociados al cliente
            ArrayList<Turno> turnosCliente = (ArrayList<Turno>) TurnoDAO.obtenerPorCliente(id);

            // Si se encuentran turnos como cliente, se devuelven
            if (!turnosCliente.isEmpty()) {
                return turnosCliente;
            }

            // Si no se encontraron como cliente, se buscan como empleado
            ArrayList<Turno> turnosEmpleado = (ArrayList<Turno>) TurnoDAO.obtenerPorEmpleado(id);

            // Si se encuentran turnos como empleado, se devuelven
            if (!turnosEmpleado.isEmpty()) {
                return turnosEmpleado;
            }

            // Si no se encontro nada, se devuelve una lista vacia
            return new ArrayList<>();

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que actualiza un turno. Valida que el turno y sus datos
     * principales sean correctos antes de enviarlo al DAO.
     *
     * @param t turno con los datos actualizados
     * @return resultado de la actualizacion
     */
    public static Result<Void> updateTurno(Turno t) {
        try {
            // Se valida que el turno no venga vacio
            if (t == null) {
                return Result.fail("message:Turno inválido\nError:400");
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

            // Se valida que tenga un estado seleccionado
            if (t.getEstado() == null) {
                return Result.fail("message:Estado inválido\nError:400");
            }

            // Se envia el turno al DAO para actualizarlo
            boolean actualizado = TurnoDAO.updateTurno(t);

            // Si no se pudo actualizar, se devuelve error
            if (!actualizado) {
                return Result.fail("message:No se pudo actualizar el turno\nError:500");
            }

            // Si todo salio bien, se confirma la actualizacion
            return Result.ok("Turno actualizado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar turno\nError:500");
        }
    }

    /**
     * Metodo que inserta un nuevo turno. Valida que el turno exista y luego lo
     * envia al DAO para guardarlo.
     *
     * @param t turno que se desea insertar
     * @return resultado de la insercion
     */
    public static Result<Void> insertTurn(Turno t) {
        try {
            // Se valida que el turno no venga vacio
            if (t == null) {
                return Result.fail("message:Turno inválido\nError:400");
            }

            // Se intenta insertar el turno en la base de datos
            boolean insertado = TurnoDAO.insertarTurno(t);

            // Se devuelve respuesta segun si se pudo insertar o no
            if (insertado) {
                return Result.ok("Turno insertado correctamente");
            } else {
                return Result.fail("message:No se pudo insertar el turno\nError:500");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Si el cliente o empleado no existe, se devuelve el mensaje recibido
            return Result.fail("message:" + e.getMessage() + "\nError:400");
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado al insertar turno\nError:500");
        }
    }

    /**
     * Metodo que elimina un turno. Primero valida el id, revisa que el turno
     * exista y luego lo cancela en la base de datos.
     *
     * @param idTurno id del turno que se desea eliminar
     * @return resultado de la eliminacion
     */
    public static Result<Void> deleteTurno(int idTurno) {
        try {
            // Se valida que el id del turno sea correcto
            if (idTurno <= 0) {
                return Result.fail("message:ID de turno inválido\nError:400");
            }

            // Se busca el turno para confirmar que exista
            Turno turnoActual = TurnoDAO.obtenerPorId(idTurno);

            // Si no existe, se devuelve error
            if (turnoActual == null) {
                return Result.fail("message:Turno no encontrado\nError:404");
            }

            // Se cancela el turno en la base de datos
            boolean eliminado = TurnoDAO.cancelarTurno(idTurno);

            // Si no se pudo cancelar, se devuelve error
            if (!eliminado) {
                return Result.fail("message:No se pudo eliminar el turno\nError:500");
            }

            // Si todo salio bien, se confirma la eliminacion
            return Result.ok("Turno eliminado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al eliminar turno\nError:500");
        }
    }

}
