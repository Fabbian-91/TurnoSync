/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

import client.gateway.ServerGateway;
import common.dto.Result;
import common.enums.EstadoTurno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import server.model.Turno;

public class TurnoController {

    // Puerta de comunicacion con el servidor
    private final ServerGateway gateway;

    /**
     * Constructor que inicializa la puerta de comunicacion.
     *
     * @param gateway puerta para comunicarse con el servidor
     */
    public TurnoController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Metodo que carga todos los turnos registrados. La informacion se obtiene
     * desde el servidor usando el gateway.
     *
     * @return lista con los turnos registrados
     */
    public Result<ArrayList<Turno>> loadTurnos() {
        return gateway.loadTurno();
    }

    /**
     * Metodo que carga los turnos de un usuario especifico. Primero valida que
     * el id sea correcto antes de pedir la informacion.
     *
     * @param id id del usuario
     * @return lista de turnos del usuario
     */
    public Result<ArrayList<Turno>> loadTurnosByUser(int id) {
        if (id <= 0) {
            return Result.fail("El id debe ser mayor a 0");
        }
        return gateway.loadGetTuro(id);
    }

    /**
     * Metodo para crear un nuevo turno. Primero valida que los datos del turno
     * esten completos. Si todo esta correcto, lo envia al servidor.
     *
     * @param turno turno que se desea registrar
     * @return resultado de la creacion del turno
     */
    public Result<Void> createTurno(Turno turno) {
        try {
            validarTurno(turno);
            return gateway.agregarTurno(turno);
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al agregar turno: " + e.getMessage());
        }
    }

    /**
     * Metodo que valida que el id del cliente sea correcto.
     *
     * @param idCliente id del cliente
     */
    private void validarIdCliente(int idCliente) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El id del cliente debe ser mayor a 0");
        }
    }

    /**
     * Metodo que valida que el id del empleado sea correcto.
     *
     * @param idEmpleado id del empleado
     */
    private void validarIdEmpleado(int idEmpleado) {
        if (idEmpleado <= 0) {
            throw new IllegalArgumentException("El id del empleado debe ser mayor a 0");
        }
    }

    /**
     * Metodo que valida que la fecha no venga vacia.
     *
     * @param fecha fecha del turno
     */
    private void validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
    }

    /**
     * Metodo que valida que la hora no venga vacia.
     *
     * @param hora hora del turno
     */
    private void validarHora(LocalTime hora) {
        if (hora == null) {
            throw new IllegalArgumentException("La hora no puede estar vacía");
        }
    }

    /**
     * Metodo que valida que el estado del turno no venga vacio.
     *
     * @param estado estado del turno
     */
    private void validarEstado(EstadoTurno estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }
    }

    /**
     * Metodo que valida toda la informacion de un turno. Revisa que el turno
     * exista y que tenga fecha, hora, estado, cliente y empleado.
     *
     * @param turno turno que se va a validar
     */
    private void validarTurno(Turno turno) {
        if (turno == null) {
            throw new IllegalArgumentException("El turno no puede ser nulo");
        }

        validarFecha(turno.getFecha());
        validarHora(turno.getHora());
        validarEstado(turno.getEstado());
        validarIdCliente(turno.getIdCliente());
        validarIdEmpleado(turno.getIdEmpleado());
    }

    /**
     * Metodo para actualizar la informacion de un turno. Primero valida los
     * datos recibidos, convierte la fecha y la hora, crea un objeto Turno con
     * la informacion nueva y lo envia al servidor.
     *
     * @param idTurno id del turno que se desea actualizar
     * @param idCliente id del cliente
     * @param fechaTexto fecha escrita en texto
     * @param horaTexto hora escrita en texto
     * @param estado estado del turno
     * @param idEmpleado id del empleado
     * @return resultado de la actualizacion
     */
    public Result<Void> updateTurno(int idTurno, int idCliente, String fechaTexto, String horaTexto,
            EstadoTurno estado, int idEmpleado) {
        try {
            if (idTurno <= 0) {
                throw new IllegalArgumentException("Debe seleccionar un turno válido");
            }

            if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
                throw new IllegalArgumentException("La fecha no puede estar vacía");
            }

            if (horaTexto == null || horaTexto.trim().isEmpty()) {
                throw new IllegalArgumentException("La hora no puede estar vacía");
            }

            validarIdCliente(idCliente);
            validarIdEmpleado(idEmpleado);
            validarEstado(estado);

            LocalDate fecha = parseFecha(fechaTexto.trim());
            LocalTime hora = parseHora(horaTexto.trim());

            validarFecha(fecha);
            validarHora(hora);

            // Se crea un turno con los datos actualizados
            Turno turno = new Turno();
            turno.setIdTurno(idTurno);
            turno.setIdCliente(idCliente);
            turno.setIdEmpleado(idEmpleado);
            turno.setFecha(fecha);
            turno.setHora(hora);
            turno.setEstado(estado);

            return gateway.updateTurno(turno);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al actualizar turno: " + e.getMessage());
        }
    }

    /**
     * Metodo que convierte el texto de la fecha a tipo LocalDate. Primero
     * intenta con el formato normal de Java. Si no funciona, intenta con el
     * formato dia/mes/año.
     *
     * @param fechaTexto fecha escrita en texto
     * @return fecha convertida
     */
    private LocalDate parseFecha(String fechaTexto) {
        try {
            return LocalDate.parse(fechaTexto);
        } catch (Exception e) {
            try {
                return LocalDate.parse(
                        fechaTexto,
                        java.time.format.DateTimeFormatter.ofPattern("d/M/yy")
                );
            } catch (Exception ex) {
                throw new IllegalArgumentException("La fecha debe tener formato válido. Ejemplo: 2026-04-24 o 24/4/26");
            }
        }
    }

    /**
     * Metodo que convierte el texto de la hora a tipo LocalTime. Primero
     * intenta convertir la hora directamente. Si no funciona, intenta con el
     * formato hora:minutos.
     *
     * @param horaTexto hora escrita en texto
     * @return hora convertida
     */
    private LocalTime parseHora(String horaTexto) {
        try {
            return LocalTime.parse(horaTexto);
        } catch (Exception e) {
            try {
                return LocalTime.parse(
                        horaTexto,
                        java.time.format.DateTimeFormatter.ofPattern("H:mm")
                );
            } catch (Exception ex) {
                throw new IllegalArgumentException("La hora debe tener formato válido. Ejemplo: 14:30");
            }
        }
    }

    /**
     * Metodo que valida que el id del turno sea correcto.
     *
     * @param idTurno id del turno
     */
    private void validarIdTurno(int idTurno) {
        if (idTurno <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un turno válido");
        }
    }

    /**
     * Metodo para eliminar un turno. Primero valida que se haya seleccionado un
     * turno correcto. Luego envia la solicitud al servidor.
     *
     * @param idTurno id del turno que se desea eliminar
     * @return resultado de la eliminacion
     */
    public Result<Void> deleteTurno(int idTurno) {
        try {
            validarIdTurno(idTurno);

            return gateway.deleteTurno(idTurno);

        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            return Result.fail("Error al eliminar turno: " + e.getMessage());
        }
    }
}
