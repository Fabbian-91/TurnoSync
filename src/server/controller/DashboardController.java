/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.controller;

/**
 *
 * @author Fabian
 */

import common.dto.CardDataDTO;
import common.dto.Request;
import common.dto.Result;
import common.enums.TipoCard;
import common.enums.TipoSolicitud;
import java.util.ArrayList;
import server.service.DashboardService;

public class DashboardController {

    public Result<ArrayList<CardDataDTO>> loadCard(Request<?> request) {
        try {
            ArrayList<CardDataDTO> data = new ArrayList<>();

            data.add(new CardDataDTO(TipoCard.USUARIOS, 0));
            data.add(new CardDataDTO(TipoCard.SOLICITUDES, 0));
            data.add(new CardDataDTO(TipoCard.TURNOS_ACTIVOS, 0));
            data.add(new CardDataDTO(TipoCard.PENDIENTES, 0));
            data.add(new CardDataDTO(TipoCard.CLIENTES, 0));
            data.add(new CardDataDTO(TipoCard.TURNOS_TOTALES, 0));
            data.add(new CardDataDTO(TipoCard.TURNOS_DESIGNADOS, 0));
            data.add(new CardDataDTO(TipoCard.EMPLEADOS, 0));

            return DashboardService.loadCard(data);

        } catch (Exception e) {
            return Result.fail("message:Error inesperado\nError:500");
        }
    }

    public Result<ArrayList<TipoSolicitud>> loadBoard(Request<?> request) {
        try {
            ArrayList<TipoSolicitud> lista = new ArrayList<>();
            lista = DashboardService.loadBoard(lista);
            return Result.ok("Datos cargados", lista);
        } catch (Exception e) {
            return Result.fail("message:Error inesperado\nError:500");
        }
    }
}
