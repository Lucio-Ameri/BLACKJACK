package ar.edu.unlu.poo.modelo.persistencia;

import ar.edu.unlu.poo.modelo.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TablaPuntuacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> mejoresJugadores;
    private HashMap<String, Double> puntuaciones;

    public TablaPuntuacion(){
        this.mejoresJugadores = new ArrayList<String>();
        this.puntuaciones = new HashMap<String, Double>();
    }

    private boolean jugadorEnLaLista(Jugador j){
        return mejoresJugadores.contains(j.getNombre());
    }

    public void actualizarLista(Jugador j){
        String s = j.getNombre();
        Double d = j.getMaximoHistorico();

        if(jugadorEnLaLista(j)){
            if(puntuaciones.get(s) < d){
                puntuaciones.replace(s, d);
            }
        }

        else{
            if(d > 1300.0){
                puntuaciones.put(s, d);
                mejoresJugadores.add(s);
            }
        }
    }

    public List<String> obtenerMejoresJugadores(){
        List<String> listaOrdenada;

        if(!mejoresJugadores.isEmpty()) {
            listaOrdenada = new ArrayList<>(mejoresJugadores);
            listaOrdenada.sort((j1, j2) -> Double.compare(puntuaciones.get(j1), puntuaciones.get(j2)));

            List<String> listaTopDefinitiva = new ArrayList<>();
            int i = 1;
            for (String s : listaOrdenada) {
                listaTopDefinitiva.add(String.format("RANGO %d : \t\t %s \t --- \t Puntuacion: %.2f\n", i, s, puntuaciones.get(s)));
                i ++;
            }

            return listaTopDefinitiva;
        }

        return listaOrdenada = new ArrayList<String>();
    }

    public void guardarTabla(){
        Serializador.guardarTablaDePuntuacion(this);
    }
}
