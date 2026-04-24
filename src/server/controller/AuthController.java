/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

import common.dto.ForgetPasswordDTO;
import common.dto.LoginDTO;
import common.dto.LoginRequestDTO;
import common.dto.RegisterDTO;
import common.dto.Request;
import common.dto.Result;
import common.enums.Rol;
import server.model.Usuario;
import server.service.AuthService;
import server.service.UsuarioService;

public class AuthController {

    /**
     * Metodo que registra un nuevo usuario. Primero crea el usuario con los
     * datos recibidos, luego lo registra en autenticacion y despues como
     * empleado.
     *
     * @param request solicitud con los datos del registro
     * @return resultado del registro
     */
    public Result<Void> register(Request<RegisterDTO> request) {
        try {
            // Variables para guardar los resultados de cada registro
            Result<Void> primerResult;
            Result<Void> segundoResult;

            // Se crea un usuario nuevo para cargarle los datos recibidos
            Usuario u = new Usuario();

            // Se toman los datos del request y se asignan al usuario
            u.setUsername(request.getData().getEmail());
            u.setPasswordHash(request.getData().getPassword());
            u.setTelefono(request.getData().getTelefono());
            u.setName(request.getData().getNombre());

            // Se le asigna el rol de empleado al usuario que se esta registrando
            u.setRol(Rol.EMPLEADO);

            // Primero se registra el usuario en la parte de autenticacion
            primerResult = AuthService.register(
                    u.getUsername(),
                    u.getPasswordHash(),
                    u.getTelefono(),
                    u.getName(),
                    u.getRol()
            );

            // Si el primer registro falla, se devuelve ese error
            if (!primerResult.ok) {
                return primerResult;
            }

            // Si el primer registro salio bien, se registra tambien como empleado
            segundoResult = UsuarioService.insertEmployee(u);

            // Si al registrar el empleado ocurre un error, se devuelve ese resultado
            if (!segundoResult.ok) {
                return segundoResult;
            }

            // Si ambos procesos salieron bien, se devuelve un mensaje de exito
            return Result.ok("Usuario creado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que permite iniciar sesion. Recibe el correo y la contraseña
     * dentro del request y se los envia al servicio de autenticacion.
     *
     * @param request solicitud con los datos del login
     * @return resultado con la informacion del usuario logueado
     */
    public Result<LoginDTO> login(Request<LoginRequestDTO> request) {
        try {
            // Se envia el correo y la contraseña al servicio para validar el login
            return AuthService.login(
                    request.getData().getEmail(),
                    request.getData().getPassword()
            );
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo para recuperar una contraseña olvidada. Recibe el correo y el
     * nombre del usuario, y los envia al servicio para validar la informacion.
     *
     * @param request solicitud con los datos de recuperacion
     * @return resultado de la recuperacion de contraseña
     */
    public Result<Void> forget(Request<ForgetPasswordDTO> request) {
        try {
            // Se envia el correo y el nombre para validar la recuperacion de contraseña
            return AuthService.forget(
                    request.getData().getEmail(),
                    request.getData().getNombre()
            );
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }
}
