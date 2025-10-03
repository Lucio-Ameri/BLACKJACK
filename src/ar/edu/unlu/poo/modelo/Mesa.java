package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IDealer;
import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IMesa;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.persistencia.Serializador;
import ar.edu.unlu.poo.modelo.persistencia.TablaPuntuacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mesa implements IMesa {
    private List<Jugador> inscriptos;
    private HashMap<Jugador, Boolean> confirmados;
    private EstadoDeLaMesa estado;
    private Dealer dealer;
    private Jugador turnoActual;
    private int lugaresDisponibles;

    public Mesa(){
        this.inscriptos = new ArrayList<Jugador>();
        this.confirmados = new HashMap<Jugador, Boolean>();
        this.estado = EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES;
        this.dealer = new Dealer();
        this.turnoActual = null;
        this.lugaresDisponibles = 7;
    }


    public boolean jugadorEnLaMesa(Jugador j){
        return inscriptos.contains(j);
    }

    private void cambiarEstadoDeLaMesa(EstadoDeLaMesa en){
        estado = en;
    }

    public boolean hayLugaresDisponibles(){
        return lugaresDisponibles > 0;
    }


    private void reiniciarConfirmados(){
        if(!confirmados.isEmpty()){
            for(Map.Entry<Jugador, Boolean> valoresContenidos : confirmados.entrySet()){
                valoresContenidos.setValue(!valoresContenidos.getValue());
            }
        }
    }

    @Override
    public void confirmarParticipacion(Jugador j){
        if(!confirmados.get(j)){
            confirmados.put(j, true);
            actualizarEstadoDeLaMesa();
        }
    }

    private boolean todosConfirmaron(){
        if(confirmados.isEmpty()){
            return false;
        }

        for(boolean valor: confirmados.values()){
            if(!valor){
                return false;
            }
        }

        return true;
    }

    private boolean esTurnoDeEsteJugador(Jugador j){
        if(turnoActual != null){
            return j == turnoActual;
        }

        return false;
    }

    private void actualizarEstadoDeLaMesa(){
        switch (estado){
            case ACEPTANDO_INSCRIPCIONES -> {
                if(!hayLugaresDisponibles() || todosConfirmaron()){
                    reiniciarConfirmados();
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.REPARTIENDO_CARTAS);
                    //notificar cambio de estado para actualizar vista.
                }

                break;
            }

            case REPARTIENDO_CARTAS -> {
                if(todosConfirmaron()){
                    repartirLasCartasIniciales();
                    turnoActual = inscriptos.get(0);
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.TURNO_JUGADOR);
                    //notificar cambio de estado para actualizar vista e informar quien juega.
                }

                break;
            }

            case TURNO_JUGADOR -> {
                if(turnoActual == null){
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.TURNO_DEALER);
                    reiniciarConfirmados();
                    //notificar cambio para actualizar vista e indicar que empieza el turno del dealer.
                }

                else{
                    //notificar cambio el turno al siguiente jugador.
                }

                break;
            }

            case TURNO_DEALER -> {
                empezarTurnoDelDealer();
                cambiarEstadoDeLaMesa(EstadoDeLaMesa.REPARTIENDO_GANANCIAS);
                reiniciarConfirmados();
                //notificar cambio de estado para actualizar vista e indicar que se repartieron las ganancias.

                break;
            }

            case REPARTIENDO_GANANCIAS -> {
                cambiarEstadoDeLaMesa(EstadoDeLaMesa.FINALIZANDO_RONDA);
                dealer.retirarManosJugadas(inscriptos);
                lugaresDisponibles = 7;
                //notificar cambio de estado para actualizar vista y preguntar si el jugador desea jugar otra ronda.

                break;
            }

            case FINALIZANDO_RONDA -> {
                if(todosConfirmaron()){
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES);
                    reiniciarConfirmados();
                    dealer.limpiarMano();
                    //notificar cambios para actualizar vista.
                }

                break;
            }
        }
    }

    private void repartirLasCartasIniciales(){
        ManoDealer manoD = dealer.getMano();

        for(int i = 0; i < 2; i++){
            for(Jugador j: inscriptos) {
                List<ManoJugador> manos = j.getManos();

                for(ManoJugador m: manos){
                    m.recibirCarta(dealer.repartirCarta());
                    //notificar cambios para actualizar vista.
                }
            }

            manoD.recibirCarta(dealer.repartirCarta());
            //notificar cambios para actualizar vista.
        }
    }

    private boolean jugaronTodos(){
        return inscriptos.size() == (inscriptos.indexOf(turnoActual) + 1);
    }

    private void pasarTurnoAlSiguienteJugador(){
        if(jugaronTodos()){
            turnoActual = null;
        }

        else{
            int index = inscriptos.indexOf(turnoActual) + 1;
            turnoActual = inscriptos.get(index);
        }

        actualizarEstadoDeLaMesa();
    }

    private void empezarTurnoDelDealer(){
        dealer.revelarMano();
        //notificar cambio de la mano del dealer.

        ManoDealer mano = dealer.getMano();

        while(mano.getEstado() == EstadoDeLaMano.EN_JUEGO){
            mano.recibirCarta(dealer.repartirCarta());
            //notificar cambio de la mano del dealer.
        }

        dealer.definirResultados(inscriptos);
        TablaPuntuacion tabla = Serializador.cargarTablaDePuntuacion();

        for(Jugador j: inscriptos){
            tabla.actualizarLista(j);
        }

        tabla.guardarTabla();
    }

    @Override
    public void confirmarNuevaParticipacion(Jugador j, double monto, boolean participacion){
        if(participacion){
            dealer.retirarDineroJugador(j, monto);
            j.agregarMano(new ManoJugador(monto));
            confirmarParticipacion(j);
            lugaresDisponibles --;
        }

        else{
            inscriptos.remove(j);
            confirmados.remove(j);

            //remover al jugador de observadores.
        }

        actualizarEstadoDeLaMesa();
    }

    public Eventos inscribirJugadorNuevo(Jugador j, double monto){
        if(!inscriptos.contains(j)){
            if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                if(hayLugaresDisponibles()){
                    lugaresDisponibles --;
                    inscriptos.add(j);
                    confirmados.put(j, false);

                    dealer.retirarDineroJugador(j, monto);
                    j.agregarMano(new ManoJugador(monto));

                    actualizarEstadoDeLaMesa();
                    return Eventos.ACCION_REALIZADA;
                }

                return Eventos.SIN_LUGARES_DISPONIBLES;
            }

            return Eventos.LA_MESA_YA_INICIO;
        }

        return Eventos.JUGADOR_YA_INSCRIPTO;
    }

    @Override
    public Eventos apostarOtraMano(Jugador j, double monto){
        if(!confirmados.get(j)){
            if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                if(hayLugaresDisponibles()){
                    if(j.transferenciaRealizable(monto)){
                        lugaresDisponibles --;
                        dealer.retirarDineroJugador(j, monto);
                        j.agregarMano(new ManoJugador(monto));

                        actualizarEstadoDeLaMesa();
                        return Eventos.ACCION_REALIZADA;
                    }

                    return Eventos.SALDO_INSUFICIENTE;
                }

                return Eventos.SIN_LUGARES_DISPONIBLES;
            }

            return Eventos.LA_MESA_YA_INICIO;
        }

        return Eventos.JUGADOR_CONFIRMADO;
    }

    @Override
    public Eventos retirarUnaMano(Jugador j, ManoJugador mano){
        if(!confirmados.get(j)){
            if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                if(j.getManos().size() > 1){
                    dealer.devolverDinero(j, mano);
                    lugaresDisponibles ++;

                    return Eventos.ACCION_REALIZADA;
                }

                return Eventos.ULTIMA_MANO;
            }

            return Eventos.LA_MESA_YA_INICIO;
        }

        return Eventos.JUGADOR_CONFIRMADO;
    }

    @Override
    public Eventos retirarmeDeLaMesa(Jugador j){
        if(!confirmados.get(j)){
            if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                lugaresDisponibles += j.getManos().size();
                dealer.eliminarJugador(j);
                inscriptos.remove(j);
                confirmados.remove(j);

                //eliminar jugador de observadores.
                actualizarEstadoDeLaMesa();
                return Eventos.ACCION_REALIZADA;
            }

            return Eventos.LA_MESA_YA_INICIO;
        }

        return Eventos.JUGADOR_CONFIRMADO;
    }

    @Override
    public Eventos jugadorJuegaSuTurno(Accion a, Jugador j, ManoJugador m){
        if(esTurnoDeEsteJugador(j)){
            switch (a){
                case PEDIR_CARTA -> {
                    m.recibirCarta(dealer.repartirCarta());
                    //notificar cambio en la vista.
                    return Eventos.ACCION_REALIZADA;
                }

                case QUEDARME -> {
                    m.quedarme();
                    //notificar cambio en la vista.
                    return Eventos.ACCION_REALIZADA;
                }

                case RENDIRME -> {
                    if(m.turnoInicial()){
                        m.rendirme();
                        //notificar cambio en la vista.
                        return Eventos.ACCION_REALIZADA;
                    }

                    return Eventos.NO_ES_TURNO_INICIAL;
                }

                case ASEGURARME -> {
                    if(m.turnoInicial()){
                        if(dealer.condicionSeguro()){
                            if(!m.getEnvite().estaAsegurado()){

                                double monto = m.getEnvite().getMontoApostado() / 2.0;

                                if(j.transferenciaRealizable(monto)){
                                    dealer.retirarDineroJugador(j, monto);
                                    m.asegurarme();
                                    //notificar cambio en la vista.
                                    return Eventos.ACCION_REALIZADA;
                                }

                                return Eventos.SALDO_INSUFICIENTE;
                            }

                            return Eventos.MANO_YA_ASEGURADA;
                        }

                        return Eventos.DEALER_NO_CUMPLE;
                    }

                    return Eventos.NO_ES_TURNO_INICIAL;
                }

                case DOBLAR_MANO -> {
                    if(m.turnoInicial()){

                        double monto = m.getEnvite().getMontoApostado();

                        if(j.transferenciaRealizable(monto)){
                            dealer.retirarDineroJugador(j, monto);
                            m.doblarMano(dealer.repartirCarta());
                            //notificar cambio en la vista.
                            return Eventos.ACCION_REALIZADA;
                        }

                        return Eventos.SALDO_INSUFICIENTE;
                    }

                    return Eventos.NO_ES_TURNO_INICIAL;
                }

                case SEPARAR_MANO -> {
                    if(m.turnoInicial()){
                        if(m.condicionParaSepararMano()){

                            double monto = m.getEnvite().getMontoApostado();

                            if(j.transferenciaRealizable(monto)){
                                dealer.retirarDineroJugador(j, monto);
                                ManoJugador nueva = m.separarMano();

                                m.recibirCarta(dealer.repartirCarta());
                                nueva.recibirCarta(dealer.repartirCarta());

                                j.agregarManoEnPosicion(j.getManos().indexOf(m) + 1, nueva);
                                //notificar cambio en la vista.
                                return Eventos.ACCION_REALIZADA;
                            }

                            return Eventos.SALDO_INSUFICIENTE;
                        }

                        return Eventos.MANO_NO_CUMPLE;
                    }

                    return Eventos.NO_ES_TURNO_INICIAL;
                }

                case PASAR_TURNO -> {
                    pasarTurnoAlSiguienteJugador();
                    return Eventos.ACCION_REALIZADA;
                }
            }
        }

        return Eventos.NO_ES_SU_TURNO;
    }


    @Override
    public IDealer getDealer(){
        return dealer;
    }

    @Override
    public EstadoDeLaMesa getEstado(){
        return estado;
    }

    @Override
    public String getJugadorTurnoActual(){
        if(turnoActual == null){
            return "NO ES TURNO DE NADIE.";
        }

        return turnoActual.getNombre();
    }

    @Override
    public List<IJugador> getInscriptos(){
        List<IJugador> jugadores = new ArrayList<IJugador>();

        if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES || estado == EstadoDeLaMesa.FINALIZANDO_RONDA){
            return jugadores;
        }

        for(Jugador j: inscriptos){
            jugadores.add(j);
        }

        return jugadores;
    }
}
