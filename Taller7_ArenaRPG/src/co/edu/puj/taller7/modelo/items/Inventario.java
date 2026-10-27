package co.edu.puj.taller7.modelo.items;

import java.util.ArrayList;
import java.util.List;

// clase generica: T se reemplaza por el tipo que se ponga al crearla, por ejemplo Inventario<Pocion>,
// asi no hay que hacer un InventarioPociones, un InventarioEquipamiento, etc.
public class Inventario<T> {
    private List<T> items = new ArrayList<>();

    public void agregar(T item) {
        items.add(item);
    }

    // devuelve T, por eso no hay que castear al sacar el objeto
    public T obtener(int index) {
        return items.get(index);
    }

    public List<T> getItems() {
        return items;
    }

    // no estan en la guia, se agregaron para gastar las pociones
    public T remover(int index) {
        return items.remove(index);
    }

    public int cantidad() {
        return items.size();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}
