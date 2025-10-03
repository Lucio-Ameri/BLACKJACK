package ar.edu.unlu.poo.vista.Consola;

import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IVentana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class VentanaCasinoConsola extends JFrame implements IVentana {
    private JPanel panelPrincipal;
    private JPanel panelCentral;
    private JPanel panelOpciones;
    private JPanel panelConectados;
    private JScrollPane panelScrolleableConectados;
    private JScrollPane panelScrolleableOpciones;
    private JSplitPane panelDivisor;
    private JTextArea areaDeConectados;
    private JTextArea areaDeOpciones;
    private JTextField areaDeAvisos;
    private JTextField campoDeEntrada;
    private JTextField headerConectados;
    private JTextField headerOpciones;
    private JTextField headerVentana;
    private String placeHolder;
    private Menu menu;

    public VentanaCasinoConsola(){
        setTitle(".\t\t\t\t\t\t\t\t          VENTANA CASINO DESDE CONSOLA - LOBBY DEL JUEGO -");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //panel principal y central.
        panelPrincipal = new JPanel(new BorderLayout());
        panelCentral = new JPanel(new BorderLayout());
        panelOpciones = new JPanel(new BorderLayout());
        panelConectados = new JPanel(new BorderLayout());


        //establezco el titulo de la ventana.
        headerVentana = new JTextField("CASINO");
        headerVentana.setHorizontalAlignment(JTextField.CENTER);
        headerVentana.setEditable(false);
        headerVentana.setForeground(Color.GREEN);
        headerVentana.setBackground(Color.BLACK);
        headerVentana.setBorder(BorderFactory.createEmptyBorder(20, 0,20, 0));
        headerVentana.setFont(new Font("Consolas", Font.BOLD, 24));
        headerVentana.setFocusable(false);


        //establezco el titulo del panel conectados.
        headerConectados = new JTextField("CONECTADOS:");
        headerConectados.setHorizontalAlignment(JTextField.CENTER);
        headerConectados.setEditable(false);
        headerConectados.setForeground(Color.GREEN);
        headerConectados.setBackground(Color.BLACK);
        headerConectados.setBorder(BorderFactory.createEmptyBorder(8, 0,8, 0));
        headerConectados.setFont(new Font("Consolas", Font.PLAIN, 18));
        headerConectados.setFocusable(false);


        //establezco el titulo del panel opciones.
        headerOpciones = new JTextField("ACCIONES:");
        headerOpciones.setHorizontalAlignment(JTextField.CENTER);
        headerOpciones.setEditable(false);
        headerOpciones.setForeground(Color.GREEN);
        headerOpciones.setBackground(Color.BLACK);
        headerOpciones.setBorder(BorderFactory.createEmptyBorder(8, 0,8, 0));
        headerOpciones.setFont(new Font("Consolas", Font.PLAIN, 18));
        headerOpciones.setFocusable(false);


        //establezco el area de avisos.
        areaDeAvisos = new JTextField();
        areaDeAvisos.setHorizontalAlignment(JTextField.CENTER);
        areaDeAvisos.setEditable(false);
        areaDeAvisos.setForeground(Color.GREEN);
        areaDeAvisos.setBackground(Color.BLACK);
        areaDeAvisos.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));
        areaDeAvisos.setFont(new Font("Consolas", Font.ITALIC, 11));
        areaDeAvisos.setFocusable(false);


        //establezco el area de Opciones.
        areaDeOpciones = new JTextArea();
        areaDeOpciones.setEditable(false);
        areaDeOpciones.setBackground(Color.BLACK);
        areaDeOpciones.setForeground(Color.GREEN);
        areaDeOpciones.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaDeOpciones.setFocusable(false);

        //defino el contenido del panel de opciones.
        panelScrolleableOpciones = new JScrollPane(areaDeOpciones);
        panelScrolleableOpciones.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelScrolleableOpciones.setBackground(Color.BLACK);

        //defino el contenido del panel Scrolleable opciones.
        panelOpciones.add(headerOpciones, BorderLayout.NORTH);
        panelOpciones.add(panelScrolleableOpciones, BorderLayout.CENTER);
        panelOpciones.setBackground(Color.BLACK);


        //establezco el area de Conectados.
        areaDeConectados = new JTextArea();
        areaDeConectados.setEditable(false);
        areaDeConectados.setBackground(Color.BLACK);
        areaDeConectados.setForeground(Color.GREEN);
        areaDeConectados.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaDeConectados.setFocusable(false);

        //defino el contenido del panel Scrolleable conectados.
        panelScrolleableConectados = new JScrollPane(areaDeConectados);
        panelScrolleableConectados.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelScrolleableConectados.setBackground(Color.BLACK);

        //establezco el contenido del panel conectados.
        panelConectados.add(headerConectados, BorderLayout.NORTH);
        panelConectados.add(panelScrolleableConectados, BorderLayout.CENTER);
        panelConectados.setBackground(Color.BLACK);


        //establezco el area de entrada.
        campoDeEntrada = new JTextField();
        campoDeEntrada.setBackground(Color.DARK_GRAY);
        campoDeEntrada.setForeground(Color.GREEN);
        campoDeEntrada.setCaretColor(Color.GREEN);
        campoDeEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        campoDeEntrada.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2), BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        //establezco el contenido del panel divisor.
        panelDivisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelConectados, panelOpciones);
        panelDivisor.setBackground(Color.BLACK);
        panelDivisor.setDividerLocation(550);
        panelDivisor.setDividerSize(0);
        panelDivisor.setBorder(null);
        panelDivisor.setEnabled(false);

        //establezco el contenido del panel central.
        panelCentral.add(panelDivisor, BorderLayout.CENTER);
        panelCentral.add(areaDeAvisos, BorderLayout.SOUTH);
        panelCentral.setBackground(Color.BLACK);

        //incluyo toodo en el panel principal.
        panelPrincipal.add(headerVentana, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(campoDeEntrada, BorderLayout.SOUTH);
        panelPrincipal.setBackground(Color.BLACK);

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

    public void mostrarCasino(List<IJugador> conectados, IJugador jugador, int posicionEnLista){
        actualizarInformacionConectados(conectados);
        actualizarInformacionOpciones(jugador, posicionEnLista);
    }

    public void actualizarInformacionConectados(List<IJugador> conectados){
        areaDeConectados.setText("\n");
        int i = 1;

        for(IJugador j: conectados){
            areaDeConectados.append(String.format("\t    %d.   %s \t SALDO ACTUAL: %.2f\n", i, j.getNombre(), j.getSaldoJugador()));
            i++;
        }
    }

    public void actualizarInformacionOpciones(IJugador jugador, int posicionEnLista){
        menu = Menu.ACCIONES_CASINO;

        headerOpciones.setText("ACCIONES:");

        areaDeOpciones.setText("\n");
        areaDeOpciones.append("\t\t\t  1.   UNIRME A LA MESA\n");
        areaDeOpciones.append("\t\t\t  2.   UNIRME A LA LISTA DE ESPERA\n");
        areaDeOpciones.append("\t\t\t  3.   SALIR DE LA LISTA DE ESPERA\n");
        areaDeOpciones.append("\t\t\t  4.   RANKING 'LOS MEJORES JUGADORES'\n");
        areaDeOpciones.append("\t\t\t  0.   SALIR DEL CASINO (VOLVER AL LOGGIN)\n\n");
        areaDeOpciones.append("\t\t INGRESE UNA DE LAS OPCIONES ANTERIORMENTE MENCIONADAS...\n\n\n\n\n\n");

        areaDeOpciones.append("\t\t\t\tDATOS DEL JUGADOR:\n\n");
        areaDeOpciones.append(String.format("\t\t\t JUGADOR: %s\n", jugador.getNombre()));
        areaDeOpciones.append(String.format("\t\t\t SALDO: $%.2f\n", jugador.getSaldoJugador()));
        areaDeOpciones.append(String.format("\t\t\t MAXIMO HISTORICO: $%.2f\n", jugador.getMaximoHistorico()));
        areaDeOpciones.append(String.format("\t\t\t POSICION EN LISTA DE ESPERA:   %d\n", posicionEnLista));

        actualizarPlaceHolder("INGRESE UNA OPCION...");
        mostrarMensajeDeError("");
    }

    public void printearRanking(List<String> ranking){

        if(!ranking.isEmpty()) {
            menu = Menu.RANKING;

            headerOpciones.setText("RANKING:");
            areaDeOpciones.setText("");
            for (String s : ranking) {
                areaDeOpciones.append(s);
            }

            areaDeOpciones.append("\n\n INGRESE '0' PARA VOLVER AL MENU DE LAS ACCIONES...");

            mostrarMensajeDeError("");
            actualizarPlaceHolder("INGRESE '0' PARA VOLVER AL MENU DE LAS ACCIONES...");
        }

        else{
            areaDeAvisos.setText("\t TODAVIA NINGUN JUGADOR SUPERO EL PUNTAJE MINIMO PARA FIGURAR!");
        }
    }

    public void pedirApuesta(double monto, Menu estado){
        menu = estado;

        headerOpciones.setText("VALIDAR APUESTA:");

        areaDeOpciones.setText("");
        areaDeOpciones.append("\tINGRESE UN UN MONTO NO NEGATIVO. CUALQUIER OTRO INGRESO SERA \n");
        areaDeOpciones.append(String.format("\tTOMADO COMO INVALIDO. TENGA EN CUENTA QUE SU SALDO ACTUAL ES %.2f\n", monto));
        areaDeOpciones.append("\tY CUALQUIER VALOR MAYOR A ESTE TAMBIEN SERA INVALIDO...\n\n\n");
        areaDeOpciones.append("\t\t INGRESE '-' PARA VOLVER AL MENU DE ACCIONES.\n\n");

        mostrarMensajeDeError("");
        actualizarPlaceHolder("INGRESE EL MONTO o '-'...");
    }
}
