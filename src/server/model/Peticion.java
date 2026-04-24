package server.model;

import common.enums.EstadoPeticion;
import common.enums.TipoSolicitud;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Peticion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPeticion;
    private TipoSolicitud tipo;
    private EstadoPeticion estado;

    private LocalDateTime creadaEn;
    private LocalDateTime procesadaEn;
    private LocalDateTime finalizadaEn;

    private Usuario usuario;

    private String detalle;

    public Peticion() {
        this.creadaEn = LocalDateTime.now();
        this.estado = EstadoPeticion.PENDIENTE;
    }

    public Peticion(int idPeticion, TipoSolicitud tipo, Usuario usuario, String detalle) {
        this.idPeticion = idPeticion;
        this.tipo = tipo;
        this.usuario = usuario;
        this.detalle = detalle;
        this.creadaEn = LocalDateTime.now();
        this.estado = EstadoPeticion.PENDIENTE;
    }

    public void marcarEnProceso() {
        this.estado = EstadoPeticion.EN_PROCESO;
        this.procesadaEn = LocalDateTime.now();
    }

    public void completar() {
        this.estado = EstadoPeticion.COMPLETADA;
        this.finalizadaEn = LocalDateTime.now();
    }

    public void fallar() {
        this.estado = EstadoPeticion.FALLIDA;
        this.finalizadaEn = LocalDateTime.now();
    }

    public int getIdPeticion() {
        return idPeticion;
    }

    public void setIdPeticion(int idPeticion) {
        this.idPeticion = idPeticion;
    }

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public void setTipo(TipoSolicitud tipo) {
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


    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
