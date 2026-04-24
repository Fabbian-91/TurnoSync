/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package common.enums;

/**
 *
 * @author Fabian
 */
public enum TipoSolicitud {
    AGREGAR_TURNO("Agregar turno"),
    CAMBIO_TURNO("Cambio de turno"),
    CANCELAR_TURNO("Cancelación de turno"),
    TURNO_COMPLETADO("Turno Completado"),
    CAMBIO_ROL("Cambio de rol");

    private final String descripcion;

    TipoSolicitud(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
