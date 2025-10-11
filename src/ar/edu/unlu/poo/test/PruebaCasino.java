package ar.edu.unlu.poo.test;

import ar.edu.unlu.poo.interfaz.ICasino;
import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.modelo.Casino;
import ar.edu.unlu.poo.modelo.Jugador;

import java.util.ArrayList;
import java.util.List;

public class PruebaCasino {
    public static void main(String[] args) {

        //creo el casino y una lista de jugadores.
        ICasino casino = new Casino();
        List<IJugador> jugadores = new ArrayList<IJugador>();

        for(int i = 0; i < 3; i++){
            Jugador j = new Jugador("NUMERO " + (i+1), 1000.0);
            jugadores.add(j);
            casino.unirmeAlCasino(j, null);
        }

        System.out.println("LISTA DE JUGADORES:");
        for(IJugador j: jugadores){
            System.out.println("RESULTADO DE UNIR AL JUGADOR AL CASINO: " + casino.unirmeAlCasino((Jugador) j, null));
            System.out.println(j.descripcion());
        }
        System.out.println();

        //corroboro que no se pueda unir a la lista de espera.
        System.out.println("RESULTADO DE INTENTAR UNIR AL JUGADOR 1 A LA LISTA DE ESPERA: " + casino.unirmeALaListaDeEspera((Jugador) jugadores.get(0), 300.0, null));
        System.out.println("TAMAÑO DE LA LISTA DE ESPERA: " + casino.getLongitudListaDeEspera());
        System.out.println("POSICION DEL JUGADOR 1 DENTRO DE LA LISTA DE ESPERA: " + casino.miPosicionEnListaDeEspera((Jugador) jugadores.get(0)));
        System.out.println("\n");

        //elimino al jugador 1 del casino.
        System.out.println("RESULTADO DE ELIMINAR AL JUGADOR 1 DEL CASINO: " + casino.irmeDelCasino((Jugador) jugadores.get(0), null));
        List<IJugador> jugs = casino.getJugadoresConectados((Jugador) jugadores.get(1));
        for(IJugador j: jugs){
            System.out.println(j.descripcion());
        }
        System.out.println();

        //vuelvo a ingresar al jugador 1 dentro del casino.
        System.out.println("RESULTADO DE UNIR DE NUEVO AL JUGADOR 1 AL CASINO: " + casino.unirmeAlCasino((Jugador) jugadores.get(0), null));
        jugs = casino.getJugadoresConectados((Jugador) jugadores.get(0));
        for(IJugador j: jugs){
            System.out.println(j.descripcion());
        }
        System.out.println();

        //agrego al jugador 2 y 3 a la mesa y la inicio.
       /*
        System.out.println("RESULTADO DE UNIR AL JUGADOR 2 A LA MESA: " + casino.unirmeALaMesa((Jugador) jugadores.get(1), 300.0));
        System.out.println("RESULTADO DE UNIR AL JUGADOR 3 A LA MESA: " + casino.unirmeALaMesa((Jugador) jugadores.get(2), 300.0));
        Mesa mesa = casino.getMesa();
        mesa.confirmarParticipacion((Jugador) jugadores.get(1));
        mesa.confirmarParticipacion((Jugador) jugadores.get(2));
        System.out.println("ESTADO DE LA MESA: " + mesa.getEstado());
        System.out.println("\n");
        */

        //intento unir al jugador 1 a la mesa, caso de no poder, lo ingreso en la lista de espera.
        System.out.println("RESULTADO DE UNIR AL JUGADOR 1 A LA MESA: " + casino.unirmeALaMesa((Jugador) jugadores.get(0), 300.0, null));
        System.out.println("RESULTADO DE UNIR AL JUGADOR 1 A LA LISTA DE ESPERA: " + casino.unirmeALaListaDeEspera((Jugador) jugadores.get(0), 300.0, null));
        System.out.println("RESULTADO DE UNIR AL JUGADOR A LA LISTA DE ESPERA " + casino.unirmeALaListaDeEspera((Jugador) jugadores.get(1), 300.0, null));
        System.out.println("TAMAÑO DE LA LISTA DE ESPERA: " + casino.getLongitudListaDeEspera());
        System.out.println("POSICION DEL JUGADOR 1 DENTRO DE LA LISTA DE ESPERA: " + casino.miPosicionEnListaDeEspera((Jugador) jugadores.get(0)));
        System.out.println("\n");

        //saco al jugador de la lista de espera.
        System.out.println("RESULTADO DE ELIMINAR AL JUGADOR 1 DE LA LISTA DE ESPERA: " + casino.salirListaDeEspera((Jugador) jugadores.get(0)));
    }
}
