package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IManoDealer;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.List;

public class ManoDealer extends Mano implements IManoDealer {

    public ManoDealer(){
        super();
    }

    public boolean primeraCartaEsAs(){
        return getCartas().get(0).esAs();
    }

    public void revelarManoCompleta(){
        List<Carta> cartas = getCartas();

        for(Carta c: cartas){
            if(c.cartaOculta()){
                c.revelar();
            }
        }

        calcularTotal();
    }

    @Override
    public void recibirCarta(Carta c){
        List<Carta> cartas = getCartas();

        if(cartas.size() != 1){
            c.revelar();
        }

        cartas.add(c);

        if(cartas.size() == 3){
            cambiarEstadoDeLaMano(EstadoDeLaMano.EN_JUEGO);
        }

        calcularTotal();
    }

    @Override
    protected void actualizarEstadoDeLaMano(int total){
        if(total > 21){
            cambiarEstadoDeLaMano(EstadoDeLaMano.PASADA);
        }

        else if(total > 16){
            if(turnoInicial() && total == 21){
                cambiarEstadoDeLaMano(EstadoDeLaMano.BLACKJACK);
            }

            else{
                cambiarEstadoDeLaMano(EstadoDeLaMano.QUEDADA);
            }
        }

        else{
            cambiarEstadoDeLaMano(getEstado());
        }
    }

    @Override
    public String descripcion(){
        List<Carta> cartas = getCartas();
        String s = "";

        for(Carta c: cartas){
            s += String.format("%s ", c.descripcion());
        }

        return s + String.format("  TOTAL MANO { %d }\nESTADO DE LA MANO: %s\n", getTotal(), getEstado());
    }
}
