/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.dao;

import client.sesion.AppSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import server.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import common.enums.EstadoCuenta;
import common.enums.EstadoTurno;
import common.enums.Rol;
import server.model.Turno;

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
        String sql = "INSERT INTO usuarios (username, password_hash, salt, rol, estado, id_cliente, id_empleado, created_by, name, phone) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getSalt());
            ps.setString(4, u.getRol().name());
            ps.setString(5, u.getEstado().name());

            ps.setNull(6, Types.INTEGER);
            ps.setNull(7, Types.INTEGER);

            ps.setString(8, AppSession.getUser());
            ps.setString(9, u.getName());
            ps.setString(10, u.getTelefono());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
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
    public static boolean actualizarPassword(String username, String name, String passwordHash, String salt) {

        //Query
        String sql = "UPDATE usuarios SET password_hash = ?, salt = ? "
                + "WHERE username = ? AND name = ?  AND estado = 'ACTIVO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            //Cambiar datos de Query
            ps.setString(1, passwordHash);
            ps.setString(2, salt);
            ps.setString(3, username);
            ps.setString(4, name);

            int filas = ps.executeUpdate();

            return filas > 0; // true si actualizó

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario obtenerDatosSesionPorEmail(String email) throws SQLException {

        String sql = "SELECT id_usuario, username, rol, name, id_cliente, id_empleado FROM turnosync.usuarios WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();

                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsername(rs.getString("username"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setName(rs.getString("name"));

                    // 🔹 Manejo con -1 en lugar de NULL
                    int idCliente = rs.getInt("id_cliente");
                    u.setIdCliente(rs.wasNull() ? -1 : idCliente);

                    int idEmpleado = rs.getInt("id_empleado");
                    u.setIdEmpleado(rs.wasNull() ? -1 : idEmpleado);

                    return u;
                }
            }
        }

        return null;
    }

    /**
     * Metodo para obtener todos los usuarios de la base de datos
     *
     * @return lista de usuarios
     */
    /**
     * Metodo para obtener todos los usuarios de la base de datos
     *
     * @return lista de usuarios
     */
    public static List<Usuario> obtenerTodosLosUsuarios() {

        List<Usuario> listaUsuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, username, rol, estado, name, phone, id_cliente, id_empleado "
                + "FROM usuarios "
                + "WHERE is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();

                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsername(rs.getString("username"));
                u.setRol(Rol.valueOf(rs.getString("rol")));
                u.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
                u.setName(rs.getString("name"));
                u.setTelefono(rs.getString("phone"));

                int idCliente = rs.getInt("id_cliente");
                u.setIdCliente(rs.wasNull() ? -1 : idCliente);

                int idEmpleado = rs.getInt("id_empleado");
                u.setIdEmpleado(rs.wasNull() ? -1 : idEmpleado);

                listaUsuarios.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaUsuarios;
    }

    /**
     * Actualizar datos del usuario (no password)
     */
    public static boolean actualizarUsuario(Usuario u) {

        String sql = "UPDATE usuarios SET rol = ?, estado = ?, name = ?, phone = ?, updated_at = NOW(), updated_by = ? WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getRol().name());
            ps.setString(2, u.getEstado().name());
            ps.setString(3, u.getName());
            ps.setString(4, u.getTelefono());
            ps.setString(5, AppSession.getUser());
            ps.setString(6, u.getUsername());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Desactivar usuario
     */
    public static boolean desactivarUsuario(String username) {

        String sql = "UPDATE usuarios SET estado = 'INACTIVO', updated_at = NOW(), updated_by = ? WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, AppSession.getUser());
            ps.setString(2, username);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Activar usuario
     */
    public static boolean activarUsuario(String username) {

        String sql = "UPDATE usuarios SET estado = 'ACTIVO', updated_at = NOW(), updated_by = ? WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, AppSession.getUser());
            ps.setString(2, username);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Buscar usuarios por nombre
     */
    public static List<Usuario> buscarPorNombre(String nombre) {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT username, rol, estado, name, phone FROM usuarios WHERE name LIKE ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Usuario u = new Usuario();

                    u.setUsername(rs.getString("username"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("phone"));

                    lista.add(u);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtener usuarios activos
     */
    public static List<Usuario> obtenerActivos() {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT username, rol, estado, name, phone FROM usuarios WHERE estado = 'ACTIVO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();

                u.setUsername(rs.getString("username"));
                u.setRol(Rol.valueOf(rs.getString("rol")));
                u.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
                u.setName(rs.getString("name"));
                u.setTelefono(rs.getString("phone"));

                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static int contarUsuarios() {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM usuarios "
                + "WHERE estado = 'ACTIVO' "
                + "AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Integer obtenerIdPorUsername(String username) throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        }

        return null;
    }

    /**
     * Cambiar contraseña de un usuario por username
     *
     * @param username usuario/email
     * @param passwordHash nueva contraseña en hash
     * @param salt nuevo salt
     * @return true si se actualizó correctamente
     */
    public static boolean cambiarPassword(String username, String passwordHash, String salt) {

        String sql = "UPDATE usuarios "
                + "SET password_hash = ?, salt = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE username = ? AND estado = 'ACTIVO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setString(2, salt);
            ps.setString(3, AppSession.getUser());
            ps.setString(4, username);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdEmpleado(int idUsuario, int idEmpleado) {
        String sql = "UPDATE usuarios SET id_empleado = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);
            ps.setString(2, AppSession.getUser());
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdCliente(String username, int idCliente) {
        String sql = "UPDATE usuarios SET id_cliente = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setString(2, AppSession.getUser());
            ps.setString(3, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdEmpleado(String username, int idEmpleado) {
        String sql = "UPDATE usuarios SET id_empleado = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE username = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);
            ps.setString(2, AppSession.getUser());
            ps.setString(3, username);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdCliente(int idUsuario, int idCliente) {
        String sql = "UPDATE usuarios SET id_cliente = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setString(2, AppSession.getUser());
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdClientePorUsuario(int idUsuario, int idCliente) {
        String sql = "UPDATE usuarios SET id_cliente = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.setString(2, AppSession.getUser());
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarIdEmpleadoPorUsuario(int idUsuario, int idEmpleado) {
        String sql = "UPDATE usuarios SET id_empleado = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);
            ps.setString(2, AppSession.getUser());
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarUsuarioPorId(int idUsuario) {
        String sql = "UPDATE usuarios "
                + "SET estado = 'INACTIVO', "
                + "is_deleted = 1, "
                + "deleted_at = NOW(), "
                + "deleted_by = ?, "
                + "updated_at = NOW(), "
                + "updated_by = ? "
                + "WHERE id_usuario = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, AppSession.getUser());
            ps.setString(2, AppSession.getUser());
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT id_usuario, username, rol, estado, name, phone, id_cliente, id_empleado "
                + "FROM usuarios "
                + "WHERE id_usuario = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();

                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsername(rs.getString("username"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));
                    u.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
                    u.setName(rs.getString("name"));
                    u.setTelefono(rs.getString("phone"));

                    int idCliente = rs.getInt("id_cliente");
                    u.setIdCliente(rs.wasNull() ? -1 : idCliente);

                    int idEmpleado = rs.getInt("id_empleado");
                    u.setIdEmpleado(rs.wasNull() ? -1 : idEmpleado);

                    return u;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean updateUser(Usuario u) {
        String sql = "UPDATE usuarios "
                + "SET username = ?, password_hash = ?, salt = ?, rol = ?, name = ?, phone = ?, "
                + "updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ? AND deleted_at IS NULL";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getSalt());
            ps.setString(4, u.getRol().name());
            ps.setString(5, u.getName());
            ps.setString(6, u.getTelefono());
            ps.setString(7, AppSession.getUser());
            ps.setInt(8, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean limpiarRelacionCliente(int idUsuario) {
        String sql = "UPDATE usuarios SET id_cliente = NULL, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, AppSession.getUser());
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean limpiarRelacionEmpleado(int idUsuario) {
        String sql = "UPDATE usuarios SET id_empleado = NULL, updated_at = NOW(), updated_by = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, AppSession.getUser());
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
