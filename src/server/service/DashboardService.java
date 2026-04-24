/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.service;

/**
 *
 * @author Fabian
 */
import common.dto.CardDataDTO;
import common.dto.Result;
import common.enums.TipoSolicitud;
import java.util.ArrayList;
import server.dao.ClienteDAO;
import server.dao.EmpleadoDAO;
import server.dao.PeticionDAO;
import server.dao.TurnoDAO;
import server.dao.UsuarioDAO;

import server.model.Peticion;

public class DashboardService {

    /**
     * Metodo que carga los valores de las tarjetas del dashboard. Recorre cada
     * tarjeta recibida y segun su tipo consulta el dato correspondiente.
     *
     * @param data lista de tarjetas que se van a completar
     * @return resultado con las tarjetas cargadas
     */
    public static Result<ArrayList<CardDataDTO>> loadCard(ArrayList<CardDataDTO> data) {
        // Se valida que la lista de tarjetas no venga vacia
        if (data == null || data.isEmpty()) {
            return Result.fail("No se recibieron tarjetas para procesar");
        }

        // Lista donde se guardan las tarjetas ya procesadas
        ArrayList<CardDataDTO> lista = new ArrayList<>();

        // Se recorre cada tarjeta para asignarle su valor real
        for (CardDataDTO cardData : data) {
            // Si la tarjeta viene nula o sin tipo, se ignora
            if (cardData == null || cardData.getTipo() == null) {
                continue;
            }

            // Segun el tipo de tarjeta, se consulta el total correspondiente
            switch (cardData.getTipo()) {
                case USUARIOS ->
                    cardData.setValor(UsuarioDAO.contarUsuarios());

                case SOLICITUDES ->
                    cardData.setValor(PeticionDAO.contarPeticiones());

                case TURNOS_ACTIVOS ->
                    cardData.setValor(TurnoDAO.contarTurnosActivos());

                case PENDIENTES ->
                    cardData.setValor(PeticionDAO.contarPeticionesPendientes());

                case CLIENTES ->
                    cardData.setValor(ClienteDAO.contarClientes());

                case TURNOS_TOTALES ->
                    cardData.setValor(TurnoDAO.contarTurnos());

                case TURNOS_DESIGNADOS ->
                    cardData.setValor(PeticionDAO.contarTurnosReservados());

                case EMPLEADOS ->
                    cardData.setValor(EmpleadoDAO.contarEmpleados());

                default ->
                    throw new AssertionError();
            }

            // Se agrega la tarjeta con el valor ya cargado a la lista final
            lista.add(cardData);
        }

        // Si no se cargo ninguna tarjeta, se devuelve error
        if (lista.isEmpty()) {
            return Result.fail("No se pudo cargar ninguna tarjeta");
        }

        // Se devuelve la lista de tarjetas cargadas correctamente
        return Result.ok("Datos consultados correctamente", lista);
    }

    /**
     * Metodo que carga los tipos de solicitud recientes para el tablero.
     * Obtiene las peticiones recientes y guarda solo el tipo de cada una.
     *
     * @param lista lista donde se agregaran los tipos de solicitud
     * @return lista con los tipos de solicitud cargados
     */
    public static ArrayList<TipoSolicitud> loadBoard(ArrayList<TipoSolicitud> lista) {
        try {
            // Se obtienen las peticiones mas recientes desde la base de datos
            ArrayList<Peticion> solicitudes = (ArrayList<Peticion>) PeticionDAO.obtenerRecientes();

            // Se recorre cada peticion y se agrega su tipo a la lista
            for (Peticion solicitude : solicitudes) {
                lista.add(solicitude.getTipo());
            }

            // Se devuelve la lista cargada
            return lista;

        } catch (Exception e) {
            // Si ocurre un error, se muestra en consola y se devuelve una lista vacia
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
