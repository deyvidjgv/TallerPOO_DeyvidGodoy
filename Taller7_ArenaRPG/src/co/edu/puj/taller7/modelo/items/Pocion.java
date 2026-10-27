package co.edu.puj.taller7.modelo.items;

public class Pocion {
    private String nombre;
    private int curacion;

    // curacion que se le pone si la que llega no es valida
    private static final int CURACION_POR_DEFECTO = 20;

    public Pocion(String nombre, int curacion) {
        this.nombre = nombre;

        if (curacion > 0) {
            this.curacion = curacion;
        } else {
            this.curacion = CURACION_POR_DEFECTO;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuracion() {
        return curacion;
    }
}
