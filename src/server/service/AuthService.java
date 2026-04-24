/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

import common.dto.LoginDTO;
import common.dto.Result;
import common.enums.EstadoCuenta;
import common.enums.Rol;
import java.sql.SQLException;
import server.dao.UsuarioDAO;
import server.model.Usuario;
import server.security.ClsCreatePasswordRandom;

import server.security.ClsEncriptar;

public class AuthService {

    /**
     * Metodo para registrar un usuario aplicando las reglas de negocio.
     * Valida los datos recibidos, revisa que el usuario no exista,
     * encripta la contraseña y guarda el usuario en la base de datos.
     *
     * @param username correo o usuario que se va a registrar
     * @param password contraseña del usuario
     * @param telefono telefono del usuario
     * @param nombre nombre del usuario
     * @param rol rol que tendra el usuario
     * @return resultado del registro
     */
    public static Result<Void> register(String username, String password, String telefono, String nombre, Rol rol) {
        try {
            // Se valida que el usuario o correo no venga vacio
            if (username == null || username.trim().isEmpty()) {
                return Result.fail("message:El email es obligatorio\nError:400");
            }

            // Se valida que la contraseña no venga vacia
            if (password == null || password.trim().isEmpty()) {
                return Result.fail("message:La contraseña es obligatoria\nError:400");
            }

            // Se valida que el telefono no venga vacio
            if (telefono == null || telefono.trim().isEmpty()) {
                return Result.fail("message:El teléfono es obligatorio\nError:400");
            }

            // Se valida que el nombre no venga vacio
            if (nombre == null || nombre.trim().isEmpty()) {
                return Result.fail("message:El nombre es obligatorio\nError:400");
            }

            // Se valida que el rol haya sido asignado
            if (rol == null) {
                return Result.fail("message:El rol es obligatorio\nError:400");
            }

            // Se revisa si ya existe un usuario con ese correo
            if (UsuarioDAO.existsByEmail(username)) {
                return Result.fail("message:El usuario ya existe\nError:409");
            }

            // Se genera un salt para reforzar la seguridad de la contraseña
            String salt = ClsEncriptar.generarSalt();

            // Se encripta la contraseña usando el salt generado
            String passwordHash = ClsEncriptar.encriptaSHA256(password, salt);

            // Se crea el usuario con los datos ya validados
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPasswordHash(passwordHash);
            u.setSalt(salt);
            u.setTelefono(telefono);
            u.setName(nombre);
            u.setRol(rol);
            u.setEstado(EstadoCuenta.ACTIVO);

            // Se intenta insertar el usuario en la base de datos
            boolean insertado = UsuarioDAO.insertarUsuario(u);

            // Si no se pudo insertar, se devuelve error
            if (!insertado) {
                return Result.fail("message:No se pudo registrar el usuario\nError:500");
            }

            // Si todo salio bien, se confirma el registro
            return Result.ok("Usuario registrado correctamente");

        } catch (Exception e) {
            // Si ocurre un error inesperado, se muestra en consola y se devuelve error
            e.printStackTrace();
            return Result.fail("message:Error inesperado al registrar usuario\nError:500");
        }
    }

    /**
     * Metodo para iniciar sesion aplicando reglas de negocio.
     * Verifica que el usuario exista, que este activo,
     * compara la contraseña ingresada con la guardada
     * y devuelve los datos necesarios para la sesion.
     *
     * @param email correo del usuario
     * @param password contraseña ingresada
     * @return resultado del login con los datos de sesion
     */
    public static Result<LoginDTO> login(String email, String password) {
        try {
            // Se limpia el correo por si trae espacios al inicio o al final
            email = email.strip();

            // Se valida que el usuario exista
            if (!UsuarioDAO.existsByEmail(email)) {
                return Result.fail("El usuario no existe.");
            }

            // Se valida que el usuario este activo
            if (!UsuarioDAO.isActive(email)) {
                return Result.fail("El usuario no se encuentra activo.");
            }

            // Se obtienen las credenciales guardadas en la base de datos
            Usuario credenciales = UsuarioDAO.obtenerCredencialesPorUsername(email);

            // Si no se pudieron obtener credenciales, se devuelve error
            if (credenciales == null) {
                return Result.fail("Error al obtener credenciales.");
            }

            // Se obtienen el salt y el hash guardados
            String saltBD = credenciales.getSalt();
            String hashBD = credenciales.getPasswordHash();

            // Se encripta la contraseña ingresada usando el mismo salt de la base de datos
            String hashIngresado = ClsEncriptar.encriptaSHA256(password, saltBD);

            // Se compara el hash guardado con el hash generado desde la contraseña ingresada
            if (!hashBD.equals(hashIngresado)) {
                return Result.fail("Credenciales incorrectas.");
            }

            // Se obtienen los datos que se van a guardar en la sesion
            Usuario usuarioSesion = UsuarioDAO.obtenerDatosSesionPorEmail(email);

            // Si no se pudo obtener la informacion de sesion, se devuelve error
            if (usuarioSesion == null) {
                return Result.fail("No se pudo obtener la información del usuario.");
            }

            // Se crea el DTO con la informacion necesaria para la sesion
            LoginDTO login = new LoginDTO();
            login.setEmail(usuarioSesion.getUsername());
            login.setNombre(usuarioSesion.getName());
            login.setRol(usuarioSesion.getRol());
            login.setId(usuarioSesion.getIdUsuario());
            login.setId_Cliente(usuarioSesion.getIdCliente());
            login.setId_empleado(usuarioSesion.getIdEmpleado());

            // Se devuelve el login correcto con los datos del usuario
            return Result.ok("Usuario logueado correctamente.", login);

        } catch (SQLException e) {
            // Si ocurre un error con la base de datos, se devuelve un mensaje claro
            return Result.fail("Error de base de datos. Intente más tarde.");
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo para recuperar o generar una nueva contraseña.
     * Valida que el usuario exista y este activo, genera una contraseña nueva,
     * la encripta y actualiza la contraseña si los datos personales coinciden.
     *
     * @param user usuario o correo
     * @param name nombre del usuario
     * @return resultado de la recuperacion de contraseña
     */
    public static Result<Void> forget(String user, String name) {
        try {
            // Se limpian los datos recibidos para evitar espacios innecesarios
            user = user.strip();
            name = name.strip();

            // Se valida que el usuario exista
            if (!UsuarioDAO.existsByEmail(user)) {
                return Result.fail("El usuario no existe.");
            }

            // Se valida que el usuario este activo
            if (!UsuarioDAO.isActive(user)) {
                return Result.fail("El usuario no está activo.");
            }

            // Se genera una nueva contraseña aleatoria
            String newPassword = ClsCreatePasswordRandom.createPasswordRandom();

            // Se genera un salt nuevo para la nueva contraseña
            String salt = ClsEncriptar.generarSalt();

            // Se encripta la nueva contraseña
            String hash = ClsEncriptar.encriptaSHA256(newPassword, salt);

            // Se actualiza la contraseña solo si el usuario y el nombre coinciden
            boolean actualizado = UsuarioDAO.actualizarPassword(
                    user,
                    name,
                    hash,
                    salt
            );

            // Si no se actualizo, significa que los datos personales no coincidieron
            if (!actualizado) {
                return Result.fail("Los datos personales no coinciden.");
            }

            // Se devuelve la nueva contraseña generada
            return Result.ok("Nueva contraseña generada: " + newPassword);

        } catch (SQLException ex) {
            // Si ocurre un error con la base de datos, se devuelve un mensaje claro
            return Result.fail("Error de base de datos. Intente más tarde.");
        } catch (Exception e) {
            // Si ocurre un error inesperado, se devuelve un mensaje general
            return Result.fail("message:Error inesperado\nError:500");
        }
    }
}