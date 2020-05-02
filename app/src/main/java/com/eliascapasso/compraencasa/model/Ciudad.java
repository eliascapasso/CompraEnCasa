package com.eliascapasso.compraencasa.model;

import java.util.ArrayList;
import java.util.Collections;

public class Ciudad {
    //Copiar el nombre de las ciudades desde google maps
    private static String[] ciudades = {"Villa Aranguren",
                                        "General Ramírez",
                                        "Hernández",
                                        "Crespo",
                                        "Libertador San Martín"};;

    public static ArrayList<String> getCiudades() {
        //Devuelve todas las ciudades ordenadas
        ArrayList<String> listaCiudades = new ArrayList<String>();
        Collections.addAll(listaCiudades, ciudades);
        Collections.sort(listaCiudades);
        return listaCiudades;
    }
}
