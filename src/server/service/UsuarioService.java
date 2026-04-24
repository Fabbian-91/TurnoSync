/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

/**
 *
 * @author Fabian
 */
import common.dto.ChangePasswordDTO;
import common.dto.Result;
import java.util.ArrayList;
import server.dao.ClienteDAO;
import server.dao.EmpleadoDAO;
import server.dao.UsuarioDAO;
import server.model.Cliente;
import server.model.Empleado;

import server.model.Usuario;
import server.security.ClsEncriptar;

public class UsuarioService {

    /**
     * Metodo que carga todos los usuarios. Obtiene los usuarios desde el DAO y
     * los agrega a la lista recibida.
     *
     * @param lista lista donde se guardaran los usuarios
     * @return lista con los usuarios cargados
     */
    public static ArrayList<Usuario> loadUser(ArrayList<Usuario> lista) {
        try {
            // Se obtienen todos los usuarios desde la base de datos
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) UsuarioDAO.obtenerTodosLosUsuarios();

            // Se agregan los usuarios obtenidos a la lista recibida
            for (Usuario usuario : usuarios) {
                lista.add(usuario);
            }

            return lista;

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Metodo que inserta un cliente relacionado a un usuario. Crea el cliente
     * con los datos del usuario y luego enlaza el id del cliente al usuario.
     *
     * @param u usuario que se convertira en cliente
     * @return resultado de la insercion del cliente
     */
    public static Result<Void> insertClient(Usuario u) {
        try {
            // Se valida que el usuario no venga vacio
            if (u == null) {
                return Result.fail("message:Usuario inválido\nError:400");
            }

            // Se crea el cliente usando los datos del usuario
            Cliente c = new Cliente();
            c.setNombre(u.getName());
            c.setTelefono(u.getTelefono());
            c.setEmail(u.getUsername());

            // Se inserta el cliente en la base de datos
            Integer idCliente = ClienteDAO.insertarCliente(c);

            // Si no se genero id, significa que no se pudo insertar
            if (idCliente == null) {
                return Result.fail("message:No se pudo insertar el cliente\nError:500");
            }

            // Se actualiza el usuario para dejarlo relacionado con el cliente creado
            boolean actualizado = UsuarioDAO.actualizarIdCliente(u.getUsername(), idCliente);

            // Si no se pudo actualizar el usuario, se devuelve error
            if (!actualizado) {
                return Result.fail("message:Se creó el cliente, pero no se actualizó el usuario\nError:500");
            }

            return Result.ok("Cliente insertado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al insertar cliente\nError:500");
        }
    }

    /**
     * Metodo que inserta un empleado relacionado a un usuario. Crea el empleado
     * con el nombre del usuario y luego enlaza el id del empleado al usuario.
     *
     * @param u usuario que se convertira en empleado
     * @return resultado de la insercion del empleado
     */
    public static Result<Void> insertEmployee(Usuario u) {
        try {
            // Se valida que el usuario no venga vacio
            if (u == null) {
                return Result.fail("message:Usuario inválido\nError:400");
            }

            // Se crea el empleado usando el nombre del usuario
            Empleado e = new Empleado();
            e.setNombre(u.getName());

            // Se inserta el empleado en la base de datos
            Integer idEmpleado = EmpleadoDAO.insertarEmpleado(e);

            // Si no se genero id, significa que no se pudo insertar
            if (idEmpleado == null) {
                return Result.fail("message:No se pudo insertar el empleado\nError:500");
            }

            // Se actualiza el usuario para dejarlo relacionado con el empleado creado
            boolean actualizado = UsuarioDAO.actualizarIdEmpleado(u.getUsername(), idEmpleado);

            // Si no se pudo actualizar el usuario, se devuelve error
            if (!actualizado) {
                return Result.fail("message:Se creó el empleado, pero no se actualizó el usuario\nError:500");
            }

            return Result.ok("Empleado insertado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al insertar empleado\nError:500");
        }
    }

    /**
     * Metodo que cambia la contraseña de un usuario. Valida los datos
     * recibidos, encripta la nueva contraseña y la actualiza en la base de
     * datos.
     *
     * @param changePasswordDTO datos necesarios para cambiar la contraseña
     * @return resultado del cambio de contraseña
     */
    public static Result<Void> changePassword(ChangePasswordDTO changePasswordDTO) {
        try {
            // Se valida que el DTO no venga vacio
            if (changePasswordDTO == null) {
                return Result.fail("message:Password inválida\nError:400");
            }

            // Se valida que el nombre venga con datos
            if (changePasswordDTO.getName() == null || changePasswordDTO.getName().isEmpty()) {
                return Result.fail("message:Password inválida\nError:400");
            }

            // Se valida que el usuario venga con datos
            if (changePasswordDTO.getUser() == null || changePasswordDTO.getUser().isEmpty()) {
                return Result.fail("message:Password inválida\nError:400");
            }

            // Se valida que la nueva contraseña venga con datos
            if (changePasswordDTO.getPassword() == null || changePasswordDTO.getPassword().isEmpty()) {
                return Result.fail("message:Password inválida\nError:400");
            }

            // Se extraen los datos que se van a usar para actualizar la contraseña
            String email = changePasswordDTO.getUser().toString();
            String name = changePasswordDTO.getName().toString();

            // Se genera un salt nuevo para la contraseña
            String salt = ClsEncriptar.generarSalt();

            // Se encripta la nueva contraseña
            String passwordHash = ClsEncriptar.encriptaSHA256(changePasswordDTO.getPassword().toString(), salt);

            // Se actualiza la contraseña en la base de datos
            boolean actualizar = UsuarioDAO.actualizarPassword(name, email, passwordHash, salt);

            // Si no se pudo actualizar, se devuelve error
            if (!actualizar) {
                System.out.println(actualizar);
                return Result.fail("message:No se pudo cambiar la contraseña\nError:500");
            }

            return Result.ok("La contraseña se actulizo exitosamente");
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve error
            return Result.fail("message:Error inesperado al insertar empleado\nError:500");
        }
    }

    /**
     * Metodo que actualiza un usuario. Valida los datos recibidos, actualiza el
     * usuario principal y luego ajusta su relacion segun el rol que tenga.
     *
     * @param u usuario con los datos actualizados
     * @return resultado de la actualizacion del usuario
     */
    public static Result<Void> updateUser(Usuario u) {
        try {
            // Se valida que el usuario no venga vacio
            if (u == null) {
                return Result.fail("message:Usuario inválido\nError:400");
            }

            // Se valida que el id del usuario sea valido
            if (u.getIdUsuario() <= 0) {
                return Result.fail("message:ID de usuario inválido\nError:400");
            }

            // Se valida que el email venga con datos
            if (u.getUsername() == null || u.getUsername().trim().isEmpty()) {
                return Result.fail("message:Email inválido\nError:400");
            }

            // Se valida que la contraseña venga con datos
            if (u.getPasswordHash() == null || u.getPasswordHash().trim().isEmpty()) {
                return Result.fail("message:Contraseña inválida\nError:400");
            }

            // Se valida que el telefono venga con datos
            if (u.getTelefono() == null || u.getTelefono().trim().isEmpty()) {
                return Result.fail("message:Teléfono inválido\nError:400");
            }

            // Se valida que el nombre venga con datos
            if (u.getName() == null || u.getName().trim().isEmpty()) {
                return Result.fail("message:Nombre inválido\nError:400");
            }

            // Se valida que el rol venga seleccionado
            if (u.getRol() == null) {
                return Result.fail("message:Rol inválido\nError:400");
            }

            // Se busca el usuario actual para saber que relaciones tiene
            Usuario usuarioActual = UsuarioDAO.obtenerUsuarioPorId(u.getIdUsuario());

            // Si no existe el usuario, se devuelve error
            if (usuarioActual == null) {
                return Result.fail("message:Usuario no encontrado\nError:404");
            }

            // Se genera un nuevo salt para la contraseña actualizada
            String salt = ClsEncriptar.generarSalt();

            // Se encripta la contraseña nueva
            String passwordHash = ClsEncriptar.encriptaSHA256(u.getPasswordHash(), salt);

            // Se guardan la contraseña encriptada y el salt en el usuario
            u.setPasswordHash(passwordHash);
            u.setSalt(salt);

            // Se actualizan los datos principales del usuario
            boolean actualizadoUsuario = UsuarioDAO.updateUser(u);

            // Si no se pudo actualizar el usuario, se devuelve error
            if (!actualizadoUsuario) {
                return Result.fail("message:No se pudo actualizar el usuario\nError:500");
            }

            // Segun el rol, se actualiza o limpia la relacion correspondiente
            switch (u.getRol()) {
                case CLIENTE:
                    return actualizarUsuarioComoCliente(u, usuarioActual);

                case EMPLEADO:
                    return actualizarUsuarioComoEmpleado(u, usuarioActual);

                case ADMIN:
                    return actualizarUsuarioComoAdmin(u, usuarioActual);

                default:
                    return Result.fail("message:Rol no soportado\nError:400");
            }

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar usuario\nError:500");
        }
    }

    /**
     * Metodo que actualiza la relacion de un usuario como empleado. Si no tiene
     * empleado relacionado, crea uno nuevo. Si ya tiene empleado, actualiza sus
     * datos. Tambien limpia la relacion de cliente si existia.
     *
     * @param nuevo usuario con los datos nuevos
     * @param actual usuario antes de actualizarse
     * @return resultado de la actualizacion como empleado
     */
    private static Result<Void> actualizarUsuarioComoEmpleado(Usuario nuevo, Usuario actual) {
        try {
            // Se obtiene el id de empleado actual
            int idEmpleado = actual.getIdEmpleado();

            // Si no tiene empleado, se crea uno nuevo
            if (idEmpleado == -1) {
                Empleado e = new Empleado();
                e.setNombre(nuevo.getName());

                Integer nuevoIdEmpleado = EmpleadoDAO.insertarEmpleado(e);

                if (nuevoIdEmpleado == null) {
                    return Result.fail("message:No se pudo crear el empleado\nError:500");
                }

                // Se enlaza el empleado creado con el usuario
                boolean enlazado = UsuarioDAO.actualizarIdEmpleado(nuevo.getIdUsuario(), nuevoIdEmpleado);

                if (!enlazado) {
                    return Result.fail("message:No se pudo enlazar el empleado al usuario\nError:500");
                }

            } else {
                // Si ya tiene empleado, se actualiza su informacion
                Empleado e = new Empleado();
                e.setIdEmpleado((long) idEmpleado);
                e.setNombre(nuevo.getName());

                boolean empleadoActualizado = EmpleadoDAO.actualizarEmpleado(e);

                if (!empleadoActualizado) {
                    return Result.fail("message:No se pudo actualizar el empleado\nError:500");
                }
            }

            // Si antes era cliente, se limpia esa relacion
            if (actual.getIdCliente() != -1) {
                UsuarioDAO.limpiarRelacionCliente(nuevo.getIdUsuario());
            }

            return Result.ok("Usuario empleado actualizado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar empleado\nError:500");
        }
    }

    /**
     * Metodo que actualiza la relacion de un usuario como administrador. Limpia
     * cualquier relacion con cliente o empleado.
     *
     * @param nuevo usuario con los datos nuevos
     * @param actual usuario antes de actualizarse
     * @return resultado de la actualizacion como administrador
     */
    private static Result<Void> actualizarUsuarioComoAdmin(Usuario nuevo, Usuario actual) {
        try {
            // Si tenia relacion con cliente, se elimina esa relacion
            if (actual.getIdCliente() != -1) {
                UsuarioDAO.limpiarRelacionCliente(nuevo.getIdUsuario());
            }

            // Si tenia relacion con empleado, se elimina esa relacion
            if (actual.getIdEmpleado() != -1) {
                UsuarioDAO.limpiarRelacionEmpleado(nuevo.getIdUsuario());
            }

            return Result.ok("Usuario administrador actualizado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar administrador\nError:500");
        }
    }

    /**
     * Metodo que actualiza la relacion de un usuario como cliente. Si no tiene
     * cliente relacionado, crea uno nuevo. Si ya tiene cliente, actualiza sus
     * datos. Tambien limpia la relacion de empleado si existia.
     *
     * @param nuevo usuario con los datos nuevos
     * @param actual usuario antes de actualizarse
     * @return resultado de la actualizacion como cliente
     */
    private static Result<Void> actualizarUsuarioComoCliente(Usuario nuevo, Usuario actual) {
        try {
            // Se obtiene el id de cliente actual
            int idCliente = actual.getIdCliente();

            // Si no tiene cliente, se crea uno nuevo
            if (idCliente == -1) {
                Cliente c = new Cliente();
                c.setNombre(nuevo.getName());
                c.setTelefono(nuevo.getTelefono());
                c.setEmail(nuevo.getUsername());

                Integer nuevoIdCliente = ClienteDAO.insertarCliente(c);

                if (nuevoIdCliente == null) {
                    return Result.fail("message:No se pudo crear el cliente\nError:500");
                }

                // Se enlaza el cliente creado con el usuario
                boolean enlazado = UsuarioDAO.actualizarIdCliente(nuevo.getIdUsuario(), nuevoIdCliente);

                if (!enlazado) {
                    return Result.fail("message:No se pudo enlazar el cliente al usuario\nError:500");
                }

            } else {
                // Si ya tiene cliente, se actualiza su informacion
                Cliente c = new Cliente();
                c.setIdCliente((long) idCliente);
                c.setNombre(nuevo.getName());
                c.setTelefono(nuevo.getTelefono());
                c.setEmail(nuevo.getUsername());

                boolean clienteActualizado = ClienteDAO.actualizarCliente(c);

                if (!clienteActualizado) {
                    return Result.fail("message:No se pudo actualizar el cliente\nError:500");
                }
            }

            // Si antes era empleado, se limpia esa relacion
            if (actual.getIdEmpleado() != -1) {
                UsuarioDAO.limpiarRelacionEmpleado(nuevo.getIdUsuario());
            }

            return Result.ok("Usuario cliente actualizado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al actualizar cliente\nError:500");
        }
    }

    /**
     * Metodo que elimina un usuario. Primero valida que exista, elimina
     * logicamente sus relaciones de cliente o empleado si las tiene, y luego
     * elimina el usuario.
     *
     * @param idUsuario id del usuario que se desea eliminar
     * @return resultado de la eliminacion
     */
    public static Result<Void> deleteUser(int idUsuario) {
        try {
            // Se valida que el id sea correcto
            if (idUsuario <= 0) {
                return Result.fail("message:ID de usuario inválido\nError:400");
            }

            // Se busca el usuario para confirmar que exista
            Usuario usuarioActual = UsuarioDAO.obtenerUsuarioPorId(idUsuario);

            // Si no existe, se devuelve error
            if (usuarioActual == null) {
                return Result.fail("message:Usuario no encontrado\nError:404");
            }

            // Si tiene cliente relacionado, se elimina logicamente ese cliente
            if (usuarioActual.getIdCliente() != -1) {
                boolean clienteEliminado = ClienteDAO.eliminarCliente((long) usuarioActual.getIdCliente());

                if (!clienteEliminado) {
                    return Result.fail("message:No se pudo eliminar el cliente relacionado\nError:500");
                }
            }

            // Si tiene empleado relacionado, se elimina logicamente ese empleado
            if (usuarioActual.getIdEmpleado() != -1) {
                boolean empleadoEliminado = EmpleadoDAO.eliminarEmpleado((long) usuarioActual.getIdEmpleado());

                if (!empleadoEliminado) {
                    return Result.fail("message:No se pudo eliminar el empleado relacionado\nError:500");
                }
            }

            // Finalmente se elimina logicamente el usuario
            boolean usuarioEliminado = UsuarioDAO.eliminarUsuarioPorId(idUsuario);

            // Si no se pudo eliminar el usuario, se devuelve error
            if (!usuarioEliminado) {
                return Result.fail("message:No se pudo eliminar el usuario\nError:500");
            }

            return Result.ok("Usuario eliminado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al eliminar usuario\nError:500");
        }
    }
}
