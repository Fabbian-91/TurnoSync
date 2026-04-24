/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package common.enums;

/**
 *
 * @author Fabian
 */
public enum Rol {
    ADMIN(1, "Administrador"),
    EMPLEADO(2, "Empleado"),
    CLIENTE(3, "Cliente");

    private int id;
    private String nombre;

    private Rol(int id, String nombre) {
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

    public static Rol getById(int id) {
        for (Rol rol : Rol.values()) {
            if (rol.getId() == id) {
                return rol;
            }
        }
        return null; 
    }
}
