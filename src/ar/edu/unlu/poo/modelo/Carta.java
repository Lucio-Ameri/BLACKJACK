package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.ICarta;
import ar.edu.unlu.poo.modelo.enumerados.PaloCarta;
import ar.edu.unlu.poo.modelo.enumerados.ValorCarta;
import ar.edu.unlu.poo.modelo.estados.VisibilidadCarta;

import java.io.Serializable;

public class Carta implements ICarta, Serializable {
    private final PaloCarta palo;
    private final ValorCarta valor;
    private VisibilidadCarta estado;

    public Carta(PaloCarta p, ValorCarta v){
        this.palo = p;
        this.valor = v;
        this.estado = VisibilidadCarta.OCULTA;
    }

    public boolean cartaOculta(){
        return estado == VisibilidadCarta.OCULTA;
    }

    @Override
    public PaloCarta getTipoDePalo(){
        return cartaOculta() ? PaloCarta.OCULTO : palo;
    }

    @Override
    public ValorCarta getTipoDeValor(){
        return cartaOculta() ? ValorCarta.OCULTO : valor;
    }

    @Override
    public int getValorNumericoCarta(){
        return cartaOculta() ? 0 : valor.getValorNumerico();
    }

    public void revelarCarta(){
        if(cartaOculta()){
            estado = VisibilidadCarta.VISIBLE;
        }
    }

    public boolean esAs(){
        return !cartaOculta() && valor.tipoAS();
    }

    public boolean cartasSimilares(Carta c){
        if(cartaOculta() || c.cartaOculta()){
            return false;
        }

        return this.valor == c.getTipoDeValor();
    }

    @Override
    public String descripcion(){
        return cartaOculta() ? "[??] " : "[" + valor.getSimboloValor() + palo.getPaloIcono() + "] ";
    }
}
