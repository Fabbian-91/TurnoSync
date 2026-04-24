/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.controller;

import client.gateway.ServerGateway;
import common.dto.CardDataDTO;
import common.dto.Result;
import common.enums.TipoSolicitud;
import java.util.ArrayList;

public class DashboardController {

    // Puerta de comunicacion con el servidor
    private final ServerGateway gateway;

    /**
     * Constructor que inicializa la puerta de comunicacion
     *
     * @param gateway puerta para comunicarse con el servidor
     */
    public DashboardController(ServerGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Metodo que carga la informacion de las tarjetas del dashboard. Le pide
     * los datos al servidor por medio del gateway.
     *
     * @return lista con la informacion de las tarjetas
     */
    public Result<ArrayList<CardDataDTO>> loadCards() {
        return gateway.loadCards();
    }

    /**
     * Metodo que carga los tipos de solicitud que se muestran en el tablero.
     * Obtiene la informacion desde el servidor usando el gateway.
     *
     * @return lista con los tipos de solicitud del tablero
     */
    public Result<ArrayList<TipoSolicitud>> loadBoard() {
        return gateway.loadBoard();
    }
}
