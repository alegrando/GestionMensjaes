package com.gestionmensjaes.app.data;

import java.util.ArrayList;

/**
 * Created by alejandro on 25/02/14.
 */
public class MensajesList {

    private static ArrayList<Mensajes> mensajesList;

    public static ArrayList<Mensajes> getMensajesList() {
        return mensajesList;
    }

    public static void setMensajesList(ArrayList<Mensajes> mensajesList) {
        MensajesList.mensajesList = mensajesList;
    }
}
