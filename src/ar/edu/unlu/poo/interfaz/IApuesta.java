package ar.edu.unlu.poo.interfaz;

public interface IApuesta {
    double getMontoApostado();

    boolean estaAsegurado();

    double getSeguroApostado();

    void asegurarse();

    boolean gananciasCalculadas();

    double getGanancias();

    String descripcion();
}
