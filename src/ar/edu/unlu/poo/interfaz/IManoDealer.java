package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.List;

public interface IManoDealer {
    String descripcion();

    List<ICarta> getCartasInterfaz();

    EstadoDeLaMano getEstado();

    int getTotal();
}
