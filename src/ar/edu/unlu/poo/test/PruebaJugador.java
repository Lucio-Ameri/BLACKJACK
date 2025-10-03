package ar.edu.unlu.poo.test;

import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IManoDealer;
import ar.edu.unlu.poo.interfaz.IManoJugador;
import ar.edu.unlu.poo.modelo.*;
import ar.edu.unlu.poo.modelo.enumerados.PaloCarta;
import ar.edu.unlu.poo.modelo.enumerados.ValorCarta;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;

import java.util.ArrayList;
import java.util.List;

public class PruebaJugador {
    public static void main(String[] args) {
        ar.edu.unlu.poo.modelo.Dealer dealer = new Dealer();
        IJugador j1 = new Jugador("LUCIO", 1000.0);
        IJugador j2 = new Jugador("FRANCO", 1000.0);

        List<IJugador> jugadores = new ArrayList<IJugador>();
        jugadores.add(j1);
        jugadores.add(j2);





        System.out.println("JUGADORES CREADOS INICIALMENTE:");
        for(IJugador j: jugadores){
            System.out.println(j.descripcion());
        }
        System.out.println();
        for(IJugador j: jugadores){
            if(j instanceof Jugador){
                Jugador instanciaJugador = (Jugador) j;
                instanciaJugador.agregarMano(new ManoJugador(300));
                dealer.retirarDineroJugador(instanciaJugador, 300.0);
            }
        }





        System.out.println("\nJUGADORES APUESTAN UNA MANO:");
        for(IJugador j: jugadores){
            System.out.println(j.descripcion());
        }




        System.out.println("\nJUGADORES RECIBEN SUS CARTAS:");
        for(IJugador j: jugadores){
            List<IManoJugador> manos = j.getManosJugadorInterfaz();

            for(IManoJugador m: manos){
                if(m instanceof ManoJugador){
                    ManoJugador instanciaMano = (ManoJugador) m;
                    do{
                        instanciaMano.recibirCarta(dealer.repartirCarta());
                    } while (instanciaMano.getEstado() == EstadoDeLaMano.TURNO_INICIAL || instanciaMano.getEstado() == EstadoDeLaMano.EN_JUEGO);
                }
            }

            System.out.println(j.descripcion());
        }




        System.out.println("\nDEALER SE REPARTE SUS CARTAS:");
        IManoDealer manoDInterfaz = dealer.getManoDealerInterfaz();
        while (manoDInterfaz.getEstado() == EstadoDeLaMano.TURNO_INICIAL || manoDInterfaz.getEstado() == EstadoDeLaMano.EN_JUEGO){
            if(manoDInterfaz instanceof ManoDealer){
                ManoDealer m = (ManoDealer) manoDInterfaz;

                Carta c = dealer.repartirCarta();
                c.revelarCarta();
                m.recibirCarta(c);
            }
        }

        System.out.println(dealer.descripcion());




        System.out.println("\n DEALER CALCULA LOS RESULTADOS DE LA RONDA:");
        List<Jugador> jugadoresReales = new ArrayList<Jugador>();
        for(IJugador j: jugadores){
            if(j instanceof Jugador){
                jugadoresReales.add((Jugador) j);
            }
        }

        dealer.definirResultados(jugadoresReales);

        for(IJugador j: jugadores){
            System.out.println(j.descripcion());
        }
        System.out.println(dealer.descripcion());





        System.out.println("\n CORROBORO QUE FUNCIONA BIEN EL DOBLAR JUGADOR");
        Jugador j3 = new Jugador("MAXI", 1000.0);
        dealer.retirarDineroJugador(j3, 300.0);

        ManoJugador m3 = new ManoJugador(300.0);
        j3.agregarMano(m3);

        for(int i = 0; i < 2; i++){

            m3.recibirCarta(dealer.repartirCarta());

            if(m3.getTotalMano() == 21){
                j3.limpiarManos();
                m3 = new ManoJugador(300.0);
                j3.agregarMano(m3);
                i = 0;
            }
        }

        m3.doblarMano(dealer.repartirCarta());
        System.out.println(j3.descripcion());





        System.out.println("\n CORROBORO QUE FUNCIONA BIEN SEPARAR MANO:");
        Jugador j4 = new Jugador("MARIANA", 1000.0);
        dealer.retirarDineroJugador(j4, 300.0);

        ManoJugador m4 = new ManoJugador(300.0);
        j4.agregarMano(m4);
        m4.recibirCarta(new Carta(PaloCarta.CORAZONES, ValorCarta.CINCO));
        m4.recibirCarta(new Carta(PaloCarta.PICAS, ValorCarta.CINCO));

        j4.agregarManoEnPosicion(j4.getManos().size(), m4.separarMano());
        dealer.retirarDineroJugador(j4, 300.0);

        List<ManoJugador> manos = j4.getManos();
        for(ManoJugador m: manos){
            m.recibirCarta(dealer.repartirCarta());
        }

        System.out.println(j4.descripcion());





        System.out.println("\n CORROBORO QUE FUNCIONA BIEN ASEGURARSE:");
        dealer = new Dealer();
        dealer.getMano().recibirCarta(new Carta(PaloCarta.CORAZONES, ValorCarta.AS));
        dealer.getMano().recibirCarta(dealer.repartirCarta());

        Jugador j5 = new Jugador("J5", 1000.0);
        ManoJugador m5 = new ManoJugador(300.0);
        dealer.retirarDineroJugador(j5, 300.0);
        j5.agregarMano(m5);
        for(int q = 0; q < 2; q++){
            m5.recibirCarta(dealer.repartirCarta());
        }
        m5.asegurarme();
        j5.actualizarSaldo(m5.getEnvite().getSeguroApostado());
        System.out.println(j5.descripcion());




        System.out.println("CORROBORO QUE FUNCIONA BIEN EL RENDIRME:");
        Jugador j6 = new Jugador("J6", 1000.0);
        dealer.retirarDineroJugador(j6, 300.0);
        ManoJugador m6 = new ManoJugador(300.0);
        j6.agregarMano(m6);
        m6.recibirCarta(new Carta(PaloCarta.CORAZONES, ValorCarta.CINCO));
        m6.recibirCarta(new Carta(PaloCarta.CORAZONES, ValorCarta.CINCO));
        m6.rendirme();
        System.out.println(j6.descripcion());
    }
}
