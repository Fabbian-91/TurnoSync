/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package common.enums;

/**
 *
 * @author Fabian
 */
public enum EstadoTurno {
    RESERVADO(1, "Reservado"),
    CANCELADO(2, "Cancelado"),
    ATENDIDO(3, "Atendido");

    private int id;
    private String nombre;

    private EstadoTurno(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String devolverDatos() {
        return id + " - " + nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public static EstadoTurno getById(int id) {
        for (EstadoTurno estado : EstadoTurno.values()) {
            if (estado.getId() == id) {
                return estado;
            }
        }
        return null;
    }
}
