package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.ICasino;
import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IMesa;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Casino implements ICasino {
    private List<Jugador> conectados;
    private List<Jugador> listaDeEspera;
    private HashMap<Jugador, Double> solicitudDeIngreso;
    private Mesa mesa;

    public Casino(){
        this.conectados = new ArrayList<Jugador>();
        this.listaDeEspera = new ArrayList<Jugador>();
        this.solicitudDeIngreso = new HashMap<Jugador, Double>();
        this.mesa = new Mesa();
        //agregar a this como observador de mesa.
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
    public Eventos unirmeAlCasino(Jugador j){
        if(!estoyConectado(j)){
            conectados.add(j);
            //notificar cambio en la vista.

            return Eventos.ACCION_REALIZADA;
        }

        return Eventos.JUGADOR_YA_INSCRIPTO;
    }

    @Override
    public Eventos irmeDelCasino(Jugador j){
        if(estoyConectado(j)) {
            if (!jugardoEnLaMesa(j)) {
                if (estoyEnListaDeEspera(j)) {
                    j.actualizarSaldo(solicitudDeIngreso.get(j));
                    listaDeEspera.remove(j);
                    solicitudDeIngreso.remove(j);
                }

                conectados.remove(j);
                //notificar cambio en la vista.

                return Eventos.ACCION_REALIZADA;
            }

            return Eventos.JUGADOR_EN_LA_MESA;
        }

        return Eventos.JUGADOR_NO_ESTA;
    }

    @Override
    public Eventos unirmeALaListaDeEspera(Jugador j, double monto){
        if(estoyConectado(j)) {
            if (!jugardoEnLaMesa(j)) {
                if (mesa.getEstado() != EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    if (!estoyEnListaDeEspera(j)) {
                        if(j.transferenciaRealizable(monto)) {
                            solicitudDeIngreso.put(j, monto);
                            listaDeEspera.add(j);

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

            //actualizar vistas.
            return Eventos.ACCION_REALIZADA;
        }

        return Eventos.JUGADOR_NO_ESTA;
    }


    @Override
    public Eventos unirmeALaMesa(Jugador j, double monto){
        if(estoyConectado(j)){
            if(!jugardoEnLaMesa(j)){
                if(!hayJugadoresEsperando()){
                    if(j.transferenciaRealizable(monto)) {
                        return mesa.inscribirJugadorNuevo(j, monto);
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
        Eventos situacion = mesa.inscribirJugadorNuevo(j, monto);

        if(situacion == Eventos.ACCION_REALIZADA){
            j.actualizarSaldo(solicitudDeIngreso.get(j));
            listaDeEspera.remove(j);
            solicitudDeIngreso.remove(j);
            return true;
        }

        return false;
    }

    //metodo que concatena agregarJugadorEsperando y la funcion actualizar observador.
}
