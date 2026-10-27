package co.edu.puj.taller7.modelo;

public class Guerrero extends Personaje {
    // atributo propio del guerrero, el resto (nombre, vida...) se hereda de Personaje
    protected int fuerzaEscudo;

    private static final int SIN_ESCUDO = 0;

    public Guerrero(String nombre, int vida, int ataqueBase, int posX, int posY, int fuerzaEscudo) {
        // super va en la primera linea para que se inicialice primero lo heredado
        super(nombre, vida, ataqueBase, posX, posY);

        if (fuerzaEscudo >= SIN_ESCUDO) {
            this.fuerzaEscudo = fuerzaEscudo;
        } else {
            this.fuerzaEscudo = SIN_ESCUDO;
        }
    }

    // ataque especial del guerrero: golpe fisico en modo furia (usa la sobrecarga de atacar)
    @Override
    public void realizarAtaqueEspecial(Personaje objetivo) {
        if (!estaVivo()) {
            System.out.println(nombre + " esta derrotado y no puede atacar");
            return;
        }
        System.out.println(nombre + " usa GOLPE DE FURIA");
        atacar(objetivo, true);
    }

    // el escudo le quita la fuerzaEscudo al golpe, pero siempre recibe minimo DANO_MINIMO de dano
    @Override
    public void defender(int danoRecibido) {
        int danoFinal = danoRecibido - fuerzaEscudo;
        if (danoFinal < DANO_MINIMO) {
            danoFinal = DANO_MINIMO;
        }
        recibirDano(danoFinal);
        System.out.printf("%s bloquea %d con el escudo y recibe %d de dano, le quedan %d PV%n",
                nombre, danoRecibido - danoFinal, danoFinal, vida);
    }

    @Override
    public char getSimbolo() {
        return 'G';
    }

    public int getFuerzaEscudo() {
        return fuerzaEscudo;
    }
}
