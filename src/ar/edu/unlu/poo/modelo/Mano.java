package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.ICarta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.ArrayList;
import java.util.List;

public abstract class Mano {
    private List<Carta> cartas;
    private EstadoDeLaMano estado;
    private int totalMano;

    public Mano(){
        this.cartas = new ArrayList<Carta>();
        this.estado = EstadoDeLaMano.TURNO_INICIAL;
        this.totalMano = 0;
    }

    public List<Carta> getCartas(){
        return cartas;
    }

    public List<ICarta> getCartasInterfaz(){
        List<ICarta> cartasI = new ArrayList<ICarta>();

        for(Carta c: cartas){
            cartasI.add(c);
        }

        return cartasI;
    }

    public EstadoDeLaMano getEstado(){
        return estado;
    }

    public int getTotalMano(){
        return totalMano;
    }

    protected void cambiarEstadoDeLaMano(EstadoDeLaMano en){
        estado = en;
    }

    public boolean turnoInicial(){
        return estado == EstadoDeLaMano.TURNO_INICIAL;
    }

    protected void calcularTotal(){
        int total = 0;
        int cantAs = 0;

        for(Carta c: cartas){
            if(c.esAs()){
                cantAs ++;
            }

            total += c.getValorNumericoCarta();
        }

        while (total > 21 && cantAs > 0){
            cantAs --;
            total -= 10;
        }

        totalMano = total;
        actualizarEstadoDeLaMano(totalMano);
    }

    protected abstract void actualizarEstadoDeLaMano(int totalMano);
    public abstract void recibirCarta(Carta c);
}
