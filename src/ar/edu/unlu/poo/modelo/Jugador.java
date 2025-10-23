package ar.edu.unlu.poo.modelo;

import ar.edu.unlu.poo.interfaz.IJugador;
import ar.edu.unlu.poo.interfaz.IManoJugador;
import ar.edu.unlu.poo.modelo.eventos.Eventos;
import ar.edu.unlu.poo.modelo.persistencia.Serializador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jugador implements IJugador, Serializable {
    private List<ManoJugador> manos;
    private Dinero saldo;
    private String nombre;
    private double maximoHistorico;

    public Jugador(String nombre, double montoInicial){
        this.manos = new ArrayList<ManoJugador>();
        this.saldo = new Dinero(montoInicial);
        this.nombre = nombre;
        this.maximoHistorico = montoInicial;
    }

    @Override
    public String getNombre(){
        return nombre;
    }

    public List<ManoJugador> getManos(){
        return manos;
    }

    @Override
    public List<IManoJugador> getManosJugadorInterfaz(){
        return new ArrayList<IManoJugador>(manos);
    }

    @Override
    public double getSaldoJugador(){
        return saldo.getMonto();
    }

    @Override
    public double getMaximoHistorico(){
        return maximoHistorico;
    }

    public void actualizarMaximoHistorico(){
        double monto = getSaldoJugador();
        if(maximoHistorico < monto){
            maximoHistorico = monto;
        }
    }

    public boolean transferenciaRealizable(double cantidad){
        return saldo.puedoTransferir(cantidad);
    }

    public void actualizarSaldo(double monto){
        saldo.actualizarMonto(monto);
    }

    @Override
    public boolean perdio(){
        return !saldo.tengoDinero();
    }

    public void agregarMano(ManoJugador m){
        manos.add(m);
    }

    public void agregarManoEnPosicion(int index, ManoJugador m){
        manos.add(index, m);
    }

    public void removerMano(ManoJugador m){
        manos.remove(m);
    }

    public void limpiarManos(){
        manos.clear();
    }

    @Override
    public Eventos guardarJugador(){
        if(saldo.tengoDinero()){
            List<Jugador> jugadores = Serializador.cargarJugadoresGuardados();

            boolean agregar = true;

            for(Jugador j: jugadores){
                if(nombre.equals(j.getNombre())){
                    agregar = false;
                    break;
                }
            }

            if(agregar){
                jugadores.add(this);
                Serializador.guardarJugadores(jugadores);
                return Eventos.JUGADOR_GUARDADO;
            }

            return Eventos.JUGADOR_YA_GUARDADO;
        }

        List<String> nombresUsados = Serializador.cargarListaNombresUsados();
        if(nombresUsados.contains(getNombre())){
            nombresUsados.remove(getNombre());
        }

        return Eventos.NO_POSEE_DINERO_PARA_GUARDAR;
    }

    @Override
    public String datosPrincipales(){
        return String.format("JUGADOR %s  -  SALDO: %s  -  MAXIMO HISTORICO: $%.2f\n\n", nombre, saldo.descripcion(), maximoHistorico);
    }

    @Override
    public String descripcion(){
        String s = String.format("JUGADOR %s  -  SALDO: %s  -  MAXIMO HISTORICO: $%.2f\n\n", nombre, saldo.descripcion(), maximoHistorico);

        int i = 1;
        for(ManoJugador m: manos){
            s += String.format("MANO %d: %s\n", i, m.descripcion());
            i++;
        }

        return s;
    }
}
