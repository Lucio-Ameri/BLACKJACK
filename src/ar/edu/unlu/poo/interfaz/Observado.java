package ar.edu.unlu.poo.interfaz;

import ar.edu.unlu.poo.modelo.eventos.Notificacion;

public interface Observado {

    void agregarObservador(Observador o);

    void eliminarObservador(Observador o);

    void notificarObservadores(Notificacion n);
}
