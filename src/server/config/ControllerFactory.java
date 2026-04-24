/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.config;

import server.controller.*;

/**
 *
 * @author Fabian
 */
public class ControllerFactory {

    // Controlador encargado de manejar el login, registro y recuperacion de contraseña
    public static final AuthController AUTH = new AuthController();

    // Controlador encargado de manejar las operaciones de usuarios
    public static final UsuarioController USUARIO = new UsuarioController();

    // Controlador encargado de manejar las operaciones de turnos
    public static final TurnoController TURNO = new TurnoController();

    // Controlador encargado de manejar la informacion del dashboard
    public static final DashboardController DASHBOARD = new DashboardController();

    // Controlador encargado de manejar las peticiones o solicitudes
    public static final PeticionController PETICION = new PeticionController();
}
