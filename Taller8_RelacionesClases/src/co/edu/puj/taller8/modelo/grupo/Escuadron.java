package co.edu.puj.taller8.modelo.grupo;

import co.edu.puj.taller8.modelo.personaje.Personaje;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// agregacion: el escuadron agrupa personajes que ya existen afuera,
// si el escuadron se borra los personajes siguen igual
public class Escuadron {
    private String nombre;
    private List<Personaje> miembros; // Agregacion (0..*)

    public Escuadron(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>(); // Inicializacion de la coleccion
    }

    public void reclutarPersonaje(Personaje personaje) {
        if (personaje == null) {
            System.out.println("el personaje no es valido");
            return;
        }
        if (miembros.contains(personaje)) {
            System.out.println(personaje.getNombre() + " ya esta en el escuadron " + nombre);
            return;
        }

        // el personaje llega ya creado, aca solo se guarda la referencia, no se hace new
        miembros.add(personaje);
        System.out.println(personaje.getNombre() + " es reclutado en el escuadron " + nombre);
    }

    public void expulsarPersonaje(Personaje personaje) {
        if (personaje != null && miembros.contains(personaje)) {
            miembros.remove(personaje);
            // el personaje sigue con su estado, vida y arma intactos, solo sale de la lista
            System.out.println(personaje.getNombre() + " es expulsado del escuadron " + nombre);
        } else {
            System.out.println("ese personaje no esta en el escuadron " + nombre);
        }
    }

    // saca a todos los miembros, los personajes no se borran
    public void disolver() {
        // asi da ConcurrentModificationException porque se borra de la lista mientras el for-each la recorre:
        // for (Personaje personaje : miembros) {
        //     miembros.remove(personaje);
        // }
        // con el Iterator se puede borrar mientras se recorre
        Iterator<Personaje> iterador = miembros.iterator();
        while (iterador.hasNext()) {
            Personaje personaje = iterador.next();
            iterador.remove();
            System.out.printf("%s queda libre (salud %d/%d)%n", personaje.getNombre(), personaje.getSalud(), personaje.getSaludMaxima());
        }
        System.out.println("el escuadron " + nombre + " se disolvio");
    }

    public boolean contiene(Personaje personaje) {
        return miembros.contains(personaje);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Personaje> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    // en la agregacion si se permite cambiar los miembros, por eso hay metodos para meter y sacar
    // (en la composicion del corazon no hay ninguno)
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("Escuadron ").append(nombre);
        texto.append(" (").append(miembros.size()).append(" miembros)");

        if (miembros.isEmpty()) {
            texto.append(": vacio");
        } else {
            texto.append(": ");
            for (int i = 0; i < miembros.size(); i++) {
                Personaje personaje = miembros.get(i);
                texto.append(personaje.getNombre()).append(" [ataque ").append(personaje.calcularAtaqueTotal()).append("]");
                if (i < miembros.size() - 1) {
                    texto.append(", ");
                }
            }
        }
        return texto.toString();
    }
}
