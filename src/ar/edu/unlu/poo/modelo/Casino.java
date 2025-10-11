package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.*;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.eventos.Notificacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        mesa.agregarObservador(this);

        this.observadores = new ArrayList<Observador>();
        this.observadoresListaDeEspera = new HashMap<Jugador, Observador>();
    }

    //funcion que sirve solo para el test, comentarla si no se testea. COMENTAR TAMBIEN EN ICASINO.
    /*
    public Mesa getMesa(){
        return mesa;
    }
    */

    @Override
    public List<IJugador> getJugadoresConectados(Jugador jug){
        List<IJugador> jugadores = new ArrayList<IJugador>();

        if(conectados.isEmpty() || !estoyConectado(jug)){
            return jugadores;
        }

        for(Jugador j: conectados){
            jugadores.add(j);
        }

        return jugadores;
    }

    @Override
    public IMesa getMesa(Jugador j){
        if(jugardoEnLaMesa(j)){
            return mesa;
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
    public int miPosicionEnListaDeEspera(Jugador j){
        if(estoyEnListaDeEspera(j)) {
            return listaDeEspera.indexOf(j) + 1;
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

    @Override
    public Eventos unirmeAlCasino(Jugador j, Observador o){
        if(!estoyConectado(j)){
            conectados.add(j);
            agregarObservador(o);

            notificarObservadores(Notificacion.NUEVO_JUGADOR);

            return Eventos.ACCION_REALIZADA;
        }

        return Eventos.JUGADOR_YA_INSCRIPTO;
    }

    @Override
    public Eventos irmeDelCasino(Jugador j, Observador o){
        if(estoyConectado(j)) {
            if (!jugardoEnLaMesa(j)) {
                if (estoyEnListaDeEspera(j)) {
                    j.actualizarSaldo(solicitudDeIngreso.get(j));

                    listaDeEspera.remove(j);
                    solicitudDeIngreso.remove(j);
                    observadoresListaDeEspera.remove(j);

                    notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
                }

                conectados.remove(j);
                eliminarObservador(o);

                notificarObservadores(Notificacion.JUGADOR_SE_FUE);

                return Eventos.ACCION_REALIZADA;
            }

            return Eventos.JUGADOR_EN_LA_MESA;
        }

        return Eventos.JUGADOR_NO_ESTA;
    }

    @Override
    public Eventos unirmeALaListaDeEspera(Jugador j, double monto, Observador o){
        if(estoyConectado(j)) {
            if (!jugardoEnLaMesa(j)) {
                if (mesa.getEstado() != EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    if (!estoyEnListaDeEspera(j)) {
                        if(j.transferenciaRealizable(monto)) {
                            solicitudDeIngreso.put(j, monto);
                            listaDeEspera.add(j);
                            observadoresListaDeEspera.put(j, o);

                            j.actualizarSaldo(- monto);

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

        return Eventos.JUGADOR_NO_ESTA;
    }

    @Override
    public Eventos salirListaDeEspera(Jugador j){
        if (estoyEnListaDeEspera(j)) {
            j.actualizarSaldo(solicitudDeIngreso.get(j));
            listaDeEspera.remove(j);
            solicitudDeIngreso.remove(j);
            observadoresListaDeEspera.remove(j);

            notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
            return Eventos.ACCION_REALIZADA;
        }

        return Eventos.JUGADOR_NO_ESTA;
    }


    @Override
    public Eventos unirmeALaMesa(Jugador j, double monto, Observador o){
        if(estoyConectado(j)){
            if(!jugardoEnLaMesa(j)){
                if(!hayJugadoresEsperando()){
                    if(j.transferenciaRealizable(monto)) {

                       return mesa.inscribirJugadorNuevo(j, monto, o);
                    }

                    return Eventos.SALDO_INSUFICIENTE;
                }

                return Eventos.GENTE_ESPERANDO;
            }

            return Eventos.JUGADOR_EN_LA_MESA;
        }

        return Eventos.JUGADOR_NO_ESTA;
    }

    private boolean agregarJugadorEsperando(Jugador j, double monto){
        Observador o = observadoresListaDeEspera.get(j);
        Eventos situacion = mesa.inscribirJugadorNuevo(j, monto, o);

        if(situacion == Eventos.ACCION_REALIZADA){
            j.actualizarSaldo(solicitudDeIngreso.get(j));

            listaDeEspera.remove(j);
            solicitudDeIngreso.remove(j);
            observadoresListaDeEspera.remove(j);
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

        if (mesa.getEstado() == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES && n != Notificacion.JUGADOR_INGRESO_MESA) {
            boolean resultado;

            do {

                resultado = false;

                if(!listaDeEspera.isEmpty()){
                Jugador j = listaDeEspera.get(0);
                double m = solicitudDeIngreso.get(j);

                resultado = agregarJugadorEsperando(j, m);
                }

            }
            while (resultado);

            notificarObservadores(Notificacion.ACTUALIZAR_LISTA_ESPERA);
        }
    }
}




/*

ME QUEDE EN LA PARTE DE QUE EL JUEGO NO CAMBIA LA VISTA CUANDO AGREGO JUGADORES A LA MESA A PARTIR DE LA SEGUNDA TANDA.
TENGO QUE ENCONTRAR LA FORMA DE REFACTORIZAR EL METODO "ACTUALIZAR" y "agregarJugadorEsperando" PARA PODER NOTIFICAR AL OBSERVADOR



 */
