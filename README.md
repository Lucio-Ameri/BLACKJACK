IDEA DEL JUEGO:

La clase CASINO funciona a modo de Modelo principal/Lobby del juego. Quise simular un Lobby comun de cualquier juego, donde tengo la opcion de salir del juego, ver rankings, unirme a partida, etc. Aqui tengo metodos como { Eventos unirmeAlCasino(Jugador j, Observador o); Eventos irmeDelCasino(Jugador j, Observador o); Eventos unirmeALaListaDeEspera(Jugador j, double monto, Observador o); } Todos estos requieren recibir Jugador de momento, pero ya entra en conflicto con el CONTROLADOR, ya que este posee una interfaz de jugador. Lo unico que se me ocurre para poder conectarlo evitando romper el MVC es en algun lado hacer el instanceOf Jugador (Lo cual tambien esta mal... No se me ocurre otra forma, tal vez cambiando todo el modelo para que tome de alguna forma IJugador, pero terminaria revelando metodos de Jugador que no deberia saber el controlador).

La clase MESA vendria a ser donde se lleva a cabo la partida. Me pasa lo mismo que en la clase CASINO.

Despues en el controlador, hago cosas que tal vez no deberia hacer de momento, ya que las voy a cambiar en el transcurso de la semana, como enviar mensajes de error a la vista, que este muy acoplada a la unica vista que hice, etc.

Pero bueno, el problema que quiero resolver es este, de la clase CASINO, MESA y JUGADOR con respecto al controlador, por como lo desarrolle.



CORRECCIONES QUE DEBO HACER:

1- Yo creo el Jugador cuando creo el controlador, lo cual esta mal. La parte de crear el jugador la voy a hacer en casino. La vista y Controlador van a pasar un nombre al Casino, y este creara el jugador y devolvera un IJugador.

2- El controlador ahora hara toda la comunicacion pasando el nombre del jugador a Casino para realizar acciones, y lo mismo a Mesa. Casino y Mesa tendran que reformular sus metodos para encontrar al jugador con el nombre indicado.

3- La persistencia de los nombres pasara a estar Tambien en Casino. Esto hara que todo sobre la persistencia este en el modelo, ya que todo esto lo hago tanto en Jugador (persistir el jugador/eliminar nombres usados), Mesa (persistir ranking), Casino ahora (persistir nombres usados.)... Preguntar si esta forma es correcta o tendre que hacer otra clase que se encargue de unir toda la persistencia, utilizandola como tributo de casino, mesa y jugador para poder realizar esto.

4- Desacoplar el controlador enviando los eventos que recibe del modelo, directamente a la vista. La vista Debera tener variables o un metodo especifico que analice cada evento y imprima el debido mensaje.
