package co.edu.puj.rpg.modelo.enemigo;

public class Enemigo {
    // los mismos atributos privados de Personaje
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;
    private int experiencia; // la experiencia que gana el heroe al derrotarlo

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque, int nivel, double oro, int experiencia) {
        this.nombre = nombre;

        if (vidaMaxima > 0) {
            this.vidaMaxima = vidaMaxima;
        } else {
            this.vidaMaxima = 50;
        }

        if (puntosAtaque > 0) {
            this.puntosAtaque = puntosAtaque;
        } else {
            this.puntosAtaque = 5;
        }

        if (nivel >= 1) {
            this.nivel = nivel;
        } else {
            this.nivel = 1;
        }

        if (oro >= 0) {
            this.oro = oro;
        } else {
            this.oro = 0.0;
        }

        if (experiencia >= 0) {
            this.experiencia = experiencia;
        } else {
            this.experiencia = 0;
        }

        this.setPuntosVida(this.vidaMaxima);
    }

    // es public porque es la unica forma de quitarle vida desde otra clase
    public void recibirDano(int cantidadDano) {
        if (cantidadDano < 0) {
            return;
        }
        setPuntosVida(puntosVida - cantidadDano);
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    // sin modificador (package-private): solo lo ven las clases del paquete enemigo,
    // ni Main ni Personaje lo pueden llamar porque modelo y modelo.enemigo son paquetes distintos
    void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > vidaMaxima) {
            this.puntosVida = vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }
}
