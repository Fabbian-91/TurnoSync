/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Fabian
 */
public class Turno {
    //Atributos del turno
    private int idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;
   
    //Atributos nulos
    private Cliente cliente;
    private Empleado empleado;
    
    //Constructor vacío
    public Turno() {
    }
    
    /**
     * Generar Turno
     * @param idTurno
     * @param fecha
     * @param hora
     * @param estado 
     */
    public Turno(int idTurno, LocalDate fecha, LocalTime hora, EstadoTurno estado) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
