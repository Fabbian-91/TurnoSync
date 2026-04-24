/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.network;

import common.dto.ChangePasswordDTO;
import common.dto.ForgetPasswordDTO;
import common.dto.LoginRequestDTO;
import common.dto.RegisterDTO;
import common.dto.Request;
import common.dto.Result;
import server.config.ControllerFactory;
import server.model.Peticion;
import server.model.Turno;
import server.model.Usuario;

public class ProtocolDispatcher {

    /**
     * Metodo que recibe una solicitud del cliente y decide que controlador debe atenderla.
     * Segun la accion recibida, redirige el request al metodo correspondiente.
     *
     * @param request solicitud recibida desde el cliente
     * @return resultado de la accion ejecutada
     */
    public static Result<?> dispatch(Request<?> request) {
        // Se revisa la accion que viene dentro del request
        switch (request.getAction()) {

            case PING:
                // Respuesta simple para comprobar que el servidor esta activo
                return Result.ok("pong");

            case LOGIN:
                // Envia la solicitud al controlador de autenticacion para iniciar sesion
                return ControllerFactory.AUTH.login((Request<LoginRequestDTO>) request);

            case REGISTER:
                // Envia la solicitud al controlador de autenticacion para registrar un usuario
                return ControllerFactory.AUTH.register((Request<RegisterDTO>) request);

            case RECOVER_PASSWORD:
                // Envia la solicitud al controlador de autenticacion para recuperar contraseña
                return ControllerFactory.AUTH.forget((Request<ForgetPasswordDTO>) request);

            case LOAD_CARDS:
                // Carga la informacion de las tarjetas del dashboard
                return ControllerFactory.DASHBOARD.loadCard(request);

            case LOAD_BOARD:
                // Carga los datos del tablero
                return ControllerFactory.DASHBOARD.loadBoard(request);

            case GESTIONAR_USUARIOS:
                // Carga la lista de usuarios para gestionarlos
                return ControllerFactory.USUARIO.loadUser(request);

            case CREAR_USUARIO:
                // Envia la solicitud para crear un nuevo usuario
                return ControllerFactory.USUARIO.createUser(request);

            case GESTIONAR_TURNOS:
                // Carga la lista general de turnos
                return ControllerFactory.TURNO.loadTurno(request);

            case GET_TURNOS:
                // Carga los turnos relacionados con un usuario o cliente especifico
                return ControllerFactory.TURNO.loadGetTurnos((Request<Integer>) request);

            case CREAR_TURNO:
                // Envia la solicitud para crear un nuevo turno
                return ControllerFactory.TURNO.createTurn(request);

            case LOAD_HISTORIAL:
                // Carga el historial de peticiones
                return ControllerFactory.PETICION.loadHistorial(request);

            case LOAD_ALLPETICION:
                // Carga las peticiones pendientes o disponibles para atender
                return ControllerFactory.PETICION.loadAtender(request);

            case CREAR_PETICION:
                // Envia la solicitud para crear una nueva peticion
                return ControllerFactory.PETICION.postPeticion(request);

            case CHANGE_PASSWORD:
                // Envia la solicitud para cambiar la contraseña del usuario
                return ControllerFactory.USUARIO.changePassword((Request<ChangePasswordDTO>) request);
                
            case UPDATE_USER:
                // Envia la solicitud para actualizar la informacion de un usuario
                return ControllerFactory.USUARIO.updateUser((Request<Usuario>) request);
                
            case DELETE_USER:
                // Envia la solicitud para eliminar un usuario
                return ControllerFactory.USUARIO.deleteUser((Request<Integer>) request);
                
            case UPDATE_TURNO:
                // Envia la solicitud para actualizar la informacion de un turno
                return ControllerFactory.TURNO.updateTurno((Request<Turno>) request);
                
            case DELETE_TURNO:
                // Envia la solicitud para eliminar un turno
                return ControllerFactory.TURNO.deleteTurno((Request<Integer>) request);
                
            case UPDATE_ESTADO_PETICION:
                // Envia la solicitud para actualizar el estado de una peticion
                return ControllerFactory.PETICION.updateEstadoPeticion((Request<Peticion>) request);
                
            default:
                // Si la accion no existe o no esta soportada, se devuelve un error
                return Result.fail("Acción no soportada");
        }
    }
}
