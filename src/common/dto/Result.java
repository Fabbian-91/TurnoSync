package common.dto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Fabian
 */
//Clase generica para manejar los resultados del client y server
public class Result<T> {

    //Atributos
    public final boolean ok;
    public final String message;
    public final T data;

    /**
     * Constructor del resultado
     *
     * @param ok
     * @param message
     * @param data
     */
    private Result(boolean ok, String message, T data) {
        this.ok = ok;
        this.message = message;
        this.data = data;
    }

    /**
     * Metodo para devolver resultado con estado ok
     *
     * @param <T>
     * @param msg
     * @param data
     * @return
     */
    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(true, msg, data);
    }

    /**
     * Metodo para devolver resultado con estado fail
     *
     * @param <T>
     * @param msg
     * @return
     */
    public static <T> Result<T> fail(String msg) {
        return new Result<>(false, msg, null);
    }

    /**
     * Resultado exitoso solo con mensaje
     * @param msg
     * @return 
     */
    public static Result<Void> ok(String msg) {
        return new Result<>(true, msg, null);
    }
}
