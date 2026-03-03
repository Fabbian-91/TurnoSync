/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.model;

/**
 *
 * @author Fabian
 */
public class Usuario {
    //Atributos del Usuario
    private int idUsuario;
    private String username;
    private String passwordHash;
    private String salt;
    private String telefono;
    private String name;
    private Rol rol;
    private EstadoUser estado;
    
    
    //Atributos nulos
    private Cliente cliente;      
    private Empleado empleado;    
    
    //Constructor vacío
    public Usuario() {
    }
    
    /**
     * Generar Usuarios
     * @param idUsuario
     * @param username
     * @param passwordHash
     * @param rol
     * @param estado 
     */
    public Usuario(String username, String passwordHash,String telefono,String name,String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = Rol.EMPLEADO;
        this.telefono=telefono;
        this.name=name;
        this.salt=salt;
        this.estado = EstadoUser.ACTIVO;
    }

    // Getters y Setters
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

    public EstadoUser getEstado() {
        return estado;
    }

    public void setEstado(EstadoUser estado) {
        this.estado = estado;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
 
    
}
