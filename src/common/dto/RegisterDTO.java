/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import java.io.Serializable;

public class RegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private String telefono;
    private String nombre;

    public RegisterDTO() {
    }

    public RegisterDTO(String email, String password, String telefono, String nombre) {
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
