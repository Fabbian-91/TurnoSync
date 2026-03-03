package app;


import client.view.JfLogin;
import server.dao.ConexionBD;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Fabian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Generamos el núevo formulario de login
        JfLogin login=new JfLogin();
        //Mostramos el formulario en pantalla
        login.show();
        //Probar Conexión a la bd
        ConexionBD.getConnection();
    }
    
}
  