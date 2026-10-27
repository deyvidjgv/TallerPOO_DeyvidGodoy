package co.edu.puj.taller7.modelo;

// abstracta: no se puede hacer new Personaje(), solo new Guerrero(...), new Mago(...) o new Enemigo(...)
// no implementa defender() de ICombatiente, eso lo hace cada subclase
public abstract class Personaje implements ICombatiente {
    // protected: las subclases los usan directo, desde Main (otro paquete) solo con los getters
    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int posicionX; // fila en el tablero
    protected int posicionY; // columna en el tablero
    // no viene en la guia pero hace falta para que la pocion no cure mas de la vida con la que empezo
    protected int vidaMaxima;

    // valores por defecto si el constructor recibe datos invalidos
    private static final int VIDA_POR_DEFECTO = 100;
    private static final int ATAQUE_POR_DEFECTO = 10;
    // en modo furia el ataque se multiplica por 2
    private static final int MULTIPLICADOR_FURIA = 2;
    // protected: Guerrero y Enemigo lo usan para que un golpe siempre quite al menos 1
    protected static final int DANO_MINIMO = 1;

    public Personaje(String nombre, int vida, int ataqueBase, int posX, int posY) {
        this.nombre = nombre;

        if (vida > 0) {
            this.vida = vida;
        } else {
            this.vida = VIDA_POR_DEFECTO;
        }
        this.vidaMaxima = this.vida;

        if (ataqueBase > 0) {
            this.ataqueBase = ataqueBase;
        } else {
            this.ataqueBase = ATAQUE_POR_DEFECTO;
        }

        this.posicionX = posX;
        this.posicionY = posY;
    }

    // sin cuerpo: cada subclase tiene que hacer su propio ataque especial
    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    // la letra con la que sale en el tablero (G, M o E)
    public abstract char getSimbolo();

    // sobrecarga: atacar(objetivo) y atacar(objetivo, modoFuria) se llaman igual pero reciben distintos parametros,
    // el compilador decide cual usar segun lo que se le mande
    public void atacar(Personaje objetivo) {
        if (!estaVivo()) {
            System.out.println(nombre + " esta derrotado y no puede atacar");
            return;
        }
        if (objetivo == null || !objetivo.estaVivo()) {
            System.out.println("el objetivo no es valido o ya fue derrotado");
            return;
        }

        System.out.printf("%s ataca a %s con %d de dano%n", nombre, objetivo.getNombre(), ataqueBase);
        objetivo.defender(ataqueBase);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (!estaVivo()) {
            System.out.println(nombre + " esta derrotado y no puede atacar");
            return;
        }
        if (objetivo == null || !objetivo.estaVivo()) {
            System.out.println("el objetivo no es valido o ya fue derrotado");
            return;
        }

        int dano = ataqueBase;
        // en modo furia pega el doble
        if (modoFuria) {
            dano = ataqueBase * MULTIPLICADOR_FURIA;
            System.out.printf("%s entra en MODO FURIA y ataca a %s con %d de dano%n", nombre, objetivo.getNombre(), dano);
        } else {
            System.out.printf("%s ataca a %s con %d de dano%n", nombre, objetivo.getNombre(), dano);
        }
        objetivo.defender(dano);
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    // la vida nunca baja de 0
    public void recibirDano(int cantidad) {
        if (cantidad < 0) {
            // si el dano fuera negativo terminaria curando
            return;
        }
        vida = vida - cantidad;
        if (vida < 0) {
            vida = 0;
        }
    }

    public void curar(int cantidad) {
        if (!estaVivo()) {
            System.out.println(nombre + " esta derrotado y no se puede curar");
            return;
        }
        if (cantidad <= 0) {
            return;
        }

        int vidaAntes = vida;
        vida = vida + cantidad;
        if (vida > vidaMaxima) {
            vida = vidaMaxima;
        }
        System.out.printf("%s recupera %d PV (%d/%d)%n", nombre, vida - vidaAntes, vida, vidaMaxima);
    }

    // aca solo se cambian las coordenadas, que la casilla exista y este libre se revisa en Main
    // porque el personaje no conoce la matriz del tablero
    public void mover(int nuevaX, int nuevaY) {
        this.posicionX = nuevaX;
        this.posicionY = nuevaY;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }
}
