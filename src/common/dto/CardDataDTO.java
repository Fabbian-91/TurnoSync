/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import common.enums.TipoCard;
import java.io.Serializable;

/**
 *
 * @author Fabian
 */
public class CardDataDTO implements Serializable {

    private TipoCard tipo;
    private int valor;

    public CardDataDTO(TipoCard tipo, int valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TipoCard getTipo() {
        return tipo;
    }

    public void setTipo(TipoCard tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
