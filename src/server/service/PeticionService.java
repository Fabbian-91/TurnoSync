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
import java.util.ArrayList;
import server.dao.PeticionDAO;
import java.sql.*;
import server.dao.UsuarioDAO;

import server.model.Peticion;

public class PeticionService {

    /**
     * Metodo que carga el historial de peticiones. Obtiene todas las peticiones
     * desde el DAO y las agrega a la lista recibida.
     *
     * @param lista lista donde se guardaran las peticiones
     * @return lista con el historial cargado
     */
    public static ArrayList<Peticion> loadHistorial(ArrayList<Peticion> lista) {
        try {
            // Se obtienen todas las peticiones desde la base de datos
            ArrayList<Peticion> peticiones = (ArrayList<Peticion>) PeticionDAO.obtenerTodas();

            // Se agregan las peticiones obtenidas a la lista recibida
            for (Peticion p : peticiones) {
                lista.add(p);
            }

            return lista;

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que carga las peticiones para atender. Obtiene las peticiones
     * junto con la informacion del usuario.
     *
     * @param lista lista donde se guardaran las peticiones
     * @return lista con las peticiones cargadas
     */
    public static ArrayList<Peticion> loadAtender(ArrayList<Peticion> lista) {
        try {
            // Se obtienen las peticiones con los datos completos del usuario
            ArrayList<Peticion> peticiones = (ArrayList<Peticion>) PeticionDAO.obtenerTodasConUsuario();

            // Se agregan las peticiones obtenidas a la lista recibida
            for (Peticion p : peticiones) {
                lista.add(p);
            }

            return lista;

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que inserta una nueva peticion. Primero valida los datos
     * principales, busca el id del usuario y luego envia la peticion al DAO
     * para guardarla.
     *
     * @param p peticion que se desea insertar
     * @return resultado de la insercion
     */
    public static Result<Void> insertPeticion(Peticion p) {
        try {
            // Se valida que la peticion no venga vacia
            if (p == null) {
                return Result.fail("message:Petición inválida\nError:400");
            }

            // Se valida que la peticion tenga un usuario
            if (p.getUsuario() == null) {
                return Result.fail("message:Usuario requerido\nError:400");
            }

            // Se valida que el usuario tenga username
            if (p.getUsuario().getUsername() == null || p.getUsuario().getUsername().trim().isEmpty()) {
                return Result.fail("message:Username requerido\nError:400");
            }

            // Se valida que la peticion tenga un tipo de solicitud
            if (p.getTipo() == null) {
                return Result.fail("message:Tipo de solicitud requerido\nError:400");
            }

            // Se valida que la peticion tenga detalle
            if (p.getDetalle() == null || p.getDetalle().trim().isEmpty()) {
                return Result.fail("message:Detalle requerido\nError:400");
            }

            // Se busca el id del usuario usando su username
            Integer idUsuario = UsuarioDAO.obtenerIdPorUsername(p.getUsuario().getUsername());

            // Si no se encuentra el usuario, no se puede crear la peticion
            if (idUsuario == null) {
                return Result.fail("message:Usuario no encontrado\nError:404");
            }

            // Se asigna el id encontrado al usuario de la peticion
            p.getUsuario().setIdUsuario(idUsuario);

            // Se manda la peticion al DAO para insertarla en la base de datos
            boolean insertado = PeticionDAO.insertarPeticion(p);

            // Se devuelve respuesta segun si se pudo insertar o no
            if (insertado) {
                return Result.ok("Petición insertada correctamente");
            } else {
                return Result.fail("message:No se pudo insertar la petición\nError:500");
            }

        } catch (SQLException e) {
            // Si ocurre un error de base de datos, se devuelve un mensaje claro
            return Result.fail("message:Error de base de datos\nError:500");
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado al insertar petición\nError:500");
        }
    }

    /**
     * Metodo que actualiza el estado de una peticion. Valida que la peticion
     * exista y luego envia el nuevo estado al DAO.
     *
     * @param p peticion con el id y el nuevo estado
     * @return resultado de la actualizacion
     */
    public static Result<Void> updateEstadoPeticion(Peticion p) {
        try {
            // Se valida que la peticion no venga vacia
            if (p == null) {
                return Result.fail("message:Petición inválida\nError:400");
            }

            // Se valida que el id de la peticion sea correcto
            if (p.getIdPeticion() <= 0) {
                return Result.fail("message:ID de petición inválido\nError:400");
            }

            // Se valida que tenga un estado seleccionado
            if (p.getEstado() == null) {
                return Result.fail("message:Estado de petición inválido\nError:400");
            }

            // Se busca la peticion actual para confirmar que exista
            Peticion peticionActual = PeticionDAO.obtenerPorId(p.getIdPeticion());

            // Si no existe, se devuelve error
            if (peticionActual == null) {
                return Result.fail("message:Petición no encontrada\nError:404");
            }

            // Se envia al DAO el id y el nuevo estado para actualizarlo
            boolean actualizado = PeticionDAO.updateEstadoPeticion(
                    p.getIdPeticion(),
                    p.getEstado()
            );

            // Si no se pudo actualizar, se devuelve error
            if (!actualizado) {
                return Result.fail("message:No se pudo actualizar la petición\nError:500");
            }

            // Si todo salio bien, se confirma la actualizacion
            return Result.ok("Petición actualizada correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar petición\nError:500");
        }
    }
}
