/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

import common.dto.Result;
import java.sql.SQLException;
import server.dao.UsuarioDAO;
import server.model.Usuario;
import server.security.ClsCreatePasswordRandom;
import server.security.ClsEncriptar;

/**
 *
 * @author Fabian
 */
public class AuthService {

    /**
     * Metodo para aplicar reglas de negocio en cada registro
     *
     * @param email
     * @param password
     * @param number
     * @param name
     * @return
     */
    public static Result<Void> register(String email, String password, String number, String name) {
        //Manejo de errores
        try {
            //Validar si el correo ya existe
            if (UsuarioDAO.existsByEmail(email)) {
                return Result.fail("El usuario ya fue registrado.");
            }

            //Generar salt y encpriptar la contraseña
            String salt = ClsEncriptar.generarSalt();
            String hash = ClsEncriptar.encriptaSHA256(password, salt);

            //Generamos el núevo usuario con los datos pasados por parametros
            Usuario u = new Usuario(email, hash, number, name, salt);

            //Insertamos el usuario en la bd
            boolean inserted = UsuarioDAO.insertarUsuario(u);

            //Validamos si se pudo insertar
            if (!inserted) {
                return Result.fail("No se pudo registrar el usuario.");
            }

            //Devolvemos resultado exitoso
            return Result.ok("Usuario registrado correctamente.", null);

        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            return Result.fail("El correo ya fue registrado.");
        } catch (java.sql.SQLException e) {
            return Result.fail("Error de base de datos. Intente más tarde.");
        } catch (Exception e) {
            return Result.fail("messge:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo para aplicar reglas de negocio a cada login
     *
     * @param email
     * @param password
     * @return
     */
    public static Result<Void> login(String email, String password) {

        //Manejo de errores
        try {
            //Validar si el email existe
            if (!UsuarioDAO.existsByEmail(email)) {
                return Result.fail("El usuario no existe.");
            }

            //Validar que el usuario este activo
            if (!UsuarioDAO.isActive(email)) {
                return Result.fail("El usuario no se encuentra activo.");
            }

            //Traer las credenciales en un núevo usuario
            Usuario credenciales = UsuarioDAO.obtenerCredencialesPorUsername(email);

            //Validar que las credeciales del usuario no sean nulas
            if (credenciales == null) {
                return Result.fail("Error al obtener credenciales.");
            }

            //Traer los datos del password
            String saltBD = credenciales.getSalt();
            String hashBD = credenciales.getPasswordHash();

            //volvemos a encriptar la contraseña ingresada con el salt de la bd
            String hashIngresado = ClsEncriptar.encriptaSHA256(password, saltBD);

            //Validamos si la contraseñas son iguales
            if (!hashBD.equals(hashIngresado)) {
                return Result.fail("Credenciales incorrectas.");
            }

            //Retornamos mesaje de operación exitosa
            return Result.ok("Usuario logueado correctamente.", null);

        } catch (SQLException e) {
            return Result.fail("Error de base de datos. Intente más tarde.");
        } catch (Exception e) {
            return Result.fail("messge:Error inesperado\nError:500");
        }
    }

    /**
     * Metodo para aplicar reglas de negocio a actualizar contraseñas
     * @param user
     * @param name
     * @param number
     * @return 
     */
    public static Result<Void> forget(String user, String name, String number) {
        //Manejo de errores
        try {
            //quitar espacio vacios
            user = user.strip();
            name = name.strip();
            number = number.strip();
            
            //Validar que el usario exista
            if (!UsuarioDAO.existsByEmail(user)) {
                return Result.fail("El usuario no existe.");
            }
            
            //Validar que este activo
            if (!UsuarioDAO.isActive(user)) {
                return Result.fail("El usuario no está activo.");
            }

            //Generamos núeva contraseña
            String newPassword = ClsCreatePasswordRandom.createPasswordRandom();
            
            //Generamos núevo salt
            String salt = ClsEncriptar.generarSalt();
            
            //Encriptamos la contraseña
            String hash = ClsEncriptar.encriptaSHA256(newPassword, salt);
            
            //Actualizamos la contraseña
            boolean actualizado = UsuarioDAO.actualizarPassword(
                    user,
                    name,
                    number,
                    hash,
                    salt
            );
            
            //Validamos si se actualizo
            if (!actualizado) {
                return Result.fail("Los datos personales no coinciden.");
            }
            
            //Retornamos mensaje y núeva contraseña
            return Result.ok("Nueva contraseña generada: " + newPassword);

        } catch (SQLException ex) {
            return Result.fail("Error de base de datos. Intente más tarde.");
        } catch (Exception e) {
            return Result.fail("message:Error inesperado\nError:500");
        }
    }
}
