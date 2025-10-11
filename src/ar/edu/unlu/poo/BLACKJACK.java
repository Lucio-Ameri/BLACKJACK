package ar.edu.unlu.poo;

import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.modelo.Casino;
import ar.edu.unlu.poo.vista.Consola.VistaConsola;

public class BLACKJACK {
    public static void main(String[] args) {
        Casino casino = new Casino();

        VistaConsola vista1 = new VistaConsola(new Controlador(casino));
        vista1.iniciar();
        VistaConsola vista2 = new VistaConsola(new Controlador(casino));
        vista2.iniciar();
    }
}
