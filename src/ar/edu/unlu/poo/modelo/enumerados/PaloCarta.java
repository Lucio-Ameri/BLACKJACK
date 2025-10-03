package ar.edu.unlu.poo.modelo.enumerados;

public enum PaloCarta {
    DIAMANTES("♦"),
    PICAS("♠"),
    TREBOLES("♣"),
    CORAZONES("♥"),
    OCULTO("?");

    private final String icono;

    PaloCarta(String icono){
        this.icono = icono;
    }

    //metodo: funcion que devuelve el icono del palo de la carta;
    public String getPaloIcono(){
        return icono;
    }
}
