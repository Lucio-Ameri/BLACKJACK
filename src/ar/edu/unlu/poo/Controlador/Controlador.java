package ar.edu.unlu.poo.Controlador;

import ar.edu.unlu.poo.interfaz.*;
import ar.edu.unlu.poo.modelo.Jugador;
import ar.edu.unlu.poo.modelo.ManoJugador;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMano;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Accion;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.eventos.Notificacion;
import ar.edu.unlu.poo.modelo.persistencia.Serializador;
import ar.edu.unlu.poo.modelo.persistencia.TablaPuntuacion;
import ar.edu.unlu.poo.vista.Consola.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Controlador implements Observador{

    private IVista vista;
    private ICasino casino;
    private IMesa mesa;
    private IJugador jugador;

    public Controlador(ICasino modelo){
        this.casino = modelo;
        this.mesa = null;
        this.jugador = null;
        this.vista = null;
    }

    private void setJugadorActual(Jugador j){
        this.jugador = j;
    }

    private void setMesa(IMesa mesa){
        this.mesa = mesa;
    }

    public void setVista(IVista v){
        this.vista = v;
    }

    public IJugador getJugador(){
        return jugador;
    }

    public int getPosicionDeEspera(){
        return casino.miPosicionEnListaDeEspera((Jugador) jugador);
    }

    public List<String> getRanking(){
        TablaPuntuacion tabla = Serializador.cargarTablaDePuntuacion();
        return tabla.obtenerMejoresJugadores();
    }

    public double getSaldoJugador(){
        return jugador.getSaldoJugador();
    }

    public List<IJugador> getJugadoresGuardados(){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();
        return new ArrayList<IJugador>(jugadores);
    }

    public List<IJugador> getJugadoresConectados(){
        return casino.getJugadoresConectados((Jugador) jugador);
    }

    public List<IJugador> getInscriptos(){
        return mesa.getInscriptos();
    }

    public IDealer getDealer(){
        return mesa.getDealer();
    }

    public EstadoDeLaMesa getEstadoMesa(){
        return mesa.getEstado();
    }

    public String getTurnoJugador(){
        return mesa.getJugadorTurnoActual();
    }

    public int manoEnTurno(){
        List<IManoJugador>manos = jugador.getManosJugadorInterfaz();
        EstadoDeLaMano estado;

        for(IManoJugador m: manos){
            estado = m.getEstado();
            if(estado == EstadoDeLaMano.EN_JUEGO || estado == EstadoDeLaMano.TURNO_INICIAL){
                return manos.indexOf(m);
            }
        }

        return -1;
    }

    public IManoJugador getManoActual(){
        List<IManoJugador> manos = jugador.getManosJugadorInterfaz();
        EstadoDeLaMano estado;

        for(IManoJugador m: manos){
            estado = m.getEstado();

            if(estado == EstadoDeLaMano.EN_JUEGO || estado == EstadoDeLaMano.TURNO_INICIAL){
                return (ManoJugador) m;
            }
        }

        return null;
    }

    public List<IManoJugador> getManosJugador(){
        return jugador.getManosJugadorInterfaz();
    }

    private boolean hayJugadoresGuardados(){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();
        return !jugadores.isEmpty();
    }

    public void procesarLoggin(Menu estado, String entrada){
        switch (estado){
            case LOGGIN -> {
                switch (entrada){
                    case "1":
                        if (hayJugadoresGuardados()) {
                            vista.mostrarMenu(Menu.CARGAR_JUGADORES, null);
                        }

                        else {
                            vista.mostrarMenu(null, "NO HAY JUGADORES GUARDADOS PARA CARGAR!");
                        }
                        break;

                    case "2":
                        vista.mostrarMenu(Menu.CREAR_JUGADOR, null);
                        break;

                    case "0":
                        System.exit(0);
                        break;

                    default:
                        vista.mostrarMenu(null, "INGRESO INVALIDO! POR FAVOR, INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS!");
                        break;
                }
            }

            case CARGAR_JUGADORES -> {

                try{
                    int valor = Integer.parseInt(entrada);
                    validarJugadorGuardado(valor);
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, "INGRESO INVALIDO!, DEBE INGRESAR UNA DE LAS ANTERIORES OPCIONES!");
                }
            }

            case CREAR_JUGADOR -> {
                if(!entrada.matches("^[a-zA-Z]+$")) {

                    if(entrada.equals("0")){
                        vista.mostrarMenu(Menu.LOGGIN, null);
                    }

                    else{
                        vista.mostrarMenu(null, "NOMBRE INVALIDO! SIGA LAS INSTRUCCIONES MENCIONADAS!");
                    }

                    return;
                }

                validarCrearJugador(entrada);
            }
        }
    }

    private void validarJugadorGuardado(int entrada){
        List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();

        if(entrada == 0){
            vista.mostrarMenu(Menu.LOGGIN, null);
        }

        else if(entrada > 0 && entrada <= jugadores.size()){
            setJugadorActual(jugadores.get(entrada - 1));

            Eventos ev = casino.unirmeAlCasino((Jugador) jugador, this);

            if(ev == Eventos.ACCION_REALIZADA){
                Serializador.eliminarJugadorGuardado((Jugador) jugador);
                vista.mostrarMenu(Menu.CASINO, null);
            }

            else{
                vista.mostrarMenu(null, "JUGADOR YA INSCRIPTO DENTRO DEL CASINO! INTENTE CON OTRO!");
            }
        }

        else{
            vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UNA DE LAS ANTERIORES OPCIONES!");
        }
    }

    private void validarCrearJugador(String entrada){
        List<String> lista = Serializador.cargarListaNombresUsados();
        if (lista == null) {
            lista = new ArrayList<>();
        }

        String nombre = entrada;
        int n;

        do {
            n = ThreadLocalRandom.current().nextInt(1, 1000);
            nombre = "%s#%03d".formatted(entrada, n);
        }
        while (lista.contains(nombre));

        lista.add(nombre);
        Serializador.guardarListaNombresUsados(lista);

        setJugadorActual(new Jugador(nombre, 1000.0));

        Eventos ev = casino.unirmeAlCasino((Jugador) jugador, this);
        if (ev == Eventos.ACCION_REALIZADA) {
            vista.mostrarMenu(Menu.CASINO, null);
        }

        else {
            vista.mostrarMenu(null, "NO SE PUDO UNIR AL JUGADOR AL CASINO!");
        }
    }

    public void procesarCasino(Menu estado, String entrada){
        switch (estado){
            case ACCIONES_CASINO -> {
                switch (entrada) {
                    case "1":
                        if(!jugador.perdio()){
                            vista.mostrarMenu(Menu.PEIDR_APUESTA_MESA, null);
                        }

                        else{
                            vista.mostrarMenu(null, "USTED NO POSEE DINERO PARA PODER JUGAR. SALGA DEL JUEGO!");
                        }
                        break;

                    case "2":
                        if(!jugador.perdio()){
                            vista.mostrarMenu(Menu.PEDIR_APUESTA_LISTA, null);
                        }

                        else{
                            vista.mostrarMenu(null, "USTED NO POSEE DINERO PARA PODER JUGAR. SALGA DEL JUEGO!");
                        }
                        break;

                    case "3":
                        switch (casino.salirListaDeEspera((Jugador) jugador)){
                            case ACCION_REALIZADA -> {
                                vista.mostrarMenu(Menu.ACCIONES_CASINO, null);
                            }

                            case JUGADOR_NO_ESTA -> {
                                vista.mostrarMenu(null, "ACCION INVALIDA! USTED NO ESTA EN LA LISTA DE ESPERA!");
                            }
                        }
                        break;

                    case "4":
                        vista.mostrarMenu(Menu.RANKING, null);
                        break;

                    case "0":
                        irmeCasino();
                        break;

                    default:
                        vista.mostrarMenu(null, "ACCION INVALIDA! INGRESE UNA OPCION DE LAS MENCIONADAS ANTERIORMENTE!");
                        break;
                }
            }

            case RANKING -> {
                switch (entrada){
                    case "0":
                        vista.mostrarMenu(Menu.ACCIONES_CASINO, null);
                        break;

                    default:
                        vista.mostrarMenu(null, "OPCION INVALIDA! INGRESE '0' COMO SE MENCIONO PARA VOLVER AL MENU ACCIONES!");
                        break;
                }
            }

            case PEIDR_APUESTA_MESA, PEDIR_APUESTA_LISTA ->{
                if(entrada.equals("-")){
                    vista.mostrarMenu(Menu.ACCIONES_CASINO, null);
                }

                if(entrada.isEmpty() || !entrada.matches("\\d+(?:[.,]\\d+)?")){
                    vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UN MONTO COMO SE SOLICITO ANTERIORMENTE!");
                }

                try{
                    double monto = Double.parseDouble(entrada.replace(',', '.'));
                    if(monto < 1.0){
                        vista.mostrarMenu(null, "INGRESO INVALIDO! POR FAVOR, INGRESE UN MONTO >= '$1.0'!");
                        return;
                    }

                    switch (estado){
                        case PEIDR_APUESTA_MESA -> {
                            ingresarMesa(monto);
                        }

                        case PEDIR_APUESTA_LISTA -> {
                            ingresarListaEspera(monto);
                        }
                    }
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UN MONTO COMO SE SOLICITO ANTERIORMENTE!");
                }
            }
        }
    }

    private void irmeCasino(){
        Eventos ev = casino.irmeDelCasino((Jugador) jugador, this);

        switch (ev){
            case JUGADOR_EN_LA_MESA -> {
                vista.mostrarMenu(null, "EL JUGADOR SE ENCUENTRA JUGANDO EN LA MESA! NO PUEDE SALIR EN ESTE MOMENTO!");
            }

            case JUGADOR_NO_ESTA -> {
                vista.mostrarMenu(null, "EL JUGADOR NO ESTA EN EL CASINO! ACCION INVALIDA!");
            }

            case ACCION_REALIZADA -> {
                jugador.guardarJugador();
                jugador = null;
                vista.mostrarMenu(Menu.LOGGIN, null);
            }
        }

    }

    private void ingresarMesa(double monto){
        Eventos ev = casino.unirmeALaMesa((Jugador) jugador, monto, this);

        switch (ev){
            case JUGADOR_NO_ESTA -> vista.mostrarMenu(null, "JUGADOR NO SE ENCUENTRA EN EL CASINO! ACCION NO REALIZABLE");
            case LA_MESA_YA_INICIO, SIN_LUGARES_DISPONIBLES -> vista.mostrarMenu(null, "LA MESA YA INICIO LA RONDA! NO SE PUDO UNIR, INTENTELO MAS TARDE! INSCRIBASE A LA LISTA DE ESPERA!");
            case JUGADOR_EN_LA_MESA -> vista.mostrarMenu(null, "JUGADOR YA EN LA MESA!");
            case GENTE_ESPERANDO -> vista.mostrarMenu(null, "DEBE INSCRIBIRSE A LA LISTA DE ESPERA! LA MESA INICIO Y HAY GENTE ESPERANDO!");
            case SALDO_INSUFICIENTE -> vista.mostrarMenu(null, "EL MONTO INGRESADO ES MAYOR AL QUE POSEE!");
            case ACCION_REALIZADA -> {
                setMesa(casino.getMesa((Jugador) jugador));
                vista.mostrarMenu(Menu.MESA, null);
            }
        }
    }

    private void ingresarListaEspera(double monto){
        Eventos ev = casino.unirmeALaListaDeEspera((Jugador) jugador, monto, this);

        switch (ev){
            case JUGADOR_NO_ESTA -> vista.mostrarMenu(null, "JUGADOR NO SE ENCUENTRA EN EL CASINO! ACCION NO REALIZABLE");


            case JUGADOR_EN_LA_MESA -> {
                vista.mostrarMenu(null, "JUGADOR YA SE ENCUENTRA EN LA MESA");
            }

            case MESA_ACEPTANDO_INSCRIPCIONES -> {
                vista.mostrarMenu(null, "LA MESA TODAVIA ESTA ACEPTANDO INSCRIPCIONES! APURATE A INSCRIBIRTE!");
            }

            case JUGADOR_YA_EN_LISTA -> {
                vista.mostrarMenu(null, String.format("JUGADOR YA ESTA EN LA LISTA DE ESPERA. SU POSICION ES: %d!", casino.miPosicionEnListaDeEspera((Jugador) jugador)));
            }

            case SALDO_INSUFICIENTE -> {
                vista.mostrarMenu(null, "EL MONTO INGRESADO ES MAYOR AL QUE POSEE!");
            }

            case ACCION_REALIZADA -> {
                vista.mostrarMenu(Menu.ACCIONES_CASINO, null);
            }
        }
    }

    public void procesarMesa(Menu estado, String entrada){
        switch (estado){

            case INSCRIPCIONES -> {
                switch (entrada){
                    case "1":
                        validarSituacionAgregarMano();
                        break;

                    case "2":
                        validarSituacionEliminarMano();
                        break;

                    case "3":
                        validarSituacionRetirarme();
                        break;

                    case "4":
                        mesa.confirmarParticipacion((Jugador) jugador);

                        if(mesa.getEstado() == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){
                            vista.mostrarMenu(Menu.ESPERAR, null);
                        }
                        break;

                    default:
                        vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...");
                        break;
                }
            }

            case REPARTIENDO -> {
                if (mesa.getEstado() == EstadoDeLaMesa.REPARTIENDO_CARTAS) {
                    vista.mostrarMenu(null, "LAS CARTAS SE ESTAN REPARTIENDO, ESPERE A QUE EL DEALER TERMINE DE REPARTIRLAS...");
                }
            }

            case TURNO_JUGADOR -> {
                List<String> requiereMano = Arrays.asList("1","2","3","4","5","6");
                if (requiereMano.contains(entrada) && getManoActual() == null) {
                    vista.mostrarMenu(null, "USTED YA JUGO SU TURNO! NO PUEDE REALIZAR ESTA ACCION...");
                }

                else if(requiereMano.contains(entrada) && !mesa.esMiTurno((Jugador) jugador)){
                    vista.mostrarMenu(null, "NO ES SU TURNO! DEBE ESPERAR A QUE LOS DEMAS JUGADORES TERMINEN DE JUGAR EL SUYO...");
                }

                else {
                    switch (entrada) {
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
                                vista.mostrarMenu(null, "ACCION INVALIDA, DEBIDO A QUE USTED ES EL UNICO INTEGRANTE DEL JUEGO!");
                            }
                        }

                        default -> vista.mostrarMenu(null, "ACCION INVALIDA! DEBE INGRESAR UNA DE LAS ANTERIORMENTE MENCIONADAS...");
                    }

                    if(manoEnTurno() == -1 && mesa.esMiTurno((Jugador) jugador)){
                        mesa.jugadorJuegaSuTurno(Accion.PASAR_TURNO, (Jugador) jugador, null);
                    }
                }
            }

            case TURNO_DEALER -> {
                if(mesa.getEstado() == EstadoDeLaMesa.TURNO_DEALER) {
                    vista.mostrarMenu(null, "EL DEALER ESTA JUGANDO SU TURNO, ESPERE A QUE TERMINE...");
                }
            }

            case GANANCIAS_REPARTIDAS -> {
                switch (entrada){
                    case "0":
                        mesa.confirmarParticipacion((Jugador) jugador);

                        if(mesa.getEstado() == EstadoDeLaMesa.REPARTIENDO_GANANCIAS) {
                            vista.mostrarMenu(Menu.ESPERAR, null);
                        }
                        break;

                    default:
                        vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE LA ACCION ANTERIORMENTE INDICADA...");
                        break;
                }
            }

            case FINALIZAR -> {
                switch (entrada){
                    case "0":
                        if(!jugador.perdio()) {
                            vista.mostrarMenu(Menu.PEDIR_APUESTA_CONFIRMACION, null);
                        }
                        else{
                            vista.mostrarMenu(null, "USTED NO POSE SALDO SUFICIENTE PARA PARTICIPAR EN EL JUEGO. INGRESE '-'...");
                        }
                        break;

                    case "-":
                        mesa.confirmarNuevaParticipacion((Jugador) jugador, 0.0, false, this);
                        vista.mostrarMenu(Menu.CASINO, null);
                        break;

                    default:
                        vista.mostrarMenu(null, "ACCION INVALIDA, POR FAVOR UNA DE LAS OPCIONES INDICADAS ANTERIORMENTE...");
                        break;
                }
            }

            case APOSTAR_MANO, PEDIR_APUESTA_CONFIRMACION -> {
                if(entrada.equals("-") && estado == Menu.APOSTAR_MANO){
                    vista.mostrarMenu(Menu.ACCIONES_MESA, null);
                }

                if(entrada.isEmpty() || !entrada.matches("\\d+(?:[.,]\\d+)?")){
                    vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UN MONTO COMO SE SOLICITO ANTERIORMENTE!");
                }

                try{
                    double monto = Double.parseDouble(entrada.replace(',', '.'));
                    if(monto < 1.0){
                        vista.mostrarMenu(null, "INGRESO INVALIDO! POR FAVOR, INGRESE UN MONTO >= '$1.0'!");
                        return;
                    }

                    switch (estado){
                        case PEDIR_APUESTA_CONFIRMACION -> {
                            mesa.confirmarNuevaParticipacion((Jugador) jugador, monto, true, this);

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
                    vista.mostrarMenu(null, "INGRESO INVALIDO! INGRESE UN MONTO COMO SE SOLICITO ANTERIORMENTE!");
                }
            }

            case ELIMINAR_MANO -> {
                try{
                    int valor = Integer.parseInt(entrada);
                    validarManoElegida(valor);
                }
                catch (NumberFormatException e){
                    vista.mostrarMenu(null, "INGRESO INVALIDO!, DEBE INGRESAR UNA DE LAS ANTERIORES OPCIONES!");
                }
            }

            case ESPERAR -> {
                vista.mostrarMenu(null, "ESPERE A QUE LOS DEMAS JUGADORES CONFIRMEN PARA PODER PASAR AL SIGUIENTE ESTADO...");
            }

            case JUGADORES_INSCRIPTOS -> {
                switch (entrada){
                    case "0" -> vista.mostrarMenu(Menu.ACCIONES_MESA, null);
                    default -> vista.mostrarMenu(null, "INGRESO INVALIDO! DEBE INGRESAR '0' PARA VOLVER AL MENU DE ACCIONES...");
                }
            }
        }
    }

    private void validarSituacionAgregarMano(){
        if(!jugador.perdio()){

            if(!mesa.confirme((Jugador) jugador)){

                if(mesa.getEstado() == EstadoDeLaMesa.ACEPTANDO_INSCRIPCIONES){

                    if(mesa.hayLugaresDisponibles()){
                        vista.mostrarMenu(Menu.APOSTAR_MANO, null);
                    }

                    else{
                        vista.mostrarMenu(null, "ACCION NO REALIZABLE DEBIDO A QUE NO QUEDAN LUGARES DISPONIBLES EN LA MESA");
                    }
                }

                else{
                    vista.mostrarMenu(null, "ACCION NO REALIZABLE, LA MESA YA INICIO");
                }
            }

            else{
                vista.mostrarMenu(null, "USTED YA CONFIRMO SU PARTICIPACION... ACCION NO REALIZABLE!");
            }
        }

        else{
            vista.mostrarMenu(null, "USTED NO POSEE EL DINERO SUFICIENTE PARA REALIZAR ESTA ACCION!");
        }
    }

    private void validarSituacionEliminarMano(){

        if(!mesa.confirme((Jugador) jugador)){

            List<IManoJugador> manos = jugador.getManosJugadorInterfaz();
            if(manos.size() > 1){
                vista.mostrarMenu(Menu.ELIMINAR_MANO, null);
            }

            else{
                vista.mostrarMenu(null, "ACCION NO REALIZABLE DEBIDO A QUE SOLO POSEE UNA MANO.");
            }
        }

        else{
            vista.mostrarMenu(null, "USTED YA CONFIRMO SU PARTICIPACION... ACCION NO REALIZABLE!");
        }
    }

    private void validarSituacionRetirarme(){

        Eventos ev = mesa.retirarmeDeLaMesa((Jugador) jugador, this);

        switch (ev){
            case JUGADOR_CONFIRMADO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE! USTED YA CONFIRMO SU PARTICIPACION, ESPERE A QUE INICIE EL JUEGO!");
            }

            case LA_MESA_YA_INICIO -> {
                vista.mostrarMenu(null, "EL JUEGO YA INICIO! ACCION NO REALIZABLE!");
            }

            case ACCION_REALIZADA -> {
                mesa = null;
                vista.mostrarMenu(Menu.CASINO, null);
            }
        }
    }

    private void pedirCarta(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.PEDIR_CARTA, (Jugador) jugador, (ManoJugador) getManoActual());

        if(ev == Eventos.NO_ES_SU_TURNO){
            vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
        }
    }

    private void quedarme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.QUEDARME, (Jugador) jugador, (ManoJugador) getManoActual());

        if(ev == Eventos.NO_ES_SU_TURNO){
            vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
        }
    }

    private void rendirme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.RENDIRME, (Jugador) jugador, (ManoJugador) getManoActual());

        switch(ev){
            case NO_ES_SU_TURNO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
            }

            case NO_ES_TURNO_INICIAL -> {
                vista.mostrarMenu(null, "NO ES EL TURNO INICIAL, NO PUEDE REALIZAR LA ACCION");
            }
        }
    }

    private void asegurarme(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.ASEGURARME, (Jugador) jugador, (ManoJugador) getManoActual());

        switch (ev){
            case NO_ES_SU_TURNO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
            }

            case NO_ES_TURNO_INICIAL -> {
                vista.mostrarMenu(null, "NO ES EL TURNO INICIAL, NO PUEDE REALIZAR LA ACCION");
            }

            case DEALER_NO_CUMPLE -> {
                vista.mostrarMenu(null, "EL DEALER NO CUMPLE LA CONDICION PARA REALIZAR ESTA ACCION...");
            }

            case MANO_YA_ASEGURADA -> {
                vista.mostrarMenu(null, "USTED YA ASEGURO SU MANO, NO PUEDE VOLVER A REALIZAR ESTA ACCION...");
            }

            case SALDO_INSUFICIENTE -> {
                vista.mostrarMenu(null, "NO POSEE EL SALDO SUFICIENTE PARA PODER REALIZAR ESTA ACCION...");
            }
        }
    }

    private void splitearMano(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.SEPARAR_MANO, (Jugador) jugador, (ManoJugador) getManoActual());

        switch (ev) {
            case NO_ES_SU_TURNO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
            }

            case NO_ES_TURNO_INICIAL -> {
                vista.mostrarMenu(null, "NO ES EL TURNO INICIAL, NO PUEDE REALIZAR LA ACCION...");
            }

            case MANO_NO_CUMPLE -> {
                vista.mostrarMenu(null, "LA MANO NO CUMPLE LA CONDICION PARA SPLITEAR, ACCION NO REALIZABLE...");
            }

            case SALDO_INSUFICIENTE -> {
                vista.mostrarMenu(null, "NO TIENE EL SALDO SUFICIENTE PARA PODER REALIZAR LA ACCION...");
            }
        }
    }

    private void doblarMano(){
        Eventos ev = mesa.jugadorJuegaSuTurno(Accion.DOBLAR_MANO, (Jugador) jugador, (ManoJugador) getManoActual());

        switch (ev) {
            case NO_ES_SU_TURNO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE! NO ES SU TURNO...");
            }

            case NO_ES_TURNO_INICIAL -> {
                vista.mostrarMenu(null, "NO ES EL TURNO INICIAL, NO PUEDE REALIZAR LA ACCION...");
            }

            case SALDO_INSUFICIENTE -> {
                vista.mostrarMenu(null, "NO TIENE EL SALDO SUFICIENTE PARA PODER REALIZAR LA ACCION...");
            }
        }
    }

    private void sumarMano(double monto){
        Eventos ev = mesa.apostarOtraMano((Jugador) jugador, monto);

        switch (ev){

            case JUGADOR_CONFIRMADO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE... USTED YA CONFIRMO SU PARTICIPACION. INGRESE '-' PARA VOLVER");
            }

            case LA_MESA_YA_INICIO -> {
                vista.mostrarMenu(null, "ACCION NO REALIZABLE... LA MESA YA INICIO, INGRESE '-' PARA VOLVER");
            }

            case SIN_LUGARES_DISPONIBLES -> {
                vista.mostrarMenu(null, "NO HAY LUGARES DISPONIBLES, NO SE PUDO CREAR LA MANO, INGRESE '-' PARA VOVLER");
            }

            case SALDO_INSUFICIENTE -> {
                vista.mostrarMenu(null, "SALDO INSUFICIENTE, INGRESE UN SALDO ACORDE AL MENCIONADO...");
            }

            case ACCION_REALIZADA -> {
                vista.mostrarMenu(Menu.ACCIONES_MESA, null);
            }
        }
    }

    private void validarManoElegida(int valor){
        List<IManoJugador> manos = jugador.getManosJugadorInterfaz();

        if(valor == 0){
            vista.mostrarMenu(Menu.ACCIONES_MESA, null);
        }

        else if(valor > 0 && valor <= manos.size()){
            Eventos ev = mesa.retirarUnaMano((Jugador) jugador, (ManoJugador) manos.get(valor - 1));

            switch (ev){
                case JUGADOR_CONFIRMADO -> {
                    vista.mostrarMenu(null, "ACCION INVALIDA! USTED YA CONFIRMO...");
                }

                case LA_MESA_YA_INICIO -> {
                    vista.mostrarMenu(null, "ACCION INVALIDA! LA MESA YA INICIO...");
                }

                case ULTIMA_MANO -> {
                    vista.mostrarMenu(null, "ACCION INVALIDA! ES LA ULTIMA MANO QUE POSEE, NO PUEDE BORRARLA...");
                }

                case ACCION_REALIZADA -> {
                    vista.mostrarMenu(Menu.INSCRIPCIONES, null);
                }
            }
        }

        else{
            vista.mostrarMenu(null, "INGRESO INVALIDO! DEBE INGRESAR UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...");
        }
    }

    public void actualizar(Notificacion n){

        switch (n){
            case CAMBIO_ESTADO_MESA -> {
                vista.mostrarMenu(Menu.CAMBIO_ESTADO, null);

                EstadoDeLaMesa estado = mesa.getEstado();
                if((estado == EstadoDeLaMesa.REPARTIENDO_CARTAS) || (estado == EstadoDeLaMesa.TURNO_DEALER)){
                    mesa.confirmarParticipacion((Jugador) jugador);
                }
            }

            case ACTUALIZAR_LISTA_ESPERA -> {
                if((mesa == null) && (casino.miPosicionEnListaDeEspera((Jugador) jugador) != -1)){
                    vista.mostrarMenu(Menu.ACTUALIZAR_LISTA_ESPERA, null);
                }
            }

            case NUEVO_JUGADOR, JUGADOR_SE_FUE -> {
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
                mesa = casino.getMesa((Jugador) jugador);
                vista.mostrarMenu(Menu.MESA, null);
            }
        }
    }
}
