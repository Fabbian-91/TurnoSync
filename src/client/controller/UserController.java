/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

import client.gateway.ServerGateway;
import common.dto.Result;
import common.enums.Rol;
import java.util.ArrayList;
import server.model.Usuario;

public class UserController {

    private final ServerGateway gateway;

    public UserController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    public Result<ArrayList<Usuario>> loadUsers() {
        return gateway.loadUser();
    }

    public Result<Void> createUser(String username, String password, String confirmPassword,
            String telefono, String nombre, Rol rol) {
        try {
            validarUsername(username);
            validarPassword(password, confirmPassword);
            validarTelefono(telefono);
            validarNombre(nombre);
            validarRol(rol);

            Usuario usuario = new Usuario(username, password, telefono, nombre, rol);
            validarUsuario(usuario);

            return gateway.agregarUsuario(usuario);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al agregar usuario: " + e.getMessage());
        }
    }

    public Result<Void> updatePassword(String email, String name, String password, String confirmPassword) {
        try {
            validarUsername(email);
            validarNombre(name);
            validarPassword(password, confirmPassword);

            return gateway.updatePassword(email, name, password);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al actualizar la contraseña: " + e.getMessage());
        }
    }

    private void validarUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }

        if (username.trim().length() < 4) {
            throw new IllegalArgumentException("El username debe tener al menos 4 caracteres");
        }
    }

    private void validarPassword(String password, String confirmPassword) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe confirmar la contraseña");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        telefono = telefono.replace("-", "");

        if (!telefono.matches("^[0-9]{8,15}$")) {
            throw new IllegalArgumentException("El teléfono debe tener solo números (8 a 15 dígitos)");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
    }

    private void validarRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("Debe seleccionar un rol");
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        validarUsername(usuario.getUsername());
        validarTelefono(usuario.getTelefono());
        validarNombre(usuario.getName());
        validarRol(usuario.getRol());
    }

    public Result<Void> updateUser(int idUsuario, String username, String password, String confirmPassword,
            String telefono, String nombre, Rol rol) {
        try {
            if (idUsuario <= 0) {
                throw new IllegalArgumentException("Debe seleccionar un usuario válido");
            }

            username = username == null ? "" : username.trim();
            password = password == null ? "" : password.trim();
            confirmPassword = confirmPassword == null ? "" : confirmPassword.trim();
            telefono = telefono == null ? "" : telefono.trim().replace("-", "").replace(" ", "");
            nombre = nombre == null ? "" : nombre.trim();

            validarUsername(username);
            validarPassword(password, confirmPassword);
            validarTelefono(telefono);
            validarNombre(nombre);
            validarRol(rol);

            Usuario usuario = new Usuario(username, password, telefono, nombre, rol);
            usuario.setIdUsuario(idUsuario);

            return gateway.updateUser(usuario);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public Result<Void> deleteUser(int idUsuario) {
        try {
            if (idUsuario <= 0) {
                throw new IllegalArgumentException("Debe seleccionar un usuario válido");
            }

            return gateway.deleteUser(idUsuario);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
