package co.edu.puj.taller8.modelo.grupo;

import co.edu.puj.taller8.modelo.personaje.Personaje;
import java.util.ArrayList;
import java.util.List;

public class Gremio {
    private String nombre;
    private List<Personaje> miembros; // lado "muchos" de la bidireccional (0..*)

    public Gremio(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    // Sincronizacion dentro de la clase Gremio
    public void agregarMiembro(Personaje personaje) {
        if (personaje == null) {
            System.out.println("el personaje no es valido");
            return;
        }

        // si ya esta no se agrega otra vez
        if (!miembros.contains(personaje)) {
            miembros.add(personaje);
        }

        // sincronizacion cruzada: si el personaje todavia no apunta a este gremio se le avisa,
        // cuando unirseAGremio vuelva a llamar a agregarMiembro ya va a estar en la lista y personaje.getGremio() va a ser this,
        // asi que no se repite
        if (personaje.getGremio() != this) {
            personaje.unirseAGremio(this);
        }
    }

    public void removerMiembro(Personaje personaje) {
        if (personaje == null) {
            return;
        }

        if (miembros.contains(personaje)) {
            miembros.remove(personaje);
        }

        // si el personaje todavia cree que esta en este gremio se le quita la referencia
        if (personaje.getGremio() == this) {
            personaje.salirDeGremio();
        }
    }

    public String getNombre() {
        return nombre;
    }

    // se devuelve una copia para que desde afuera no agreguen o quiten miembros sin pasar por la sincronizacion
    public List<Personaje> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public int getCantidadMiembros() {
        return miembros.size();
    }

    // version que da StackOverflowError (seccion 5.1), llama el toString() completo de cada miembro
    // y Personaje.toString() llama el de su gremio:
    // public String toString() {
    //     StringBuilder texto = new StringBuilder("Gremio " + nombre);
    //     for (Personaje personaje : miembros) {
    //         texto.append(personaje.toString());
    //     }
    //     return texto.toString();
    // }
    // corregido: de los miembros solo se ponen los nombres
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("Gremio ").append(nombre);
        texto.append(" (").append(miembros.size()).append(" miembros)");

        if (miembros.isEmpty()) {
            texto.append(": sin miembros");
        } else {
            texto.append(": ");
            for (int i = 0; i < miembros.size(); i++) {
                texto.append(miembros.get(i).getNombre());
                if (i < miembros.size() - 1) {
                    texto.append(", ");
                }
            }
        }
        return texto.toString();
    }
}
