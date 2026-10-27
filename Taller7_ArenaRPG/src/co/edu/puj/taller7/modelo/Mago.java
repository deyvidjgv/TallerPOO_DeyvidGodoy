package co.edu.puj.taller7.modelo;

public class Mago extends Personaje {
    // atributo propio del mago
    protected int puntosMana;
    protected int manaMaximo;

    private static final int COSTO_BOLA_FUEGO = 15;
    private static final int COSTO_ESCUDO_MAGICO = 5;
    // la bola de fuego pega el doble y el escudo magico deja pasar la mitad
    private static final int MULTIPLICADOR_BOLA_FUEGO = 2;
    private static final int DIVISOR_ESCUDO_MAGICO = 2;
    private static final int SIN_MANA = 0;

    public Mago(String nombre, int vida, int ataqueBase, int posX, int posY, int puntosMana) {
        super(nombre, vida, ataqueBase, posX, posY);

        if (puntosMana >= SIN_MANA) {
            this.puntosMana = puntosMana;
        } else {
            this.puntosMana = SIN_MANA;
        }
        this.manaMaximo = this.puntosMana;
    }

    // ataque especial del mago: bola de fuego que gasta mana y pega el doble,
    // si no le alcanza el mana ataca normal con el baston
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (!estaVivo()) {
            System.out.println(nombre + " esta derrotado y no puede atacar");
            return;
        }
        if (objetivo == null || !objetivo.estaVivo()) {
            System.out.println("el objetivo no es valido o ya fue derrotado");
            return;
        }

        if (puntosMana >= COSTO_BOLA_FUEGO) {
            puntosMana = puntosMana - COSTO_BOLA_FUEGO;
            int dano = ataqueBase * MULTIPLICADOR_BOLA_FUEGO;
            System.out.printf("%s lanza BOLA DE FUEGO a %s con %d de dano (gasta %d de mana, le quedan %d)%n",
                    nombre, objetivo.getNombre(), dano, COSTO_BOLA_FUEGO, puntosMana);
            objetivo.defender(dano);
        } else {
            System.out.println(nombre + " no tiene mana suficiente, ataca con el baston");
            atacar(objetivo);
        }
    }

    // escudo magico: si tiene mana se gasta un poco y el dano baja a la mitad
    @Override
    public void defender(int danoRecibido) {
        int danoFinal = danoRecibido;
        if (puntosMana >= COSTO_ESCUDO_MAGICO) {
            puntosMana = puntosMana - COSTO_ESCUDO_MAGICO;
            danoFinal = danoRecibido / DIVISOR_ESCUDO_MAGICO;
            recibirDano(danoFinal);
            System.out.printf("%s usa el escudo magico y recibe %d de dano en vez de %d, le quedan %d PV%n",
                    nombre, danoFinal, danoRecibido, vida);
        } else {
            recibirDano(danoFinal);
            System.out.printf("%s no tiene mana para el escudo y recibe %d de dano, le quedan %d PV%n",
                    nombre, danoFinal, vida);
        }
    }

    public void recuperarMana(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        puntosMana = puntosMana + cantidad;
        if (puntosMana > manaMaximo) {
            puntosMana = manaMaximo;
        }
    }

    @Override
    public char getSimbolo() {
        return 'M';
    }

    public int getPuntosMana() {
        return puntosMana;
    }

    public int getManaMaximo() {
        return manaMaximo;
    }
}
