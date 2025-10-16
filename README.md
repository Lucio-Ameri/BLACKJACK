IDEA DEL JUEGO:    

La clase CASINO funciona a modo de Modelo principal/Lobby del juego. Quise simular un Lobby comun de cualquier juego, donde tengo la opcion de salir del juego, ver rankings, unirme a partida, etc. Aqui tengo metodos como 
{ Eventos unirmeAlCasino(Jugador j, Observador o); Eventos irmeDelCasino(Jugador j, Observador o); Eventos unirmeALaListaDeEspera(Jugador j, double monto, Observador o); }
Todos estos requieren recibir Jugador de momento, pero ya entra en conflicto con el CONTROLADOR, ya que este posee una interfaz de jugador. Lo unico que se me ocurre para poder conectarlo evitando romper el MVC es en algun lado hacer el 
instanceOf Jugador (Lo cual tambien esta mal... No se me ocurre otra forma, tal vez cambiando todo el modelo para que tome de alguna forma IJugador, pero terminaria revelando metodos de Jugador que no deberia saber el controlador). 
                    
La clase MESA vendria a ser donde se lleva a cabo la partida. Me pasa lo mismo que en la clase CASINO.

Despues en el controlador, hago cosas que tal vez no deberia hacer de momento, ya que las voy a cambiar en el transcurso de la semana, como enviar mensajes de error a la vista, que este muy acoplada a la unica vista que hice, etc. 


Pero bueno, el problema que quiero resolver es este, de la clase CASINO, MESA y JUGADOR con respecto al controlador, por como lo desarrolle.
