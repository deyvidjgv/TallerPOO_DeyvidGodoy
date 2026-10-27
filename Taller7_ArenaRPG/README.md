# Taller 7: Arena RPG con POO Avanzada, Colecciones y Genéricos

Batalla por turnos en un tablero de 5x5. El escuadrón (Thorin el guerrero y Gandalf el mago) gana si derrota a los tres enemigos o si Thorin llega al cofre `[C]` del centro. Pierde si caen todos los héroes.

## Estructura

```
src/co/edu/puj/taller7/
├── modelo/
│   ├── ICombatiente.java    interfaz con defender(int danoRecibido)
│   ├── Personaje.java       clase abstracta: atributos protected, realizarAtaqueEspecial abstracto, sobrecarga de atacar
│   ├── Guerrero.java        extends Personaje, fuerzaEscudo, golpe de furia
│   ├── Mago.java            extends Personaje, puntosMana, bola de fuego y escudo mágico
│   ├── Enemigo.java         extends Personaje, armadura y zarpazo
│   └── items/
│       ├── Inventario.java  clase genérica Inventario<T>
│       └── Pocion.java
└── principal/
    └── Main.java            tablero char[][], ciclo de turnos y menú de cada héroe
```

## Cómo ejecutar

Desde esta carpeta, en PowerShell:

```powershell
javac -encoding UTF-8 -d out src/co/edu/puj/taller7/modelo/*.java src/co/edu/puj/taller7/modelo/items/*.java src/co/edu/puj/taller7/principal/*.java
java -cp out co.edu.puj.taller7.principal.Main
```

En IntelliJ: marcar `src` como *Sources Root* y ejecutar `co.edu.puj.taller7.principal.Main`.

## Cómo se juega

En cada turno se imprime el tablero (`G` guerrero, `M` mago, `E` enemigo, `C` cofre, `.` vacío) y una tabla con la vida, el maná o el escudo y la posición de cada uno. Cada héroe vivo elige:

1. **Atacar:** usa su ataque especial contra el enemigo que se elija.
2. **Usar inventario:** se toma una poción (se gasta y sale del inventario).
3. **Mover:** `w` arriba, `s` abajo, `a` izquierda, `d` derecha. No deja salirse del tablero ni pisar una casilla ocupada.

Después atacan los enemigos al héroe con menos vida (el 30 % de las veces usan su zarpazo) y al final del turno se quitan de las listas los que murieron.

## Organización del código

`Main` está dividido en métodos, uno por cada paso del ciclo: `inicializar`, `turnoDelJugador`, `atacarEnemigo`, `usarPocion`, `moverHeroe`, `turnoDeLosEnemigos`, `recuperarManaDeLosMagos` y `mostrarResultadoFinal`. El estado de la partida (tablero, listas, inventario) son atributos de la clase, y `main` solo hace `new Main().jugar()`.

El tamaño del tablero, la posición del cofre, las opciones del menú, las teclas, la curación de las pociones y las probabilidades están en constantes `static final`. Lo mismo pasa con los multiplicadores y bonos de daño de cada clase del modelo.

## Errores explicados

En [explicacion_depuracion.md](explicacion_depuracion.md) está la sección 5:

- `IndexOutOfBoundsException` al elegir un enemigo o una poción que no existe: se valida el índice antes del `get`.
- `ArrayIndexOutOfBoundsException` al salirse del tablero: se revisan los límites antes de mover.
- `ClassCastException` al castear un `Guerrero` a `Mago`: se usa `instanceof` antes del cast. El ejemplo quedó comentado en `Main`.
- Error de compilación `Personaje is abstract; cannot be instantiated`, también comentado en `Main`.
- Celdas vacías del tablero (`'\0'`) y objetivo `null` cuando ya no quedan héroes: `actualizarTablero` llena toda la matriz antes de dibujar y el turno enemigo revisa el `null`.
- La guía de depuración en IntelliJ que seguí (breakpoints, F7/F8, Variables y Evaluate Expression).
