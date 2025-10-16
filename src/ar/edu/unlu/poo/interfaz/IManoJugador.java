package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.List;

public interface IManoJugador {
    IApuesta getEnviteInterfaz();

    List<ICarta> getCartasInterfaz();

    String descripcion();

    EstadoDeLaMano getEstado();

    int getTotal();
}
