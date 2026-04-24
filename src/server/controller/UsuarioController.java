/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

import common.dto.ChangePasswordDTO;
import common.dto.Request;
import common.dto.Result;
import java.util.ArrayList;
import server.model.Usuario;
import server.service.AuthService;
import server.service.UsuarioService;

public class UsuarioController {

    /**
     * Metodo que carga todos los usuarios registrados. Crea una lista vacia y
     * le pide al servicio que cargue los datos.
     *
     * @param request solicitud recibida desde el cliente
     * @return lista con los usuarios cargados
     */
    public Result<ArrayList<Usuario>> loadUser(Request<?> request) {
        try {
            // Se crea la lista donde se van a guardar los usuarios
            ArrayList<Usuario> lista = new ArrayList<>();

            // Se le pide al servicio que cargue los usuarios en la lista
            lista = UsuarioService.loadUser(lista);

            // Se devuelve la lista cargada correctamente
            return Result.ok("Datos cargados", lista);
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que crea un nuevo usuario. Primero valida los datos recibidos y
     * luego registra el usuario segun el rol que tenga asignado.
     *
     * @param request solicitud con los datos del usuario
     * @return resultado de la creacion del usuario
     */
    public Result<?> createUser(Request<?> request) {
        try {
            // Se obtiene el usuario enviado dentro del request
            Usuario u = (Usuario) request.getData();

            // Se valida que el usuario no venga vacio
            if (u == null) {
                return Result.fail("message:Datos de usuario inválidos\nError:400");
            }

            // Se valida que el usuario tenga un rol asignado
            if (u.getRol() == null) {
                return Result.fail("message:Rol inválido\nError:400");
            }

            // Variable para guardar el resultado del registro en autenticacion
            Result<Void> primerResult;

            // Se revisa el rol para saber como se debe guardar el usuario
            switch (u.getRol()) {
                case CLIENTE:
                    // Resultado para guardar el usuario como cliente
                    Result<Void> segundoResult;

                    // Primero se registra el usuario en autenticacion
                    primerResult = AuthService.register(
                            u.getUsername(),
                            u.getPasswordHash(),
                            u.getTelefono(),
                            u.getName(),
                            u.getRol()
                    );

                    // Si falla el registro de autenticacion, se devuelve ese error
                    if (!primerResult.ok) {
                        return primerResult;
                    }

                    // Luego se registra el usuario como cliente
                    segundoResult = UsuarioService.insertClient(u);

                    // Si falla el registro como cliente, se devuelve ese error
                    if (!segundoResult.ok) {
                        return segundoResult;
                    }

                    // Si todo salio bien, se confirma la creacion del cliente
                    return Result.ok("Usuario cliente creado correctamente");

                case EMPLEADO:
                    // Resultado para guardar el usuario como empleado
                    Result<Void> tercerResult;

                    // Primero se registra el usuario en autenticacion
                    primerResult = AuthService.register(
                            u.getUsername(),
                            u.getPasswordHash(),
                            u.getTelefono(),
                            u.getName(),
                            u.getRol()
                    );

                    // Si falla el registro de autenticacion, se devuelve ese error
                    if (!primerResult.ok) {
                        return primerResult;
                    }

                    // Luego se registra el usuario como empleado
                    tercerResult = UsuarioService.insertEmployee(u);

                    // Si falla el registro como empleado, se devuelve ese error
                    if (!tercerResult.ok) {
                        return tercerResult;
                    }

                    // Si todo salio bien, se confirma la creacion del empleado
                    return Result.ok("Usuario empleado creado correctamente");

                default:
                    // Para otros roles, solo se registra el usuario en autenticacion
                    primerResult = AuthService.register(
                            u.getUsername(),
                            u.getPasswordHash(),
                            u.getTelefono(),
                            u.getName(),
                            u.getRol()
                    );

                    // Si falla el registro, se devuelve ese error
                    if (!primerResult.ok) {
                        return primerResult;
                    }

                    // Si todo salio bien, se confirma la creacion del usuario
                    return Result.ok("Usuario creado correctamente");
            }

        } catch (Exception e) {
            // Se imprime el error en consola para revisar el problema
            e.printStackTrace();

            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que cambia la contraseña de un usuario. Recibe los datos
     * necesarios y se los envia al servicio.
     *
     * @param request solicitud con los datos del cambio de contraseña
     * @return resultado del cambio de contraseña
     */
    public Result<?> changePassword(Request<ChangePasswordDTO> request) {
        try {
            // Se obtiene el DTO con los datos para cambiar la contraseña
            ChangePasswordDTO changePasswordDTO = request.getData();

            // Se envia al servicio para realizar el cambio de contraseña
            Result<Void> result = UsuarioService.changePassword(changePasswordDTO);

            // Se devuelve el resultado recibido del servicio
            return result;

        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo que actualiza la informacion de un usuario. Primero valida que los
     * datos principales sean correctos y luego envia el usuario al servicio
     * para actualizarlo.
     *
     * @param request solicitud con el usuario actualizado
     * @return resultado de la actualizacion del usuario
     */
    public Result<?> updateUser(Request<Usuario> request) {
        try {
            // Se obtiene el usuario que se desea actualizar
            Usuario u = request.getData();

            // Se valida que el usuario no venga vacio
            if (u == null) {
                return Result.fail("message:Datos de usuario inválidos\nError:400");
            }

            // Se valida que el id del usuario sea correcto
            if (u.getIdUsuario() <= 0) {
                return Result.fail("message:ID de usuario inválido\nError:400");
            }

            // Se valida que el email o username no venga vacio
            if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
                return Result.fail("message:El email no puede estar vacío\nError:400");
            }

            // Se valida que la contraseña no venga vacia
            if (u.getPasswordHash() == null || u.getPasswordHash().trim().isEmpty()) {
                return Result.fail("message:La contraseña no puede estar vacía\nError:400");
            }

            // Se valida que el telefono no venga vacio
            if (u.getTelefono() == null || u.getTelefono().trim().isEmpty()) {
                return Result.fail("message:El teléfono no puede estar vacío\nError:400");
            }

            // Se valida que el nombre no venga vacio
            if (u.getName() == null || u.getName().trim().isEmpty()) {
                return Result.fail("message:El nombre no puede estar vacío\nError:400");
            }

            // Se valida que el usuario tenga un rol asignado
            if (u.getRol() == null) {
                return Result.fail("message:Rol inválido\nError:400");
            }

            // Se envia el usuario al servicio para actualizarlo
            Result<Void> result = UsuarioService.updateUser(u);

            // Si el servicio devuelve error, se retorna ese resultado
            if (!result.ok) {
                return result;
            }

            // Si todo salio bien, se confirma que el usuario fue actualizado
            return Result.ok("Usuario actualizado correctamente");

        } catch (Exception e) {
            // Se imprime el error en consola para revisar el problema
            e.printStackTrace();

            // Se devuelve un mensaje general de error
            return Result.fail("message:Error inesperado al actualizar usuario\nError:500");
        }
    }

    /**
     * Metodo que elimina un usuario. Toma el id recibido en el request, valida
     * que sea correcto y luego lo envia al servicio para eliminarlo.
     *
     * @param request solicitud con el id del usuario
     * @return resultado de la eliminacion del usuario
     */
    public Result<?> deleteUser(Request<Integer> request) {
        try {
            // Se obtiene el id del usuario enviado en el request
            Integer idUsuario = request.getData();

            // Se valida que el id exista y sea mayor a cero
            if (idUsuario == null || idUsuario <= 0) {
                return Result.fail("message:ID de usuario inválido\nError:400");
            }

            // Se envia el id al servicio para eliminar el usuario
            return UsuarioService.deleteUser(idUsuario);

        } catch (Exception e) {
            // Se imprime el error en consola para revisar el problema
            e.printStackTrace();

            // Se devuelve un mensaje general de error
            return Result.fail("message:Error inesperado al eliminar usuario\nError:500");
        }
    }
}
