package ar.edu.unlu.poo.vista.Consola;

import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IVentana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class VentanaLogginConsola extends JFrame implements IVentana {
    private JPanel panelPrincipal;
    private JPanel panelCentral;
    private JScrollPane panelScrolleable;
    private JTextArea areaDeTexto;
    private JTextField areaDeAvisos;
    private JTextField header;
    private JTextField campoDeEntrada;
    private String placeHolder;
    private Menu menu;

    public VentanaLogginConsola(){
        setTitle(".\t\t\t\t\t VENTANA LOGGIN JUGADOR DESDE CONSOLA");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panelPrincipal = new JPanel(new BorderLayout());
        panelCentral = new JPanel(new BorderLayout());

        header = new JTextField("MENU LOGGIN");
        header.setHorizontalAlignment(JTextField.CENTER);
        header.setEditable(false);
        header.setForeground(Color.GREEN);
        header.setBackground(Color.BLACK);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0,20, 0));
        header.setFont(new Font("Consolas", Font.BOLD, 24));
        header.setFocusable(false);


        areaDeAvisos = new JTextField();
        areaDeAvisos.setHorizontalAlignment(JTextField.CENTER);
        areaDeAvisos.setEditable(false);
        areaDeAvisos.setForeground(Color.GREEN);
        areaDeAvisos.setBackground(Color.BLACK);
        areaDeAvisos.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));
        areaDeAvisos.setFont(new Font("Consolas", Font.ITALIC, 11));
        areaDeAvisos.setFocusable(false);


        areaDeTexto = new JTextArea();
        areaDeTexto.setEditable(false);
        areaDeTexto.setBackground(Color.BLACK);
        areaDeTexto.setForeground(Color.GREEN);
        areaDeTexto.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaDeTexto.setFocusable(false);

        panelScrolleable = new JScrollPane(areaDeTexto);
        panelScrolleable.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelScrolleable.setBackground(Color.BLACK);

        campoDeEntrada = new JTextField();
        campoDeEntrada.setBackground(Color.DARK_GRAY);
        campoDeEntrada.setForeground(Color.GREEN);
        campoDeEntrada.setCaretColor(Color.GREEN);
        campoDeEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        campoDeEntrada.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2), BorderFactory.createEmptyBorder(8, 8, 8, 8)));


        panelCentral.add(panelScrolleable, BorderLayout.CENTER);
        panelCentral.add(areaDeAvisos, BorderLayout.SOUTH);

        panelPrincipal.add(header, BorderLayout.NORTH);
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

    private void limpiarPantalla(){
        areaDeTexto.setText("\n\n");
        areaDeAvisos.setText("");
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

    public void mostrarMenuPrincipal(){
        menu = Menu.LOGGIN;

        limpiarPantalla();

        areaDeTexto.append("\t\t\t\t 1.   CARGAR UN JUGADOR GUARDADO.\n");
        areaDeTexto.append("\t\t\t\t 2.   CREAR UN JUGADOR NUEVO.\n");
        areaDeTexto.append("\t\t\t\t 0.   SALIR DEL JUEGO.\n\n\n");
        areaDeTexto.append("\t\t       INGRESE UNA DE LAS OPCIONES ANTERIORMENTE INDICADAS...");

        actualizarPlaceHolder("INGRESE UNA OPCION...");
    }

    public void mostrarMenuCargarJugador(List<IJugador> jugadores){
        menu = Menu.CARGAR_JUGADORES;

        limpiarPantalla();

        int i = 1;
        for(IJugador j: jugadores) {
            areaDeTexto.append(String.format("\t %d.   %s\n", i, j.descripcion()));
            i++;
        }
        areaDeTexto.append("\t 0.   VOLVER AL MENU ANTERIOR.\n\n\n");
        areaDeTexto.append("\t INGRESE UNA DE LAS OPCIONES ANTERIORMENTE INDICADAS...\n\n");

        actualizarPlaceHolder("INGRESE EL NUMERO DEL JUGADOR A CARGAR...");
    }

    public void mostrarMenuCrearJugador(){
        menu = Menu.CREAR_JUGADOR;

        limpiarPantalla();

        areaDeTexto.append("\t EN ESTE APARTADO INGRESARA EL NOMBRE QUE TENDRA EL JUGADOR. ESTE NOMBRE SOLO\n");
        areaDeTexto.append("\t PODRA TENER LETRAS, CUALQUIER OTRO INGRESO SERA TOMADO COMO INVALIDO Y DEBERA\n");
        areaDeTexto.append("\t REPETIR TODO EL PROCESO...\n\n");
        areaDeTexto.append("\t\t\t INGRESE SOLO '0' PARA VOLVER AL MENU ANTERIOR...\n\n");

        actualizarPlaceHolder("INGRESE EL NOMBRE DEL JUGADOR O '0' PARA VOLVER AL MENU ANTERIOR...");
    }

    public void mostrarMensajeDeError(String mensaje){
        areaDeAvisos.setText(mensaje);
    }
}
