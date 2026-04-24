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
import server.model.Cliente;

public class ClienteDAO {

    /**
     * Metodo que inserta un nuevo cliente en la base de datos.
     * Guarda los datos principales del cliente y devuelve el id generado.
     *
     * @param c cliente que se desea insertar
     * @return id del cliente creado, o null si no se pudo insertar
     */
    public static Integer insertarCliente(Cliente c) {
        String sql = "INSERT INTO clientes (nombre, telefono, email, created_at, created_by, is_deleted) "
                + "VALUES (?, ?, ?, NOW(), ?, 0)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Se asignan los datos del cliente al insert
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());

            // Se guarda el usuario que esta creando el registro
            ps.setString(4, AppSession.getUser());

            // Se ejecuta el insert en la base de datos
            int filas = ps.executeUpdate();

            // Si se inserto correctamente, se obtiene el id generado
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // id_cliente generado
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            // Si ocurre un error con la base de datos, se muestra en consola
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo que obtiene todos los clientes activos.
     * Solo trae los clientes que no han sido eliminados logicamente.
     *
     * @return lista de clientes activos
     */
    public static List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id_cliente, nombre, telefono, email, created_at, created_by, "
                + "updated_at, updated_by, deleted_at, deleted_by, is_deleted "
                + "FROM clientes WHERE is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren todos los registros encontrados
            while (rs.next()) {
                // Se crea un cliente por cada fila encontrada
                Cliente c = new Cliente();

                // Se cargan los datos principales del cliente
                c.setIdCliente(rs.getLong("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));

                // Se cargan las fechas solo si existen en la base de datos
                if (rs.getTimestamp("created_at") != null) {
                    c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                c.setCreatedBy(rs.getString("created_by"));

                if (rs.getTimestamp("updated_at") != null) {
                    c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                c.setUpdatedBy(rs.getString("updated_by"));

                if (rs.getTimestamp("deleted_at") != null) {
                    c.setDeletedAt(rs.getTimestamp("deleted_at").toLocalDateTime());
                }
                c.setDeletedBy(rs.getString("deleted_by"));

                c.setIsDeleted(rs.getBoolean("is_deleted"));

                // Se agrega el cliente cargado a la lista
                lista.add(c);
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que busca un cliente por su id.
     * Solo devuelve el cliente si no esta eliminado logicamente.
     *
     * @param idCliente id del cliente que se desea buscar
     * @return cliente encontrado, o null si no existe
     */
    public static Cliente obtenerPorId(Long idCliente) {
        String sql = "SELECT id_cliente, nombre, telefono, email, created_at, created_by, "
                + "updated_at, updated_by, deleted_at, deleted_by, is_deleted "
                + "FROM clientes WHERE id_cliente = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id del cliente en la consulta
            ps.setLong(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                // Si se encuentra el cliente, se cargan sus datos
                if (rs.next()) {
                    Cliente c = new Cliente();

                    c.setIdCliente(rs.getLong("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));

                    // Se cargan las fechas solo si existen
                    if (rs.getTimestamp("created_at") != null) {
                        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    c.setCreatedBy(rs.getString("created_by"));

                    if (rs.getTimestamp("updated_at") != null) {
                        c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                    c.setUpdatedBy(rs.getString("updated_by"));

                    if (rs.getTimestamp("deleted_at") != null) {
                        c.setDeletedAt(rs.getTimestamp("deleted_at").toLocalDateTime());
                    }
                    c.setDeletedBy(rs.getString("deleted_by"));

                    c.setIsDeleted(rs.getBoolean("is_deleted"));

                    // Se devuelve el cliente encontrado
                    return c;
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al buscar, se muestra en consola
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo que actualiza los datos de un cliente.
     * Modifica nombre, telefono y email, y guarda quien realizo el cambio.
     *
     * @param c cliente con los datos actualizados
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean actualizarCliente(Cliente c) {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, email = ?, "
                + "updated_at = NOW(), updated_by = ? "
                + "WHERE id_cliente = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se colocan los nuevos datos del cliente
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getTelefono());
            ps.setString(3, c.getEmail());

            // Se guarda el usuario que hizo la actualizacion
            ps.setString(4, AppSession.getUser());

            // Se indica cual cliente se va a actualizar
            ps.setLong(5, c.getIdCliente());

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que elimina un cliente de forma logica.
     * No borra el registro de la base, solo lo marca como eliminado.
     *
     * @param idCliente id del cliente que se desea eliminar
     * @return true si se elimino correctamente, false si no
     */
    public static boolean eliminarCliente(Long idCliente) {
        String sql = "UPDATE clientes SET is_deleted = 1, deleted_at = NOW(), deleted_by = ? "
                + "WHERE id_cliente = ? AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se guarda el usuario que realiza la eliminacion
            ps.setString(1, AppSession.getUser());

            // Se indica cual cliente se va a eliminar
            ps.setLong(2, idCliente);

            // Si se actualizo al menos una fila, significa que se elimino logicamente
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al eliminar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que cuenta cuantos clientes activos existen.
     * Solo toma en cuenta los clientes que no estan eliminados.
     *
     * @return cantidad de clientes activos
     */
    public static int contarClientes() {
        String sql = "SELECT COUNT(*) AS total FROM clientes WHERE is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Si la consulta devuelve resultado, se obtiene el total
            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            // Si ocurre un error al contar, se muestra en consola
            e.printStackTrace();
        }

        return 0; // si ocurre error o no hay datos
    }
}