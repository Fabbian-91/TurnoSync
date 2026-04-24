/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.sesion;

import client.view.JIFAtender;
import client.view.JIFBienvenida;
import client.view.JIFConfiguracion;
import client.view.JIFGestionTurnos;
import client.view.JIFGestionUsers;
import client.view.JIFHistorial;
import client.view.JIFMisTurnos;
import client.view.JIFSolicitud;
import client.view.MDIPrincipal;
import common.dto.LoginDTO;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;
import common.enums.Rol;

/**
 *
 * @author Fabian
 */
public class AppSession {

    // Ventana principal de la aplicacion
    private static MDIPrincipal principal;

    // Formularios internos que se usan dentro del sistema
    private static JIFBienvenida bienvenida;
    private static JIFGestionUsers gestion;
    private static JIFGestionTurnos gestionTurnos;
    private static JIFAtender atender;
    private static JIFConfiguracion configuracion;
    private static JIFHistorial historial;
    private static JIFMisTurnos turnos;
    private static JIFSolicitud solicitud;

    // Datos del usuario que inicio sesion
    private static String user;
    private static Rol rol;
    private static String name;
    private static int idUser;
    private static int idCliete;
    private static int idEmpleado;

    // Variable que guarda la fecha y hora actual en formato texto
    private static String fechaHora;

    // Lista de listeners que se notifican cuando cambia la fecha y hora
    private static final List<Runnable> listeners = new CopyOnWriteArrayList<>();

    // Timer global que actualiza la fecha y hora cada segundo
    private static Timer timer;

    // Formatos que se usan para mostrar la fecha y la hora
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Metodo para devolver el formulario de atender. Si no existe todavia, lo
     * crea antes de devolverlo.
     *
     * @return formulario de atender solicitudes
     */
    public static JIFAtender getAtender() {
        // Se revisa si el formulario aun no ha sido creado
        if (atender == null) {
            // Si no existe, se crea una nueva instancia
            atender = new JIFAtender();
        }

        // Se devuelve el formulario ya creado
        return atender;
    }

    /**
     * Metodo para devolver el formulario de configuracion. Si no existe
     * todavia, lo crea antes de devolverlo.
     *
     * @return formulario de configuracion
     */
    public static JIFConfiguracion getConfiguracion() {
        // Se revisa si el formulario aun no existe
        if (configuracion == null) {
            // Si no existe, se crea una nueva instancia
            configuracion = new JIFConfiguracion();
        }

        // Se devuelve el formulario de configuracion
        return configuracion;
    }

    /**
     * Metodo para devolver el formulario de historial. Si no existe todavia, lo
     * crea antes de devolverlo.
     *
     * @return formulario de historial
     */
    public static JIFHistorial getHistorial() {
        // Se revisa si el formulario aun no ha sido creado
        if (historial == null) {
            // Si no existe, se crea una nueva instancia
            historial = new JIFHistorial();
        }

        // Se devuelve el formulario de historial
        return historial;
    }

    /**
     * Metodo para devolver el formulario de mis turnos. Si no existe todavia,
     * lo crea antes de devolverlo.
     *
     * @return formulario de mis turnos
     */
    public static JIFMisTurnos getMisTurnos() {
        // Se revisa si el formulario aun no existe
        if (turnos == null) {
            // Si no existe, se crea una nueva instancia
            turnos = new JIFMisTurnos();
        }

        // Se devuelve el formulario de mis turnos
        return turnos;
    }

    /**
     * Metodo para devolver el formulario de solicitud. Si no existe todavia, lo
     * crea antes de devolverlo.
     *
     * @return formulario de solicitudes
     */
    public static JIFSolicitud getSolicitud() {
        // Se revisa si el formulario aun no existe
        if (solicitud == null) {
            // Si no existe, se crea una nueva instancia
            solicitud = new JIFSolicitud();
        }

        // Se devuelve el formulario de solicitud
        return solicitud;
    }

    /**
     * Metodo para devolver el contenedor principal. Si no existe todavia, lo
     * crea antes de devolverlo.
     *
     * @return ventana principal de la aplicacion
     */
    public static MDIPrincipal getMainMDI() {
        // Se valida si el contenedor principal ya fue inicializado
        if (principal == null) {
            // Si no existe, se crea la ventana principal
            principal = new MDIPrincipal();
        }

        // Se retorna el contenedor principal
        return principal;
    }

    /**
     * Metodo para devolver el formulario de bienvenida. Si no existe todavia,
     * lo crea antes de devolverlo.
     *
     * @return formulario de bienvenida
     */
    public static JIFBienvenida getBienvenida() {
        // Se valida si el formulario de bienvenida ya fue inicializado
        if (bienvenida == null) {
            // Si no existe, se crea una nueva instancia
            bienvenida = new JIFBienvenida();
        }

        // Se retorna la instancia de bienvenida
        return bienvenida;
    }

    /**
     * Metodo para devolver el formulario de gestion de usuarios. Si no existe
     * todavia, lo crea antes de devolverlo.
     *
     * @return formulario de gestion de usuarios
     */
    public static JIFGestionUsers getGestionUsers() {
        // Se valida si el formulario de gestion de usuarios ya fue inicializado
        if (gestion == null) {
            // Si no existe, se crea una nueva instancia
            gestion = new JIFGestionUsers();
        }

        // Se retorna la instancia de gestion de usuarios
        return gestion;
    }

    /**
     * Metodo para devolver el formulario de gestion de turnos. Si no existe
     * todavia, lo crea antes de devolverlo.
     *
     * @return formulario de gestion de turnos
     */
    public static JIFGestionTurnos getGestionTurnos() {
        // Se valida si el formulario de gestion de turnos ya fue inicializado
        if (gestionTurnos == null) {
            // Si no existe, se crea una nueva instancia
            gestionTurnos = new JIFGestionTurnos();
        }

        // Se retorna la instancia de gestion de turnos
        return gestionTurnos;
    }

    /**
     * Metodo para guardar los datos de la sesion del usuario. Se usa despues de
     * iniciar sesion correctamente.
     *
     * @param login informacion del usuario que inicio sesion
     */
    public static void setSession(LoginDTO login) {
        // Se guarda el correo o usuario que inicio sesion
        user = login.getEmail();

        // Se guarda el rol del usuario
        rol = login.getRol();

        // Se guarda el nombre del usuario
        name = login.getNombre();

        // Se guardan los ids relacionados con el usuario
        idUser = login.getId();
        idCliete = login.getId_Cliente();
        idEmpleado = login.getId_empleado();

        // Se imprimen los ids en consola para revisar que llegaron correctamente
        System.out.println(idUser + " " + idCliete + " " + idEmpleado);

        // Se inicia el timer que actualiza la fecha y hora de la sesion
        iniciarTimer();
    }

    /**
     * Metodo que inicia el timer global. Este timer actualiza la fecha y hora
     * cada segundo y avisa a las vistas que esten escuchando.
     */
    private static void iniciarTimer() {
        // Si ya habia un timer activo, se detiene para evitar duplicados
        if (timer != null) {
            timer.stop();
        }

        // Se crea un timer que se ejecuta cada segundo
        timer = new Timer(1000, e -> {
            // Se obtiene la fecha actual del sistema
            LocalDate date = LocalDate.now();

            // Se obtiene la hora actual del sistema
            LocalTime time = LocalTime.now();

            // Se arma el texto que muestra la fecha y la hora formateadas
            fechaHora = "Fecha: " + date.format(FORMATO_FECHA)
                    + " | Hora: " + time.format(FORMATO_HORA);

            // Se notifica a todos los listeners para que actualicen su informacion
            for (Runnable r : listeners) {
                r.run();
            }
        });

        // Se inicia el timer
        timer.start();
    }

    /**
     * Metodo que permite agregar un listener. Las vistas lo usan para enterarse
     * cuando cambia la fecha y hora.
     *
     * @param listener accion que se ejecutara cuando cambie la fecha y hora
     */
    public static void addListener(Runnable listener) {
        // Se agrega el listener a la lista
        listeners.add(listener);
    }

    /**
     * Metodo que permite quitar un listener. Esto ayuda a liberar memoria
     * cuando una vista ya no se usa.
     *
     * @param listener listener que se desea eliminar
     */
    public static void removeListener(Runnable listener) {
        // Se elimina el listener de la lista
        listeners.remove(listener);
    }

    /**
     * Metodo que devuelve la fecha y hora actual de la sesion.
     *
     * @return fecha y hora en formato texto
     */
    public static String getFechaHora() {
        // Se devuelve el texto de fecha y hora actual
        return fechaHora;
    }

    /**
     * Metodo para limpiar los datos de la sesion. Se usa normalmente cuando el
     * usuario cierra sesion.
     */
    public static void clearSession() {
        // Se limpian los datos principales del usuario
        user = null;
        name = null;
        rol = null;

        // Se limpia la fecha y hora guardada
        fechaHora = null;

        // Si el timer esta activo, se detiene
        if (timer != null) {
            timer.stop();

            // Se deja el timer en null para indicar que ya no esta activo
            timer = null;
        }

        // Se limpian todos los listeners registrados
        listeners.clear();
    }

    /**
     * Metodo para devolver el username del usuario.
     *
     * @return usuario actual
     */
    public static String getUsername() {
        // Se devuelve el usuario que inicio sesion
        return user;
    }

    /**
     * Metodo para devolver el rol del usuario.
     *
     * @return rol del usuario actual
     */
    public static Rol getRol() {
        // Se devuelve el rol guardado en la sesion
        return rol;
    }

    /**
     * Metodo para devolver el usuario actual.
     *
     * @return usuario actual
     */
    public static String getUser() {
        // Se devuelve el usuario guardado
        return user;
    }

    /**
     * Metodo para cambiar el usuario guardado en la sesion.
     *
     * @param user nuevo usuario
     */
    public static void setUser(String user) {
        // Se actualiza el usuario de la sesion
        AppSession.user = user;
    }

    /**
     * Metodo para devolver el nombre del usuario actual.
     *
     * @return nombre del usuario
     */
    public static String getName() {
        // Se devuelve el nombre guardado en la sesion
        return name;
    }

    /**
     * Metodo para cambiar el nombre guardado en la sesion.
     *
     * @param name nuevo nombre
     */
    public static void setName(String name) {
        // Se actualiza el nombre de la sesion
        AppSession.name = name;
    }

    /**
     * Metodo para devolver el id del usuario.
     *
     * @return id del usuario
     */
    public static int getIdUser() {
        // Se devuelve el id del usuario actual
        return idUser;
    }

    /**
     * Metodo para cambiar el id del usuario.
     *
     * @param idUser nuevo id del usuario
     */
    public static void setIdUser(int idUser) {
        // Se actualiza el id del usuario
        AppSession.idUser = idUser;
    }

    /**
     * Metodo para devolver el id del cliente.
     *
     * @return id del cliente
     */
    public static int getIdCliete() {
        // Se devuelve el id del cliente asociado
        return idCliete;
    }

    /**
     * Metodo para cambiar el id del cliente.
     *
     * @param idCliete nuevo id del cliente
     */
    public static void setIdCliete(int idCliete) {
        // Se actualiza el id del cliente
        AppSession.idCliete = idCliete;
    }

    /**
     * Metodo para devolver el id del empleado.
     *
     * @return id del empleado
     */
    public static int getIdEmpleado() {
        // Se devuelve el id del empleado asociado
        return idEmpleado;
    }

    /**
     * Metodo para cambiar el id del empleado.
     *
     * @param idEmpleado nuevo id del empleado
     */
    public static void setIdEmpleado(int idEmpleado) {
        // Se actualiza el id del empleado
        AppSession.idEmpleado = idEmpleado;
    }
}
