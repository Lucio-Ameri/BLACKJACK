package ar.edu.unlu.poo.test;

import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.Mesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;

public class PruebaMesa {
    public static void main(String[] args) {

        //creo los jugadores.
        IJugador j1 = new Jugador("Lucio", 1000.0);
        IJugador j2 = new Jugador("Franco", 1000.0);

        //creo la mesa e imprimo su estado actual.
        Mesa ronda = new Mesa();
        System.out.println("ESTADO DE LA MESA ANTES DE INSCRIBIR JUGADORES: " + ronda.getEstado());
        System.out.println();

        //inscribo a los jugadores, imprimiendo su contenido y verificando si el estado de la mesa cambio.
        System.out.println("VALOR OBTENIDO AL INSCRIBIR A LUCIO: " + ronda.inscribirJugadorNuevo((Jugador) j1, 300.0));
        System.out.println("VALOR OBTENIDO AL INSCRIBIR A FRANCO: " + ronda.inscribirJugadorNuevo((Jugador)j2, 200.0));
        System.out.println();

        System.out.println(j1.descripcion());
        System.out.println(j2.descripcion());

        System.out.println("ESTADO DE LA MESA LUEGO DE INSCRIBIR JUGADORES: " + ronda.getEstado());
        System.out.println();

        //apuesto y elimino otras manos de los jugadores para corroborar que las funciones funcionen correctamente.
        Jugador aux = (Jugador) j1;

        ronda.apostarOtraMano(aux, 100.0);
        ronda.retirarUnaMano(aux, aux.getManos().get(1));
        ronda.confirmarParticipacion(aux);

        ronda.apostarOtraMano((Jugador) j2, 300.0);
        ronda.confirmarParticipacion((Jugador) j2);

        //verifico que luego de confirmar ambos jugadores, sus valores estan bien y el estado de la mesa cambia.
        System.out.println("ESTADO DE LA MESA LUEGO DE CONFIRMAR JUGADORES: " + ronda.getEstado());
        System.out.println();

        System.out.println(j1.descripcion());
        System.out.println(j2.descripcion());
        System.out.println();

        //reparto las cartas y verifico si estan fueron bien repartidas.
        ronda.confirmarParticipacion((Jugador) j1);
        ronda.confirmarParticipacion((Jugador) j2);

        Jugador aux1 = (Jugador) j1;
        Jugador aux2 = (Jugador) j2;

        System.out.println(aux1.descripcion());
        System.out.println(aux2.descripcion());
        System.out.println(ronda.getDealer().descripcion());

        System.out.println("ESTADO DE LA MESA LUEGO DE REPARTIR LAS CARTAS INICIALES: " + ronda.getEstado());
        System.out.println(ronda.getJugadorTurnoActual());
        System.out.println();

        //jugador 2 intenta jugar antes que jugador 1.
        System.out.println("RESULTADO DE QUERER JUGAR CUANDO NO ES SU TURNO: " + ronda.jugadorJuegaSuTurno(Accion.DOBLAR_MANO, aux2, aux2.getManos().get(0)));
        System.out.println();

        //jugador 1 juega su turno.
        System.out.println("RESULTADO CUANDO EL JUGADOR CORRECTO JUEGA SU TURNO: " + ronda.jugadorJuegaSuTurno(Accion.PEDIR_CARTA, aux1, aux1.getManos().get(0)));
        System.out.println(aux1.descripcion());

        //jugador 1 pasa turno.
        ronda.jugadorJuegaSuTurno(Accion.QUEDARME, aux1, aux1.getManos().get(0));
        ronda.jugadorJuegaSuTurno(Accion.PASAR_TURNO, aux1, aux1.getManos().get(0));

        System.out.println(ronda.getJugadorTurnoActual());
        System.out.println();

        //jugador 2 juega su turno.
        System.out.println(ronda.jugadorJuegaSuTurno(Accion.DOBLAR_MANO, aux2, aux2.getManos().get(0)));
        System.out.println();

        System.out.println(ronda.jugadorJuegaSuTurno(Accion.DOBLAR_MANO, aux2, aux2.getManos().get(1)));
        ronda.jugadorJuegaSuTurno(Accion.PASAR_TURNO, aux2, aux2.getManos().get(1));

        System.out.println("ESTADO DE LA MESA LUEGO DE JUGAR TODOS LOS TURNOS: " + ronda.getEstado());
        System.out.println();

        System.out.println(aux1.descripcion());
        System.out.println(aux2.descripcion());

        //empieza el turno del dealer y reparte las ganancias.
        ronda.confirmarParticipacion(aux1);
        ronda.confirmarParticipacion(aux2);

        System.out.println(aux1.descripcion());
        System.out.println(aux2.descripcion());
        System.out.println(ronda.getDealer().descripcion());

        System.out.println("ESTADO DE LA MESA LUEGO DE JUGAR TURNO DEALER: " + ronda.getEstado());
        System.out.println();

        //jugadores confirman para poder eliminar las manos y poder reinscribirse.
        ronda.confirmarParticipacion(aux1);
        ronda.confirmarParticipacion(aux2);

        System.out.println("ESTADO DE LA MESA LUEGO DE CONFIRMAR PARA PODER REINSCRIBIRSE: " + ronda.getEstado());
        System.out.println();

        System.out.println(aux1.descripcion());
        System.out.println(aux2.descripcion());

        //jugadores se reinscriben o se dan de baja.
        ronda.confirmarNuevaParticipacion(aux1, 200.0, true);
        ronda.confirmarNuevaParticipacion(aux2, 0.0, false);

        System.out.println("ESTADO DE LA MESA LUEGO DE CONFIRMAR SI SIGUEN O NO: " + ronda.getEstado());
        System.out.println();

        //nuevo jugador se inscribe.
        Jugador j3 = new Jugador("Melina", 1000.0);
        ronda.inscribirJugadorNuevo(j3, 500);

        //ver si retirarme de la ronda funciona.
        System.out.println(ronda.retirarmeDeLaMesa(aux1));
    }
}
