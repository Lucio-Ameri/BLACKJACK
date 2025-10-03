package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.modelo.enumerados.PaloCarta;
import ar.edu.unlu.poo.modelo.enumerados.ValorCarta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> mazo;

    public Baraja(){
        this.mazo = new ArrayList<Carta>();
        generarLaBaraja();
    }

    private boolean barajaVacia(){
        return mazo.isEmpty();
    }

    private void generarLaBaraja(){
        generarCartas();
        mezclarCartas();
    }

    private void generarCartas(){
        PaloCarta[] palos = PaloCarta.values();
        ValorCarta[] valores = ValorCarta.values();

        for(int i = 0; i < 8; i++){
            for(PaloCarta p: palos){
                for(ValorCarta v: valores){

                    if(p != PaloCarta.OCULTO && v != ValorCarta.OCULTO){
                        mazo.add(new Carta(p, v));
                    }
                }
            }
        }
    }

    private void mezclarCartas(){
        Collections.shuffle(mazo);
    }

    public Carta repartirCarta(){
        if(barajaVacia()){
            generarLaBaraja();
        }

        return mazo.remove(0);
    }
}
