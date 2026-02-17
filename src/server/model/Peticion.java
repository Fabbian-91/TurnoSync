/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

import java.time.LocalDateTime;

/**
 *
 * @author Fabian
 */
public class Peticion {

    private int idPeticion;
    private String tipo;
    private EstadoPeticion estado;

    private LocalDateTime creadaEn;
    private LocalDateTime procesadaEn;
    private LocalDateTime finalizadaEn;

    private Usuario usuario;
    private Turno turno;   // Puede ser null
    private String detalle;

    // Constructor vacío
    public Peticion() {
    }

    // Constructor básico
    public Peticion(int idPeticion, String tipo, EstadoPeticion estado,
            LocalDateTime creadaEn, Usuario usuario) {
        this.idPeticion = idPeticion;
        this.tipo = tipo;
        this.estado = estado;
        this.creadaEn = creadaEn;
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getIdPeticion() {
        return idPeticion;
    }

    public void setIdPeticion(int idPeticion) {
        this.idPeticion = idPeticion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public EstadoPeticion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPeticion estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(LocalDateTime creadaEn) {
        this.creadaEn = creadaEn;
    }

    public LocalDateTime getProcesadaEn() {
        return procesadaEn;
    }

    public void setProcesadaEn(LocalDateTime procesadaEn) {
        this.procesadaEn = procesadaEn;
    }

    public LocalDateTime getFinalizadaEn() {
        return finalizadaEn;
    }

    public void setFinalizadaEn(LocalDateTime finalizadaEn) {
        this.finalizadaEn = finalizadaEn;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

}
