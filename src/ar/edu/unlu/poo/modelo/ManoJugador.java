package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IApuesta;
import ar.edu.unlu.poo.interfaz.IManoJugador;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.io.Serializable;
import java.util.List;

public class ManoJugador extends Mano implements IManoJugador, Serializable {
    private Apuesta envite;

    public ManoJugador(double monto){
        super();
        this.envite = new Apuesta(monto);
    }

    public Apuesta getEnvite(){
        return envite;
    }

    @Override
    public IApuesta getEnviteInterfaz(){
        return envite;
    }

    public boolean condicionParaSepararMano(){
        List<Carta> cartas = getCartas();
        return cartas.get(0).cartasSimilares(cartas.get(cartas.size() - 1));
    }

    public void quedarme(){
        cambiarEstadoDeLaMano(EstadoDeLaMano.QUEDADA);
    }

    public void asegurarme(){
        envite.asegurarse();
    }

    public void rendirme(){
        cambiarEstadoDeLaMano(EstadoDeLaMano.RENDIDA);
    }

    public void doblarMano(Carta c){
        envite.doblarApuesta();
        recibirCarta(c);

        if(getEstado() == EstadoDeLaMano.EN_JUEGO){
            quedarme();
        }
    }

    public ManoJugador separarMano(){
        List<Carta> cartas = getCartas();

        ManoJugador nuevaMano = new ManoJugador(envite.getMontoApostado());
        nuevaMano.recibirCarta(cartas.remove(cartas.size() - 1));

        this.cambiarEstadoDeLaMano(EstadoDeLaMano.TURNO_INICIAL);
        this.calcularTotal();
        return nuevaMano;
    }

    @Override
    public void actualizarEstadoDeLaMano(int total){
        if(total > 21){
            cambiarEstadoDeLaMano(EstadoDeLaMano.PASADA);
        }

        else if(total == 21){
            if(turnoInicial()){
                cambiarEstadoDeLaMano(EstadoDeLaMano.BLACKJACK);
            }

            else {
                cambiarEstadoDeLaMano(EstadoDeLaMano.QUEDADA);
            }
        }

        else{
            cambiarEstadoDeLaMano(getEstado());
        }
    }

    @Override
    public void recibirCarta(Carta c){
        List<Carta> cartas = getCartas();

        c.revelar();
        cartas.add(c);

        if(cartas.size() == 3){
            cambiarEstadoDeLaMano(EstadoDeLaMano.EN_JUEGO);
        }

        calcularTotal();
    }

    @Override
    public String descripcion(){
        List<Carta> cartas = getCartas();
        String s = "";

        for(Carta c: cartas){
            s += String.format("%s ", c.descripcion());
        }

        return s + String.format("  TOTAL DE LA MANO: { %d }   ESTADO DE LA MANO: %s\n%s", getTotal(), getEstado(), getEnvite().descripcion());
    }
}
