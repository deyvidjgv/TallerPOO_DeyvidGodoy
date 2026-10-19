package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        int opcion = 0;
        int opcionTienda = 0;
        int ronda = 1;
        boolean huyo = false;
        boolean pasoTurno = false;
        int vidaAntes = 0;
        int danoHecho = 0;
        int pocionesAntes = 0;
        int nivelAntes = 0;
        double nuevoSaldo = 0;
        double oroAntes = 0;
        int curacionPocion = 50;
        double precioPocion = 30.0;
        String respuesta = "";
        String nombre = "";
        String estado = "";
        StringBuilder historial;

        System.out.println(" -------- simulador de batalla: arena de heroes --------");
        System.out.print("desea correr primero las pruebas de limites (punto 6.2)? (s/n): ");
        respuesta = console.nextLine();
        if (respuesta.trim().equalsIgnoreCase("s")) {
            pruebasDeLimites();
        }

        System.out.print("digite el nombre de su heroe: ");
        nombre = console.nextLine().trim();
        if (nombre.isEmpty()) {
            nombre = "Aragorn";
        }

        Personaje heroe = new Personaje(nombre, 100, 15, 50.0);

        // CASO A: si se quita el comentario no compila, el error que sale es:
        // 'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'
        // heroe.puntosVida = 200;
        // System.out.println("Vida: " + heroe.puntosVida);
        // tampoco se puede subir el nivel a mano porque setNivel es protected:
        // heroe.setNivel(5);
        // 'setNivel(int)' has protected access in 'co.edu.puj.rpg.modelo.Personaje'

        Enemigo[] enemigos = new Enemigo[3];
        enemigos[0] = new Enemigo("Goblin", 45, 8, 1, 20.0, 40);
        enemigos[1] = new Enemigo("Orco", 80, 12, 2, 35.0, 60);
        enemigos[2] = new Enemigo("Troll", 110, 15, 3, 60.0, 80);

        for (int i = 0; i < enemigos.length; i++) {
            Enemigo enemigo = enemigos[i];
            historial = new StringBuilder();
            ronda = 1;

            System.out.println();
            System.out.printf("======== combate %d de %d ========%n", i + 1, enemigos.length);
            System.out.printf("aparece un %s de nivel %d con %d PV%n", enemigo.getNombre(), enemigo.getNivel(), enemigo.getPuntosVida());

            do {
                System.out.println();
                System.out.printf("--- ronda %d --- %s nivel %d: %d/%d PV, oro %.2f | %s: %d/%d PV%n", ronda,
                        heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.getOro(),
                        enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
                System.out.println("1. Atacar");
                System.out.println("2. Usar Pocion");
                System.out.println("3. Ver Estado");
                System.out.println("4. Huir");
                System.out.println("5. Tienda");
                System.out.print("elija una opcion: ");

                // si no escribe un numero se descarta lo que escribio y queda una opcion invalida
                if (console.hasNextInt()) {
                    opcion = console.nextInt();
                } else {
                    console.next();
                    opcion = 0;
                }

                pasoTurno = false;

                if (opcion < 1 || opcion > 5) {
                    System.out.println("ERROR: opcion no valida, digite un numero del 1 al 5");
                } else {
                    switch (opcion) {
                        case 1:
                            vidaAntes = enemigo.getPuntosVida();
                            heroe.atacar(enemigo);
                            danoHecho = vidaAntes - enemigo.getPuntosVida();

                            if (heroe.isUltimoGolpeCritico()) {
                                historial.append("ronda " + ronda + ": " + heroe.getNombre() + " le hace " + danoHecho + " de dano a " + enemigo.getNombre() + " (golpe critico)\n");
                            } else {
                                historial.append("ronda " + ronda + ": " + heroe.getNombre() + " le hace " + danoHecho + " de dano a " + enemigo.getNombre() + "\n");
                            }

                            // si lo derroto se le da la recompensa: oro y experiencia
                            if (!enemigo.isEstaVivo()) {
                                historial.append("ronda " + ronda + ": " + enemigo.getNombre() + " fue derrotado\n");

                                System.out.println();
                                System.out.println("derroto a " + enemigo.getNombre() + "!!");
                                heroe.setOro(heroe.getOro() + enemigo.getOro());
                                System.out.printf("recibe %.2f de oro, ahora tiene %.2f%n", enemigo.getOro(), heroe.getOro());
                                historial.append("ronda " + ronda + ": " + heroe.getNombre() + " recibe " + String.format("%.2f", enemigo.getOro()) + " de oro\n");

                                nivelAntes = heroe.getNivel();
                                heroe.ganarExperiencia(enemigo.getExperiencia());
                                historial.append("ronda " + ronda + ": " + heroe.getNombre() + " gana " + enemigo.getExperiencia() + " de experiencia\n");
                                if (heroe.getNivel() > nivelAntes) {
                                    historial.append("ronda " + ronda + ": " + heroe.getNombre() + " sube a nivel " + heroe.getNivel() + "\n");
                                }
                            }
                            pasoTurno = true;
                            break;
                        case 2:
                            if (heroe.getPuntosVida() == heroe.getVidaMaxima()) {
                                System.out.println("ya tiene la vida llena, no hace falta la pocion");
                            } else {
                                vidaAntes = heroe.getPuntosVida();
                                pocionesAntes = heroe.getPocionesGrandes();
                                heroe.usarPocion(curacionPocion);

                                // si la cantidad de pociones bajo es porque si se la tomo
                                if (heroe.getPocionesGrandes() < pocionesAntes) {
                                    historial.append("ronda " + ronda + ": " + heroe.getNombre() + " se cura " + (heroe.getPuntosVida() - vidaAntes) + " PV\n");
                                    pasoTurno = true;
                                }
                            }
                            break;
                        case 3:
                            if (heroe.isEstaVivo()) {
                                estado = "en pie";
                            } else {
                                estado = "derrotado";
                            }
                            System.out.println();
                            System.out.println(" -------- estado del heroe --------");
                            System.out.printf("nombre      : %s%n", heroe.getNombre());
                            System.out.printf("estado      : %s%n", estado);
                            System.out.printf("nivel       : %d%n", heroe.getNivel());
                            System.out.printf("experiencia : %d%n", heroe.getExperiencia());
                            System.out.printf("vida        : %d / %d%n", heroe.getPuntosVida(), heroe.getVidaMaxima());
                            System.out.printf("ataque      : %d%n", heroe.getPuntosAtaque());
                            System.out.printf("oro         : %.2f%n", heroe.getOro());
                            System.out.printf("pociones    : %d%n", heroe.getPocionesGrandes());
                            System.out.println(" -------- estado del enemigo --------");
                            System.out.printf("nombre      : %s%n", enemigo.getNombre());
                            System.out.printf("nivel       : %d%n", enemigo.getNivel());
                            System.out.printf("vida        : %d / %d%n", enemigo.getPuntosVida(), enemigo.getVidaMaxima());
                            System.out.printf("ataque      : %d%n", enemigo.getPuntosAtaque());
                            break;
                        case 4:
                            System.out.println(heroe.getNombre() + " huye del combate");
                            historial.append("ronda " + ronda + ": " + heroe.getNombre() + " huye del combate\n");
                            huyo = true;
                            break;
                        case 5:
                            do {
                                System.out.println();
                                System.out.println(" -------- tienda --------");
                                System.out.printf("oro disponible: %.2f%n", heroe.getOro());
                                System.out.printf("1. Pocion Grande de Salud (+%d PV) ..... %.2f de oro%n", curacionPocion, precioPocion);
                                System.out.println("2. Salir de la tienda");
                                System.out.print("elija una opcion: ");

                                if (console.hasNextInt()) {
                                    opcionTienda = console.nextInt();
                                } else {
                                    console.next();
                                    opcionTienda = 0;
                                }

                                if (opcionTienda == 1) {
                                    // se calcula el saldo nuevo y se manda al setter, si queda negativo lo rechaza
                                    oroAntes = heroe.getOro();
                                    nuevoSaldo = oroAntes - precioPocion;
                                    heroe.setOro(nuevoSaldo);

                                    // si el oro bajo es porque el setter acepto la compra, si no lo rechazo
                                    if (heroe.getOro() < oroAntes) {
                                        heroe.agregarPocionGrande();
                                        System.out.printf("compro una Pocion Grande de Salud, le quedan %.2f de oro%n", heroe.getOro());
                                        historial.append("tienda: " + heroe.getNombre() + " compra una Pocion Grande de Salud\n");
                                    } else {
                                        System.out.println("no se entrego la pocion");
                                        historial.append("tienda: compra rechazada por saldo insuficiente\n");
                                    }
                                } else if (opcionTienda == 2) {
                                    System.out.println("sale de la tienda");
                                } else {
                                    System.out.println("ERROR: opcion no valida, digite 1 o 2");
                                }
                            } while (opcionTienda != 2);
                            break;
                        default:
                            System.out.println("ERROR: opcion no valida");
                    }
                }

                // el enemigo solo ataca si el heroe gasto el turno (atacar o pocion) y el enemigo sigue vivo
                if (pasoTurno && enemigo.isEstaVivo()) {
                    vidaAntes = heroe.getPuntosVida();
                    heroe.recibirDano(enemigo.getPuntosAtaque());
                    System.out.printf("%s ataca a %s y le hace %d de dano, le quedan %d PV%n",
                            enemigo.getNombre(), heroe.getNombre(), vidaAntes - heroe.getPuntosVida(), heroe.getPuntosVida());
                    historial.append("ronda " + ronda + ": " + enemigo.getNombre() + " le hace " + (vidaAntes - heroe.getPuntosVida()) + " de dano a " + heroe.getNombre() + "\n");

                    if (!heroe.isEstaVivo()) {
                        historial.append("ronda " + ronda + ": " + heroe.getNombre() + " fue derrotado\n");
                    }
                }

                // al final de cada ronda se imprime todo lo que lleva el historial
                if (pasoTurno || huyo) {
                    System.out.println();
                    System.out.println(" -------- historial de batalla --------");
                    System.out.print(historial.toString());
                    System.out.println(" --------------------------------------");
                    ronda++;
                }

            } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

            // si huyo o lo derrotaron no se sigue con el siguiente enemigo
            if (huyo || !heroe.isEstaVivo()) {
                break;
            }
        }

        System.out.println();
        System.out.println(" -------- fin del juego --------");
        if (!heroe.isEstaVivo()) {
            System.out.println(heroe.getNombre() + " fue derrotado en la arena");
        } else if (huyo) {
            System.out.println(heroe.getNombre() + " escapo de la arena");
        } else {
            System.out.println(heroe.getNombre() + " derroto a todos los enemigos de la arena");
        }
        System.out.printf("nivel: %d | vida: %d/%d | oro: %.2f%n",
                heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.getOro());

        console.close();
    }

    // CASO C: es el metodo de la guia, como los setters ya validan no se pasa de los limites
    public static void ejecutarAccion(Personaje heroe) {
        heroe.setPuntosVida(999999); // Excede vidaMaxima
        System.out.printf("setPuntosVida(999999) -> vida: %d/%d, esta vivo: %b%n", heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.isEstaVivo());
        heroe.setPuntosVida(-100); // Vida negativa
        System.out.printf("setPuntosVida(-100)   -> vida: %d/%d, esta vivo: %b%n", heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.isEstaVivo());
    }

    // punto 6.2: pruebas manuales con los valores -10, 0 y 100000
    public static void pruebasDeLimites() {
        System.out.println();
        System.out.println(" -------- pruebas setPuntosVida (vida maxima 100) --------");
        Personaje prueba1 = new Personaje("Prueba", 100, 10, 50.0);
        prueba1.setPuntosVida(-10);
        System.out.printf("setPuntosVida(-10)     -> vida: %d, esta vivo: %b%n", prueba1.getPuntosVida(), prueba1.isEstaVivo());
        prueba1.setPuntosVida(0);
        System.out.printf("setPuntosVida(0)       -> vida: %d, esta vivo: %b%n", prueba1.getPuntosVida(), prueba1.isEstaVivo());
        prueba1.setPuntosVida(100000);
        System.out.printf("setPuntosVida(100000)  -> vida: %d, esta vivo: %b%n", prueba1.getPuntosVida(), prueba1.isEstaVivo());

        System.out.println(" -------- pruebas setOro (empieza con 50.00) --------");
        Personaje prueba2 = new Personaje("Prueba", 100, 10, 50.0);
        prueba2.setOro(-10);
        System.out.printf("setOro(-10)            -> oro: %.2f%n", prueba2.getOro());
        prueba2.setOro(0);
        System.out.printf("setOro(0)              -> oro: %.2f%n", prueba2.getOro());
        prueba2.setOro(100000);
        System.out.printf("setOro(100000)         -> oro: %.2f%n", prueba2.getOro());

        // setNivel no se puede probar desde aca porque es protected y Main esta en otro paquete,
        // el nivel solo cambia con ganarExperiencia
        System.out.println(" -------- prueba de nivel con ganarExperiencia --------");
        Personaje prueba3 = new Personaje("Prueba", 100, 10, 50.0);
        prueba3.ganarExperiencia(-10);
        System.out.printf("ganarExperiencia(-10)  -> nivel: %d, experiencia: %d%n", prueba3.getNivel(), prueba3.getExperiencia());
        prueba3.ganarExperiencia(100);
        System.out.printf("ganarExperiencia(100)  -> nivel: %d, experiencia: %d%n", prueba3.getNivel(), prueba3.getExperiencia());

        System.out.println(" -------- caso B: constructor corregido con vida -50 --------");
        Personaje prueba4 = new Personaje(-50);
        System.out.printf("new Personaje(-50)     -> vida: %d, esta vivo: %b%n", prueba4.getPuntosVida(), prueba4.isEstaVivo());

        System.out.println(" -------- caso C: ejecutarAccion --------");
        Personaje prueba5 = new Personaje("Aragorn", 100, 15, 50.0);
        ejecutarAccion(prueba5);
        System.out.println();
    }
}
