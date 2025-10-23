package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.List;

public interface ICasino {
    List<IJugador> getJugadoresConectados(IJugador jug);

    IMesa getMesa(IJugador j);

    int getLongitudListaDeEspera();

    int miPosicionEnListaDeEspera(IJugador j);

    IJugador unirmeAlCasino(String nombre, Observador o);

    Eventos irmeDelCasino(IJugador j, Observador o);

    Eventos unirmeALaListaDeEspera(IJugador j, double monto, Observador o);

    Eventos salirListaDeEspera(IJugador j);

    Eventos unirmeALaMesa(IJugador j, double monto, Observador o);

    Eventos hayJugadoresGuardados();

    List<IJugador> jugadoresGuardados();

    List<String> getRankingMundial(IJugador jug);

    Jugador getIjugadorConectado(String nombre);
}
