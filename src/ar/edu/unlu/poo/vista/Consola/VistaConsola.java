package ar.edu.unlu.poo.vista.Consola;


import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.interfaz.IControlador;
import ar.edu.unlu.poo.interfaz.IVista;
import ar.edu.unlu.poo.modelo.Casino;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;
import ar.edu.unlu.poo.modelo.eventos.Eventos;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

public class VistaConsola extends JFrame  implements IVista{
    private final Map<Menu, Runnable> ejecutador = new EnumMap<>(Menu.class);
    private VentanaLogginConsola ventanaLoggin;
    private VentanaCasinoConsola ventanaCasino;
    private VentanaMesaConsola ventanaMesa;
    private IControlador controler;

    public VistaConsola(IControlador controler){
        this.controler = controler;
        controler.setVista(this);

        this.ventanaLoggin = new VentanaLogginConsola();
        this.ventanaCasino = new VentanaCasinoConsola();
        this.ventanaMesa = new VentanaMesaConsola();

        this.ventanaLoggin.getCampoDeEntrada().addActionListener(e ->{
            String entrada = ventanaLoggin.getTextoIngresado();
            Menu estado = ventanaLoggin.getMenu();
            ventanaLoggin.limpiarCampoDeEntrada();

            controler.procesarLoggin(estado, entrada);
        });

        this.ventanaCasino.getCampoDeEntrada().addActionListener(e ->{
            String entrada = ventanaCasino.getTextoIngresado();
            Menu estado = ventanaCasino.getMenu();
            ventanaCasino.limpiarCampoDeEntrada();

            controler.procesarCasino(estado, entrada);
        });

        this.ventanaMesa.getCampoDeEntrada().addActionListener(e ->{
            String entrada = ventanaMesa.getTextoIngresado();
            Menu estado = ventanaMesa.getMenu();
            ventanaMesa.limpiarCampoDeEntrada();

            controler.procesarMesa(estado, entrada);
        });

        iniciarEjecutador();
    }

    public void iniciar(){
        activarLoggin();
    }

    @Override
    public void mostrarMenu(Menu estado, Eventos situacion){
        if(situacion != null) {
            String mensaje = mensajeError(situacion);

            if (ventanaLoggin.isVisible()) {
                ventanaLoggin.mostrarMensajeDeError(mensaje);
            }

            else if (ventanaCasino.isVisible()) {
                ventanaCasino.mostrarMensajeDeError(mensaje);
            }

            else {
                ventanaMesa.mostrarMensajeDeError(mensaje);
            }
        }

        else{
            ejecutador.get(estado).run();
        }
    }

    private void activarLoggin(){
        ventanaLoggin.setVisible(true);
        ventanaCasino.setVisible(false);
        ventanaMesa.setVisible(false);
        mostrarMenu(Menu.LOGGIN, null);
    }

    private void activarCasino(){
        ventanaLoggin.setVisible(false);
        ventanaCasino.setVisible(true);
        ventanaMesa.setVisible(false);
        mostrarMenu(Menu.CASINO, null);
    }

    private void activarMesa(){
        ventanaLoggin.setVisible(false);
        ventanaCasino.setVisible(false);
        ventanaMesa.setVisible(true);
        mostrarMenu(Menu.MESA, null);
    }

    private void iniciarEjecutador(){
        //metodos para VENTANA LOGGIN.
        ejecutador.put(Menu.LOGGIN, () -> {
            if(ventanaLoggin.isVisible()){
                ventanaLoggin.mostrarMenuPrincipal();
            }

            else{
                activarLoggin();
            }
        });
        ejecutador.put(Menu.CARGAR_JUGADORES, () -> ventanaLoggin.mostrarMenuCargarJugador(controler.getGuardados()));
        ejecutador.put(Menu.CREAR_JUGADOR, () -> ventanaLoggin.mostrarMenuCrearJugador());


        //metodos para VENTANA CASINO.
        ejecutador.put(Menu.CASINO, () -> {
            if(ventanaCasino.isVisible()){
                ventanaCasino.mostrarCasino(controler.getConectadosCasino(), controler.getJugador(), controler.posicionDeEspera());
            }
            else{
                activarCasino();
            }
        });
        ejecutador.put(Menu.CONECTADOS, () -> ventanaCasino.actualizarInformacionConectados(controler.getConectadosCasino()));
        ejecutador.put(Menu.ACCIONES_CASINO, () -> ventanaCasino.actualizarInformacionOpciones(controler.getJugador(), controler.posicionDeEspera()));
        ejecutador.put(Menu.RANKING, () -> ventanaCasino.printearRanking(controler.getRanking()));
        ejecutador.put(Menu.PEDIR_APUESTA_LISTA, () -> ventanaCasino.pedirApuesta(controler.getSaldoJugador(), Menu.PEDIR_APUESTA_LISTA));
        ejecutador.put(Menu.PEDIR_APUESTA_MESA, () -> ventanaCasino.pedirApuesta(controler.getSaldoJugador(), Menu.PEDIR_APUESTA_MESA));
        ejecutador.put(Menu.ACTUALIZAR_LISTA_ESPERA, () -> {
            if(ventanaCasino.getMenu() != Menu.RANKING){
                ventanaCasino.actualizarInformacionOpciones(controler.getJugador(), controler.posicionDeEspera());
            }
        });
        ejecutador.put(Menu.ACTUALIZAR_CONECTADOS, () -> ventanaCasino.actualizarInformacionConectados(controler.getConectadosCasino()));


        //metodos para VENTANA MESA.
        ejecutador.put(Menu.MESA, () -> {
            if(ventanaMesa.isVisible()){
                ventanaMesa.mostrarMesa(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.getTurnoJugador(), controler.manoEnTurno());
            }

            else{
                activarMesa();
            }
        });
        ejecutador.put(Menu.INSCRIPCIONES, () -> {
            ventanaMesa.imprimirAreaDeInformacion(null, controler.getEstadoMesa(), null);
            ventanaMesa.imprimirAreaDeJuego(null, null, controler.getEstadoMesa(), -1);
            ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa());
        });
        ejecutador.put(Menu.REPARTIENDO, () -> {
            ventanaMesa.imprimirAreaDeInformacion(null, controler.getEstadoMesa(), null);
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());
            ventanaMesa.imprimirMenuAcciones(null, controler.getEstadoMesa());
        });
        ejecutador.put(Menu.TURNO_JUGADOR, () -> {
            ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador());
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());
            ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa());
        });
        ejecutador.put(Menu.TURNO_DEALER, () -> {
            ventanaMesa.imprimirAreaDeInformacion(null, controler. getEstadoMesa(), null);
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), -1);
            ventanaMesa.imprimirMenuAcciones(null, controler.getEstadoMesa());
        });
        ejecutador.put(Menu.GANANCIAS_REPARTIDAS, () -> {
            ventanaMesa.imprimirAreaDeInformacion(null, controler.getEstadoMesa(), null);
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), -1);
            ventanaMesa.imprimirMenuAcciones(null, controler.getEstadoMesa());
        });
        ejecutador.put(Menu.FINALIZAR, () -> {
            ventanaMesa.imprimirAreaDeInformacion(null, controler.getEstadoMesa(), null);
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), null, controler.getEstadoMesa(), -1);
            ventanaMesa.imprimirMenuAcciones(null, controler.getEstadoMesa());
        });
        ejecutador.put(Menu.APOSTAR_MANO, () -> ventanaMesa.pedirApuesta(controler.getSaldoJugador(), Menu.APOSTAR_MANO));
        ejecutador.put(Menu.PEDIR_APUESTA_CONFIRMACION, () -> ventanaMesa.pedirApuesta(controler.getSaldoJugador(), Menu.PEDIR_APUESTA_CONFIRMACION));
        ejecutador.put(Menu.ELIMINAR_MANO, () -> ventanaMesa.eliminarMano(controler.getManosJugador()));
        ejecutador.put(Menu.ESPERAR, () -> ventanaMesa.aEsperar());
        ejecutador.put(Menu.JUGADORES_INSCRIPTOS, () -> ventanaMesa.mostrarDatosIncriptos(controler.getInscriptosMesa(), controler.getJugador()));
        ejecutador.put(Menu.ACCIONES_MESA, () -> ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa()));
        ejecutador.put(Menu.CAMBIO_ESTADO, () -> {
            ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador());
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());
            ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa());
        });
        ejecutador.put(Menu.ACTUALIZAR_JUEGO, () -> {
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());

            if(ventanaMesa.getMenu() == Menu.JUGADORES_INSCRIPTOS){
                ventanaMesa.mostrarDatosIncriptos(controler.getInscriptosMesa(), controler.getJugador());
            }

            ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador());
        });
        ejecutador.put(Menu.ACTUALIZAR_INFORMACION, () -> ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador()));
    }

    private String mensajeError(Eventos situacion){
        String mensaje = "";

        switch (situacion){
            case ACCION_INVALIDA -> mensaje = "INGRESO INVALIDO! POR FAVOR, INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS!";
            case SIN_JUGADORES_GUARDADOS -> mensaje = "ACCION INVALIDA! NO HAY JUGADORES GUARDADOS PARA CARGAR!";
            case JUGADOR_YA_CONECTADO -> mensaje = "JUGADOR YA INSCRIPTO DENTRO DEL CASINO! INTENTE CON UNO DIFERENTE!";
            case JUGADOR_PERDIO ->  mensaje = "USTED NO POSEE DINERO PARA PODER JUGAR. SALGA DEL JUEGO!";
            case JUGADOR_NO_ESTA -> mensaje = "EL JUGADOR NO SE ENCUENTRA EN LA LISTA DE ESPERA! ACCION INVALIDA!";
            case JUGADOR_NO_CONECTADO -> mensaje = "EL JUGADOR NO SE ENCUENTRA CONECTADO DENTRO DEL CASINO! ACCION INVALIDA!";
            case JUGADOR_EN_LA_MESA -> mensaje = "EL JUGADOR SE ENCUENTRA JUGANDO EN LA MESA! NO PUEDE SALIR EN ESTE MOMENTO!";
            case INGRESO_INVALIDO -> mensaje = "INGRESO INVALIDO! INGRESE UN MONTO COMO SE SOLICITO ANTERIORMENTE!";
            case SALDO_INSUFICIENTE -> mensaje = "EL MONTO INGRESADO ES MAYOR AL QUE POSEE!";
            case LA_MESA_YA_INICIO, SIN_LUGARES_DISPONIBLES -> mensaje = "LA MESA YA INICIO LA RONDA! NO SE PUDO UNIR, INTENTELO MAS TARDE! INSCRIBASE A LA LISTA DE ESPERA!";
            case MESA_ACEPTANDO_INSCRIPCIONES -> mensaje = "LA MESA TODAVIA ESTA ACEPTANDO INSCRIPCIONES! APURATE A INSCRIBIRTE!";
            case GENTE_ESPERANDO -> mensaje = "DEBE INSCRIBIRSE A LA LISTA DE ESPERA! LA MESA INICIO Y HAY GENTE ESPERANDO!";
            case JUGADOR_YA_EN_LISTA -> mensaje = String.format("EL JUGADOR YA ESTA EN LA LISTA DE ESPERA. SU POSICION ES: %d!", controler.posicionDeEspera());
            case JUGADOR_CONFIRMADO -> mensaje = "\"USTED YA CONFIRMO SU PARTICIPACION... ACCION NO REALIZABLE!";
            case ULTIMA_MANO -> mensaje = "ACCION NO REALIZABLE DEBIDO A QUE SOLO POSEE UNA MANO.";
            case DEBE_ESPERAR -> {
                EstadoDeLaMesa estado = controler.getEstadoMesa();
                if(estado == EstadoDeLaMesa.REPARTIENDO_CARTAS){
                    mensaje = "LAS CARTAS SE ESTAN REPARTIENDO, ESPERE A QUE EL DEALER TERMINE DE REPARTIRLAS...";
                }

                else if(estado == EstadoDeLaMesa.TURNO_DEALER){
                    mensaje = "EL DEALER ESTA JUGANDO SU TURNO, ESPERE A QUE TERMINE...";
                }

                else{
                    mensaje = "ESPERE A QUE LOS DEMAS JUGADORES CONFIRMEN PARA PODER PASAR AL SIGUIENTE ESTADO...";
                }
            }
            case YA_JUGO_SU_TURNO -> mensaje = "USTED YA JUGO SU TURNO! NO PUEDE REALIZAR ESTA ACCION...";
            case NO_ES_SU_TURNO -> mensaje = "NO ES SU TURNO! DEBE ESPERAR A QUE LOS DEMAS JUGADORES TERMINEN DE JUGAR EL SUYO...";
            case NO_ES_TURNO_INICIAL -> mensaje = "NO ES EL TURNO INICIAL, NO PUEDE REALIZAR LA ACCION";
            case DEALER_NO_CUMPLE -> mensaje = "EL DEALER NO CUMPLE LA CONDICION PARA REALIZAR ESTA ACCION...";
            case MANO_YA_ASEGURADA -> mensaje = "USTED YA ASEGURO SU MANO, NO PUEDE VOLVER A REALIZAR ESTA ACCION...";
            case MANO_NO_CUMPLE ->  mensaje = "LA MANO NO CUMPLE LA CONDICION PARA SPLITEAR, ACCION NO REALIZABLE...";
            case UNICO_JUGADOR -> mensaje = "ACCION INVALIDA, DEBIDO A QUE USTED ES EL UNICO INTEGRANTE DEL JUEGO!";

        }

        return mensaje;
    }
}
