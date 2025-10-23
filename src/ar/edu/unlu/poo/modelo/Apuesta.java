package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IApuesta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaApuesta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.io.Serializable;

public class Apuesta implements IApuesta, Serializable {
    private Dinero montoApuestaPrincipal;
    private Dinero montoSeguroApostado;
    private Dinero montoGanancias;
    private EstadoDeLaApuesta estado;

    public Apuesta(double cantidad){
        this.montoApuestaPrincipal = new Dinero(cantidad);
        this.montoSeguroApostado = null;
        this.montoGanancias = null;
        this.estado = EstadoDeLaApuesta.JUGANDO;
    }

    @Override
    public double getMontoApostado(){
        return montoApuestaPrincipal.getMonto();
    }

    public boolean estaAsegurado(){
        return montoSeguroApostado != null;
    }

    @Override
    public double getSeguroApostado(){
        return estaAsegurado() ? montoSeguroApostado.getMonto() : 0.0;
    }

    public void asegurarse(){
        montoSeguroApostado = new Dinero(montoApuestaPrincipal.getMonto() / 2.0);
    }

    private boolean gananciasCalculadas(){
        return montoGanancias != null;
    }

    @Override
    public double getGanancias(){
        return gananciasCalculadas() ? montoGanancias.getMonto() : 0.0;
    }

    public void doblarApuesta(){
        montoApuestaPrincipal.actualizarMonto(montoApuestaPrincipal.getMonto());
    }

    public void calcularGanancias(EstadoDeLaMano estadoDealer, EstadoDeLaMano estadoJugador, int totalDealer, int totalJugador){
        this.montoGanancias = new Dinero(0.0);

        if(estadoJugador == EstadoDeLaMano.RENDIDA){
            montoGanancias.actualizarMonto(getMontoApostado() / 2.0);
            estado = EstadoDeLaApuesta.PERDIO;
        }

        else{
            switch (estadoDealer){
                case BLACKJACK -> {
                    calcularGananciasDealerBlackJack(estadoJugador);

                    if(estaAsegurado()){
                        montoGanancias.actualizarMonto(getSeguroApostado() * 2.0);
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
            montoGanancias.actualizarMonto(getMontoApostado());
        }

        else{
            estado = EstadoDeLaApuesta.PERDIO;
        }
    }

    private void calcularGananciasDealerSeQuedo(EstadoDeLaMano estadoJugador, int totalDealer, int totalJugador){
        double monto = getMontoApostado();

        if(estadoJugador == EstadoDeLaMano.BLACKJACK){
            estado = EstadoDeLaApuesta.GANO;
            montoGanancias.actualizarMonto(monto + (monto * 1.5));
        }

        else if(estadoJugador == EstadoDeLaMano.QUEDADA){
            if(totalJugador == totalDealer){
                estado = EstadoDeLaApuesta.EMPATO;
                montoGanancias.actualizarMonto(monto);
            }

            else if(totalJugador > totalDealer){
                estado = EstadoDeLaApuesta.GANO;
                montoGanancias.actualizarMonto(monto * 2.0);
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
                montoGanancias.actualizarMonto(monto + (monto * 1.5));
            }

            else{
                montoGanancias.actualizarMonto(monto * 2.0);
            }
        }
    }

    @Override
    public String descripcion(){
        if(estado == EstadoDeLaApuesta.JUGANDO){
            return String.format("MONTO APOSTADO: %s   SEGURO APOSTADO: %s\nSITUACION: %s!", montoApuestaPrincipal.descripcion(), estaAsegurado() ? montoSeguroApostado.descripcion() : 0.0, estado);
        }

        else{
            return String.format("MONTO APOSTADO: %s   SEGURO APOSTADO: %s\nSITUACION: %s!   DEALER PAGO: %s", montoApuestaPrincipal.descripcion(), estaAsegurado() ? montoSeguroApostado.descripcion() : 0.0, estado, montoGanancias.descripcion());
        }
    }
}