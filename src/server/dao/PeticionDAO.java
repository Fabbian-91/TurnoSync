package server.dao;

import common.enums.EstadoCuenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import server.model.Peticion;
import common.enums.TipoSolicitud;
import common.enums.EstadoPeticion;
import common.enums.Rol;
import server.model.Usuario;
import server.model.Turno;

public class PeticionDAO {

    /**
     * Metodo que inserta una nueva peticion en la base de datos. Guarda el
     * tipo, estado, fecha, usuario y detalle de la peticion.
     *
     * @param p peticion que se desea guardar
     * @return true si se inserto correctamente, false si ocurrio un error
     */
    public static boolean insertarPeticion(Peticion p) {

        String sql = "INSERT INTO peticiones (tipo, estado, creada_en, id_usuario, detalle) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se asignan los datos de la peticion al insert
            ps.setString(1, p.getTipo().name());
            ps.setString(2, p.getEstado().name());
            ps.setTimestamp(3, Timestamp.valueOf(p.getCreadaEn()));
            ps.setInt(4, p.getUsuario().getIdUsuario());
            ps.setString(5, p.getDetalle());

            // Se ejecuta el insert en la base de datos
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Si ocurre un error con la base de datos, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que obtiene todas las peticiones registradas.
     *
     * @return lista con todas las peticiones
     */
    public static List<Peticion> obtenerTodas() {

        ArrayList<Peticion> lista = new ArrayList<>();

        String sql = "SELECT * FROM peticiones";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren los registros encontrados
            while (rs.next()) {
                // Cada fila se convierte en una peticion y se agrega a la lista
                lista.add(mapPeticion(rs));
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que obtiene las peticiones mas recientes. Las ordena desde la mas
     * nueva hasta la mas antigua.
     *
     * @return lista con las peticiones recientes
     */
    public static List<Peticion> obtenerRecientes() {

        ArrayList<Peticion> lista = new ArrayList<>();

        String sql = "SELECT * FROM peticiones ORDER BY creada_en DESC LIMIT 20";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren las peticiones recientes encontradas
            while (rs.next()) {
                lista.add(mapPeticion(rs));
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que busca una peticion por su id.
     *
     * @param id id de la peticion que se desea buscar
     * @return peticion encontrada, o null si no existe
     */
    public static Peticion obtenerPorId(int id) {

        String sql = "SELECT * FROM peticiones WHERE id_peticion = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el id en la consulta
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                // Si se encuentra una fila, se convierte en peticion
                if (rs.next()) {
                    System.out.println("Fila encontrada");
                    return mapPeticion(rs);
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al buscar, se muestra en consola
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo que marca una peticion como en proceso.
     *
     * @param id id de la peticion
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean marcarEnProceso(int id) {

        String sql = "UPDATE peticiones SET estado = 'EN_PROCESO', procesada_en = NOW() WHERE id_peticion = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se indica cual peticion se va a actualizar
            ps.setInt(1, id);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que marca una peticion como completada.
     *
     * @param id id de la peticion
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean completarPeticion(int id) {

        String sql = "UPDATE peticiones SET estado = 'COMPLETADA', finalizada_en = NOW() WHERE id_peticion = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se indica cual peticion se va a completar
            ps.setInt(1, id);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que marca una peticion como fallida.
     *
     * @param id id de la peticion
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean fallarPeticion(int id) {

        String sql = "UPDATE peticiones SET estado = 'FALLIDA', finalizada_en = NOW() WHERE id_peticion = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se indica cual peticion se va a marcar como fallida
            ps.setInt(1, id);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo que obtiene las peticiones de un usuario.
     *
     * @param username usuario usado para buscar las peticiones
     * @return lista con las peticiones del usuario
     */
    public static List<Peticion> obtenerPorUsuario(String username) {

        List<Peticion> lista = new ArrayList<>();

        String sql = "SELECT * FROM peticiones WHERE id_usuario = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el usuario en la consulta
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan a la lista las peticiones encontradas
                while (rs.next()) {
                    lista.add(mapPeticion(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que obtiene las peticiones por estado.
     *
     * @param estado estado que se desea buscar
     * @return lista con las peticiones que tienen ese estado
     */
    public static List<Peticion> obtenerPorEstado(String estado) {

        List<Peticion> lista = new ArrayList<>();

        String sql = "SELECT * FROM peticiones WHERE estado = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se coloca el estado en la consulta
            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan a la lista las peticiones encontradas
                while (rs.next()) {
                    lista.add(mapPeticion(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que convierte una fila de la base de datos en un objeto Peticion.
     *
     * @param rs resultado de la consulta
     * @return objeto Peticion con los datos cargados
     * @throws SQLException si ocurre un error leyendo los datos
     */
    private static Peticion mapPeticion(ResultSet rs) throws SQLException {

        // Se crea una nueva peticion para cargarle los datos de la fila
        Peticion p = new Peticion();

        // Se cargan los datos principales de la peticion
        p.setIdPeticion(rs.getInt("id_peticion"));
        p.setTipo(TipoSolicitud.valueOf(rs.getString("tipo")));
        p.setEstado(EstadoPeticion.valueOf(rs.getString("estado")));

        // Se cargan las fechas solo si vienen con datos
        Timestamp creada = rs.getTimestamp("creada_en");
        if (creada != null) {
            p.setCreadaEn(creada.toLocalDateTime());
        }

        Timestamp procesada = rs.getTimestamp("procesada_en");
        if (procesada != null) {
            p.setProcesadaEn(procesada.toLocalDateTime());
        }

        Timestamp finalizada = rs.getTimestamp("finalizada_en");
        if (finalizada != null) {
            p.setFinalizadaEn(finalizada.toLocalDateTime());
        }

        // Se crea un usuario basico para asociarlo a la peticion
        Usuario u = new Usuario();
        u.setUsername(rs.getString("id_usuario"));
        p.setUsuario(u);

        // Se carga el detalle de la peticion
        p.setDetalle(rs.getString("detalle"));

        return p;
    }

    /**
     * Metodo que cuenta las peticiones que no estan fallidas.
     *
     * @return cantidad de peticiones validas
     */
    public static int contarPeticiones() {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM peticiones "
                + "WHERE estado <> 'FALLIDA'";

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
     * Metodo que cuenta las peticiones pendientes.
     *
     * @return cantidad de peticiones pendientes
     */
    public static int contarPeticionesPendientes() {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM peticiones "
                + "WHERE estado = 'PENDIENTE'";

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
     * Metodo que cuenta las peticiones de tipo turno que no estan fallidas.
     *
     * @return cantidad de peticiones de turno
     */
    public static int contarPeticionesTurnoTotales() {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM peticiones "
                + "WHERE tipo = 'TURNO' "
                + "AND estado <> 'FALLIDA'";

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
     * Metodo que cuenta los turnos reservados.
     *
     * @return cantidad de turnos reservados
     */
    public static int contarTurnosReservados() {

        String sql = "SELECT COUNT(*) AS total "
                + "FROM turnos "
                + "WHERE estado = 'RESERVADO' "
                + "AND is_deleted = 0";

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
     * Metodo que obtiene las peticiones que no son de cambio de rol.
     *
     * @return lista de peticiones sin las de cambio de rol
     */
    public static List<Peticion> obtenerSinCambioRol() {

        ArrayList<Peticion> lista = new ArrayList<>();

        String sql = "SELECT * FROM peticiones WHERE tipo <> ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se excluyen las peticiones de cambio de rol
            ps.setString(1, "CAMBIO_ROL"); // o TipoSolicitud.CAMBIO_ROL.name()

            try (ResultSet rs = ps.executeQuery()) {
                // Se agregan las peticiones encontradas a la lista
                while (rs.next()) {
                    lista.add(mapPeticion(rs));
                }
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que obtiene todas las peticiones junto con la informacion del
     * usuario.
     *
     * @return lista de peticiones con su usuario completo
     */
    public static List<Peticion> obtenerTodasConUsuario() {

        List<Peticion> lista = new ArrayList<>();

        String sql = """
        SELECT 
            p.*,

            u.id_usuario AS u_id_usuario,
            u.username AS u_username,
            u.password_hash AS u_password_hash,
            u.salt AS u_salt,
            u.phone AS u_phone,
            u.name AS u_name,
            u.rol AS u_rol,
            u.estado AS u_estado

        FROM peticiones p
        INNER JOIN usuarios u ON p.id_usuario = u.id_usuario
        """;

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Se recorren las peticiones junto con los datos del usuario
            while (rs.next()) {
                lista.add(mapPeticionConUsuario(rs));
            }

        } catch (SQLException e) {
            // Si ocurre un error al consultar, se muestra en consola
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Metodo que convierte una fila de la base de datos en una peticion con la
     * informacion completa del usuario.
     *
     * @param rs resultado de la consulta
     * @return peticion con usuario completo
     * @throws SQLException si ocurre un error leyendo los datos
     */
    private static Peticion mapPeticionConUsuario(ResultSet rs) throws SQLException {

        // Primero se cargan los datos normales de la peticion
        Peticion p = mapPeticion(rs);

        // Se crea el usuario completo con los datos que vienen del join
        Usuario u = new Usuario();

        u.setIdUsuario(rs.getInt("u_id_usuario"));
        u.setUsername(rs.getString("u_username"));
        u.setPasswordHash(rs.getString("u_password_hash"));
        u.setSalt(rs.getString("u_salt"));
        u.setTelefono(rs.getString("u_phone"));
        u.setName(rs.getString("u_name"));
        u.setRol(Rol.valueOf(rs.getString("u_rol")));
        u.setEstado(EstadoCuenta.valueOf(rs.getString("u_estado")));

        // Se asigna el usuario completo a la peticion
        p.setUsuario(u);

        return p;
    }

    /**
     * Metodo que actualiza el estado de una peticion. Tambien actualiza la
     * fecha de proceso o finalizacion segun el estado.
     *
     * @param idPeticion id de la peticion que se desea actualizar
     * @param estado nuevo estado de la peticion
     * @return true si se actualizo correctamente, false si no
     */
    public static boolean updateEstadoPeticion(int idPeticion, EstadoPeticion estado) {
        String sql = "UPDATE peticiones "
                + "SET estado = ?, "
                + "procesada_en = CASE WHEN ? = 'EN_PROCESO' THEN NOW() ELSE procesada_en END, "
                + "finalizada_en = CASE WHEN ? IN ('COMPLETADA', 'FALLIDA') THEN NOW() ELSE finalizada_en END "
                + "WHERE id_peticion = ?";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            // Se asigna el nuevo estado de la peticion
            ps.setString(1, estado.name());

            // Se usa el estado para decidir si se actualiza la fecha de proceso
            ps.setString(2, estado.name());

            // Se usa el estado para decidir si se actualiza la fecha de finalizacion
            ps.setString(3, estado.name());

            // Se indica cual peticion se va a actualizar
            ps.setInt(4, idPeticion);

            // Si se actualizo al menos una fila, se devuelve true
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Si ocurre un error al actualizar, se muestra en consola
            e.printStackTrace();
            return false;
        }
    }
}
