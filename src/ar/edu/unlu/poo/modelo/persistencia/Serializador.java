package ar.edu.unlu.poo.modelo.persistencia;

import ar.edu.unlu.poo.modelo.Jugador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serializador {
    private static final String ARCHIVO_TABLA_DE_PUNTAJES = "tablaDePuntajes.dat";
    private static final String ARCHIVO_NOMBRES_YA_UTILIZADOS = "listaNombresUsados.dat";
    private static final String ARCHIVO_JUGADORES_SERIALIZADOS = "jugadoresGuardados.dat";


    //----------------------------------- metodos genericos -----------------------------------
    public static <T> void guardarObjeto(T objeto, String ruta){
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(objeto);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando en " + ruta, e);
        }
    }

    private static <T> T cargarObjeto(String ruta, T valorPorDefecto) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
                return (T) entrada.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error cargando desde " + ruta, e);
            }
        }
        return valorPorDefecto;
    }



    // -------------------------------- metodos especificos ---------------------------------
    public static void guardarTablaDePuntuacion(TablaPuntuacion tabla) {
        guardarObjeto(tabla, ARCHIVO_TABLA_DE_PUNTAJES);
    }

    public static TablaPuntuacion cargarTablaDePuntuacion() {
        return cargarObjeto(ARCHIVO_TABLA_DE_PUNTAJES, new TablaPuntuacion());
    }

    public static void guardarListaNombresUsados(List<String> nombres) {
        guardarObjeto(nombres, ARCHIVO_NOMBRES_YA_UTILIZADOS);
    }

    public static List<String> cargarListaNombresUsados() {
        return cargarObjeto(ARCHIVO_NOMBRES_YA_UTILIZADOS, new ArrayList<>());
    }

    public static void guardarJugadores(List<Jugador> jugadores) {
        guardarObjeto(jugadores, ARCHIVO_JUGADORES_SERIALIZADOS);
    }

    public static List<Jugador> cargarJugadoresGuardados() {
        return cargarObjeto(ARCHIVO_JUGADORES_SERIALIZADOS, new ArrayList<>());
    }

    public static void eliminarJugadorGuardado(Jugador jugador) {
        List<Jugador> jugadores = cargarJugadoresGuardados();
        jugadores.removeIf(j -> j.getNombre().equals(jugador.getNombre()));
        guardarJugadores(jugadores);
    }
}
