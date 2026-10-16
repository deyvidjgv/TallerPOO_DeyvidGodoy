# Sección 6: Análisis y Depuración de Código

Los cuatro fragmentos están en el paquete `com.taller.poo.depuracion` ya corregidos, con un comentario `// correccion:` donde estaba la falla. Los puse en un subpaquete porque `CuentaBancaria` y `Vehiculo` ya existen en `com.taller.poo` y con el mismo nombre en el mismo paquete el proyecto no compila.

## Caso 1: PruebaEstudiante (NullPointerException)

**1. ¿Qué error sale?**
No alcanza a llegar a una `NullPointerException`, el compilador lo frena antes:
`error: variable estudiante1 might not have been initialized`.
Como `estudiante1` es una variable local, Java no le pone `null` por defecto y no deja usarla sin haberle asignado algo. Si se le asigna `null` a propósito (`Estudiante estudiante1 = null;`) ahí sí compila y en ejecución salta la `NullPointerException`.

**2. ¿Por qué no puede llamar a `obtenerReporteAcademicos()`?**
Porque la variable solo está declarada, no apunta a ningún objeto. Un método de instancia necesita un objeto que tenga los datos (nombre, promedio, matrícula) y aquí ese objeto nunca se creó.

**3. Instrucción para corregirlo:**
```java
Estudiante estudiante1 = new Estudiante("1090123456", "Laura Martinez", 4.35, true);
```

## Caso 2: GestorVehiculos (instancia desde contexto estático)

**1. Mensaje del compilador:**
`error: non-static method encenderMotor() cannot be referenced from a static context`

**2. ¿Por qué pasa?**
`main` es `static`, pertenece a la clase y se ejecuta sin que exista ningún objeto. `encenderMotor()` es de instancia, o sea que es de un objeto en particular. Desde `main` no hay un `this` sobre el cual llamarlo, entonces Java no sabe de qué objeto se está hablando.

**3. Las dos formas de corregirlo:**
- Crear un objeto y llamar el método desde él: `GestorVehiculos gestor = new GestorVehiculos(); gestor.encenderMotor();` (es la que dejé en el código).
- Volver el método estático: `public static void encenderMotor()`, así se puede llamar directo desde `main`.

## Caso 3: CuentaBancaria (firma incorrecta del constructor)

**1. ¿Por qué el `void` lo daña?**
Un constructor no lleva tipo de retorno. Al ponerle `void` Java lo toma como un método normal que casualmente se llama igual que la clase, ya no como un constructor.

**2. ¿Por qué no encuentra el constructor?**
Como la clase quedó sin ningún constructor declarado, Java le pone el constructor por defecto sin parámetros. Al hacer `new CuentaBancaria("A-101", 500.0)` busca uno que reciba `String, double` y no existe:
`error: constructor CuentaBancaria in class CuentaBancaria cannot be applied to given types; required: no arguments, found: String,double`

**3. Constructor correcto:**
```java
public CuentaBancaria(String numeroCuenta, double saldoInicial) {
    this.numeroCuenta = numeroCuenta;
    this.saldo = saldoInicial;
}
```

## Caso 4: Vehiculo (variable shadowing)

**1. ¿Qué imprime `new Vehiculo(200, true)` y luego `imprimirEstado()`?**
`Velocidad Máxima: 0 | Encendido: false`. Lo ejecuté y eso fue lo que salió. Los atributos nunca se asignaron, se quedaron con sus valores por defecto.

**2. ¿Qué es el shadowing?**
Cuando el parámetro se llama igual que el atributo, dentro del constructor el nombre `velocidadMaxima` se refiere al parámetro, que "tapa" al atributo. Entonces `velocidadMaxima = velocidadMaxima;` asigna el parámetro a sí mismo y el atributo ni se toca. Con el debugger se ve que `this.velocidadMaxima` sigue en 0 después de esa línea.

**3. Constructor corregido:**
```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    this.velocidadMaxima = velocidadMaxima;
    this.encendido = encendido;
}
```
