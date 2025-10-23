package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.*;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.eventos.Notificacion;
import ar.edu.unlu.poo.modelo.persistencia.Serializador;
import ar.edu.unlu.poo.modelo.persistencia.TablaPuntuacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Mesa implements IMesa {
    private List<Jugador> inscriptos;
    private List<Observador> observadores;
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
        this.observadores = new CopyOnWriteArrayList<>();
    }

    public boolean jugadorEnLaMesa(Jugador j){
        return inscriptos.contains(j);
    }

    @Override
    public boolean confirme(IJugador j){
        return confirmados.get(getJugador(j.getNombre()));
    }

    @Override
    public boolean hayLugaresDisponibles(){
        return lugaresDisponibles > 0;
    }

    private void cambiarEstadoDeLaMesa(EstadoDeLaMesa en){
        estado = en;
    }

    private void reiniciarConfirmados(){
        if (!confirmados.isEmpty()) {
            for (Map.Entry<Jugador, Boolean> e : confirmados.entrySet()) {
                e.setValue(false);
            }
        }
    }

    @Override
    public void confirmarParticipacion(IJugador j){
        Jugador jug = getJugador(j.getNombre());

        if(jug != null) {
            if (!confirmados.get(jug)) {
                confirmados.put(jug, true);
                actualizarEstadoDeLaMesa();
            }
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

    @Override
    public boolean esMiTurno(IJugador j){
        Jugador jug = getJugador(j.getNombre());
        return ((turnoActual != null) && (turnoActual == jug));
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
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.REPARTIENDO_CARTAS);
                    reiniciarConfirmados();

                    notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
                }

            }

            case REPARTIENDO_CARTAS -> {
                if(todosConfirmaron()){
                    repartirLasCartasIniciales();
                    turnoActual = inscriptos.get(0);
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.TURNO_JUGADOR);
                    reiniciarConfirmados();

                    notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
                }

            }

            case TURNO_JUGADOR -> {
                if(turnoActual == null){
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.TURNO_DEALER);
                    reiniciarConfirmados();

                    notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
                }

                else{
                    notificarObservadores(Notificacion.JUGADOR_PASO_TURNO);
                }
            }

            case TURNO_DEALER -> {
                reiniciarConfirmados();

                empezarTurnoDelDealer();
                cambiarEstadoDeLaMesa(EstadoDeLaMesa.REPARTIENDO_GANANCIAS);

                notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
            }

            case REPARTIENDO_GANANCIAS -> {
                if(todosConfirmaron()) {
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.FINALIZANDO_RONDA);
                    dealer.retirarManosJugadas(inscriptos);
                    lugaresDisponibles = 7;
                    reiniciarConfirmados();

                    notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
                }
            }

            case FINALIZANDO_RONDA -> {
                if(todosConfirmaron() || inscriptos.isEmpty()){
                    cambiarEstadoDeLaMesa(EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES);
                    reiniciarConfirmados();
                    dealer.limpiarMano();
                    notificarObservadores(Notificacion.CAMBIO_ESTADO_MESA);
                }
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
                }
            }

            manoD.recibirCarta(dealer.repartirCarta());
            notificarObservadores(Notificacion.CARTA_REPARTIDA);
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
        notificarObservadores(Notificacion.DEALER_REVELO_MANO);

        ManoDealer mano = dealer.getMano();

        while(mano.getEstado() == EstadoDeLaMano.EN_JUEGO || mano.turnoInicial()){
            mano.recibirCarta(dealer.repartirCarta());
            notificarObservadores(Notificacion.CARTA_REPARTIDA);
        }

        dealer.definirResultados(inscriptos);
        TablaPuntuacion tabla = Serializador.cargarTablaDePuntuacion();

        for(Jugador j: inscriptos){
            tabla.actualizarLista(j);
        }
    }

    @Override
    public void confirmarNuevaParticipacion(IJugador j, double monto, boolean participacion, Observador o){
        try {
            Jugador jug = getJugador(j.getNombre());

            if (participacion) {
                dealer.retirarDineroJugador(jug, monto);
                jug.agregarMano(new ManoJugador(monto));
                confirmarParticipacion(j);
                lugaresDisponibles--;
            }

            else {
                inscriptos.remove(jug);
                confirmados.remove(jug);
                eliminarObservador(o);
            }

            actualizarEstadoDeLaMesa();
        }
        catch (NullPointerException e){
            System.out.println("ERROR, JUGADOR NULO!");
        }
    }

    public Eventos inscribirJugadorNuevo(Jugador j, double monto, Observador o){
        if(!inscriptos.contains(j)){
            if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                if(hayLugaresDisponibles()){
                    lugaresDisponibles --;
                    inscriptos.add(j);
                    confirmados.put(j, false);

                    dealer.retirarDineroJugador(j, monto);
                    j.agregarMano(new ManoJugador(monto));

                    agregarObservador(o);

                    actualizarEstadoDeLaMesa();
                    return Eventos.ACCION_REALIZADA;
                }

                return Eventos.SIN_LUGARES_DISPONIBLES;
            }

            return Eventos.LA_MESA_YA_INICIO;
        }

        return Eventos.JUGADOR_EN_LA_MESA;
    }

    @Override
    public Eventos apostarOtraMano(IJugador j, double monto){
        Jugador jug = getJugador(j.getNombre());
        if(jug != null) {
            if (!confirmados.get(jug)) {
                if (estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    if (hayLugaresDisponibles()) {
                        if (jug.transferenciaRealizable(monto)) {
                            lugaresDisponibles--;
                            dealer.retirarDineroJugador(jug, monto);
                            jug.agregarMano(new ManoJugador(monto));

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

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos retirarUnaMano(IJugador j, int posMano){
        Jugador jug = getJugador(j.getNombre());
        ManoJugador mano = jug.getManos().get(posMano);

        if(jug != null) {
            if (!confirmados.get(jug)) {
                if (estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    if (jug.getManos().size() > 1) {
                        dealer.devolverDinero(jug, mano);
                        lugaresDisponibles++;

                        return Eventos.ACCION_REALIZADA;
                    }

                    return Eventos.ULTIMA_MANO;
                }

                return Eventos.LA_MESA_YA_INICIO;
            }

            return Eventos.JUGADOR_CONFIRMADO;
        }

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos retirarmeDeLaMesa(IJugador j, Observador o){
        Jugador jug = getJugador(j.getNombre());

        if(jug != null) {
            if (!confirmados.get(jug)) {
                if (estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                    lugaresDisponibles += jug.getManos().size();
                    dealer.eliminarJugador(jug);
                    inscriptos.remove(jug);
                    confirmados.remove(jug);
                    eliminarObservador(o);

                    actualizarEstadoDeLaMesa();
                    return Eventos.ACCION_REALIZADA;
                }

                return Eventos.LA_MESA_YA_INICIO;
            }

            return Eventos.JUGADOR_CONFIRMADO;
        }

        return  Eventos.JUGADOR_NO_CONECTADO;
    }

    @Override
    public Eventos jugadorJuegaSuTurno(Accion a, IJugador j){
        Jugador jug = getJugador(j.getNombre());
        if(jug != null) {

            if (esTurnoDeEsteJugador(jug)) {

                ManoJugador m = null;

                if(a != Accion.PASAR_TURNO) {
                    m = getManoTurnoActual(jug);
                }

                switch (a) {
                    case PEDIR_CARTA -> {
                        m.recibirCarta(dealer.repartirCarta());

                        notificarObservadores(Notificacion.CARTA_REPARTIDA);
                        return Eventos.ACCION_REALIZADA;
                    }

                    case QUEDARME -> {
                        m.quedarme();

                        notificarObservadores(Notificacion.JUGADOR_REALIZO_JUGADA);
                        return Eventos.ACCION_REALIZADA;
                    }

                    case RENDIRME -> {
                        if (m.turnoInicial()) {
                            m.rendirme();

                            notificarObservadores(Notificacion.JUGADOR_REALIZO_JUGADA);
                            return Eventos.ACCION_REALIZADA;
                        }

                        return Eventos.NO_ES_TURNO_INICIAL;
                    }

                    case ASEGURARME -> {
                        if (m.turnoInicial()) {
                            if (dealer.condicionSeguro()) {
                                if (!m.getEnvite().estaAsegurado()) {

                                    double monto = m.getEnvite().getMontoApostado() / 2.0;

                                    if (jug.transferenciaRealizable(monto)) {
                                        dealer.retirarDineroJugador(jug, monto);
                                        m.asegurarme();

                                        notificarObservadores(Notificacion.JUGADOR_REALIZO_JUGADA);
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
                        if (m.turnoInicial()) {

                            double monto = m.getEnvite().getMontoApostado();

                            if (jug.transferenciaRealizable(monto)) {
                                dealer.retirarDineroJugador(jug, monto);
                                m.doblarMano(dealer.repartirCarta());

                                notificarObservadores(Notificacion.JUGADOR_REALIZO_JUGADA);
                                return Eventos.ACCION_REALIZADA;
                            }

                            return Eventos.SALDO_INSUFICIENTE;
                        }

                        return Eventos.NO_ES_TURNO_INICIAL;
                    }

                    case SEPARAR_MANO -> {
                        if (m.turnoInicial()) {
                            if (m.condicionParaSepararMano()) {

                                double monto = m.getEnvite().getMontoApostado();

                                if (jug.transferenciaRealizable(monto)) {
                                    dealer.retirarDineroJugador(jug, monto);
                                    ManoJugador nueva = m.separarMano();

                                    m.recibirCarta(dealer.repartirCarta());
                                    nueva.recibirCarta(dealer.repartirCarta());

                                    jug.agregarManoEnPosicion(jug.getManos().indexOf(m) + 1, nueva);

                                    notificarObservadores(Notificacion.JUGADOR_REALIZO_JUGADA);
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

        return Eventos.JUGADOR_NO_CONECTADO;
    }

    private ManoJugador getManoTurnoActual(Jugador j){
        List<ManoJugador> manos = j.getManos();
        EstadoDeLaMano estado;

        for(ManoJugador m: manos){
            estado = m.getEstado();

            if(estado == EstadoDeLaMano.EN_JUEGO || estado == EstadoDeLaMano.TURNO_INICIAL){
                return m;
            }
        }

        return null;
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

        if(estado == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES || estado == EstadoDeLaMesa.FINALIZANDO_RONDA){
            return new ArrayList<IJugador>();
        }

        return new ArrayList<IJugador>(inscriptos);
    }

    public void agregarObservador(Observador o){
        observadores.add(o);
        o.actualizar(Notificacion.JUGADOR_INGRESO_MESA);
    }

    public void eliminarObservador(Observador o){
        observadores.remove(o);
    }

    public void notificarObservadores(Notificacion n){
        for(Observador o: observadores){
            o.actualizar(n);
        }
    }

    private Jugador getJugador(String nombre){
        for(Jugador j: inscriptos){
            if(j.getNombre().equals(nombre)){
                return j;
            }
        }

        return null;
    }
}
