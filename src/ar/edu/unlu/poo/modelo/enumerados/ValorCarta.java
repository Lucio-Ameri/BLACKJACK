package ar.edu.unlu.poo.modelo.enumerados;

public enum ValorCarta {
    DOS(2, "2"),
    TRES(3, "3"),
    CUATRO(4, "4"),
    CINCO(5, "5"),
    SEIS(6, "6"),
    SIETE(7, "7"),
    OCHO(8, "8"),
    NUEVE(9, "9"),
    DIEZ(10, "10"),
    J(10, "J"),
    Q(10, "Q"),
    K(10, "K"),
    AS(11, "A"),
    OCULTO(0, "?");


    private final int valor;
    private final String simbolo;

    ValorCarta(int valor, String simbolo){
        this.simbolo = simbolo;
        this.valor = valor;
    }


    //metodo: funcion que devuelve el valor numerico de la carta;
    public int getValorNumerico(){
        return valor;
    }


    //metodo: funcion que devuelve el simbolo numerico de la carta;
    public String getSimboloValor(){
        return simbolo;
    }


    //metodo; funcion que verifica si este valor corresponde al TIPO AS;
    public boolean tipoAS(){
        return (this == AS);
    }
}
