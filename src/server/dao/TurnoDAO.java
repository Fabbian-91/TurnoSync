package server.dao;

import client.sesion.AppSession;
import java.sql.*;
import common.enums.EstadoTurno;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import server.model.Turno;

public class TurnoDAO {

    /**
     * Metodo que inserta un nuevo turno en la base de datos. Guarda la fecha,
     * hora, estado, cliente, empleado y el usuario que lo crea.
     *
     * @param t turno que se desea insertar
     * @return true si se inserto correctamente, false si ocurrio un error
     * @throws Exception si el cliente o empleado no existe
     */
    public static boolean insertarTurno(Turno t) throws Exception {
        String sql = "INSERT INTO turnos (fecha, hora, estado, id_cliente, id_empleado, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se asignan los datos principales del turno
            ps.setDate(1, Date.valueOf(t.getFecha()));
            ps.setTime(2, Time.valueOf(t.getHora()));
            ps.setString(3, t.getEstado().name());

            // Se asigna el cliente si viene con un id valido
            if (t.getIdCliente() > 0) {
                ps.setInt(4, t.getIdCliente());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            // Se asigna el empleado si viene con un id valido
            if (t.getIdEmpleado() > 0) {
                ps.setInt(5, t.getIdEmpleado());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            // Se guarda el usuario que crea el turno
            ps.setString(6, AppSession.getUser());

            // Si se inserto al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Si el cliente o empleado no existe, se lanza un mensaje mas claro
            throw new SQLIntegrityConstraintViolationException("El cliente o empleado ingresado no existe.");
        } catch (SQLException e) {
            // Si ocurre un error de base de datos, se muestra el mensaje en consola
            System.out.println("Error de base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Metodo que obtiene todos los turnos activos. No trae turnos eliminados ni
     * cancelados.
     *
     * @return lista de turnos activos
     */
    public static List<Turno> obtenerTodos() {
        List<Turno> lista = new ArrayList<>();

        String sql = "SELECT * FROM turnos "
                + "WHERE is_deleted = 0 "
                + "AND estado <> 'CANCELADO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren los turnos encontrados
            while (rs.next()) {
                // Cada fila se convierte en un turno y se agrega a la lista
                lista.add(mapTurno(rs));
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que busca un turno por su id. Solo devuelve el turno si no esta
     * eliminado.
     *
     * @param id id del turno que se desea buscar
     * @return turno encontrado, o null si no existe
     */
    public static Turno obtenerPorId(int id) {
        String sql = "SELECT * FROM turnos "
                + "WHERE id_turno = ? "
                + "AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id del turno en la consulta
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                // Si se encuentra el turno, se convierte a objeto Turno
                if (rs.next()) {
                    return mapTurno(rs);
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al buscar, se muestra en consola
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo que actualiza un turno existente. Modifica fecha, hora, estado,
     * cliente, empleado y datos de auditoria.
     *
     * @param t turno con los datos actualizados
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean updateTurno(Turno t) {
        String sql = "UPDATE turnos "
                + "SET fecha = ?, "
                + "hora = ?, "
                + "estado = ?, "
                + "id_cliente = ?, "
                + "id_empleado = ?, "
                + "updated_at = NOW(), "
                + "updated_by = ? "
                + "WHERE id_turno = ? "
                + "AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se colocan los nuevos datos principales del turno
            ps.setDate(1, Date.valueOf(t.getFecha()));
            ps.setTime(2, Time.valueOf(t.getHora()));
            ps.setString(3, t.getEstado().name());

            // Se actualiza el cliente si viene con id valido
            if (t.getIdCliente() > 0) {
                ps.setInt(4, t.getIdCliente());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            // Se actualiza el empleado si viene con id valido
            if (t.getIdEmpleado() > 0) {
                ps.setInt(5, t.getIdEmpleado());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            // Se guarda el usuario que hizo la actualizacion
            ps.setString(6, AppSession.getUser());

            // Se indica cual turno se va a actualizar
            ps.setInt(7, t.getIdTurno());

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Si el cliente o empleado no existe, se muestra el mensaje
            System.out.println("El cliente o empleado ingresado no existe.");
            return false;
        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que actualiza un turno. Cambia los datos principales del turno y
     * registra quien hizo el cambio.
     *
     * @param t turno con los datos actualizados
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean actualizarTurno(Turno t) {
        String sql = "UPDATE turnos SET fecha = ?, hora = ?, estado = ?, id_cliente = ?, id_empleado = ?, "
                + "updated_at = NOW(), updated_by = ? WHERE id_turno = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se colocan los datos actualizados del turno
            ps.setDate(1, Date.valueOf(t.getFecha()));
            ps.setTime(2, Time.valueOf(t.getHora()));
            ps.setString(3, t.getEstado().name());

            // Se coloca el cliente si corresponde
            if (t.getIdCliente() > 0) {
                ps.setInt(4, t.getIdCliente());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            // Se coloca el empleado si corresponde
            if (t.getIdEmpleado() > 0) {
                ps.setInt(5, t.getIdEmpleado());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            // Se guarda el usuario que actualizo el turno
            ps.setString(6, AppSession.getUser());

            // Se indica el turno que se va a modificar
            ps.setInt(7, t.getIdTurno());

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que marca un turno como eliminado. Guarda la fecha y el usuario
     * que realizo la eliminacion.
     *
     * @param id id del turno que se desea eliminar
     * @return true si se elimino correctamente, false si no
     */
    public static boolean eliminarTurno(int id) {
        String sql = "UPDATE turnos SET deleted_at = NOW(), deleted_by = ? WHERE id_turno = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se guarda el usuario que elimina el turno
            ps.setString(1, AppSession.getUser());

            // Se indica cual turno se va a eliminar
            ps.setInt(2, id);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al eliminar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que obtiene los turnos de un cliente. Solo trae turnos activos y
     * que no esten cancelados.
     *
     * @param idCliente id del cliente
     * @return lista de turnos del cliente
     */
    public static List<Turno> obtenerPorCliente(int idCliente) {
        List<Turno> lista = new ArrayList<>();

        String sql = "SELECT * FROM turnos "
                + "WHERE id_cliente = ? "
                + "AND is_deleted = 0 "
                + "AND estado <> 'CANCELADO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id del cliente en la consulta
            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan a la lista los turnos encontrados
                while (rs.next()) {
                    lista.add(mapTurno(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que obtiene los turnos de un empleado. Solo trae turnos activos y
     * que no esten cancelados.
     *
     * @param idEmpleado id del empleado
     * @return lista de turnos del empleado
     */
    public static List<Turno> obtenerPorEmpleado(int idEmpleado) {
        List<Turno> lista = new ArrayList<>();

        String sql = "SELECT * FROM turnos "
                + "WHERE id_empleado = ? "
                + "AND is_deleted = 0 "
                + "AND estado <> 'CANCELADO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id del empleado en la consulta
            ps.setInt(1, idEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan a la lista los turnos encontrados
                while (rs.next()) {
                    lista.add(mapTurno(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que obtiene los turnos de una fecha especifica. Solo trae turnos
     * activos y que no esten cancelados.
     *
     * @param fecha fecha que se desea consultar
     * @return lista de turnos de esa fecha
     */
    public static List<Turno> obtenerPorFecha(Date fecha) {
        List<Turno> lista = new ArrayList<>();

        String sql = "SELECT * FROM turnos "
                + "WHERE fecha = ? "
                + "AND is_deleted = 0 "
                + "AND estado <> 'CANCELADO'";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca la fecha en la consulta
            ps.setDate(1, fecha);

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan a la lista los turnos encontrados
                while (rs.next()) {
                    lista.add(mapTurno(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que convierte una fila de la base de datos en un objeto Turno.
     *
     * @param rs resultado de la consulta
     * @return objeto Turno con los datos cargados
     * @throws SQLException si ocurre un error leyendo los datos
     */
    private static Turno mapTurno(ResultSet rs) throws SQLException {
        // Se crea un turno para cargarle los datos de la fila
        Turno t = new Turno();

        // Se cargan los datos principales del turno
        t.setIdTurno(rs.getInt("id_turno"));
        t.setFecha(rs.getDate("fecha").toLocalDate());
        t.setHora(rs.getTime("hora").toLocalTime());
        t.setEstado(EstadoTurno.valueOf(rs.getString("estado")));

        // Se carga el id del cliente solo si no viene nulo
        int idCliente = rs.getInt("id_cliente");
        if (!rs.wasNull()) {
            t.setIdCliente(idCliente);
        }

        // Se carga el id del empleado solo si no viene nulo
        int idEmpleado = rs.getInt("id_empleado");
        if (!rs.wasNull()) {
            t.setIdEmpleado(idEmpleado);
        }

        return t;
    }

    /**
     * Metodo que cuenta los turnos activos. No toma en cuenta los turnos
     * eliminados ni cancelados.
     *
     * @return cantidad de turnos activos
     */
    public static int contarTurnosActivos() {
        String sql = "SELECT COUNT(*) AS total "
                + "FROM turnos "
                + "WHERE is_deleted = 0 "
                + "AND estado <> 'CANCELADO'";

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

    /**
     * Metodo que cuenta todos los turnos que no estan eliminados.
     *
     * @return cantidad total de turnos
     */
    public static int contarTurnos() {
        String sql = "SELECT COUNT(*) AS total "
                + "FROM turnos "
                + "WHERE is_deleted = 0";

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

    /**
     * Metodo que cancela un turno. Cambia el estado a cancelado y tambien lo
     * marca como eliminado.
     *
     * @param idTurno id del turno que se desea cancelar
     * @return true si se cancelo correctamente, false si no
     */
    public static boolean cancelarTurno(int idTurno) {
        String sql = "UPDATE turnos "
                + "SET estado = 'CANCELADO', "
                + "is_deleted = 1, "
                + "deleted_at = NOW(), "
                + "deleted_by = ?, "
                + "updated_at = NOW(), "
                + "updated_by = ? "
                + "WHERE id_turno = ? "
                + "AND is_deleted = 0";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se guarda el usuario que cancela y elimina el turno
            ps.setString(1, AppSession.getUser());
            ps.setString(2, AppSession.getUser());

            // Se indica cual turno se va a cancelar
            ps.setInt(3, idTurno);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al cancelar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }
}
