package ar.edu.unlu.poo.vista.Consola;


import ar.edu.unlu.poo.Controlador.Controlador;
import ar.edu.unlu.poo.interfaz.IVista;
import ar.edu.unlu.poo.modelo.Casino;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

public class VistaConsola extends JFrame implements IVista {
    private final Map<Menu, Runnable> ejecutador = new EnumMap<>(Menu.class);
    private VentanaLogginConsola ventanaLoggin;
    private VentanaCasinoConsola ventanaCasino;
    private VentanaMesaConsola ventanaMesa;
    private Controlador controler;

    public VistaConsola(Controlador controler){
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

    public void mostrarMenu(Menu estado, String mensaje){
        if(mensaje != null && !mensaje.isBlank()) {
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
        ejecutador.put(Menu.LOGGIN, () ->{
            if(ventanaLoggin.isVisible()){
                ventanaLoggin.mostrarMenuPrincipal();
            }
            else{
                activarLoggin();
            }
        });
        ejecutador.put(Menu.CARGAR_JUGADORES, () -> ventanaLoggin.mostrarMenuCargarJugador(controler.getJugadoresGuardados()));
        ejecutador.put(Menu.CREAR_JUGADOR, () -> ventanaLoggin.mostrarMenuCrearJugador());


        ejecutador.put(Menu.CASINO, () ->{
            if(ventanaCasino.isVisible()){
                ventanaCasino.mostrarCasino(controler.getJugadoresConectados(), controler.getJugador(), controler.getPosicionDeEspera());
            }
            else{
                activarCasino();
            }
        });
        ejecutador.put(Menu.CONECTADOS, () -> ventanaCasino.actualizarInformacionConectados(controler.getJugadoresConectados()));
        ejecutador.put(Menu.ACCIONES_CASINO, () -> ventanaCasino.actualizarInformacionOpciones(controler.getJugador(), controler.getPosicionDeEspera()));
        ejecutador.put(Menu.RANKING, () -> ventanaCasino.printearRanking(controler.getRanking()));
        ejecutador.put(Menu.PEDIR_APUESTA_LISTA, () -> ventanaCasino.pedirApuesta(controler.getSaldoJugador(), Menu.PEDIR_APUESTA_LISTA));
        ejecutador.put(Menu.PEIDR_APUESTA_MESA, () -> ventanaCasino.pedirApuesta(controler.getSaldoJugador(), Menu.PEIDR_APUESTA_MESA));
        ejecutador.put(Menu.ACTUALIZAR_LISTA_ESPERA, () -> {
            if(ventanaCasino.getMenu() != Menu.RANKING) {
                ventanaCasino.actualizarInformacionOpciones(controler.getJugador(), controler.getPosicionDeEspera());
            }
        });
        ejecutador.put(Menu.ACTUALIZAR_CONECTADOS, () -> ventanaCasino.actualizarInformacionConectados(controler.getJugadoresConectados()));


        ejecutador.put(Menu.MESA, () ->{
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
        ejecutador.put(Menu.JUGADORES_INSCRIPTOS, () -> ventanaMesa.mostrarDatosIncriptos(controler.getInscriptos(), controler.getJugador()));
        ejecutador.put(Menu.ACCIONES_MESA, () -> ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa()));
        ejecutador.put(Menu.CAMBIO_ESTADO, () -> {
            ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador());
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());
            ventanaMesa.imprimirMenuAcciones(controler.getJugador(), controler.getEstadoMesa());
        });
        ejecutador.put(Menu.ACTUALIZAR_JUEGO, () -> {
            ventanaMesa.imprimirAreaDeJuego(controler.getJugador(), controler.getDealer(), controler.getEstadoMesa(), controler.manoEnTurno());

            if(ventanaMesa.getMenu() == Menu.JUGADORES_INSCRIPTOS){
                ventanaMesa.mostrarDatosIncriptos(controler.getInscriptos(), controler.getJugador());
            }

            ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador());
        });
        ejecutador.put(Menu.ACTUALIZAR_INFORMACION, () -> ventanaMesa.imprimirAreaDeInformacion(controler.getJugador(), controler.getEstadoMesa(), controler.getTurnoJugador()));
    }
}
