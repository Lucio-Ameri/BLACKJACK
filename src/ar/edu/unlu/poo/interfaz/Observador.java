package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.eventos.Notificacion;

public interface Observador {
    void actualizar(Notificacion n);
}
