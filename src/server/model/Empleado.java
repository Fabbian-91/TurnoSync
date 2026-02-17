/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

/**
 *
 * @author Fabian
 */
public class Empleado {

    //Atributos del empleado
    private int idEmpleado;
    private String nombre;
    private String especialidad;

    //Constructor vacío
    public Empleado() {
    }

    /**
     * Generar empleados
     *
     * @param idEmpleado
     * @param nombre
     * @param especialidad
     */
    public Empleado(int idEmpleado, String nombre, String especialidad) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}
