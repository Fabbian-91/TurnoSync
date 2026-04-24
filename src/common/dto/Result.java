package common.dto;

import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Fabian
 */
//Clase generica para manejar los resultados del client y server
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final boolean ok;
    public final String message;
    public final T data;

    private Result(boolean ok, String message, T data) {
        this.ok = ok;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(true, msg, data);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(false, msg, null);
    }

    public static Result<Void> ok(String msg) {
        return new Result<>(true, msg, null);
    }
}
