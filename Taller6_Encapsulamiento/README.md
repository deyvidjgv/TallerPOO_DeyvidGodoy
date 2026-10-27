# Taller 6: Encapsulamiento y Modificadores de Acceso

Simulador de batalla por consola ("Arena de Héroes"). El héroe pelea contra tres enemigos seguidos (Goblin, Orco y Troll). Todo el estado del personaje está encapsulado: solo se lee con getters y solo se cambia con setters que validan las reglas.

## Estructura

Es la estructura de paquetes que pide la guía:

```
src/co/edu/puj/rpg/
├── modelo/
│   ├── Personaje.java       atributos private, dos constructores con this, setters validados
│   └── enemigo/
│       └── Enemigo.java     mismos atributos private, setPuntosVida package-private
└── principal/
    └── Main.java            menú de combate do-while + switch, tienda, historial y pruebas de límites
```

## Cómo ejecutar

Desde esta carpeta, en PowerShell:

```powershell
javac -encoding UTF-8 -d out src/co/edu/puj/rpg/modelo/*.java src/co/edu/puj/rpg/modelo/enemigo/*.java src/co/edu/puj/rpg/principal/*.java
java -cp out co.edu.puj.rpg.principal.Main
```

En IntelliJ: marcar `src` como *Sources Root* y ejecutar `co.edu.puj.rpg.principal.Main`.

Al empezar pregunta si se quieren correr las pruebas de límites del punto 6.2 (`s`/`n`) y el nombre del héroe (si se deja vacío queda `Aragorn`).

## Qué incluye

| Requisito de la guía | Dónde quedó |
|---|---|
| Atributos `private` (`nombre`, `puntosVida`, `vidaMaxima`, `puntosAtaque`, `nivel`, `oro`, `estaVivo`) | `Personaje` y `Enemigo` |
| Constructor completo con `this` | `Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro)` |
| `setPuntosVida`: 0 o menos queda en 0 y muerto; no pasa de `vidaMaxima` | `Personaje.setPuntosVida` |
| `setOro`: rechaza saldo negativo sin cambiar el atributo | `Personaje.setOro` |
| `setNivel`: no acepta menos de 1; es `protected` | `Personaje.setNivel` |
| Getters `public` | `Personaje` |
| Menú `do-while` + `switch` (Atacar, Usar Poción, Ver Estado, Huir) y validación `if-else` | `Main.combatir` |
| `printf` con `%s`, `%d` y `%.2f` | estado del combate y del héroe |
| Historial con `StringBuilder` que se imprime al final de cada ronda | `Main.registrar` e `imprimirHistorial` |
| Reto 1: tienda de Pociones Grandes que pasa por `setOro()` | opción 5, `Main.abrirTienda` |
| Reto 2: experiencia con subida de nivel y golpe crítico del 15 % con `Math.random()` | `Personaje.ganarExperiencia` y `Personaje.atacar` |

Los valores del juego (vida y ataque del héroe, precio y curación de la poción, opciones del menú, probabilidad del crítico, etc.) están en constantes `static final` al inicio de cada clase.

## Errores explicados

En [explicacion_encapsulamiento.md](explicacion_encapsulamiento.md) está la sección 5 de depuración:

- **Caso A:** acceso directo a un atributo privado. Error del compilador: `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'`. Quedó comentado en `Main`.
- **Caso B:** constructor que se salta las validaciones y deja al personaje vivo con vida negativa. Quedó comentado en `Personaje`, con la versión corregida debajo.
- **Caso C:** setter sin validar, que deja pasar 999999 o -100. Es el método `ejecutarAccion` de `Main`.
- **6.2:** pruebas con los valores límite -10, 0 y 100000 en los setters.
