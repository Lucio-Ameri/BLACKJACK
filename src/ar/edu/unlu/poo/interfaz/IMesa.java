package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.ManoJugador;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.List;

public interface IMesa {
    void confirmarParticipacion(IJugador j);

    void confirmarNuevaParticipacion(IJugador j, double monto, boolean participacion, Observador o);

    Eventos apostarOtraMano(IJugador j, double monto);

    Eventos retirarUnaMano(IJugador j, int posMano);

    Eventos retirarmeDeLaMesa(IJugador j, Observador o);

    Eventos jugadorJuegaSuTurno(Accion a, IJugador j);

    IDealer getDealer();

    EstadoDeLaMesa getEstado();

    String getJugadorTurnoActual();

    List<IJugador> getInscriptos();

    boolean esMiTurno(IJugador j);

    boolean hayLugaresDisponibles();

    boolean confirme(IJugador j);
}
