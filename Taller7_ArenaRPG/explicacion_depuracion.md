# Taller 7: Arena RPG con POO Avanzada, Colecciones y Genéricos

Se corre `co.edu.puj.taller7.principal.Main`. La batalla empieza con el tablero de la guía: Thorin `[G]` en `[0][0]`, Gandalf `[M]` en `[1][1]`, el cofre `[C]` en `[2][2]` y tres enemigos `[E]` en las esquinas.

## Cómo quedó organizado

| Parte de la guía | Dónde quedó |
|---|---|
| Interfaz `ICombatiente` con `defender(int danoRecibido)` | `modelo/ICombatiente.java` |
| Clase abstracta `Personaje` con atributos `protected`, `realizarAtaqueEspecial` abstracto, `estaVivo()`, `recibirDano()` y getters | `modelo/Personaje.java` |
| `Guerrero` (`fuerzaEscudo`) y `Mago` (`puntosMana`) con `extends`, `super(...)` y `@Override` | `modelo/Guerrero.java`, `modelo/Mago.java` |
| Sobrecarga `atacar(Personaje objetivo)` y `atacar(Personaje objetivo, boolean modoFuria)` | `Personaje`, el Guerrero usa la de modo furia en su ataque especial |
| `List<Personaje> escuadron = new ArrayList<>()` con `add`, `get`, `size`, `removeIf` y `for-each` | `Main` |
| Genérico `Inventario<T>` y `Pocion` | `modelo/items/` |
| Tablero `char[5][5]` y `mostrarTablero` con bucles anidados | `Main` (`mostrarTablero` y `actualizarTablero`) |

Agregué una clase `Enemigo` que también hereda de `Personaje`. No sale en el diagrama, pero `realizarAtaqueEspecial(Personaje objetivo)` recibe un `Personaje`. Para poder atacar al enemigo con ese método y guardarlo en un `List<Personaje>`, el enemigo tiene que ser un `Personaje`. También le agregué `vidaMaxima` a `Personaje` para que las pociones no curen más de la vida inicial.

En el tablero uso `tablero[posicionX][posicionY]`, igual que en la expresión de la guía. La x es la fila y la y es la columna.

## ¿Por qué clase abstracta y no solo interfaz?

`ICombatiente` solo dice *qué* tiene que hacer un combatiente (defenderse), sin decir cómo. Cada clase lo resuelve a su manera: el Guerrero bloquea con el escudo, el Mago gasta maná para recibir la mitad y el Enemigo usa la armadura. `Personaje` tiene que ser una clase abstracta porque todos comparten atributos (`nombre`, `vida`, posición) y métodos que ya funcionan igual para todos (`estaVivo()`, `recibirDano()`). Una interfaz no puede tener esos atributos de instancia, así que tocaría repetirlos en cada subclase.

## Reto integrador (4.1 y 4.2)

1. **Inicialización:** el mapa `char[][]`, el `escuadron`, los `enemigos` y el `Inventario<Pocion>` con 3 pociones.
2. **Ciclo `do-while`:** sigue mientras queden héroes, la suma de su vida sea mayor a 0, queden enemigos y no se haya llegado al cofre.
3. **Renderizado:** al empezar cada turno se imprime el tablero y una tabla con vida, maná o escudo y posición.
4. **Turno del jugador:** un `for-each` sobre los héroes con el menú `switch`: 1 ataca con `realizarAtaqueEspecial()`, 2 usa una poción del inventario (se saca con `remover`) y 3 mueve con w/a/s/d.
5. **Turno enemigo:** cada enemigo ataca al héroe que tenga menos vida, y el 30 % de las veces usa su ataque especial.
6. **Limpieza:** `removeIf(personaje -> !personaje.estaVivo())` en las dos listas al final del turno.

**Victoria:** `enemigos.isEmpty()` o que Thorin (el héroe principal, `escuadron.get(0)`) se pare en el cofre. Si el Mago pisa el cofre no pasa nada, solo lo abre el héroe principal.
**Derrota:** `escuadron.isEmpty()` o que la suma de la vida sea `<= 0`.

Cada golpe imprime el daño, cuánto bloqueó y la vida que queda. Después de cada ataque o movimiento se vuelve a dibujar la matriz, así el que muere desaparece del mapa en el mismo momento.

## Sección 5: Depuración

### 5.1 Matriz de excepciones

**IndexOutOfBoundsException**
- *Dónde podía pasar:* al elegir un enemigo o una poción con un número que no existe (`enemigos.get(7)`), o al moverse fuera del tablero (`tablero[5][0]` o `tablero[-1][0]`).
- *Cómo quedó:* antes de cada `get` se revisa `indice < 0 || indice >= lista.size()`. Antes de mover se revisa `nuevaX < 0 || nuevaX >= tablero.length` y lo mismo con la columna. Si está fuera sale "no se puede salir del tablero" y el héroe no pierde el turno.
- *En IntelliJ:* breakpoint condicional en la línea del `if` de límites con la condición `nuevaX >= tablero.length`. En **Variables** se ve el valor de `nuevaX` comparado con `tablero.length`.

**ClassCastException**
- *Dónde podía pasar:* en la tabla de estadísticas toca castear a `Mago` para leer el maná. Si se castea a ciegas, `(Mago) escuadron.get(0)` compila, pero al correr sale `java.lang.ClassCastException: class co.edu.puj.taller7.modelo.Guerrero cannot be cast to class co.edu.puj.taller7.modelo.Mago`, porque el primero es Thorin. El ejemplo quedó comentado en `Main`.
- *Cómo quedó:* siempre se pregunta con `instanceof` antes de castear. El inventario se declaró `Inventario<Pocion>` y no como tipo crudo (*raw type*), así que `inventario.agregar("espada")` ni siquiera compila y al sacar una poción no hay que castear.

**Error de compilación: instanciar la abstracta**
- `Personaje p = new Personaje();` sale subrayado en rojo. javac dice `error: Personaje is abstract; cannot be instantiated`. También quedó comentado en `Main`.
- *Corrección:* instanciar una subclase concreta, `Personaje p = new Guerrero("Boromir", 100, 14, 3, 3, 8);`.

**NullPointerException en celdas vacías:** la matriz es de `char`, entonces no guarda `null`, pero una celda que nunca se llenó queda en `'\0'`. `actualizarTablero` llena toda la matriz con `'.'` antes de dibujar, así no queda ninguna celda vacía. `buscarObjetivo` devuelve `null` si ya no hay héroes vivos, y en el turno enemigo se revisa `if (objetivo == null)` antes de atacar.

### 5.2 Guía de depuración que seguí

1. **Breakpoints:** en `Main.java:77` (el `do {` del combate) y en la primera línea de `realizarAtaqueEspecial` de `Guerrero.java:23` y `Mago.java:30`.
2. **Modo Debug:** Shift + F9 o el escarabajo verde.
3. **F8 / F7:** con F8 se pasa el turno línea por línea. Parado en `objetivo.defender(dano)`, F7 entra a `defender()` del enemigo y se ve cómo la armadura le resta al daño.
4. **Variables:** al desplegar `escuadron` se ven el elemento `0` (Guerrero) y el `1` (Mago) con los atributos heredados `nombre`, `vida`, `posicionX`, `posicionY` y los propios (`fuerzaEscudo`, `puntosMana`). Si se pone un breakpoint justo después del `removeIf` (`Main.java:96`), se ve cómo baja el `size` de `enemigos` cuando uno muere.
5. **Evaluate Expression (Alt + F8):** probé `escuadron.get(0).estaVivo()`, `escuadron.size()` y `tablero[heroe.getPosicionX()][heroe.getPosicionY()]`, que da `'G'` o `'M'` según el héroe del turno. Uso los getters porque en `Main` no hay variables sueltas `posicionX` y `posicionY`: esas coordenadas son atributos de cada héroe.
