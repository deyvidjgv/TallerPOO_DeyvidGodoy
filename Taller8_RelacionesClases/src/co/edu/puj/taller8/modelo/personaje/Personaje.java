package co.edu.puj.taller8.modelo.personaje;

import co.edu.puj.taller8.modelo.equipo.Arma;
import co.edu.puj.taller8.modelo.grupo.Gremio;

public class Personaje {
    private String nombre;
    private int nivel;
    private Arma armaEquipada;  // Asociacion Unidireccional (0..1), si es null pelea desarmado
    private Gremio gremio;      // Asociacion Bidireccional (0..* a 1), como maximo un gremio
    private CorazonRPG corazon; // Composicion (1..1), no hay personaje sin corazon

    private static final int NIVEL_MINIMO = 1;
    // el corazon crece con el nivel: salud 100 + 10 por nivel y mana 50 + 5 por nivel
    private static final int SALUD_INICIAL = 100;
    private static final int SALUD_POR_NIVEL = 10;
    private static final int MANA_INICIAL = 50;
    private static final int MANA_POR_NIVEL = 5;
    // sin arma el ataque es el nivel por este valor
    public static final int ATAQUE_POR_NIVEL = 5;

    public Personaje(String nombre, int nivel) {
        this.nombre = nombre;

        if (nivel >= NIVEL_MINIMO) {
            this.nivel = nivel;
        } else {
            this.nivel = NIVEL_MINIMO;
        }

        this.armaEquipada = null; // Inicialmente desarmado
        this.gremio = null;       // empieza sin gremio

        // composicion: el corazon se crea aca adentro, nadie de afuera lo manda ni lo puede cambiar,
        // por eso no hay setCorazon() ni getCorazon()
        this.corazon = new CorazonRPG(SALUD_INICIAL + this.nivel * SALUD_POR_NIVEL, MANA_INICIAL + this.nivel * MANA_POR_NIVEL);
    }

    // ---------------- asociacion unidireccional con Arma ----------------

    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.println("no hay arma para equipar");
            return;
        }

        if (this.armaEquipada != null) {
            System.out.println(nombre + " cambia " + armaEquipada.getNombre() + " por " + nuevaArma.getNombre());
        }
        this.armaEquipada = nuevaArma;
        System.out.println(nombre + " equipa " + nuevaArma.toString());
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {
            System.out.println(nombre + " guarda " + armaEquipada.getNombre() + " y queda desarmado");
            this.armaEquipada = null;
        } else {
            System.out.println(nombre + " no tiene arma para retirar");
        }
    }

    public int calcularAtaqueTotal() {
        int ataqueBase = this.nivel * ATAQUE_POR_NIVEL;
        // se revisa null antes de llamar getDanioBase(), si no sale NullPointerException cuando esta desarmado
        if (this.armaEquipada != null) {
            ataqueBase = ataqueBase + this.armaEquipada.getDanioBase();
        }
        return ataqueBase;
    }

    // ---------------- asociacion bidireccional con Gremio ----------------

    // Sincronizacion dentro de la clase Personaje
    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null) {
            System.out.println("el gremio no es valido");
            return;
        }
        if (nuevoGremio == this.gremio) {
            System.out.println(nombre + " ya pertenece al gremio " + nuevoGremio.getNombre());
            return;
        }

        // si ya estaba en otro gremio primero se sale de ese
        if (this.gremio != null) {
            salirDeGremio();
        }

        this.gremio = nuevoGremio;
        System.out.println(nombre + " se une al gremio " + nuevoGremio.getNombre());

        // invocacion cruzada: el gremio tambien lo tiene que tener en su lista,
        // como this.gremio ya es nuevoGremio, agregarMiembro no vuelve a llamar a unirseAGremio (no hay recursion infinita)
        nuevoGremio.agregarMiembro(this);
    }

    public void salirDeGremio() {
        if (this.gremio == null) {
            System.out.println(nombre + " no pertenece a ningun gremio");
            return;
        }

        Gremio anterior = this.gremio;
        this.gremio = null;
        System.out.println(nombre + " sale del gremio " + anterior.getNombre());

        // del otro lado tambien se tiene que quitar de la lista
        anterior.removerMiembro(this);
    }

    // ---------------- composicion con CorazonRPG ----------------
    // desde afuera el corazon solo se toca por medio de estos metodos

    public void recibirDanio(int cantidad) {
        corazon.recibirDanio(cantidad);
        System.out.printf("%s recibe %d de danio, le quedan %d de salud%n", nombre, cantidad, corazon.getSaludActual());
    }

    public void curar(int cantidad) {
        corazon.curar(cantidad);
        System.out.printf("%s se cura, ahora tiene %d de salud%n", nombre, corazon.getSaludActual());
    }

    public boolean lanzarHechizo(int costoMana) {
        if (corazon.gastarMana(costoMana)) {
            System.out.printf("%s lanza un hechizo y gasta %d de mana, le quedan %d%n", nombre, costoMana, corazon.getManaActual());
            return true;
        }
        System.out.println(nombre + " no tiene mana suficiente");
        return false;
    }

    public boolean estaVivo() {
        return corazon.estaLatiendo();
    }

    public int getSalud() {
        return corazon.getSaludActual();
    }

    public int getSaludMaxima() {
        return corazon.getSaludBase();
    }

    public int getMana() {
        return corazon.getManaActual();
    }

    public int getManaMaximo() {
        return corazon.getManaBase();
    }

    public String reporteCorazon() {
        return corazon.toString();
    }

    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Gremio getGremio() {
        return gremio;
    }

    // del gremio solo se pone el nombre, si se llamara gremio.toString() y el gremio llamara el toString()
    // de cada miembro se quedarian llamandose entre los dos hasta el StackOverflowError
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("Personaje: ").append(nombre).append("\n");
        texto.append("  nivel        : ").append(nivel).append("\n");
        texto.append("  corazon      : ").append(corazon.toString()).append("\n");

        texto.append("  arma         : ");
        if (armaEquipada != null) {
            texto.append(armaEquipada.toString());
        } else {
            texto.append("desarmado");
        }
        texto.append("\n");

        texto.append("  ataque total : ").append(calcularAtaqueTotal()).append("\n");

        texto.append("  gremio       : ");
        if (gremio != null) {
            texto.append(gremio.getNombre());
        } else {
            texto.append("sin gremio");
        }
        return texto.toString();
    }
}
