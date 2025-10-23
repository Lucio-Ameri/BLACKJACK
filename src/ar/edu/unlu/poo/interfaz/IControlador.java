package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.vista.Consola.Menu;

import java.util.List;

public interface IControlador {
    void setVista(IVista v);

    IJugador getJugador();

    List<IManoJugador> getManosJugador();

    List<IJugador> getGuardados();

    List<IJugador> getInscriptosMesa();

    List<IJugador> getConectadosCasino();

    List<String> getRanking();

    int posicionDeEspera();

    double getSaldoJugador();

    IDealer getDealer();

    EstadoDeLaMesa getEstadoMesa();

    String getTurnoJugador();

    int manoEnTurno();

    void procesarLoggin(Menu estado, String ingreso);

    void procesarCasino(Menu estado, String ingreso);

    void procesarMesa(Menu estado, String ingreso);
}
