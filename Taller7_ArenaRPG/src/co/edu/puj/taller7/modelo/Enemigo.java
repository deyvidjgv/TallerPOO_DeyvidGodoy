package co.edu.puj.taller7.modelo;

// no sale en el diagrama de la guia, pero el enemigo tiene que ser un Personaje para que
// realizarAtaqueEspecial(Personaje objetivo) lo pueda recibir y para guardarlo en un List<Personaje>
public class Enemigo extends Personaje {
    protected int armadura;

    // el zarpazo pega el ataque base mas este bono
    private static final int BONO_ZARPAZO = 8;
    private static final int SIN_ARMADURA = 0;

    public Enemigo(String nombre, int vida, int ataqueBase, int posX, int posY, int armadura) {
        super(nombre, vida, ataqueBase, posX, posY);

        if (armadura >= SIN_ARMADURA) {
            this.armadura = armadura;
        } else {
            this.armadura = SIN_ARMADURA;
        }
    }

    // ataque especial del enemigo: zarpazo que pega el ataque base mas BONO_ZARPAZO
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (!estaVivo()) {
            return;
        }
        if (objetivo == null || !objetivo.estaVivo()) {
            return;
        }

        int dano = ataqueBase + BONO_ZARPAZO;
        System.out.printf("%s lanza un ZARPAZO a %s con %d de dano%n", nombre, objetivo.getNombre(), dano);
        objetivo.defender(dano);
    }

    // la armadura le quita un poco a cada golpe, minimo recibe DANO_MINIMO
    @Override
    public void defender(int danoRecibido) {
        int danoFinal = danoRecibido - armadura;
        if (danoFinal < DANO_MINIMO) {
            danoFinal = DANO_MINIMO;
        }
        recibirDano(danoFinal);
        System.out.printf("%s recibe %d de dano (la armadura bloquea %d), le quedan %d PV%n",
                nombre, danoFinal, danoRecibido - danoFinal, vida);
    }

    @Override
    public char getSimbolo() {
        return 'E';
    }

    public int getArmadura() {
        return armadura;
    }
}
