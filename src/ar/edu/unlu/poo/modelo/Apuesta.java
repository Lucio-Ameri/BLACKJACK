package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IApuesta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaApuesta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.io.Serializable;

public class Apuesta implements IApuesta, Serializable {
    private Dinero montoApostado;
    private Dinero seguroApostado;
    private Dinero ganancia;
    private EstadoDeLaApuesta estado;

    public Apuesta(double monto){
        this.montoApostado = new Dinero(monto);
        this.seguroApostado = null;
        this.ganancia = null;
        this.estado = EstadoDeLaApuesta.JUGANDO;
    }

    @Override
    public double getMontoApostado(){
        return montoApostado.getMonto();
    }

    @Override
    public boolean estaAsegurado(){
        return seguroApostado != null;
    }

    @Override
    public double getSeguroApostado(){
        return estaAsegurado() ? seguroApostado.getMonto() : 0.0;
    }

    @Override
    public void asegurarse(){
        seguroApostado = new Dinero(getSeguroApostado() / 2.0);
    }

    @Override
    public boolean gananciasCalculadas(){
        return ganancia != null;
    }

    @Override
    public double getGanancias(){
        return gananciasCalculadas() ? ganancia.getMonto() : -1.0;
    }

    public void doblarApuesta(){
        montoApostado.actualizarMonto(getMontoApostado());
    }

    public void calcularGanancias(EstadoDeLaMano estadoDealer, EstadoDeLaMano estadoJugador, int totalDealer, int totalJugador){
        this.ganancia = new Dinero(0.0);

        if(estadoJugador == EstadoDeLaMano.RENDIDA){
            ganancia.actualizarMonto(getMontoApostado() / 2.0);
            estado = EstadoDeLaApuesta.PERDIO;
        }

        else{
            switch (estadoDealer){
                case BLACKJACK -> {
                    calcularGananciasDealerBlackJack(estadoJugador);

                    if(estaAsegurado()){
                        ganancia.actualizarMonto(getSeguroApostado() * 2.0);
                    }
                    break;
                }

                case QUEDADA -> {
                    calcularGananciasDealerSeQuedo(estadoJugador, totalDealer, totalJugador);
                    break;
                }

                case PASADA -> {
                    calcularGananciasDealerSePaso(estadoJugador);
                    break;
                }
            }
        }
    }

    private void calcularGananciasDealerBlackJack(EstadoDeLaMano estadoJugador){
        if(estadoJugador == EstadoDeLaMano.BLACKJACK){
            estado = EstadoDeLaApuesta.EMPATO;
            ganancia.actualizarMonto(getMontoApostado());
        }

        else{
            estado = EstadoDeLaApuesta.PERDIO;
        }
    }

    private void calcularGananciasDealerSeQuedo(EstadoDeLaMano estadoJugador, int totalDealer, int totalJugador){
        double monto = getMontoApostado();

        if(estadoJugador == EstadoDeLaMano.BLACKJACK){
            estado = EstadoDeLaApuesta.GANO;
            ganancia.actualizarMonto(monto + (monto * 1.5));
        }

        else if(estadoJugador == EstadoDeLaMano.QUEDADA){
            if(totalJugador == totalDealer){
                estado = EstadoDeLaApuesta.EMPATO;
                ganancia.actualizarMonto(monto);
            }

            else if(totalJugador > totalDealer){
                estado = EstadoDeLaApuesta.GANO;
                ganancia.actualizarMonto(monto * 2.0);
            }

            else{
                estado = EstadoDeLaApuesta.PERDIO;
            }
        }

        else{
            estado = EstadoDeLaApuesta.PERDIO;
        }
    }

    private void calcularGananciasDealerSePaso(EstadoDeLaMano estadoJugador){
        if(estadoJugador == EstadoDeLaMano.PASADA){
            estado = EstadoDeLaApuesta.PERDIO;
        }

        else{
            estado = EstadoDeLaApuesta.GANO;
            double monto = getMontoApostado();

            if(estadoJugador == EstadoDeLaMano.BLACKJACK){
                ganancia.actualizarMonto(monto + (monto * 1.5));
            }

            else{
                ganancia.actualizarMonto(monto * 2.0);
            }
        }
    }

    @Override
    public String descripcion(){
        String situacion = "";

        switch (estado){
            case GANO -> situacion = "GANO!";
            case EMPATO -> situacion = "EMPATO!";
            case PERDIO -> situacion = "PERDIO!";
        }

        if(estado == EstadoDeLaApuesta.JUGANDO){
            return String.format("--- MONTO APOSTADO: $%.2f --- SEGURO APOSTADO: $%.2f --- SITUACION: JUGANDO! ", getMontoApostado(), getSeguroApostado());
        }

        return String.format("--- MONTO APOSTADO: $%.2f --- SEGURO APOSTADO: $%.2f --- SITUACION: %s --- DEALER PAGA: $%.2f ", getMontoApostado(), getSeguroApostado(), situacion, getGanancias());
    }
}
