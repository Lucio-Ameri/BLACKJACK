package ar.edu.unlu.poo.vista.Consola;

import ar.edu.unlu.poo.interfaz.IDealer;
import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IManoJugador;
import ar.edu.unlu.poo.interfaz.IVentana;
import ar.edu.unlu.poo.modelo.estados.EstadoDeLaMesa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class VentanaMesaConsola extends JFrame implements IVentana {
    private JPanel panelPrincipal;
    private JPanel panelCentral;
    private JPanel panelJuego;
    private JPanel panelAcciones;
    private JScrollPane panelScrolleableAcciones;
    private JScrollPane panelScrolleableJuego;
    private JSplitPane panelDivisor;
    private JTextField headerVentana;
    private JTextField headerJuego;
    private JTextField headerAcciones;
    private JTextField campoDeEntrada;
    private JTextField areaDeAvisos;
    private JTextField areaDeInformacion;
    private JTextArea areaDeJuego;
    private JTextArea areaDeAcciones;
    private String placeHolder;
    private Menu menu;

    public VentanaMesaConsola(){
        setTitle(".\t\t\t\t\t\t\t\t          VENTANA MESA DESDE CONSOLA - SALA DE JUEGO -");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //panel principal y central.
        panelPrincipal = new JPanel(new BorderLayout());
        panelCentral = new JPanel(new BorderLayout());
        panelJuego = new JPanel(new BorderLayout());
        panelAcciones = new JPanel(new BorderLayout());

        //establezco el diseño del header principal
        headerVentana = new JTextField("SALA DE JUEGO");
        headerVentana.setHorizontalAlignment(JTextField.CENTER);
        headerVentana.setEditable(false);
        headerVentana.setForeground(Color.GREEN);
        headerVentana.setBackground(Color.BLACK);
        headerVentana.setBorder(BorderFactory.createEmptyBorder(20, 0,20, 0));
        headerVentana.setFont(new Font("Consolas", Font.BOLD, 24));
        headerVentana.setFocusable(false);


        //establezco el diseño del header juego.
        headerJuego = new JTextField("MESA DE JUEGO:");
        headerJuego.setHorizontalAlignment(JTextField.CENTER);
        headerJuego.setEditable(false);
        headerJuego.setForeground(Color.GREEN);
        headerJuego.setBackground(Color.BLACK);
        headerJuego.setBorder(BorderFactory.createEmptyBorder(8, 0,8, 0));
        headerJuego.setFont(new Font("Consolas", Font.PLAIN, 18));
        headerJuego.setFocusable(false);


        //establezco el diseño del header acciones.
        headerAcciones = new JTextField("ACCIONES:");
        headerAcciones.setHorizontalAlignment(JTextField.CENTER);
        headerAcciones.setEditable(false);
        headerAcciones.setForeground(Color.GREEN);
        headerAcciones.setBackground(Color.BLACK);
        headerAcciones.setBorder(BorderFactory.createEmptyBorder(8, 0,8, 0));
        headerAcciones.setFont(new Font("Consolas", Font.PLAIN, 18));
        headerAcciones.setFocusable(false);


        //establezco el diseño para el area de avisos.
        areaDeAvisos = new JTextField();
        areaDeAvisos.setHorizontalAlignment(JTextField.CENTER);
        areaDeAvisos.setEditable(false);
        areaDeAvisos.setForeground(Color.GREEN);
        areaDeAvisos.setBackground(Color.BLACK);
        areaDeAvisos.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));
        areaDeAvisos.setFont(new Font("Consolas", Font.ITALIC, 11));
        areaDeAvisos.setFocusable(false);


        //establezco el diseño para el area de informacion.
        areaDeInformacion = new JTextField();
        areaDeInformacion.setHorizontalAlignment(JTextField.CENTER);
        areaDeInformacion.setEditable(false);
        areaDeInformacion.setForeground(Color.GREEN);
        areaDeInformacion.setBackground(Color.BLACK);
        areaDeInformacion.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));
        areaDeInformacion.setFont(new Font("Consolas", Font.ITALIC, 11));
        areaDeInformacion.setFocusable(false);



        //establezco el diseño para el area de texto del juego.
        areaDeJuego = new JTextArea();
        areaDeJuego.setEditable(false);
        areaDeJuego.setBackground(Color.BLACK);
        areaDeJuego.setForeground(Color.GREEN);
        areaDeJuego.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaDeJuego.setFocusable(false);

        //establezco el panel scrolleable del juego.
        panelScrolleableJuego = new JScrollPane(areaDeJuego);
        panelScrolleableJuego.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelScrolleableJuego.setBackground(Color.BLACK);

        //establezco el contenido del panel de juego.
        panelJuego.add(headerJuego, BorderLayout.NORTH);
        panelJuego.add(panelScrolleableJuego, BorderLayout.CENTER);
        panelJuego.setBackground(Color.BLACK);



        //establezco el diseño para el area de texto de las acciones.
        areaDeAcciones = new JTextArea();
        areaDeAcciones.setEditable(false);
        areaDeAcciones.setBackground(Color.BLACK);
        areaDeAcciones.setForeground(Color.GREEN);
        areaDeAcciones.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaDeAcciones.setFocusable(false);

        //establezco el panel scrolleable de las acciones.
        panelScrolleableAcciones = new JScrollPane(areaDeAcciones);
        panelScrolleableAcciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelScrolleableAcciones.setBackground(Color.BLACK);

        //establezco el contenido del panel de las acciones.
        panelAcciones.add(headerAcciones, BorderLayout.NORTH);
        panelAcciones.add(panelScrolleableAcciones, BorderLayout.CENTER);
        panelAcciones.setBackground(Color.BLACK);


        //establezco el panel divisor.
        panelDivisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelJuego, panelAcciones);
        panelDivisor.setBackground(Color.BLACK);
        panelDivisor.setDividerLocation(800);
        panelDivisor.setDividerSize(0);
        panelDivisor.setBorder(null);
        panelDivisor.setEnabled(false);



        //establezco el diseño del campo de entrada.
        campoDeEntrada = new JTextField();
        campoDeEntrada.setBackground(Color.DARK_GRAY);
        campoDeEntrada.setForeground(Color.GREEN);
        campoDeEntrada.setCaretColor(Color.GREEN);
        campoDeEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        campoDeEntrada.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2), BorderFactory.createEmptyBorder(8, 8, 8, 8)));


        //establezco el contenido del panel central de la ventana.
        panelCentral.add(areaDeInformacion, BorderLayout.NORTH);
        panelCentral.add(panelDivisor, BorderLayout.CENTER);
        panelCentral.add(areaDeAvisos, BorderLayout.SOUTH);
        panelCentral.setBackground(Color.BLACK);


        //establezco el contenido del panel principal de la ventana
        panelPrincipal.add(headerVentana, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(campoDeEntrada, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        configurarPlaceholder();
    }

    private void configurarPlaceholder() {
        campoDeEntrada.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoDeEntrada.getText().equals(placeHolder)) {
                    campoDeEntrada.setText("");
                    campoDeEntrada.setForeground(Color.GREEN);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoDeEntrada.getText().isEmpty()) {
                    campoDeEntrada.setText(placeHolder);
                    campoDeEntrada.setForeground(Color.GREEN);
                }
            }
        });
    }

    private void actualizarPlaceHolder(String mensaje){
        this.placeHolder = mensaje;
        campoDeEntrada.setText(placeHolder);
    }

    public JTextField getCampoDeEntrada() {
        return campoDeEntrada;
    }

    public String getTextoIngresado() {
        return campoDeEntrada.getText().trim();
    }

    public void limpiarCampoDeEntrada(){
        campoDeEntrada.setText("");
        panelPrincipal.requestFocusInWindow();
    }

    public Menu getMenu(){
        return menu;
    }

    public void mostrarMensajeDeError(String mensaje) {
        areaDeAvisos.setText(mensaje);
    }

    public void mostrarMesa(IJugador jugador, IDealer dealer, EstadoDeLaMesa estado, String turno, int manoEnTurno){
        actualizarAreaDeJuego(jugador, dealer, estado, manoEnTurno);
        actualizarAreaDeInformacion(jugador, estado, turno);
        imprimirMenuDeAcciones(jugador, estado);
    }

    public void actualizarAreaDeJuego(IJugador jugador, IDealer dealer, EstadoDeLaMesa estado, int manoEnTurno){
        areaDeJuego.setText("\n");

        switch (estado){
            case ACEPTANDO_INSCRIPCIONES -> {
                areaDeJuego.append("\n\t\t\t DEALER DICE:\n\n\n");
                areaDeJuego.append("\t LAS INSCRIPCIONES TODAVIA ESTAN ABIERTAS... PUEDE REALIZAR APUESTAS,\n");
                areaDeJuego.append("\t ELIMINAR LAS MISMAS, HASTA QUE USTED CONFIRME SU PARTICIPACION... \n");
                areaDeJuego.append("\t UNA VEZ TODOS CONFIRMEN LA PARTICIPACION Y/O SE ACABEN LOS LUGARES\n");
                areaDeJuego.append("\t DE LA MESA, EL JUEGO ARRANCARA AUTOMATICAMENTE\n");
            }

            case REPARTIENDO_CARTAS, TURNO_DEALER, TURNO_JUGADOR ->{
                areaDeJuego.append(String.format("%s\n\n", dealer.descripcion()));

                List<IManoJugador> manos = jugador.getManosJugadorInterfaz();
                areaDeJuego.append("\t\t\t USTED:\n\n");

                int i = 0;
                for(IManoJugador m: manos){
                    if(i == manoEnTurno) {
                        areaDeJuego.append(String.format("MANO %d <-- EN JUEGO: %s\n", i + 1, m.descripcion()));
                    }
                    else{
                        areaDeJuego.append(String.format("MANO %d: %s\n", i + 1, m.descripcion()));
                    }
                    i++;
                }
            }

            case REPARTIENDO_GANANCIAS -> {
                areaDeJuego.append(String.format("%s\n\n", dealer.descripcion()));
                areaDeJuego.append("\t\t\t USTED:\n\n");
                areaDeJuego.append(String.format("%s", jugador.descripcion()));
            }

            case FINALIZANDO_RONDA -> {
                areaDeJuego.append("\n\t\t\t DEALER DICE:\n\n\n");
                areaDeJuego.append("\t EL JUEGO TERMINO, DESEA JUGAR OTRA PARTIDA? DEBERA INGRESAR UN MONTO\n");
                areaDeJuego.append(String.format("\t NO NEGATIVO Y NO MAYOR A SU SALDO ACTUAL $%.2f SI DESEA HACERLO...\n", jugador.getSaldoJugador()));
                areaDeJuego.append("\t SI NO DESEA PARTICIPAR, INGRESE '-' PARA PODER SALIR DE LA MESA...");
            }
        }
    }

    public void actualizarAreaDeInformacion(IJugador jugador, EstadoDeLaMesa estado, String turno){
        areaDeInformacion.setText("");

        switch (estado){
            case ACEPTANDO_INSCRIPCIONES, REPARTIENDO_CARTAS, TURNO_DEALER, REPARTIENDO_GANANCIAS, FINALIZANDO_RONDA -> {
                areaDeInformacion.setText(String.format("ESTADO DE LA MESA: %s \t TURNO ACTUAL: NO ES TURNO DE NINGUN JUGADOR...", estado));
            }

            case TURNO_JUGADOR -> {
                if(jugador.getNombre().equals(turno)) {
                    areaDeInformacion.setText(String.format("ESTADO DE LA MESA: %s \t TURNO ACTUAL: ES SU TURNO '%s'...", estado, turno));
                }
                else{
                    areaDeInformacion.setText(String.format("ESTADO DE LA MESA: %s \t TURNO ACTUAL: JUGADOR '%s'...", estado, turno));
                }
            }
        }
    }

    public void imprimirMenuDeAcciones(IJugador jugador, EstadoDeLaMesa estado){

        areaDeAcciones.setText("\n");
        headerAcciones.setText("ACCIONES");

        switch (estado){
            case ACEPTANDO_INSCRIPCIONES -> {
                menu = Menu.INSCRIPCIONES;
                areaDeAcciones.append("1.  APOSTAR OTRA MANO.\n");
                areaDeAcciones.append("2.  RETIRAR UNA APUESTA.\n");
                areaDeAcciones.append("3.  RETIRARME DE LA MESA.\n");
                areaDeAcciones.append("4.  CONFIRMAR PARTICIPACION.\n\n\n");
                areaDeAcciones.append("INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...\n\n\n\n");
                areaDeAcciones.append(String.format("\t\tDATOS DE USTED\n\nJUGADOR: %s  \nSALDO ACTUAL: %.2f  \nMAXIMO HISTORICO: %.2f", jugador.getNombre(), jugador.getSaldoJugador(), jugador.getMaximoHistorico()));

                actualizarPlaceHolder("INGRESE UNA OPCION MENCIONADA EN EL APARTADO DE ACCIONES...");
            }

            case REPARTIENDO_CARTAS -> {
                menu = Menu.REPARTIENDO;
                actualizarPlaceHolder("REPARTIENDO CARTAS...");
            }

            case TURNO_JUGADOR -> {
                menu = Menu.TURNO_JUGADOR;
                areaDeAcciones.append("1.   PEDIR CARTA.\n");
                areaDeAcciones.append("2.   QUEDARME.\n");
                areaDeAcciones.append("3.   RENDIRME.\n");
                areaDeAcciones.append("4.   ASEGURARME.\n");
                areaDeAcciones.append("5.   SPLITEAR MANO.\n");
                areaDeAcciones.append("6.   DOBLAR MANO.\n");
                areaDeAcciones.append("7.   MOSTRAR MANOS DE LOS DEMAS JUGADORES.\n");
                areaDeAcciones.append("8.   LISTA DE PARTICIPANTES.\n\n\n");
                areaDeAcciones.append("INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...\n\n");
                areaDeAcciones.append(String.format("\t\tDATOS DE USTED\n\nJUGADOR: %s  \nSALDO ACTUAL: %.2f  \nMAXIMO HISTORICO: %.2f", jugador.getNombre(), jugador.getSaldoJugador(), jugador.getMaximoHistorico()));

                actualizarPlaceHolder("INGRESE UNA OPCION MENCIONADA EN EL APARTADO DE ACCIONES...");
            }

            case TURNO_DEALER -> {
                menu = Menu.TURNO_DEALER;
                actualizarPlaceHolder("DEALER JUEGA SU TURNO...");
            }

            case REPARTIENDO_GANANCIAS -> {
                menu = Menu.GANANCIAS_REPARTIDAS;

                areaDeAcciones.append("LAS GANANCIAS FUERON REPARTIDAS EN BASE A ESTOS RESULTADOS...\n");
                areaDeAcciones.append("INGRESE '0' PARA PODER PASAR AL SIGUIENTE ESTADO Y PODER FINALIZAR LA PARTIDA\n");

                actualizarPlaceHolder("INGRESE '0' PARA CONTINUAR...");
            }

            case FINALIZANDO_RONDA -> {
                menu = Menu.FINALIZAR;
                actualizarPlaceHolder("INGRESE EL MONTO INDICADO PARA PARTICIPAR o '-' PARA SALIR...");
            }
        }

        mostrarMensajeDeError("");
    }

    public void esperando(){
        menu = Menu.ESPERANDO;
        areaDeAcciones.setText("\n");
        areaDeAcciones.append("ESPERANDO A QUE LOS DEMAS JUGADORES CONFIRMEN PARA PODER CONTINUAR...");
        actualizarPlaceHolder("");
        mostrarMensajeDeError("");
    }

    public void pedirApuesta(double monto, Menu estado){
        menu = estado;

        areaDeAcciones.setText("\n");

        if(estado == Menu.INSCRIPCIONES){
            headerAcciones.setText("VALIDAR APUESTA:");

            areaDeAcciones.append("\tINGRESE UN UN MONTO NO NEGATIVO. CUALQUIER OTRO INGRESO SERA \n");
            areaDeAcciones.append(String.format("\tTOMADO COMO INVALIDO. TENGA EN CUENTA QUE SU SALDO ACTUAL ES %.2f\n", monto));
            areaDeAcciones.append("\tY CUALQUIER VALOR MAYOR A ESTE TAMBIEN SERA INVALIDO...\n\n\n");
            areaDeAcciones.append("\t\t INGRESE '-' PARA VOLVER AL MENU DE ACCIONES.\n\n");

            actualizarPlaceHolder("INGRESE EL MONTO o '-'...");
        }

        mostrarMensajeDeError("");
    }

    public void eliminarMano(List<IManoJugador> manos){
        menu = Menu.ELIMINAR_MANO;

        areaDeAcciones.setText("\n");

        int i = 1;

        headerAcciones.setText("ELIMINAR UNA MANO:");
        for(IManoJugador m: manos){
            double apostado = m.getEnviteInterfaz().getMontoApostado();
            areaDeAcciones.append(String.format("MANO %d: APUESTA: $%.2f\n", i, apostado));
            i++;
        }
        areaDeAcciones.append("0.  PARA NO ELIMINAR NINGUNA Y VOLVER AL MENU ACCIONES...");

        areaDeAcciones.append("\n\n INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...");
        actualizarPlaceHolder("INGRESE EL NUMERO DE LA MANO A ELIMINAR...");
    }

    public void mostrarManosDeLosInscriptos(List<IJugador> inscriptos){


    }

}
