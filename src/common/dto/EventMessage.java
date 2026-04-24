/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import common.enums.TypeEvent;
import java.io.Serializable;

public class EventMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private TypeEvent type;
    private Object data;

    public EventMessage(TypeEvent type, Object data) {
        this.type = type;
        this.data = data;
    }

    public TypeEvent getType() {
        return type;
    }

    public void setType(TypeEvent type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
