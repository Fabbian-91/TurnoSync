/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.dao;

import client.sesion.AppSession;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.model.Empleado;

public class EmpleadoDAO {

    /**
     * Metodo que inserta un nuevo empleado en la base de datos. Guarda el
     * nombre del empleado y devuelve el id generado.
     *
     * @param e empleado que se desea insertar
     * @return id del empleado creado, o null si no se pudo insertar
     */
    public static Integer insertarEmpleado(Empleado e) {
        String sql = "INSERT INTO empleados (nombre) VALUES (?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Se asigna el nombre del empleado al insert
            ps.setString(1, e.getNombre());

            // Se ejecuta el insert en la base de datos
            int filas = ps.executeUpdate();

            // Si se inserto correctamente, se obtiene el id generado
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // id_empleado generado
                    }
                }
            }

            return null;

        } catch (SQLException ex) {
            // Si ocurre un error con la base de datos, se muestra en consola
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo que obtiene todos los empleados activos. Solo trae los empleados
     * que no han sido eliminados logicamente.
     *
     * @return lista de empleados activos
     */
    public static List<Empleado> obtenerTodos() {
        List<Empleado> lista = new ArrayList<>();

        String sql = "SELECT id_empleado, nombre, created_at, created_by, "
                + "updated_at, updated_by, deleted_at, deleted_by, is_deleted "
                + "FROM empleados WHERE is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren todos los registros encontrados
            while (rs.next()) {
                // Se crea un empleado por cada fila encontrada
                Empleado e = new Empleado();

                // Se cargan los datos principales del empleado
                e.setIdEmpleado(rs.getLong("id_empleado"));
                e.setNombre(rs.getString("nombre"));

                // Se cargan las fechas solo si existen en la base de datos
                if (rs.getTimestamp("created_at") != null) {
                    e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                e.setCreatedBy(rs.getString("created_by"));

                if (rs.getTimestamp("updated_at") != null) {
                    e.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                e.setUpdatedBy(rs.getString("updated_by"));

                if (rs.getTimestamp("deleted_at") != null) {
                    e.setDeletedAt(rs.getTimestamp("deleted_at").toLocalDateTime());
                }
                e.setDeletedBy(rs.getString("deleted_by"));

                e.setIsDeleted(rs.getBoolean("is_deleted"));

                // Se agrega el empleado cargado a la lista
                lista.add(e);
            }

        } catch (SQLException ex) {
            // Si ocurre un error al consultar, se muestra en consola
            ex.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que busca un empleado por su id. Solo devuelve el empleado si no
     * esta eliminado logicamente.
     *
     * @param idEmpleado id del empleado que se desea buscar
     * @return empleado encontrado, o null si no existe
     */
    public static Empleado obtenerPorId(Long idEmpleado) {
        String sql = "SELECT id_empleado, nombre, created_at, created_by, "
                + "updated_at, updated_by, deleted_at, deleted_by, is_deleted "
                + "FROM empleados WHERE id_empleado = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id del empleado en la consulta
            ps.setLong(1, idEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                // Si se encuentra el empleado, se cargan sus datos
                if (rs.next()) {
                    Empleado e = new Empleado();

                    e.setIdEmpleado(rs.getLong("id_empleado"));
                    e.setNombre(rs.getString("nombre"));

                    // Se cargan las fechas solo si existen
                    if (rs.getTimestamp("created_at") != null) {
                        e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    e.setCreatedBy(rs.getString("created_by"));

                    if (rs.getTimestamp("updated_at") != null) {
                        e.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                    e.setUpdatedBy(rs.getString("updated_by"));

                    if (rs.getTimestamp("deleted_at") != null) {
                        e.setDeletedAt(rs.getTimestamp("deleted_at").toLocalDateTime());
                    }
                    e.setDeletedBy(rs.getString("deleted_by"));

                    e.setIsDeleted(rs.getBoolean("is_deleted"));

                    // Se devuelve el empleado encontrado
                    return e;
                }
            }

        } catch (SQLException ex) {
            // Si ocurre un error al buscar, se muestra en consola
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo que actualiza los datos de un empleado. Modifica el nombre y
     * guarda quien realizo el cambio.
     *
     * @param e empleado con los datos actualizados
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean actualizarEmpleado(Empleado e) {
        String sql = "UPDATE empleados SET nombre = ?, updated_at = NOW(), updated_by = ? "
                + "WHERE id_empleado = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el nuevo nombre del empleado
            ps.setString(1, e.getNombre());

            // Se guarda el usuario que hizo la actualizacion
            ps.setString(2, AppSession.getUser());

            // Se indica cual empleado se va a actualizar
            ps.setLong(3, e.getIdEmpleado());

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            // Si ocurre un error al actualizar, se muestra en consola
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que elimina un empleado de forma logica. No borra el registro de
     * la base, solo lo marca como eliminado.
     *
     * @param idEmpleado id del empleado que se desea eliminar
     * @return true si se elimino correctamente, false si no
     */
    public static boolean eliminarEmpleado(Long idEmpleado) {
        String sql = "UPDATE empleados SET is_deleted = 1, deleted_at = NOW(), deleted_by = ? "
                + "WHERE id_empleado = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se guarda el usuario que realiza la eliminacion
            ps.setString(1, AppSession.getUser());

            // Se indica cual empleado se va a eliminar
            ps.setLong(2, idEmpleado);

            // Si se actualizo al menos una fila, significa que se elimino logicamente
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            // Si ocurre un error al eliminar, se muestra en consola
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que cuenta cuantos empleados activos existen. Solo toma en cuenta
     * los empleados que no estan eliminados.
     *
     * @return cantidad de empleados activos
     */
    public static int contarEmpleados() {
        String sql = "SELECT COUNT(*) AS total FROM empleados WHERE is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Si la consulta devuelve resultado, se obtiene el total
            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            // Si ocurre un error al contar, se muestra en consola
            e.printStackTrace();
        }

        return 0;
    }
}
