# Taller 8: Relaciones entre Clases en Java

Arena de héroes por consola donde se ven las cuatro relaciones del diagrama UML: asociación unidireccional, asociación bidireccional, agregación y composición.

## Estructura

Cada subpaquete del modelo agrupa una parte del diagrama:

```
src/co/edu/puj/taller8/
├── modelo/
│   ├── personaje/
│   │   ├── Personaje.java      tiene un Arma (0..1), un Gremio (0..1) y un CorazonRPG (1..1)
│   │   └── CorazonRPG.java     composición: su constructor no es public, solo Personaje lo crea
│   ├── equipo/
│   │   └── Arma.java           asociación unidireccional: no sabe quién la tiene
│   └── grupo/
│       ├── Gremio.java         asociación bidireccional con Personaje (lista de miembros)
│       └── Escuadron.java      agregación: guarda personajes que ya existen afuera
└── principal/
    └── AplicacionArenaRPG.java menú do-while + switch y pruebas de las relaciones
```

`CorazonRPG` está en el mismo paquete que `Personaje` a propósito: así su constructor puede ser package-private y nadie fuera de `modelo.personaje` puede hacer `new CorazonRPG(...)`.

## Cómo ejecutar

Desde esta carpeta, en PowerShell:

```powershell
javac -encoding UTF-8 -d out src/co/edu/puj/taller8/modelo/personaje/*.java src/co/edu/puj/taller8/modelo/equipo/*.java src/co/edu/puj/taller8/modelo/grupo/*.java src/co/edu/puj/taller8/principal/*.java
java -cp out co.edu.puj.taller8.principal.AplicacionArenaRPG
```

En IntelliJ: marcar `src` como *Sources Root* y ejecutar `co.edu.puj.taller8.principal.AplicacionArenaRPG`.

Al empezar pregunta si se quieren correr las pruebas de las relaciones (`s`). Muestran las cuatro relaciones funcionando sin pasar por el menú.

## Menú

| Opción | Qué hace | Relación que prueba |
|---|---|---|
| 1 | Crear personaje (el corazón se crea solo) | composición |
| 2 | Crear gremio | |
| 3 | Reclutar personaje en un escuadrón (nuevo o existente) | agregación |
| 4 | Equipar arma | asociación unidireccional |
| 5 | Mostrar estado: tabla de personajes, detalle, escuadrones y gremios | |
| 6 | Unir personaje a gremio y mostrar los dos lados | asociación bidireccional |
| 7 | Desequipar arma | asociación unidireccional |
| 8 | Expulsar un personaje o disolver un escuadrón | agregación |
| 9 | Salir | |

Las opciones 1 a 5 son las de la plantilla. Agregué la 6, la 7 y la 8 para poder probar desde el menú la bidireccional, el desequipar y la agregación.

## Organización del código

Cada opción del menú es un método de `AplicacionArenaRPG` (`crearPersonaje`, `crearGremio`, `reclutarEnEscuadron`, `equiparArma`, `mostrarEstadoDetallado`, `unirAGremio`, `desequiparArma`, `gestionarEscuadron`), y `main` solo hace `new AplicacionArenaRPG().ejecutar()`. Las opciones del menú, los valores por defecto y las fórmulas del corazón y del ataque (`ATAQUE_POR_NIVEL`, `SALUD_POR_NIVEL`, etc.) están en constantes `static final`.

## Errores explicados

En [explicacion_depuracion.md](explicacion_depuracion.md) está la sección 5:

- **`StackOverflowError`** por los `toString()` cruzados de `Personaje` y `Gremio`. La versión mala quedó comentada en `Gremio.java`.
- **`NullPointerException`** al usar el arma cuando el personaje está desarmado: se revisa `!= null` antes de usarla.
- **Desincronización bidireccional:** el personaje dice que está en un gremio que no lo tiene en la lista. Se sincronizan los dos lados.
- **`ConcurrentModificationException`** al borrar de la lista dentro de un `for-each`: `disolver()` usa un `Iterator`.
