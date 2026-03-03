/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import server.model.Usuario;
import java.sql.ResultSet;

/**
 *
 * @author Fabian
 */
public class UsuarioDAO {

    /**
     * Para Insertar un núevo usuario en la base de datos
     *
     * @param u
     * @return
     */
    public static boolean insertarUsuario(Usuario u) {

        //Generar insert de usuario
        String sql = "INSERT INTO usuarios (username, password_hash, salt, rol, estado, created_by,name,phone) VALUES (?,?,?,?,?,?,?,?)";

        //Pasamos los parametros de conexiónn
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

            //Pasamos los datos del insert
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getSalt());
            ps.setString(4, u.getRol().name());
            ps.setString(5, u.getEstado().name());
            ps.setString(6, "SYSTEM");
            ps.setString(7, u.getName());
            ps.setString(8, u.getTelefono());

            //Ejecutamos el script
            ps.executeUpdate();

            return true;
        } catch (SQLException insert) {
            insert.printStackTrace();
            return false;
        }

    }

    /**
     * Metodo para validaar si un email ya esta registrado
     *
     * @param e
     * @return
     * @throws SQLException
     */
    public static boolean existsByEmail(String e) throws SQLException {

        String sql = "SELECT EXISTS (SELECT 1 FROM usuarios WHERE username = ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        }
    }

    /**
     * Metodo para validar si un usuario se encuentra activo
     *
     * @param e
     * @return
     * @throws SQLException
     */
    public static boolean isActive(String e) throws SQLException {

        String sql = "SELECT 1 FROM usuarios WHERE username = ? AND estado = 'ACTIVO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Metodo para obtener la credeciales de un usuario
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public static Usuario obtenerCredencialesPorUsername(String username) throws SQLException {

        String sql = "SELECT password_hash, salt FROM usuarios WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Usuario u = new Usuario();
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setSalt(rs.getString("salt"));

                    return u;
                }
            }
        }

        return null;
    }

    /**
     * Metodo para actualizar la contraseña validando username, name y phone
     *
     * @param username
     * @param name
     * @param phone
     * @param passwordHash
     * @param salt
     * @return true si se actualizó correctamente
     */
    public static boolean actualizarPassword(String username, String name, String phone, String passwordHash, String salt) {
        
        //Query
        String sql = "UPDATE usuarios SET password_hash = ?, salt = ? "
                + "WHERE username = ? AND name = ? AND phone = ? AND estado = 'ACTIVO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            //Cambiar datos de Query
            ps.setString(1, passwordHash);
            ps.setString(2, salt);
            ps.setString(3, username);
            ps.setString(4, name);
            ps.setString(5, phone);

            int filas = ps.executeUpdate();

            return filas > 0; // true si actualizó

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
