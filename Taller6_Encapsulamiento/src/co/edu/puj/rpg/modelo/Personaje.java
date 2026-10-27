package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {
    // atributos privados, desde afuera solo se usan con los getters y setters
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;

    // para los retos: experiencia, inventario de pociones grandes y si el ultimo golpe fue critico
    private int experiencia;
    private int pocionesGrandes;
    private boolean ultimoGolpeCritico;

    // public para que Main pueda mostrar el umbral sin repetir el numero
    public static final int EXPERIENCIA_PARA_SUBIR = 100;

    // valores por defecto cuando el constructor recibe datos invalidos
    private static final String NOMBRE_POR_DEFECTO = "Heroe";
    private static final int VIDA_MAXIMA_POR_DEFECTO = 100;
    private static final int ATAQUE_POR_DEFECTO = 10;
    private static final int NIVEL_INICIAL = 1;
    private static final int POCIONES_INICIALES = 1;
    private static final double SIN_ORO = 0.0;

    // golpe critico: 15% de probabilidad y el dano se duplica
    private static final double PROBABILIDAD_CRITICO = 0.15;
    private static final int MULTIPLICADOR_CRITICO = 2;

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;

        if (vidaMaxima > 0) {
            this.vidaMaxima = vidaMaxima;
        } else {
            this.vidaMaxima = VIDA_MAXIMA_POR_DEFECTO;
        }

        if (puntosAtaque > 0) {
            this.puntosAtaque = puntosAtaque;
        } else {
            this.puntosAtaque = ATAQUE_POR_DEFECTO;
        }

        // el oro no puede empezar negativo
        if (oro >= SIN_ORO) {
            this.oro = oro;
        } else {
            this.oro = SIN_ORO;
        }

        this.nivel = NIVEL_INICIAL;
        this.experiencia = 0;
        this.pocionesGrandes = POCIONES_INICIALES;
        this.ultimoGolpeCritico = false;

        // empieza con la vida llena, se manda por el setter para que estaVivo quede en true
        this.setPuntosVida(this.vidaMaxima);
    }

    // CASO B: asi venia el constructor en la guia, compila pero deja al personaje vivo con vida negativa
    // public Personaje(int puntosVida) {
    //     this.puntosVida = -50;
    //     this.estaVivo = true;
    // }
    // corregido: la vida que llega pasa por setPuntosVida y estaVivo queda acorde a esa vida
    public Personaje(int puntosVida) {
        this.nombre = NOMBRE_POR_DEFECTO;
        this.vidaMaxima = VIDA_MAXIMA_POR_DEFECTO;
        this.puntosAtaque = ATAQUE_POR_DEFECTO;
        this.nivel = NIVEL_INICIAL;
        this.oro = SIN_ORO;
        this.experiencia = 0;
        this.pocionesGrandes = POCIONES_INICIALES;
        this.ultimoGolpeCritico = false;
        this.setPuntosVida(puntosVida);
    }

    public void atacar(Enemigo enemigo) {
        if (!estaVivo) {
            System.out.println(nombre + " esta derrotado y no puede atacar");
            return;
        }
        if (!enemigo.isEstaVivo()) {
            System.out.println(enemigo.getNombre() + " ya fue derrotado");
            return;
        }

        int dano = puntosAtaque;
        ultimoGolpeCritico = false;

        // golpe critico: Math.random() da un numero entre 0 y 1, si sale menor a 0.15 es el 15%
        if (Math.random() < PROBABILIDAD_CRITICO) {
            dano = dano * MULTIPLICADOR_CRITICO;
            ultimoGolpeCritico = true;
            System.out.println("GOLPE CRITICO!! el dano se duplica");
        }

        // setPuntosVida de Enemigo no es public, por eso se le quita vida con recibirDano
        enemigo.recibirDano(dano);
        System.out.printf("%s ataca a %s y le hace %d de dano, le quedan %d PV%n",
                nombre, enemigo.getNombre(), dano, enemigo.getPuntosVida());
    }

    public void usarPocion(int cantidadRestaurada) {
        if (!estaVivo) {
            System.out.println(nombre + " esta derrotado y no puede usar pociones");
            return;
        }
        if (pocionesGrandes <= 0) {
            System.out.println("no tiene pociones, puede comprar en la tienda");
            return;
        }
        if (cantidadRestaurada <= 0) {
            System.out.println("la cantidad a curar tiene que ser mayor a 0");
            return;
        }

        pocionesGrandes = pocionesGrandes - 1;
        int vidaAntes = puntosVida;
        // el setter se encarga de que no se pase de la vida maxima
        setPuntosVida(puntosVida + cantidadRestaurada);
        System.out.printf("%s usa una pocion y recupera %d PV (%d/%d)%n",
                nombre, puntosVida - vidaAntes, puntosVida, vidaMaxima);
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano < 0) {
            // si el dano fuera negativo terminaria curando
            System.out.println("el dano no puede ser negativo");
            return;
        }
        setPuntosVida(puntosVida - cantidadDano);
    }

    public void ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return;
        }

        experiencia = experiencia + puntos;
        System.out.printf("%s gana %d de experiencia (%d/%d)%n", nombre, puntos, experiencia, EXPERIENCIA_PARA_SUBIR);

        if (experiencia >= EXPERIENCIA_PARA_SUBIR) {
            experiencia = experiencia - EXPERIENCIA_PARA_SUBIR;
            // el nivel solo sube por aca, con el setter protegido
            setNivel(nivel + 1);
            System.out.printf("%s sube a nivel %d!!%n", nombre, nivel);
        }
    }

    public void agregarPocionGrande() {
        pocionesGrandes = pocionesGrandes + 1;
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

    public int getPocionesGrandes() {
        return pocionesGrandes;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }

    public void setPuntosVida(int vida) {
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

    // si el oro queda negativo se rechaza y el atributo no cambia
    public void setOro(double oro) {
        if (oro < SIN_ORO) {
            System.out.println("saldo insuficiente, la operacion fue rechazada");
        } else {
            this.oro = oro;
        }
    }

    // protected: desde Main (otro paquete) no se puede llamar
    protected void setNivel(int nivel) {
        if (nivel < NIVEL_INICIAL) {
            System.out.println("nivel no valido, se queda en " + this.nivel);
        } else {
            this.nivel = nivel;
        }
    }
}
