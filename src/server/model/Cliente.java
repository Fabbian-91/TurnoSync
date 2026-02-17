/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

/**
 *
 * @author Fabian
 */
public class Cliente {

    //Atributos del cliente
    private int idCliente;
    private String nombre;
    private String telefono;
    private String email;

    //Contructor vacío
    public Cliente() {
    }

    /**
     * Generar clientes
     *
     * @param idCliente
     * @param nombre
     * @param telefono
     * @param email
     */
    public Cliente(int idCliente, String nombre, String telefono, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
