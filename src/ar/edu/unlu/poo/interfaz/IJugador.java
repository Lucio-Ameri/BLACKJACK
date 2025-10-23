package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.List;

public interface IJugador {
    String getNombre();

    List<IManoJugador> getManosJugadorInterfaz();

    double getSaldoJugador();

    double getMaximoHistorico();

    boolean perdio();

    String descripcion();

    String datosPrincipales();
}
