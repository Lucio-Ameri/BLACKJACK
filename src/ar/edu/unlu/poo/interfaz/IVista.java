package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.vista.Consola.Menu;

public interface IVista {
    void mostrarMenu(Menu estado, Eventos situacion);
}
