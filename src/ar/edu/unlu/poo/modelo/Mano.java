package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.ICarta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.ArrayList;
import java.util.List;

public abstract class Mano {
    private List<Carta> cartas;
    private EstadoDeLaMano estado;
    private int total;

    public Mano(){
        this.cartas = new ArrayList<Carta>();
        this.estado = EstadoDeLaMano.TURNO_INICIAL;
        this.total = 0;
    }

    public List<Carta> getCartas(){
        return cartas;
    }

    public List<ICarta> getCartasInterfaz(){
        return new ArrayList<ICarta>(cartas);
    }

    public EstadoDeLaMano getEstado(){
        return estado;
    }

    public int getTotal(){
        return total;
    }

    public boolean turnoInicial(){
        return estado == EstadoDeLaMano.TURNO_INICIAL;
    }

    protected void cambiarEstadoDeLaMano(EstadoDeLaMano nuevo){
        estado = nuevo;
    }

    protected void calcularTotal(){
        this.total = calcularTotalInterno();
        actualizarEstadoDeLaMano(total);
    }

    private int calcularTotalInterno(){
        int total = 0, totalAs = 0;

        for(Carta c: cartas){
            if(c.esAs()){
                totalAs++;
            }

            total += c.getValorNumericoCarta();
        }

        while (total > 21 && totalAs > 0){
            totalAs--;
            total -= 10;
        }

        return total;
    }

    protected abstract void actualizarEstadoDeLaMano(int total);
    public abstract void recibirCarta(Carta c);
}
