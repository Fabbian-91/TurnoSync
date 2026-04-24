/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.gateway;

import client.network.ClientGateway;
import common.dto.CardDataDTO;
import common.dto.ChangePasswordDTO;
import common.dto.ForgetPasswordDTO;
import common.dto.LoginDTO;
import common.dto.LoginRequestDTO;
import common.dto.RegisterDTO;
import common.dto.Request;
import common.dto.Result;
import common.enums.ActionType;
import common.enums.EstadoPeticion;
import common.enums.TipoSolicitud;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import server.model.Peticion;
import server.model.Turno;
import server.model.Usuario;

public class SocketServerGateway implements ServerGateway {

    //Puerta del cliente de comunicación con el server
    private final ClientGateway clientGateway;

    /**
     * Metodo contructor
     *
     * @param clientGateway
     */
    public SocketServerGateway(ClientGateway clientGateway) {
        this.clientGateway = clientGateway;
    }

    /**
     * Metodo sobre escrito de la interfar de puertas para hacer un registro
     *
     * @param email
     * @param password
     * @param number
     * @param name
     * @return
     */
    @Override
    public Result<Void> register(String email, String password, String number, String name) {
        //Generamos un núevo registro y le pasamos los parametros
        RegisterDTO dto = new RegisterDTO(email, password, number, name);

        //Generamos una núeva petición con núevo registro
        Request<RegisterDTO> request = new Request<>();
        //Indicamos el tipo de acción
        request.setAction(ActionType.REGISTER);
        //Seteamos su data por el núevo registro
        request.setData(dto);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<LoginDTO> login(String email, String password) {
        LoginRequestDTO dto = new LoginRequestDTO(email, password);

        Request<LoginRequestDTO> request = new Request<>();
        request.setAction(ActionType.LOGIN);
        request.setData(dto);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<LoginDTO>) result;
    }

    @Override
    public Result<Void> updateForget(String user, String name) {
        ForgetPasswordDTO dto = new ForgetPasswordDTO(user, name);

        Request<ForgetPasswordDTO> request = new Request<>();
        request.setAction(ActionType.RECOVER_PASSWORD);
        request.setData(dto);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<ArrayList<CardDataDTO>> loadCards() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.LOAD_CARDS);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<CardDataDTO>>) result;
    }

    @Override
    public Result<ArrayList<TipoSolicitud>> loadBoard() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.LOAD_BOARD);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<TipoSolicitud>>) result;
    }

    @Override
    public Result<ArrayList<Usuario>> loadUser() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.GESTIONAR_USUARIOS);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<Usuario>>) result;
    }

    @Override
    public Result<ArrayList<Turno>> loadTurno() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.GESTIONAR_TURNOS);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<Turno>>) result;
    }

    @Override
    public Result<ArrayList<Peticion>> loadHistorial() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.LOAD_HISTORIAL);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<Peticion>>) result;
    }

    @Override
    public Result<ArrayList<Peticion>> loadAtender() {
        Request<Void> request = new Request<>();
        request.setAction(ActionType.LOAD_ALLPETICION);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<Peticion>>) result;
    }

    @Override
    public Result<ArrayList<Turno>> loadGetTuro(int id) {
        Request<Integer> request = new Request<>();
        request.setAction(ActionType.GET_TURNOS);
        request.setData(id);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<ArrayList<Turno>>) result;
    }

    @Override
    public Result<Void> agregarUsuario(Usuario u) {
        Request<Usuario> request = new Request<>();
        request.setAction(ActionType.CREAR_USUARIO);
        request.setData(u);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> agregarTurno(Turno t) {
        Request<Turno> request = new Request<>();
        request.setAction(ActionType.CREAR_TURNO);
        request.setData(t);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> eniviarPeticion(Peticion p) {
        Request<Peticion> request = new Request<>();
        request.setAction(ActionType.CREAR_PETICION);
        request.setData(p);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> updatePassword(String email, String name, String password) {
        Request<ChangePasswordDTO> request = new Request<>();
        request.setAction(ActionType.CHANGE_PASSWORD);
        ChangePasswordDTO changePassword = new ChangePasswordDTO(email, name, password);
        request.setData(changePassword);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;

    }

    @Override
    public Result<Void> updateUser(Usuario u) {
        Request<Usuario> request = new Request<>();
        request.setAction(ActionType.UPDATE_USER);
        request.setData(u);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> deleteUser(int idUsuario) {
        Request<Integer> request = new Request<>();
        request.setAction(ActionType.DELETE_USER);
        request.setData(idUsuario);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> updateTurno(Turno t) {
        Request<Turno> request = new Request<>();
        request.setAction(ActionType.UPDATE_TURNO);
        request.setData(t);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> deleteTurno(int idTurno) {
        Request<Integer> request = new Request<>();
        request.setAction(ActionType.DELETE_TURNO);
        request.setData(idTurno);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }

    @Override
    public Result<Void> updateEstadoPeticion(int idPeticion, EstadoPeticion estado) {
        Peticion peticion = new Peticion();
        peticion.setIdPeticion(idPeticion);
        peticion.setEstado(estado);

        Request<Peticion> request = new Request<>();
        request.setAction(ActionType.UPDATE_ESTADO_PETICION);
        request.setData(peticion);

        Result<?> result = clientGateway.sendRequest(request);
        return (Result<Void>) result;
    }
}
