package ar.edu.unlu.poo.modelo;

import java.io.Serializable;

public class Dinero implements Serializable {
    private double monto;

    public Dinero(double cantidad){
        this.monto = cantidad;
    }

    public boolean tengoDinero(){
        return monto >= 1.0;
    }

    public double getMonto(){
        return monto;
    }

    public boolean puedoTransferir(double cantidad){
        return monto >= cantidad;
    }

    public void actualizarMonto(double cantidad){
        monto += cantidad;
    }

    public String descripcion(){
        return String.format("$%.2f", monto);
    }
}
