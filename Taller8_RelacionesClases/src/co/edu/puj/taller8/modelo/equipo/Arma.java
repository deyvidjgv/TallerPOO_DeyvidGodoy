package co.edu.puj.taller8.modelo.equipo;

// asociacion unidireccional: el Personaje conoce su Arma, pero el Arma no tiene ningun atributo Personaje,
// no sabe quien la tiene equipada
public class Arma {
    private String nombre;
    private int danioBase;

    private static final int SIN_DANIO = 0;

    public Arma(String nombre, int danioBase) {
        this.nombre = nombre;

        if (danioBase >= SIN_DANIO) {
            this.danioBase = danioBase;
        } else {
            this.danioBase = SIN_DANIO;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getDanioBase() {
        return danioBase;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append(nombre);
        texto.append(" (+");
        texto.append(danioBase);
        texto.append(" de danio)");
        return texto.toString();
    }
}
