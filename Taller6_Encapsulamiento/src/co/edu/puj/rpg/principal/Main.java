package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;
import java.util.Scanner;

public class Main {

    // opciones del menu de combate
    private static final int OPCION_ATACAR = 1;
    private static final int OPCION_USAR_POCION = 2;
    private static final int OPCION_VER_ESTADO = 3;
    private static final int OPCION_HUIR = 4;
    private static final int OPCION_TIENDA = 5;
    private static final int OPCION_INVALIDA = 0;

    // opciones del submenu de la tienda
    private static final int TIENDA_COMPRAR_POCION = 1;
    private static final int TIENDA_SALIR = 2;

    // datos iniciales del heroe
    private static final String NOMBRE_HEROE_POR_DEFECTO = "Aragorn";
    private static final int VIDA_MAXIMA_HEROE = 100;
    private static final int ATAQUE_HEROE = 15;
    private static final double ORO_INICIAL_HEROE = 50.0;

    // pocion grande de la tienda
    private static final int CURACION_POCION = 50;
    private static final double PRECIO_POCION = 30.0;

    // valores limite del punto 6.2
    private static final int VALOR_NEGATIVO = -10;
    private static final int VALOR_CERO = 0;
    private static final int VALOR_EXCESIVO = 100000;
    private static final int VIDA_NEGATIVA_CASO_B = -50;
    private static final int VIDA_EXCESIVA_CASO_C = 999999;
    private static final int VIDA_NEGATIVA_CASO_C = -100;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        System.out.println(" -------- simulador de batalla: arena de heroes --------");
        System.out.print("desea correr primero las pruebas de limites (punto 6.2)? (s/n): ");
        if (console.nextLine().trim().equalsIgnoreCase("s")) {
            pruebasDeLimites();
        }

        Personaje heroe = new Personaje(pedirNombre(console), VIDA_MAXIMA_HEROE, ATAQUE_HEROE, ORO_INICIAL_HEROE);

        // CASO A: si se quita el comentario no compila, el error que sale es:
        // 'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'
        // heroe.puntosVida = 200;
        // System.out.println("Vida: " + heroe.puntosVida);
        // tampoco se puede subir el nivel a mano porque setNivel es protected:
        // heroe.setNivel(5);
        // 'setNivel(int)' has protected access in 'co.edu.puj.rpg.modelo.Personaje'

        Enemigo[] enemigos = crearEnemigos();
        boolean huyo = false;

        for (int i = 0; i < enemigos.length && !huyo && heroe.isEstaVivo(); i++) {
            System.out.println();
            System.out.printf("======== combate %d de %d ========%n", i + 1, enemigos.length);
            huyo = combatir(console, heroe, enemigos[i]);
        }

        mostrarResultadoFinal(heroe, huyo);
        console.close();
    }

    public static String pedirNombre(Scanner console) {
        System.out.print("digite el nombre de su heroe: ");
        String nombre = console.nextLine().trim();
        if (nombre.isEmpty()) {
            return NOMBRE_HEROE_POR_DEFECTO;
        }
        return nombre;
    }

    public static Enemigo[] crearEnemigos() {
        return new Enemigo[]{
                new Enemigo("Goblin", 45, 8, 1, 20.0, 40),
                new Enemigo("Orco", 80, 12, 2, 35.0, 60),
                new Enemigo("Troll", 110, 15, 3, 60.0, 80)
        };
    }

    // un combate completo contra un enemigo, devuelve true si el heroe huyo
    public static boolean combatir(Scanner console, Personaje heroe, Enemigo enemigo) {
        StringBuilder historial = new StringBuilder();
        int ronda = 1;
        boolean huyo = false;

        System.out.printf("aparece un %s de nivel %d con %d PV%n", enemigo.getNombre(), enemigo.getNivel(), enemigo.getPuntosVida());

        do {
            mostrarMenu(ronda, heroe, enemigo);
            int opcion = leerOpcion(console);
            boolean pasoTurno = false;

            // if-else para la entrada invalida, asi el programa no se interrumpe
            if (opcion < OPCION_ATACAR || opcion > OPCION_TIENDA) {
                System.out.printf("ERROR: opcion no valida, digite un numero del %d al %d%n", OPCION_ATACAR, OPCION_TIENDA);
            } else {
                switch (opcion) {
                    case OPCION_ATACAR:
                        atacar(heroe, enemigo, historial, ronda);
                        pasoTurno = true;
                        break;
                    case OPCION_USAR_POCION:
                        pasoTurno = usarPocion(heroe, historial, ronda);
                        break;
                    case OPCION_VER_ESTADO:
                        mostrarEstado(heroe, enemigo);
                        break;
                    case OPCION_HUIR:
                        System.out.println(heroe.getNombre() + " huye del combate");
                        registrar(historial, ronda, heroe.getNombre() + " huye del combate");
                        huyo = true;
                        break;
                    case OPCION_TIENDA:
                        abrirTienda(console, heroe, historial);
                        break;
                    default:
                        System.out.println("ERROR: opcion no valida");
                }
            }

            // el enemigo solo ataca si el heroe gasto el turno (atacar o pocion) y el enemigo sigue vivo
            if (pasoTurno && enemigo.isEstaVivo()) {
                contraatacar(heroe, enemigo, historial, ronda);
            }

            // al final de cada ronda se imprime todo lo que lleva el historial
            if (pasoTurno || huyo) {
                imprimirHistorial(historial);
                ronda++;
            }

        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        return huyo;
    }

    public static void mostrarMenu(int ronda, Personaje heroe, Enemigo enemigo) {
        System.out.println();
        System.out.printf("--- ronda %d --- %s nivel %d: %d/%d PV, oro %.2f | %s: %d/%d PV%n", ronda,
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.getOro(),
                enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
        System.out.printf("%d. Atacar%n", OPCION_ATACAR);
        System.out.printf("%d. Usar Pocion%n", OPCION_USAR_POCION);
        System.out.printf("%d. Ver Estado%n", OPCION_VER_ESTADO);
        System.out.printf("%d. Huir%n", OPCION_HUIR);
        System.out.printf("%d. Tienda%n", OPCION_TIENDA);
        System.out.print("elija una opcion: ");
    }

    public static void atacar(Personaje heroe, Enemigo enemigo, StringBuilder historial, int ronda) {
        int vidaAntes = enemigo.getPuntosVida();
        heroe.atacar(enemigo);
        int danoHecho = vidaAntes - enemigo.getPuntosVida();

        String evento = heroe.getNombre() + " le hace " + danoHecho + " de dano a " + enemigo.getNombre();
        if (heroe.isUltimoGolpeCritico()) {
            evento = evento + " (golpe critico)";
        }
        registrar(historial, ronda, evento);

        if (!enemigo.isEstaVivo()) {
            darRecompensa(heroe, enemigo, historial, ronda);
        }
    }

    // si lo derroto se le da la recompensa: oro y experiencia
    public static void darRecompensa(Personaje heroe, Enemigo enemigo, StringBuilder historial, int ronda) {
        registrar(historial, ronda, enemigo.getNombre() + " fue derrotado");

        System.out.println();
        System.out.println("derroto a " + enemigo.getNombre() + "!!");
        heroe.setOro(heroe.getOro() + enemigo.getOro());
        System.out.printf("recibe %.2f de oro, ahora tiene %.2f%n", enemigo.getOro(), heroe.getOro());
        registrar(historial, ronda, heroe.getNombre() + " recibe " + String.format("%.2f", enemigo.getOro()) + " de oro");

        int nivelAntes = heroe.getNivel();
        heroe.ganarExperiencia(enemigo.getExperiencia());
        registrar(historial, ronda, heroe.getNombre() + " gana " + enemigo.getExperiencia() + " de experiencia");
        if (heroe.getNivel() > nivelAntes) {
            registrar(historial, ronda, heroe.getNombre() + " sube a nivel " + heroe.getNivel());
        }
    }

    // devuelve true si el heroe si se tomo la pocion y gasto el turno
    public static boolean usarPocion(Personaje heroe, StringBuilder historial, int ronda) {
        if (heroe.getPuntosVida() == heroe.getVidaMaxima()) {
            System.out.println("ya tiene la vida llena, no hace falta la pocion");
            return false;
        }

        int vidaAntes = heroe.getPuntosVida();
        int pocionesAntes = heroe.getPocionesGrandes();
        heroe.usarPocion(CURACION_POCION);

        // si la cantidad de pociones bajo es porque si se la tomo
        if (heroe.getPocionesGrandes() < pocionesAntes) {
            registrar(historial, ronda, heroe.getNombre() + " se cura " + (heroe.getPuntosVida() - vidaAntes) + " PV");
            return true;
        }
        return false;
    }

    public static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        String estado;
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
    }

    public static void abrirTienda(Scanner console, Personaje heroe, StringBuilder historial) {
        int opcionTienda;
        do {
            System.out.println();
            System.out.println(" -------- tienda --------");
            System.out.printf("oro disponible: %.2f%n", heroe.getOro());
            System.out.printf("%d. Pocion Grande de Salud (+%d PV) ..... %.2f de oro%n", TIENDA_COMPRAR_POCION, CURACION_POCION, PRECIO_POCION);
            System.out.printf("%d. Salir de la tienda%n", TIENDA_SALIR);
            System.out.print("elija una opcion: ");
            opcionTienda = leerOpcion(console);

            if (opcionTienda == TIENDA_COMPRAR_POCION) {
                comprarPocion(heroe, historial);
            } else if (opcionTienda == TIENDA_SALIR) {
                System.out.println("sale de la tienda");
            } else {
                System.out.printf("ERROR: opcion no valida, digite %d o %d%n", TIENDA_COMPRAR_POCION, TIENDA_SALIR);
            }
        } while (opcionTienda != TIENDA_SALIR);
    }

    public static void comprarPocion(Personaje heroe, StringBuilder historial) {
        // se calcula el saldo nuevo y se manda al setter, si queda negativo lo rechaza
        double oroAntes = heroe.getOro();
        heroe.setOro(oroAntes - PRECIO_POCION);

        // si el oro bajo es porque el setter acepto la compra, si no lo rechazo
        if (heroe.getOro() < oroAntes) {
            heroe.agregarPocionGrande();
            System.out.printf("compro una Pocion Grande de Salud, le quedan %.2f de oro%n", heroe.getOro());
            historial.append("tienda: " + heroe.getNombre() + " compra una Pocion Grande de Salud\n");
        } else {
            System.out.println("no se entrego la pocion");
            historial.append("tienda: compra rechazada por saldo insuficiente\n");
        }
    }

    public static void contraatacar(Personaje heroe, Enemigo enemigo, StringBuilder historial, int ronda) {
        int vidaAntes = heroe.getPuntosVida();
        heroe.recibirDano(enemigo.getPuntosAtaque());
        int danoRecibido = vidaAntes - heroe.getPuntosVida();
        System.out.printf("%s ataca a %s y le hace %d de dano, le quedan %d PV%n",
                enemigo.getNombre(), heroe.getNombre(), danoRecibido, heroe.getPuntosVida());
        registrar(historial, ronda, enemigo.getNombre() + " le hace " + danoRecibido + " de dano a " + heroe.getNombre());

        if (!heroe.isEstaVivo()) {
            registrar(historial, ronda, heroe.getNombre() + " fue derrotado");
        }
    }

    // todos los eventos del combate se guardan con el mismo formato "ronda N: ..."
    public static void registrar(StringBuilder historial, int ronda, String evento) {
        historial.append("ronda ").append(ronda).append(": ").append(evento).append("\n");
    }

    public static void imprimirHistorial(StringBuilder historial) {
        System.out.println();
        System.out.println(" -------- historial de batalla --------");
        System.out.print(historial.toString());
        System.out.println(" --------------------------------------");
    }

    public static void mostrarResultadoFinal(Personaje heroe, boolean huyo) {
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
    }

    // si no escribe un numero se descarta lo que escribio y queda una opcion invalida
    public static int leerOpcion(Scanner console) {
        if (console.hasNextInt()) {
            return console.nextInt();
        }
        console.next();
        return OPCION_INVALIDA;
    }

    // CASO C: es el metodo de la guia, como los setters ya validan no se pasa de los limites
    public static void ejecutarAccion(Personaje heroe) {
        heroe.setPuntosVida(VIDA_EXCESIVA_CASO_C); // Excede vidaMaxima
        System.out.printf("setPuntosVida(%d) -> vida: %d/%d, esta vivo: %b%n", VIDA_EXCESIVA_CASO_C, heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.isEstaVivo());
        heroe.setPuntosVida(VIDA_NEGATIVA_CASO_C); // Vida negativa
        System.out.printf("setPuntosVida(%d)   -> vida: %d/%d, esta vivo: %b%n", VIDA_NEGATIVA_CASO_C, heroe.getPuntosVida(), heroe.getVidaMaxima(), heroe.isEstaVivo());
    }

    // punto 6.2: pruebas manuales con los valores -10, 0 y 100000
    public static void pruebasDeLimites() {
        int[] valoresLimite = {VALOR_NEGATIVO, VALOR_CERO, VALOR_EXCESIVO};

        System.out.println();
        System.out.printf(" -------- pruebas setPuntosVida (vida maxima %d) --------%n", VIDA_MAXIMA_HEROE);
        Personaje pruebaVida = crearPersonajeDePrueba();
        for (int valor : valoresLimite) {
            pruebaVida.setPuntosVida(valor);
            System.out.printf("%-22s -> vida: %d, esta vivo: %b%n", "setPuntosVida(" + valor + ")", pruebaVida.getPuntosVida(), pruebaVida.isEstaVivo());
        }

        System.out.printf(" -------- pruebas setOro (empieza con %.2f) --------%n", ORO_INICIAL_HEROE);
        Personaje pruebaOro = crearPersonajeDePrueba();
        for (int valor : valoresLimite) {
            pruebaOro.setOro(valor);
            System.out.printf("%-22s -> oro: %.2f%n", "setOro(" + valor + ")", pruebaOro.getOro());
        }

        // setNivel no se puede probar desde aca porque es protected y Main esta en otro paquete,
        // el nivel solo cambia con ganarExperiencia
        System.out.println(" -------- prueba de nivel con ganarExperiencia --------");
        Personaje pruebaNivel = crearPersonajeDePrueba();
        for (int puntos : new int[]{VALOR_NEGATIVO, Personaje.EXPERIENCIA_PARA_SUBIR}) {
            pruebaNivel.ganarExperiencia(puntos);
            System.out.printf("%-22s -> nivel: %d, experiencia: %d%n", "ganarExperiencia(" + puntos + ")", pruebaNivel.getNivel(), pruebaNivel.getExperiencia());
        }

        System.out.printf(" -------- caso B: constructor corregido con vida %d --------%n", VIDA_NEGATIVA_CASO_B);
        Personaje pruebaCasoB = new Personaje(VIDA_NEGATIVA_CASO_B);
        System.out.printf("%-22s -> vida: %d, esta vivo: %b%n", "new Personaje(" + VIDA_NEGATIVA_CASO_B + ")", pruebaCasoB.getPuntosVida(), pruebaCasoB.isEstaVivo());

        System.out.println(" -------- caso C: ejecutarAccion --------");
        ejecutarAccion(new Personaje(NOMBRE_HEROE_POR_DEFECTO, VIDA_MAXIMA_HEROE, ATAQUE_HEROE, ORO_INICIAL_HEROE));
        System.out.println();
    }

    public static Personaje crearPersonajeDePrueba() {
        return new Personaje("Prueba", VIDA_MAXIMA_HEROE, ATAQUE_HEROE, ORO_INICIAL_HEROE);
    }
}
