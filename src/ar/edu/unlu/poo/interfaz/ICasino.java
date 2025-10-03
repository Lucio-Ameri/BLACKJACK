package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.List;

public interface ICasino {
    List<IJugador> getJugadoresConectados(Jugador jug);

    IMesa getMesa(Jugador j);

    int getLongitudListaDeEspera();

    int miPosicionEnListaDeEspera(Jugador j);

    Eventos unirmeAlCasino(Jugador j);

    Eventos irmeDelCasino(Jugador j);

    Eventos unirmeALaListaDeEspera(Jugador j, double monto);

    Eventos salirListaDeEspera(Jugador j);

    Eventos unirmeALaMesa(Jugador j, double monto);

    //funcion que solo sirve para el testing, si no se testea comentarla.
    //Mesa getMesa();
}
