/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.security;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Fabian
 */
public class ClsCreatePasswordRandom {

    /**
     * Metodo para crear contraseña seguras
     * @return 
     */
    public static String createPasswordRandom() {
        //Caracteres de la contraseña
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        //Generar contraseñas aleatorias con la libreria apache commons lang
        String pwd = RandomStringUtils.random(15, characters);
        //Retornamos la contraseña
        return pwd;
    }
}
