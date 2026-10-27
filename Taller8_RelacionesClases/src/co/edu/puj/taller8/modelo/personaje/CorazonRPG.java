package co.edu.puj.taller8.modelo.personaje;

// composicion: el corazon solo lo crea el constructor de Personaje y no se puede cambiar desde afuera.
// esta en el mismo paquete que Personaje para que su constructor pueda ser package-private
public class CorazonRPG {
    private int saludBase;
    private int manaBase;
    // la base es el maximo, estas son las que van cambiando
    private int saludActual;
    private int manaActual;

    private static final int SALUD_POR_DEFECTO = 100;
    private static final int SIN_MANA = 0;

    // sin public: desde otro paquete (por ejemplo principal) no se puede hacer new CorazonRPG(...),
    // solo lo crea Personaje en su constructor
    CorazonRPG(int saludBase, int manaBase) {
        if (saludBase > 0) {
            this.saludBase = saludBase;
        } else {
            this.saludBase = SALUD_POR_DEFECTO;
        }

        if (manaBase >= SIN_MANA) {
            this.manaBase = manaBase;
        } else {
            this.manaBase = SIN_MANA;
        }

        // empieza lleno
        this.saludActual = this.saludBase;
        this.manaActual = this.manaBase;
    }

    public void recibirDanio(int cantidad) {
        if (cantidad < 0) {
            return;
        }
        saludActual = saludActual - cantidad;
        if (saludActual < 0) {
            saludActual = 0;
        }
    }

    public void curar(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        saludActual = saludActual + cantidad;
        if (saludActual > saludBase) {
            saludActual = saludBase;
        }
    }

    // devuelve false si no le alcanza el mana
    public boolean gastarMana(int cantidad) {
        if (cantidad <= 0 || cantidad > manaActual) {
            return false;
        }
        manaActual = manaActual - cantidad;
        return true;
    }

    public boolean estaLatiendo() {
        return saludActual > 0;
    }

    public int getSaludBase() {
        return saludBase;
    }

    public int getManaBase() {
        return manaBase;
    }

    public int getSaludActual() {
        return saludActual;
    }

    public int getManaActual() {
        return manaActual;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("salud ");
        texto.append(saludActual).append("/").append(saludBase);
        texto.append(" | mana ");
        texto.append(manaActual).append("/").append(manaBase);
        if (!estaLatiendo()) {
            texto.append(" | sin vida");
        }
        return texto.toString();
    }
}
