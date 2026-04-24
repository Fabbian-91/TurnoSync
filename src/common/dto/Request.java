/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import common.enums.ActionType;
import common.enums.TipoSolicitud;
import java.io.Serializable;

/**
 *
 * @author Fabian
 */
public class Request<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestId;
    private ActionType action;
    private String token;
    private T data;

    public Request() {
    }

    public Request(String requestId, ActionType action, String token, T data) {
        this.requestId = requestId;
        this.action = action;
        this.token = token;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
