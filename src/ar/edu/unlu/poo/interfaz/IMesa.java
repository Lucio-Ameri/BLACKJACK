package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.ManoJugador;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.List;

public interface IMesa {
    void confirmarParticipacion(Jugador j);

    void confirmarNuevaParticipacion(Jugador j, double monto, boolean participacion, Observador o);

    Eventos apostarOtraMano(Jugador j, double monto);

    Eventos retirarUnaMano(Jugador j, ManoJugador mano);

    Eventos retirarmeDeLaMesa(Jugador j, Observador o);

    Eventos jugadorJuegaSuTurno(Accion a, Jugador j, ManoJugador m);

    IDealer getDealer();

    EstadoDeLaMesa getEstado();

    String getJugadorTurnoActual();

    List<IJugador> getInscriptos();

    boolean esMiTurno(Jugador j);

    boolean hayLugaresDisponibles();

    boolean confirme(Jugador j);
}
