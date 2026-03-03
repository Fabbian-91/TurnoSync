/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package client.gateway;
import common.dto.Result;
/**
 *
 * @author Fabian
 */
//Interfaz para aplicar a diferentes tipos de resultados
public interface ServerGateway {
    //Generamos un núevo resultado donde su generico se void para devolver unicamente el resultado de la operación
    Result<Void> register(String email, String password, String n, String na);//Interfaz para generar respuestas registros
    Result<Void> login(String email,String password);//Interfaz para generar respuestas de login
    Result<Void> updateForget(String data, String data0, String data1);
}
