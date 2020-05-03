package com.eliascapasso.compraencasa.model;

import java.util.ArrayList;
import java.util.Collections;

public class Categoria {
    private static String[] categorias = {"Panadería",
            "Ferretería",
            "Farmacia",
            "Supermercado",
            "Electrodomesticos",
            "Roticería",
            "Pizzería",
            "Comida hecha",
            "Bebidas",
            "Carnicería",
            "Verdulería",
            "Dietetica",
            "Bazar",
            "Librería",
            "Limpieza",
            "Veterinaria",
            "Deportes",
            "Belleza",
            "Informática",
            "Regalería",
            "Juguetería",
            "Mueblería",
            "Vivero",
            "Bicicletería",
            "Heladería",
            "Decoración",
            "Artesanía",
            "Marroquinería",
            "Drugstore",
            "Maxikiosco",
            "Kiosco",
            "Almacén"};;

    public static ArrayList<String> getCategorias() {
        //Devuelve todas las categorias ordenadas
        ArrayList<String> listaCategorias = new ArrayList<String>();
        Collections.addAll(listaCategorias, categorias);
        Collections.sort(listaCategorias);
        return listaCategorias;
    }
}
