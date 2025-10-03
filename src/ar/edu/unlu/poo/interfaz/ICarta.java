package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.enumerados.PaloCarta;
import ar.edu.unlu.poo.modelo.enumerados.ValorCarta;

public interface ICarta {
    PaloCarta getTipoDePalo();

    ValorCarta getTipoDeValor();

    int getValorNumericoCarta();

    String descripcion();
}
