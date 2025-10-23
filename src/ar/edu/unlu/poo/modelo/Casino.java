package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.*;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.eventos.Notificacion;
import ar.edu.unlu.poo.modelo.persistencia.Serializador;
import ar.edu.unlu.poo.modelo.persistencia.TablaPuntuacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Casino implements ICasino, Observador, Observado {
    private List<Jugador> conectados;
    private List<Jugador> listaDeEspera;
    private List<Observador> observadores;
    private HashMap<Jugador, Observador> observadoresListaDeEspera;
    private HashMap<Jugador, Double> solicitudDeIngreso;
    private Mesa mesa;

    public Casino(){
        this.conectados = new ArrayList<Jugador>();
        this.listaDeEspera = new ArrayList<Jugador>();
        this.solicitudDeIngreso = new HashMap<Jugador, Double>();
        this.mesa = new Mesa();
        this.observadores = new ArrayList<Observador>();
        this.observadoresListaDeEspera = new HashMap<Jugador, Observador>();

        mesa.agregarObservador(this);
    }

    @Override
    public List<IJugador> getJugadoresConectados(IJugador jug){
        Jugador j = getJugador(jug.getNombre());

        if(j != null) {
            if (!conectados.isEmpty() || estoyConectado(j)) {
                return new ArrayList<IJugador>(conectados);
            }
        }

        return new ArrayList<IJugador>();
    }

    public Jugador getIjugadorConectado(String nombre){
        return getJugador(nombre);
    }

    @Override
    public IMesa getMesa(IJugador j) {
        Jugador jug = getJugador(j.getNombre());
        if(jug != null) {

            if (jugardoEnLaMesa(jug)) {
                return mesa;
            }

            return null;
        }
        return null;
    }

    private boolean jugardoEnLaMesa(Jugador j){
        return mesa.jugadorEnLaMesa(j);
    }

    @Override
    public int getLongitudListaDeEspera(){
        return listaDeEspera.size();
    }

    @Override
    public int miPosicionEnListaDeEspera(IJugador j){
        Jugador jug = getJugador(j.getNombre());

        if(jug != null) {

            if (estoyEnListaDeEspera(jug)) {
                return listaDeEspera.indexOf(jug) + 1;
            }
        }
        return -1;
    }

    private boolean estoyEnListaDeEspera(Jugador j){
        return listaDeEspera.contains(j);
    }

    private boolean hayJugadoresEsperando(){
        return !listaDeEspera.isEmpty();
    }

    private boolean estoyConectado(Jugador j){
        return conectados.contains(j);
    }

    private Jugador getJugador(String nombre){
        for(Jugador j: conectados){
            if(j.getNombre().equals(nombre)){
                return j;
            }
        }

        return null;
    }

    @Override
    public Eventos hayJugadoresGuardados(){
        if(Serializador.cargarJugadoresGuardados().isEmpty()){
            return Eventos.SIN_JUGADORES_GUARDADOS;
        }

        return null;
    }

    @Override
    public List<IJugador> jugadoresGuardados(){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();
        return new ArrayList<IJugador>(jugadores);
    }

    @Override
    public List<String> getRankingMundial(IJugador jug){
        Jugador j = getJugador(jug.getNombre());

        if(j != null) {
            TablaPuntuacion tabla = Serializador.cargarTablaDePuntuacion();
            return tabla.obtenerMejoresJugadores();
        }

        return null;
    }

    private Jugador conectarJugador(String nombre){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();
        Jugador nuevoIngreso;

        for(Jugador j: jugadores){
            if(j.getNombre().equals(nombre)){

                if(!conectados.contains(j)) {
                    nuevoIngreso = jugadores.get(jugadores.indexOf(j));
                    jugadores.remove(j);

                    return nuevoIngreso;
                }

                return null;
            }
        }

        List<String> usados = Serializador.cargarListaNombresUsados();
        int n;

        do {
            n = ThreadLocalRandom.current().nextInt(1, 1000);
            nombre = "%s#%03d".formatted(nombre, n);
        }
        while (usados.contains(nombre));

        usados.add(nombre);
        Serializador.guardarListaNombresUsados(usados);

        nuevoIngreso = new Jugador(nombre, 1000.0);

        if(!conectados.contains(nuevoIngreso)) {
            return nuevoIngreso;
        }

        return null;
    }

    private void guardarJugador(Jugador j){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();
        String nombre = j.getNombre();

        if(!j.perdio()){
            for (Jugador jug : jugadores) {
                if (jug.getNombre().equals(nombre)) {
                    jugadores.remove(jug);
                    break;
                }
            }

            jugadores.add(j);
            Serializador.guardarJugadores(jugadores);
            return;
        }

        List<String> usados = Serializador.cargarListaNombresUsados();
        usados.remove(nombre);
        Serializador.guardarListaNombresUsados(usados);
    }

    @Override
    public IJugador unirmeAlCasino(String nombre, Observador o){

        Jugador j = conectarJugador(nombre);

        if(j != null){
            Serializador.eliminarJugadorGuardado(j);
            conectados.add(j);

            notificarObservadores(Notificacion.NUEVO_JUGADOR);
            agregarObservador(o);

            return j;
        }

        return null;
    }

    @Override
    public Eventos irmeDelCasino(IJugador j, Observador o){
        Jugador jugador = getJugador(j.getNombre());

        if(jugador != null){

            if (!jugardoEnLaMesa(jugador)) {

                if (estoyEnListaDeEspera(jugador)) {
                    jugador.actualizarSaldo(solicitudDeIngreso.get(jugador));

                    listaDeEspera.remove(jugador);
                    solicitudDeIngreso.remove(jugador);
                    observadoresListaDeEspera.remove(jugador);

                    guardarJugador(jugador);

                    notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
                }

                conectados.remove(jugador);
                eliminarObservador(o);

                notificarObservadores(Notificacion.JUGADOR_SE_FUE);

                return Eventos.ACCION_REALIZADA;
            }

            return Eventos.JUGADOR_EN_LA_MESA;
        }

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos unirmeALaListaDeEspera(IJugador j, double monto, Observador o){
        Jugador jugador = getJugador(j.getNombre());

        if(jugador != null) {

            if (!jugardoEnLaMesa(jugador)) {
                if (mesa.getEstado() != EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    if (!estoyEnListaDeEspera(jugador)) {
                        if(jugador.transferenciaRealizable(monto)) {
                            jugador.actualizarSaldo(- monto);
                            listaDeEspera.add(jugador);
                            solicitudDeIngreso.put(jugador, monto);

                            observadoresListaDeEspera.put(jugador, o);
                            notificarObservadores(Notificacion.ACTUALIZAR_CONECTADOS);

                            return Eventos.ACCION_REALIZADA;
                        }

                        return Eventos.SALDO_INSUFICIENTE;
                    }

                    return Eventos.JUGADOR_YA_EN_LISTA;
                }

                return Eventos.MESA_ACEPTANDO_INSCRIPCIONES;
            }

            return Eventos.JUGADOR_EN_LA_MESA;
        }

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos salirListaDeEspera(IJugador j){
        Jugador jugador = getJugador(j.getNombre());

        if(jugador != null) {

            if (estoyEnListaDeEspera(jugador)) {
                jugador.actualizarSaldo(solicitudDeIngreso.get(jugador));
                listaDeEspera.remove(jugador);
                solicitudDeIngreso.remove(jugador);
                observadoresListaDeEspera.remove(jugador);

                notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
                return null;
            }

            return Eventos.JUGADOR_NO_ESTA;
        }

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos unirmeALaMesa(IJugador j, double monto, Observador o){
        Jugador jug = getJugador(j.getNombre());

        if(jug != null){
            if(!hayJugadoresEsperando()){
                if(jug.transferenciaRealizable(monto)) {
                    Eventos ev = mesa.inscribirJugadorNuevo(jug, monto, o);
                    if(ev == Eventos.ACCION_REALIZADA){
                        notificarObservadores(Notificacion.ACTUALIZAR_CONECTADOS);
                    }

                    return ev;
                }

                return Eventos.SALDO_INSUFICIENTE;
            }

            return Eventos.GENTE_ESPERANDO;
        }

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    private boolean agregarJugadorEsperando(Jugador j, double monto){
        Observador o = observadoresListaDeEspera.get(j);
        Eventos situacion = mesa.inscribirJugadorNuevo(j, monto, o);

        if(situacion == Eventos.ACCION_REALIZADA){
            j.actualizarSaldo(solicitudDeIngreso.get(j));

            listaDeEspera.remove(j);
            solicitudDeIngreso.remove(j);
            observadoresListaDeEspera.remove(j);

            notificarObservadores(Notificacion.ACTUALIZAR_CONECTADOS);
            return true;
        }

        return false;
    }

    public void agregarObservador(Observador o){
        observadores.add(o);
    }

    public void eliminarObservador(Observador o){
        observadores.remove(o);
    }

    public void notificarObservadores(Notificacion n){
        for(Observador o: observadores){
            o.actualizar(n);
        }
    }

    public void actualizar(Notificacion n){
        if((n != Notificacion.CAMBIO_ESTADO_MESA) && (mesa.getEstado() != EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES)){
            return;
        }

        if(mesa.getEstado() == EstadoDeLaMesa.FINALIZANDO_RONDA){
            notificarObservadores(Notificacion.ACTUALIZAR_CONECTADOS);
        }

        boolean resultado;
        int i = 0;

        do {
            resultado = false;

            if(!listaDeEspera.isEmpty()){
                Jugador j = listaDeEspera.get(i);
                double m = solicitudDeIngreso.get(j);

                resultado = agregarJugadorEsperando(j, m);
                i++;
            }
        }
        while (resultado);

        for(int j = 0; j < i; j++){
            listaDeEspera.remove(j);
        }

        notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
    }
}
