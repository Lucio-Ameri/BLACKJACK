package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IDealer;
import ar.edu.unlu.poo.interfaz.IManoDealer;

import java.util.List;

public class Dealer implements IDealer {
    private ManoDealer mano;
    private Baraja mazo;

    public Dealer(){
        this.mano = new ManoDealer();
        this.mazo = new Baraja();
    }

    public ManoDealer getMano(){
        return mano;
    }

    @Override
    public IManoDealer getManoDealerInterfaz(){
        return mano;
    }

    public boolean condicionSeguro(){
        return mano.primeraCartaEsAs();
    }

    public void revelarMano(){
        mano.revelarManoCompleta();
    }

    public Carta repartirCarta(){
        return mazo.repartirCarta();
    }

    public void definirResultados(List<Jugador> jugadores){
        for(Jugador j: jugadores){
            List<ManoJugador> manos = j.getManos();

            for(ManoJugador m: manos){
                Apuesta envite = m.getEnvite();
                envite.calcularGanancias(mano.getEstado(), m.getEstado(), mano.getTotalMano(), m.getTotalMano());

                j.actualizarSaldo(envite.getGanancias());
                j.actualizarMaximoHistorico();
            }
        }
    }

    public void retirarManosJugadas(List<Jugador> jugadores){
        for(Jugador j: jugadores){
            j.limpiarManos();
        }
    }

    public void devolverDinero(Jugador j, ManoJugador m){
        j.actualizarSaldo(m.getEnvite().getMontoApostado());
        j.removerMano(m);
    }

    public void retirarDineroJugador(Jugador j, double monto){
        j.actualizarSaldo(- monto);
    }

    public void eliminarJugador(Jugador j){
        List<ManoJugador> manos = j.getManos();

        for(ManoJugador m: manos){
            j.actualizarSaldo(m.getEnvite().getMontoApostado());
        }

        j.limpiarManos();
    }

    public void limpiarMano(){
        mano = new ManoDealer();
    }

    @Override
    public String descripcion(){
        return String.format("\t\t\t DEALER: \nMANO DEALER: %s\n\n\n", mano.descripcion());
    }
}
