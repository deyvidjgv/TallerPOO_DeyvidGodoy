# Talleres de Programación Orientada a Objetos en Java

**Autor:** Deyvid Godoy

Repositorio con los talleres de POO. Cada taller está en su propia carpeta, con su código, su README y el documento donde explico los errores de la sección de depuración.

| Taller | Tema | Carpeta | Clase para ejecutar | Explicación de errores |
|---|---|---|---|---|
| 5 | Clases y objetos | raíz (`src/main/java`) | `com.taller.poo.Principal` y `com.taller.poo.SistemaGestionCuentas` | [explicacion_debugging.md](explicacion_debugging.md) |
| 6 | Encapsulamiento y modificadores de acceso | [Taller6_Encapsulamiento](Taller6_Encapsulamiento/README.md) | `co.edu.puj.rpg.principal.Main` | [explicacion_encapsulamiento.md](Taller6_Encapsulamiento/explicacion_encapsulamiento.md) |
| 7 | Arena RPG: herencia, interfaces, colecciones y genéricos | [Taller7_ArenaRPG](Taller7_ArenaRPG/README.md) | `co.edu.puj.taller7.principal.Main` | [explicacion_depuracion.md](Taller7_ArenaRPG/explicacion_depuracion.md) |
| 8 | Relaciones entre clases | [Taller8_RelacionesClases](Taller8_RelacionesClases/README.md) | `co.edu.puj.taller8.principal.AplicacionArenaRPG` | [explicacion_depuracion.md](Taller8_RelacionesClases/explicacion_depuracion.md) |

## Requisitos

- JDK 17 o superior (lo probé con JDK 25).
- IntelliJ IDEA (Community o Ultimate).

Los PDF de los enunciados no se suben al repositorio (están en el `.gitignore`), igual que la carpeta `.idea/` y las carpetas de compilación (`target/`, `out/`).

## Cómo ejecutar

**En IntelliJ:** abrir la carpeta del repositorio. El Taller 5 es el proyecto Maven de la raíz. Para los talleres 6, 7 y 8 se hace clic derecho en la carpeta `src` del taller > *Mark Directory as* > *Sources Root*, y luego se ejecuta la clase con el `main` que dice la tabla.

**Desde la terminal (PowerShell), Taller 5:**

```powershell
javac -encoding UTF-8 -d out src/main/java/com/taller/poo/*.java src/main/java/com/taller/poo/depuracion/*.java
java -cp out com.taller.poo.Principal
java -cp out com.taller.poo.SistemaGestionCuentas
```

Los comandos de los otros talleres están en el README de cada carpeta.

## Taller 5: Clases y objetos

### Estructura

```
src/main/java/com/taller/poo/
├── Vehiculo.java               atributos privados, constructor por defecto y parametrizado, this
├── Estudiante.java             obtenerReporteAcademicos() arma el reporte con StringBuilder y lo retorna
├── CuentaBancaria.java         tres constructores sobrecargados, depositar() y retirar() con if-else
├── ConversorMoneda.java        clase utilitaria con métodos estáticos y constructor privado
├── Principal.java              crea 3 objetos de cada clase, cambia su estado e imprime con printf
├── SistemaGestionCuentas.java  reto integrador: menú do-while + switch sobre un arreglo de 3 cuentas
└── depuracion/                 los 4 casos de estudio de la sección 6, ya corregidos
```

### Qué hace cada programa

- **`Principal`**: crea los vehículos, estudiantes y cuentas, hace depósitos y retiros (incluido uno negativo y uno sin saldo para ver la validación), usa `ConversorMoneda` sin crear objetos e imprime los tres reportes con `printf`.
- **`SistemaGestionCuentas`**: menú por consola para depositar, retirar, ver el reporte consolidado y salir. La búsqueda de la cuenta está en un solo método, `buscarCuenta()`, que usan el depósito y el retiro. Si se escribe una letra en lugar de un número, el programa no se cae: muestra el error y vuelve al menú.

### Errores explicados

En [explicacion_debugging.md](explicacion_debugging.md) están los 4 casos de la sección 6, con el error que sale, la causa y la corrección:

1. `PruebaEstudiante`: variable sin instanciar (el compilador la frena; con `null` sale `NullPointerException`).
2. `GestorVehiculos`: `non-static method encenderMotor() cannot be referenced from a static context`, por llamar un método de instancia desde el `main` sin crear el objeto.
3. `CuentaBancaria`: el "constructor" con `void` deja de ser constructor y pasa a ser un método normal.
4. `Vehiculo`: sombreado de variables (*variable shadowing*). Sin `this` los atributos se quedan en `0` y `false`.

Las preguntas de análisis sobre primitivos y wrappers están respondidas en los comentarios de `CuentaBancaria.java` y `Vehiculo.java`.
