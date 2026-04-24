/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package common.enums;

/**
 *
 * @author Fabian
 */
public enum EstadoCuenta {
    ACTIVO(1,"Activo"),
    INACTIVO(2,"Inactivo");
    
    private int id;
    private String nombre;

    private EstadoCuenta(int id, String nombre) {
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

    public static EstadoCuenta getById(int id) {
        for (EstadoCuenta estado : EstadoCuenta.values()) {
            if (estado.getId() == id) {
                return estado;
            }
        }
        return null; 
    }
    
}
