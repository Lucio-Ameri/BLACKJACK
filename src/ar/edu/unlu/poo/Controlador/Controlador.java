package ar.edu.unlu.poo.Controlador;

import ar.edu.unlu.poo.interfaz.*;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.ManoJugador;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.eventos.Notificacion;
import ar.edu.unlu.poo.vista.Consola.Menu;

import java.util.Arrays;
import java.util.List;

public class Controlador implements Observador, IControlador {
    private IVista vista;
    private ICasino casino;
    private IMesa mesa;
    private IJugador jugador;

    public Controlador(ICasino modelo) {
        this.casino = modelo;
        this.mesa = null;
        this.vista = null;
        this.jugador = null;
    }

    private void setMesa(IMesa m) {
        this.mesa = m;
    }

    @Override
    public void setVista(IVista v) {
        this.vista = v;
    }

    private void setJugadorActual(IJugador j) {
        this.jugador = j;
    }

    @Override
    public IJugador getJugador() {
        return jugador;
    }

    @Override
    public List<IManoJugador> getManosJugador(){
        return jugador.getManosJugadorInterfaz();
    }

    @Override
    public List<IJugador> getGuardados(){
        return casino.jugadoresGuardados();
    }

    @Override
    public List<IJugador> getInscriptosMesa(){
        return mesa.getInscriptos();
    }

    @Override
    public List<IJugador> getConectadosCasino(){
        return casino.getJugadoresConectados(jugador);
    }

    @Override
    public List<String> getRanking(){
        return casino.getRankingMundial(jugador);
    }

    @Override
    public int posicionDeEspera(){
        return casino.miPosicionEnListaDeEspera(jugador);
    }

    @Override
    public double getSaldoJugador(){
        return jugador.getSaldoJugador();
    }

    @Override
    public IDealer getDealer(){
        return mesa.getDealer();
    }

    @Override
    public EstadoDeLaMesa getEstadoMesa(){
        return mesa.getEstado();
    }

    @Override
    public String getTurnoJugador(){
        return mesa.getJugadorTurnoActual();
    }

    @Override
    public int manoEnTurno(){
        List<IManoJugador> manos = jugador.getManosJugadorInterfaz();
        EstadoDeLaMano estado;

        for(IManoJugador m: manos){
            estado = m.getEstado();

            if(estado == EstadoDeLaMano.EN_JUEGO || estado == EstadoDeLaMano.TURNO_INICIAL){
                return manos.indexOf(m);
            }
        }

        return -1;
    }

    @Override
    public void procesarLoggin(Menu estado, String ingreso) {
        switch (estado) {
            case LOGGIN -> {
                switch (ingreso) {
                    case "1" -> vista.mostrarMenu(Menu.CARGAR_JUGADORES, casino.hayJugadoresGuardados());

                    case "2" -> vista.mostrarMenu(Menu.CREAR_JUGADOR, null);

                    case "0" -> System.exit(0);

                    default -> vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }

            case CARGAR_JUGADORES -> {
                try {
                    int valor = Integer.parseInt(ingreso);
                    validarJugadorGuardado(valor);
                } catch (NumberFormatException e) {
                    vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }

            case CREAR_JUGADOR -> {
                if (!ingreso.matches("^[a-zA-Z]+$")) {

                    if (ingreso.equals("0")) {
                        vista.mostrarMenu(Menu.LOGGIN, null);
                    } else {
                        vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                    }

                    return;
                }

                validarCrearJugador(ingreso);
            }
        }
    }

    private void validarJugadorGuardado(int ingreso) {
        List<IJugador> jugadores = casino.jugadoresGuardados();

        if (ingreso == 0) {
            vista.mostrarMenu(Menu.LOGGIN, null);
        } else if (ingreso > 0 && ingreso <= jugadores.size()) {

            setJugadorActual(casino.unirmeAlCasino(jugadores.get(ingreso - 1).getNombre(), this));

            if (jugador != null) {
                vista.mostrarMenu(Menu.CASINO, null);
            } else {
                vista.mostrarMenu(null, Eventos.JUGADOR_YA_CONECTADO);
            }
        } else {
            vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
        }
    }

    private void validarCrearJugador(String ingreso) {
        setJugadorActual(casino.unirmeAlCasino(ingreso, this));

        if (jugador != null) {
            vista.mostrarMenu(Menu.CASINO, null);
        } else {
            vista.mostrarMenu(null, Eventos.JUGADOR_YA_CONECTADO);
        }
    }

    @Override
    public void procesarCasino(Menu estado, String ingreso) {
        switch (estado) {
            case ACCIONES_CASINO -> {
                switch (ingreso) {
                    case "1" -> {
                        if (jugador.perdio()) {
                            vista.mostrarMenu(null, Eventos.JUGADOR_PERDIO);
                        } else {
                            vista.mostrarMenu(Menu.PEDIR_APUESTA_MESA, null);
                        }
                    }

                    case "2" -> {
                        if (jugador.perdio()) {
                            vista.mostrarMenu(null, Eventos.JUGADOR_PERDIO);
                        } else {
                            vista.mostrarMenu(Menu.PEDIR_APUESTA_LISTA, null);
                        }
                    }

                    case "3" -> vista.mostrarMenu(Menu.ACCIONES_CASINO, casino.salirListaDeEspera(jugador));

                    case "4" -> vista.mostrarMenu(Menu.RANKING, null);

                    case "0" -> irmeDelCasino();

                    default -> vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }

            case RANKING -> {
                switch (ingreso){
                    case "0" -> vista.mostrarMenu(Menu.ACCIONES_CASINO, null);

                    default -> vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }

            case PEDIR_APUESTA_MESA, PEDIR_APUESTA_LISTA -> {
                if(ingreso.equals("-")){
                    vista.mostrarMenu(Menu.ACCIONES_CASINO, null);
                }

                if(ingreso.isEmpty() || !ingreso.matches("\\d+(?:[.,]\\d+)?")){
                    vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                }

                try{
                    double monto = Double.parseDouble(ingreso.replace(',', '.'));
                    if(monto < 1.0){
                        vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                        return;
                    }

                    switch (estado){
                        case PEDIR_APUESTA_MESA -> {
                            ingresarMesa(monto);
                        }

                        case PEDIR_APUESTA_LISTA -> {
                            ingresarListaEspera(monto);
                        }
                    }
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                }
            }
        }
    }

    private void irmeDelCasino(){
        Eventos ev = casino.irmeDelCasino(jugador, this);

        switch (ev){
            case ACCION_REALIZADA -> {
                jugador = null;
                vista.mostrarMenu(Menu.LOGGIN, null);
            }

            default -> vista.mostrarMenu(null, ev);
        }
    }

    private void ingresarMesa(double monto){
        Eventos ev = casino.unirmeALaMesa(jugador, monto, this);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void ingresarListaEspera(double monto){
        Eventos ev = casino.unirmeALaListaDeEspera(jugador, monto, this);
        switch (ev){
            case ACCION_REALIZADA -> vista.mostrarMenu(Menu.ACCIONES_CASINO, null);

            default -> vista.mostrarMenu(null, ev);
        }
    }

    @Override
    public void procesarMesa(Menu estado, String ingreso){
        switch (estado) {

            case INSCRIPCIONES -> {

                switch (ingreso) {
                    case "1" -> validarSituacionAgregarMano();

                    case "2" -> validarSituacionEliminarMano();

                    case "3" -> validarSituacionRetirarme();

                    case "4" -> {
                        mesa.confirmarParticipacion(jugador);

                        if (mesa.getEstado() == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES) {
                            vista.mostrarMenu(Menu.ESPERAR, null);
                        }
                    }
                }
            }

            case REPARTIENDO -> {
                if (mesa.getEstado() == EstadoDeLaMesa.REPARTIENDO_CARTAS) {
                    vista.mostrarMenu(null, Eventos.DEBE_ESPERAR);
                }
            }

            case TURNO_JUGADOR -> {
                List<String> requiereMano = Arrays.asList("1","2","3","4","5","6");
                if (requiereMano.contains(ingreso) && manoEnTurno() == -1) {
                    vista.mostrarMenu(null, Eventos.YA_JUGO_SU_TURNO);
                }

                else if(requiereMano.contains(ingreso) && !mesa.esMiTurno(jugador)){
                    vista.mostrarMenu(null, Eventos.NO_ES_SU_TURNO);
                }

                else {
                    switch (ingreso) {
                        case "1" -> pedirCarta();
                        case "2" -> quedarme();
                        case "3" -> rendirme();
                        case "4" -> asegurarme();
                        case "5" -> splitearMano();
                        case "6" -> doblarMano();
                        case "7" -> {
                            if(mesa.getInscriptos().size() != 1) {
                                vista.mostrarMenu(Menu.JUGADORES_INSCRIPTOS, null);
                            }

                            else{
                                vista.mostrarMenu(null, Eventos.UNICO_JUGADOR);
                            }
                        }

                        default -> vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                    }

                    if(manoEnTurno() == -1 && mesa.esMiTurno(jugador)){
                        mesa.jugadorJuegaSuTurno(Accion.PASAR_TURNO, jugador);
                    }
                }
            }

            case TURNO_DEALER -> {
                if(mesa.getEstado() == EstadoDeLaMesa.TURNO_DEALER) {
                    vista.mostrarMenu(null, Eventos.DEBE_ESPERAR);
                }
            }

            case GANANCIAS_REPARTIDAS -> {
                switch (ingreso){
                    case "0":
                        mesa.confirmarParticipacion(jugador);

                        if(mesa.getEstado() == EstadoDeLaMesa.REPARTIENDO_GANANCIAS) {
                            vista.mostrarMenu(Menu.ESPERAR, null);
                        }
                        break;

                    default:
                        vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                        break;
                }
            }

            case FINALIZAR -> {
                switch (ingreso){
                    case "0":
                        if(!jugador.perdio()) {
                            vista.mostrarMenu(Menu.PEDIR_APUESTA_CONFIRMACION, null);
                        }

                        else{
                            vista.mostrarMenu(null, Eventos.JUGADOR_PERDIO);
                        }
                        break;

                    case "-":
                        mesa.confirmarNuevaParticipacion(jugador, 0.0, false, this);
                        vista.mostrarMenu(Menu.CASINO, null);
                        break;

                    default:
                        vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                        break;
                }
            }

            case APOSTAR_MANO, PEDIR_APUESTA_CONFIRMACION -> {
                if(ingreso.equals("-") && estado == Menu.APOSTAR_MANO){
                    vista.mostrarMenu(Menu.ACCIONES_MESA, null);
                }

                if(ingreso.isEmpty() || !ingreso.matches("\\d+(?:[.,]\\d+)?")){
                    vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                }

                try{
                    double monto = Double.parseDouble(ingreso.replace(',', '.'));
                    if(monto < 1.0){
                        vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                        return;
                    }

                    switch (estado){
                        case PEDIR_APUESTA_CONFIRMACION -> {
                            mesa.confirmarNuevaParticipacion(jugador, monto, true, this);

                            if(mesa.getEstado() == EstadoDeLaMesa.FINALIZANDO_RONDA) {
                                vista.mostrarMenu(Menu.ESPERAR, null);
                            }
                        }

                        case APOSTAR_MANO -> {
                            sumarMano(monto);
                        }
                    }
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, Eventos.INGRESO_INVALIDO);
                }
            }

            case ELIMINAR_MANO -> {
                try{
                    int valor = Integer.parseInt(ingreso);
                    validarManoElegida(valor);
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }

            case ESPERAR -> {
                vista.mostrarMenu(null, Eventos.DEBE_ESPERAR);
            }

            case JUGADORES_INSCRIPTOS -> {
                switch (ingreso){
                    case "0" -> vista.mostrarMenu(Menu.ACCIONES_MESA, null);

                    default -> vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
                }
            }
        }
    }

    private void validarSituacionAgregarMano(){
        if(!jugador.perdio()){
            if(!mesa.confirme(jugador)){
                if(mesa.getEstado() == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                    if(mesa.hayLugaresDisponibles()){
                        vista.mostrarMenu(Menu.APOSTAR_MANO, null);
                    }

                    else {
                        vista.mostrarMenu(null, Eventos.SIN_LUGARES_DISPONIBLES);
                    }
                }

                else {
                    vista.mostrarMenu(null, Eventos.LA_MESA_YA_INICIO);
                }
            }

            else {
                vista.mostrarMenu(null, Eventos.JUGADOR_CONFIRMADO);
            }
        }

        else {
            vista.mostrarMenu(null, Eventos.JUGADOR_PERDIO);
        }
    }

    private void validarSituacionEliminarMano(){
        if(!mesa.confirme(jugador)){
            List<IManoJugador> manos = jugador.getManosJugadorInterfaz();

            if(manos.size() > 1){
                vista.mostrarMenu(Menu.ELIMINAR_MANO, null);
            }

            else {
                vista.mostrarMenu(null, Eventos.ULTIMA_MANO);
            }
        }

        else {
            vista.mostrarMenu(null, Eventos.JUGADOR_CONFIRMADO);
        }
    }

    private void validarSituacionRetirarme(){
        Eventos ev = mesa.retirarmeDeLaMesa(jugador, this);

        switch (ev){
            case ACCION_REALIZADA -> {
                mesa = null;
                vista.mostrarMenu(Menu.CASINO, null);
            }

            default -> vista.mostrarMenu(null, ev);
        }
    }

    private void pedirCarta(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.PEDIR_CARTA, jugador);

        if(ev == Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void quedarme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.QUEDARME, jugador);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void rendirme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.RENDIRME, jugador);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void asegurarme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.ASEGURARME, jugador);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void splitearMano(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.SEPARAR_MANO, jugador);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void doblarMano(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.DOBLAR_MANO, jugador);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }
    }

    private void sumarMano(double monto){
        Eventos ev = mesa.apostarOtraMano(jugador, monto);

        if(ev != Eventos.ACCION_REALIZADA){
            vista.mostrarMenu(null, ev);
        }

        else{
            vista.mostrarMenu(Menu.ACCIONES_MESA, null);
        }
    }

    private void validarManoElegida(int valor){
        List<IManoJugador> manos = jugador.getManosJugadorInterfaz();

        if(valor == 0){
            vista.mostrarMenu(Menu.ACCIONES_MESA, null);
        }

        else if(valor > 0 && valor <= manos.size()){
            Eventos ev = mesa.retirarUnaMano(jugador, valor - 1);

            if(ev != Eventos.ACCION_REALIZADA){
                vista.mostrarMenu(null, ev);
            }

            else{
                vista.mostrarMenu(Menu.INSCRIPCIONES, null);
            }
        }

        else{
            vista.mostrarMenu(null, Eventos.ACCION_INVALIDA);
        }
    }

    public void actualizar(Notificacion n){
        switch (n){
            case CAMBIO_ESTADO_MESA -> {
                vista.mostrarMenu(Menu.CAMBIO_ESTADO, null);

                EstadoDeLaMesa estado = mesa.getEstado();
                if((estado == EstadoDeLaMesa.REPARTIENDO_CARTAS) || (estado == EstadoDeLaMesa.TURNO_DEALER)){
                    mesa.confirmarParticipacion(jugador);
                }
            }

            case ACTUALIZAR_LISTA_ESPERA -> {
                if((mesa == null) && (casino.miPosicionEnListaDeEspera(jugador) != -1)){
                    vista.mostrarMenu(Menu.ACTUALIZAR_LISTA_ESPERA, null);
                }
            }

            case NUEVO_JUGADOR, JUGADOR_SE_FUE, ACTUALIZAR_CONECTADOS-> {
                if(mesa == null){
                    vista.mostrarMenu(Menu.ACTUALIZAR_CONECTADOS, null);
                }
            }

            case CARTA_REPARTIDA, DEALER_REVELO_MANO, JUGADOR_REALIZO_JUGADA -> {
                vista.mostrarMenu(Menu.ACTUALIZAR_JUEGO, null);
            }

            case JUGADOR_PASO_TURNO -> {
                vista.mostrarMenu(Menu.ACTUALIZAR_INFORMACION, null);
            }

            case JUGADOR_INGRESO_MESA -> {
                setMesa(casino.getMesa(jugador));
                vista.mostrarMenu(Menu.MESA, null);
            }
        }
    }
}
