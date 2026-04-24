/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

import common.enums.EstadoCuenta;
import common.enums.Rol;
import java.io.Serializable;

public class Usuario implements Serializable {

    // Identificador de version para poder enviar objetos por la red
    private static final long serialVersionUID = 1L;

    // Atributos principales del usuario
    private int idUsuario;
    private String username;
    private String passwordHash;
    private String salt;
    private String telefono;
    private String name;
    private Rol rol;
    private EstadoCuenta estado;

    // Ids que relacionan al usuario con un cliente o empleado
    private int idCliente;
    private int idEmpleado;

    // Constructor vacio para poder crear un usuario sin datos iniciales
    public Usuario() {
    }

    // Constructor completo para crear un usuario con todos sus datos
    public Usuario(int idUsuario, String username, String passwordHash, String salt, String telefono, String name, Rol rol, EstadoCuenta estado, int idCliente, int idEmpleado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.telefono = telefono;
        this.name = name;
        this.rol = rol;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
    }

    // Constructor por defecto para crear un usuario como empleado activo
    public Usuario(String username, String passwordHash, String telefono, String name, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.name = name;
        this.salt = salt;
        this.rol = Rol.EMPLEADO;
        this.estado = EstadoCuenta.ACTIVO;
    }

    // Constructor para crear un usuario indicando el rol que va a tener
    public Usuario(String username, String passwordHash, String telefono, String name, Rol rol) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.name = name;
        this.rol = rol;
    }

    // Metodos get y set para acceder y modificar los datos del usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
}

