# Taller 8: Relaciones entre Clases en Java

Se corre `co.edu.puj.taller8.principal.AplicacionArenaRPG`. Al empezar pregunta si se quieren correr las pruebas de las relaciones; con `s` muestra las cuatro relaciones funcionando sin pasar por el menú.

## Del diagrama al código

| Relación | Multiplicidad | Cómo quedó en Java |
|---|---|---|
| `Personaje` → `Arma` (asociación unidireccional) | 0..1 | `private Arma armaEquipada;`, empieza en `null`. `Arma` no tiene ningún atributo `Personaje` |
| `Personaje` ↔ `Gremio` (bidireccional) | 0..* a 1 | `Personaje` tiene `private Gremio gremio;` y `Gremio` tiene `private List<Personaje> miembros;` |
| `Escuadron` o-- `Personaje` (agregación) | 0..* | `private List<Personaje> miembros;`. Los personajes llegan ya creados por `reclutarPersonaje(personaje)` |
| `Personaje` *-- `CorazonRPG` (composición) | 1..1 | `private CorazonRPG corazon;` se crea con `new` dentro del constructor de `Personaje` |

Las de 0..1 y 1..1 son atributos directos y las de 0..* son `List` con `ArrayList`. La diferencia entre 0..1 y 1..1 se ve en el código: `armaEquipada` puede ser `null` y por eso siempre se revisa antes de usarla, mientras que `corazon` nunca es `null` porque se crea en el constructor.

**Agregación vs. composición:**
- El `Escuadron` no hace `new Personaje`, solo guarda referencias. Tiene métodos para meter y sacar miembros, y `disolver()` los saca a todos. En la prueba 3 se disuelve el escuadrón, se le pone `null`, y Aragorn sigue con su salud y su gremio.
- `CorazonRPG` solo se crea en el constructor de `Personaje`. No hay `setCorazon()` ni `getCorazon()`. Desde afuera el corazón solo se toca por medio del personaje (`recibirDanio`, `curar`, `lanzarHechizo`, `reporteCorazon`). Si alguien escribe `gimli.setCorazon(...)`, no compila (`cannot find symbol`). Además, `CorazonRPG` está en el mismo paquete que `Personaje` (`modelo.personaje`) y su constructor no es `public`, así que desde `principal` tampoco se puede hacer `new CorazonRPG(...)`: javac responde `CorazonRPG(int,int) is not public in CorazonRPG; cannot be accessed from outside package`.

**Sincronización bidireccional:** se puede unir desde cualquiera de los dos lados, `personaje.unirseAGremio(gremio)` o `gremio.agregarMiembro(personaje)`, y los dos quedan iguales. No hay recursión infinita porque cada método revisa antes de llamar al otro:
- `unirseAGremio` termina si el gremio ya es el mismo. Si el personaje estaba en otro gremio, primero llama a `salirDeGremio()`, que lo saca de la lista del gremio anterior.
- `agregarMiembro` solo lo agrega si no está en la lista, y solo llama a `personaje.unirseAGremio(this)` si `personaje.getGremio() != this`.

En la prueba 2, Aragorn pasa de Montaraces a Comunidad y Montaraces queda con 0 miembros. `Gremio.getMiembros()` devuelve una copia de la lista para que desde afuera nadie haga `add` sin pasar por la sincronización.

## Reto integrador (4.1)

- **`do-while` + `switch`:** el menú de la plantilla (opciones 1 a 5). Agregué 6 (unir a gremio), 7 (desequipar arma) y 8 (expulsar personaje o disolver escuadrón), porque si no, la bidireccional, el desequipar y la agregación no se podían probar desde el menú. Por eso salir quedó en el 9.
- **Colecciones globales:** `personajes`, `gremios` y `escuadrones`. Un personaje "libre" es el que no está en ningún escuadrón. Sigue en la lista `personajes`, y en la tabla sale como `libre`.
- **`printf`:** la opción 5 imprime la tabla de personajes (nivel, salud, maná, arma, ataque, gremio, escuadrón) y la tabla de escuadrones con el ataque y la salud sumados.
- **`StringBuilder`:** los `toString()` de `CorazonRPG`, `Arma`, `Gremio`, `Escuadron` y `Personaje`.

## Sección 5: Depuración

### 5.1 StackOverflowError por `toString()` cruzado

Si `Personaje.toString()` imprime `gremio.toString()` y `Gremio.toString()` imprime el `toString()` de cada miembro, se llaman entre los dos para siempre:

```
Personaje.toString() ---> Gremio.toString() ---> Personaje.toString() ---> ... StackOverflowError
```

La versión mala quedó comentada en `Gremio.java` (encima del `toString`).

**Cómo se ve en IntelliJ:**
1. Breakpoint en la primera línea del `toString()` de `Personaje` (`Personaje.java:178`).
2. Shift + F9 para correr en Debug y elegir la opción 5 del menú con algún personaje.
3. En la ventana **Frames** se ve la pila. Con la versión mala salen `Personaje.toString` y `Gremio.toString` uno debajo del otro, repetidos cada vez que se le da F9 (Resume). Eso confirma la recursión circular.

**Solución:** romper la cadena en un lado. En este caso, en los dos: `Gremio.toString()` solo pone `miembros.get(i).getNombre()` y `Personaje.toString()` solo pone `gremio.getNombre()`. En la prueba 2 se imprimen los dos `toString` y no pasa nada.

### 5.2 Referencias nulas y desincronización

**NullPointerException**
- *Causa:* llamar `armaEquipada.getDanioBase()` cuando está desarmado, o crear un personaje sin instanciar el `CorazonRPG`.
- *Cómo quedó:* `calcularAtaqueTotal()`, `equiparArma()`, `desequiparArma()`, `toString()` y la tabla del menú revisan `!= null` antes de usar el arma o el gremio. El corazón se crea en `Personaje.java:36`, así que nunca está en `null`. En la prueba 1 se llama `desequiparArma()` sin arma y solo muestra el mensaje.
- *En IntelliJ:* en la pestaña **Variables** se despliega el personaje y se ve `armaEquipada = null` antes de la línea que fallaría.

**Desincronización bidireccional**
- *Causa:* poner `this.gremio = nuevoGremio` y olvidar `nuevoGremio.agregarMiembro(this)`. Así el personaje dice que está en el gremio, pero el gremio no lo tiene en la lista.
- *Cómo quedó:* la sincronización está en los dos lados (`Personaje.java:75` y `Gremio.java:17`). La opción 6 del menú imprime los dos lados después de unir.
- *En IntelliJ:* con Alt + F8 se agregan como **Watch** `personaje.getGremio()` y `gremio.getMiembros()`, y se va con F8 por `unirseAGremio` viendo cómo cambian los dos.

**ConcurrentModificationException**
- *Causa:* borrar de la lista dentro de un `for-each` que la está recorriendo.
- *Cómo quedó:* `Escuadron.disolver()` (`Escuadron.java:45`) usa un `Iterator` con `iterador.remove()`. El `for-each` malo quedó comentado ahí mismo. Además, `getMiembros()` devuelve una copia, así que recorrer esa copia mientras se expulsa del escuadrón tampoco falla.
- Lo probé aparte: con 3 elementos el `for-each` que borra lanza `java.util.ConcurrentModificationException`. Con 2 no lanza nada, pero queda uno sin borrar, porque al borrar el primero el ciclo cree que ya terminó. Ese caso es peor, porque falla sin avisar.
- *En IntelliJ:* breakpoint condicional dentro del ciclo y revisar en **Variables** el `size` de la lista contra la posición del cursor.
