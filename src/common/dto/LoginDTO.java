/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import common.enums.Rol;
import java.io.Serializable;

public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private Rol rol;
    private String nombre;
    private int id;
    private int id_Cliente;
    private int id_empleado;
    private String token; 

    public LoginDTO() {
    }

    public LoginDTO(String email, Rol rol, String nombre, int id, int id_Cliente, int id_empleado, String token) {
        this.email = email;
        this.rol = rol;
        this.nombre = nombre;
        this.id = id;
        this.id_Cliente = id_Cliente;
        this.id_empleado = id_empleado;
        this.token = token;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }
    
}
