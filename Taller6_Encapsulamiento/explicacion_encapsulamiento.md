# Sección 5: Depuración de Encapsulamiento

Dejé el proyecto con la estructura exacta de la guía (`Personaje`, `enemigo/Enemigo` y `principal/Main`), así que los casos no van en clases aparte: el caso A quedó comentado en `Main` (si lo dejo activo no compila nada), el caso B está comentado tal cual en `Personaje` y debajo el segundo constructor ya corregido, y el caso C es el método `ejecutarAccion` de `Main`.

Para ver las pruebas, al correr `Main` pregunta si quiere ejecutar primero las pruebas de límites, se responde `s`.

## Caso A: Acceso directo no autorizado

**Error del compilador:**
- En IntelliJ: `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'`
- Con javac: `error: puntosVida has private access in Personaje` (sale dos veces, una por la asignación y otra por el `println`)

**Causa:** `puntosVida` es `private`, solo lo puede usar el código que está dentro de la clase `Personaje`. `Main` está en otro paquete (`co.edu.puj.rpg.principal`) y no lo puede leer ni cambiar directo. Si se pudiera, cualquiera le pondría 200 de vida aunque la máxima sea 100.

**Corrección:** usar la API pública:
```java
heroe.setPuntosVida(200);
System.out.println("Vida: " + heroe.getPuntosVida()); // imprime 100, el setter no deja pasar la vida maxima
```

También probé `heroe.setNivel(5)` desde `Main` y sale `'setNivel(int)' has protected access in 'co.edu.puj.rpg.modelo.Personaje'`, porque `setNivel` es `protected` y `Main` no está en el mismo paquete ni es subclase.

## Caso B: Constructor que se salta las validaciones

**¿Qué pasa?** Este compila sin errores, el problema se ve al ejecutar: el personaje queda con `puntosVida = -50` y `estaVivo = true`, o sea vivo con vida negativa. Además ignora el parámetro que le mandan.

**Causa:** el constructor le asigna el valor directo al atributo (`this.puntosVida = -50`) y no pasa por `setPuntosVida`, entonces nunca se revisa la regla de "0 o menos = derrotado". Que el atributo sea `private` no sirve de nada si la misma clase le mete datos malos.

**Corrección:** el segundo constructor de `Personaje` usa el setter:
```java
public Personaje(int puntosVida) {
    ...
    this.setPuntosVida(puntosVida);
}
```
Con `new Personaje(-50)` queda `vida: 0, esta vivo: false`. En el constructor completo la vida también se manda por el setter y el oro se revisa con un `if` para que no empiece negativo.

## Caso C: Fuga de estado y asignación fuera de límites

**¿Qué pasa?** Si el setter solo hace `this.puntosVida = vida`, con `setPuntosVida(999999)` queda con más vida que la máxima, y con `setPuntosVida(-100)` queda con vida negativa pero sigue `estaVivo = true`.

**Causa:** el setter no valida nada, entonces es lo mismo que tener el atributo público.

**Corrección:** `setPuntosVida` revisa los dos límites:
- si la vida es 0 o menos queda en 0 y `estaVivo` pasa a `false`
- si es mayor a `vidaMaxima` queda en `vidaMaxima`
- en otro caso se asigna y `estaVivo` queda en `true`

Resultado de `ejecutarAccion`:
```
setPuntosVida(999999) -> vida: 100/100, esta vivo: true
setPuntosVida(-100)   -> vida: 0/100, esta vivo: false
```

## 6.2 Verificación

1. Al escribir `heroe.` en `Main` el autocompletado solo muestra métodos `public`: los getters, `setPuntosVida`, `setOro`, `atacar`, `usarPocion`, `recibirDano`, `ganarExperiencia`, `agregarPocionGrande` (y los de `Object` como `toString`). No sale ningún atributo y tampoco `setNivel` porque es `protected`. Con `enemigo.` tampoco sale `setPuntosVida`, porque en `Enemigo` está sin modificador (package-private) y solo lo ven las clases del paquete `enemigo`.
2. El acceso directo a un atributo privado da el error del caso A.
3. Pruebas con los valores límite (salida real del programa):
```
setPuntosVida(-10)     -> vida: 0, esta vivo: false
setPuntosVida(0)       -> vida: 0, esta vivo: false
setPuntosVida(100000)  -> vida: 100, esta vivo: true
setOro(-10)            -> oro: 50,00   (sale "saldo insuficiente" y no cambia)
setOro(0)              -> oro: 0,00
setOro(100000)         -> oro: 100000,00
```
`setNivel` no se puede llamar desde `Main` por ser `protected`, así que se prueba con `ganarExperiencia`: con `-10` no cambia nada (nivel 1) y con `100` sube a nivel 2. Dentro de `setNivel`, si llega un valor menor a 1 el nivel se queda como estaba.
