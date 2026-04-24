/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package client.gateway;

import common.dto.CardDataDTO;
import common.dto.LoginDTO;
import common.dto.Result;
import common.enums.ActionType;
import common.enums.EstadoPeticion;
import common.enums.TipoSolicitud;
import java.util.ArrayList;
import server.model.Peticion;
import server.model.Turno;
import server.model.Usuario;

/**
 *
 * @author Fabian
 */
// Interfaz que define los metodos de comunicacion con el servidor
public interface ServerGateway {

    /**
     * Metodo para registrar un nuevo usuario. Devuelve solamente el resultado
     * de la operacion.
     *
     * @param email correo del usuario
     * @param password contraseña del usuario
     * @param n telefono o dato adicional del usuario
     * @param na nombre del usuario
     * @return resultado del registro
     */
    Result<Void> register(String email, String password, String n, String na);

    /**
     * Metodo para iniciar sesion. Envia el correo y la contraseña al servidor.
     *
     * @param email correo del usuario
     * @param password contraseña del usuario
     * @return resultado con la informacion del login
     */
    Result<LoginDTO> login(String email, String password);

    /**
     * Metodo para recuperar o actualizar una contraseña olvidada. Envia el
     * usuario y el nombre para validar la informacion.
     *
     * @param user usuario o correo
     * @param name nombre del usuario
     * @return resultado de la actualizacion
     */
    Result<Void> updateForget(String user, String name);

    /**
     * Metodo que carga los datos de las tarjetas del dashboard.
     *
     * @return lista con la informacion de las tarjetas
     */
    Result<ArrayList<CardDataDTO>> loadCards();

    /**
     * Metodo que carga los tipos de solicitud para el tablero.
     *
     * @return lista con los tipos de solicitud
     */
    Result<ArrayList<TipoSolicitud>> loadBoard();

    /**
     * Metodo que carga todos los usuarios registrados.
     *
     * @return lista de usuarios
     */
    Result<ArrayList<Usuario>> loadUser();

    /**
     * Metodo que carga todos los turnos registrados.
     *
     * @return lista de turnos
     */
    Result<ArrayList<Turno>> loadTurno();

    /**
     * Metodo que carga el historial de peticiones.
     *
     * @return lista de peticiones del historial
     */
    Result<ArrayList<Peticion>> loadHistorial();

    /**
     * Metodo que carga las peticiones pendientes por atender.
     *
     * @return lista de peticiones pendientes
     */
    Result<ArrayList<Peticion>> loadAtender();

    /**
     * Metodo que carga los turnos de un usuario especifico.
     *
     * @param id id del usuario
     * @return lista de turnos del usuario
     */
    Result<ArrayList<Turno>> loadGetTuro(int id);

    /**
     * Metodo para agregar un nuevo usuario.
     *
     * @param u usuario que se desea agregar
     * @return resultado de la operacion
     */
    Result<Void> agregarUsuario(Usuario u);

    /**
     * Metodo para agregar un nuevo turno.
     *
     * @param t turno que se desea agregar
     * @return resultado de la operacion
     */
    Result<Void> agregarTurno(Turno t);

    /**
     * Metodo para enviar una nueva peticion.
     *
     * @param p peticion que se desea enviar
     * @return resultado de la operacion
     */
    Result<Void> eniviarPeticion(Peticion p);

    /**
     * Metodo para actualizar la contraseña de un usuario.
     *
     * @param email correo o usuario
     * @param name nombre del usuario
     * @param password nueva contraseña
     * @return resultado de la actualizacion
     */
    Result<Void> updatePassword(String email, String name, String password);

    /**
     * Metodo para actualizar la informacion de un usuario.
     *
     * @param u usuario con los datos actualizados
     * @return resultado de la actualizacion
     */
    Result<Void> updateUser(Usuario u);

    /**
     * Metodo para eliminar un usuario.
     *
     * @param idUsuario id del usuario que se desea eliminar
     * @return resultado de la eliminacion
     */
    Result<Void> deleteUser(int idUsuario);

    /**
     * Metodo para actualizar la informacion de un turno.
     *
     * @param t turno con los datos actualizados
     * @return resultado de la actualizacion
     */
    Result<Void> updateTurno(Turno t);

    /**
     * Metodo para eliminar un turno.
     *
     * @param idTurno id del turno que se desea eliminar
     * @return resultado de la eliminacion
     */
    Result<Void> deleteTurno(int idTurno);

    /**
     * Metodo para actualizar el estado de una peticion.
     *
     * @param idPeticion id de la peticion
     * @param estado nuevo estado de la peticion
     * @return resultado de la actualizacion
     */
    Result<Void> updateEstadoPeticion(int idPeticion, EstadoPeticion estado);
}
