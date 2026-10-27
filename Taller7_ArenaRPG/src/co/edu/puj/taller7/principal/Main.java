package co.edu.puj.taller7.principal;

import co.edu.puj.taller7.modelo.Enemigo;
import co.edu.puj.taller7.modelo.Guerrero;
import co.edu.puj.taller7.modelo.Mago;
import co.edu.puj.taller7.modelo.Personaje;
import co.edu.puj.taller7.modelo.items.Inventario;
import co.edu.puj.taller7.modelo.items.Pocion;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // tablero de 5x5, el cofre queda en el centro
    private static final int FILAS_TABLERO = 5;
    private static final int COLUMNAS_TABLERO = 5;
    private static final int COFRE_X = 2;
    private static final int COFRE_Y = 2;
    private static final char CASILLA_VACIA = '.';
    private static final char SIMBOLO_COFRE = 'C';

    // opciones del menu de cada heroe
    private static final int OPCION_ATACAR = 1;
    private static final int OPCION_USAR_INVENTARIO = 2;
    private static final int OPCION_MOVER = 3;
    // lo que devuelve leerNumero cuando escriben letras
    private static final int NUMERO_INVALIDO = -1;

    // teclas de movimiento
    private static final String ARRIBA = "w";
    private static final String ABAJO = "s";
    private static final String IZQUIERDA = "a";
    private static final String DERECHA = "d";

    private static final int CURACION_POCION_PEQUENA = 30;
    private static final int CURACION_POCION_GRANDE = 60;

    // el enemigo usa el ataque especial el 30% de las veces
    private static final double PROBABILIDAD_ATAQUE_ESPECIAL = 0.3;
    // mana que recuperan los magos al final de cada turno
    private static final int MANA_POR_TURNO = 5;

    // estado de la partida
    private final Scanner console = new Scanner(System.in);
    // el tablero es tablero[posicionX][posicionY], la x es la fila y la y la columna
    private final char[][] tablero = new char[FILAS_TABLERO][COLUMNAS_TABLERO];
    // escuadron = la lista de heroes de la guia, se usa List y no variables sueltas (p1, p2...)
    private final List<Personaje> escuadron = new ArrayList<>();
    private final List<Personaje> enemigos = new ArrayList<>();
    private final Inventario<Pocion> inventario = new Inventario<>();
    private Personaje heroePrincipal;
    private boolean llegoAlCofre = false;
    private int turno = 1;

    public static void main(String[] args) {
        // ERROR DE COMPILACION (5.1): si se quita el comentario no compila porque Personaje es abstracta
        // Personaje p = new Personaje();
        // error: Personaje is abstract; cannot be instantiated
        // corregido: se instancia una subclase concreta
        // Personaje p = new Guerrero("Boromir", 100, 14, 3, 3, 8);

        // ClassCastException (5.1): compila, pero al correr revienta porque escuadron.get(0) es un Guerrero
        // Mago mago = (Mago) escuadron.get(0);
        // java.lang.ClassCastException: class co.edu.puj.taller7.modelo.Guerrero cannot be cast to class co.edu.puj.taller7.modelo.Mago
        // por eso en mostrarEstadisticas se pregunta con instanceof antes de castear

        new Main().jugar();
    }

    public void jugar() {
        // 1. inicializacion: el mapa, la lista de heroes, la de enemigos y el inventario
        inicializar();
        mostrarIntroduccion();

        // 2. ciclo principal: sigue mientras haya heroes vivos, queden enemigos y no se haya llegado al cofre
        do {
            // 3. renderizado: tablero y estadisticas
            actualizarTablero(tablero, escuadron, enemigos, COFRE_X, COFRE_Y);
            System.out.println();
            System.out.printf("================ turno %d ================%n", turno);
            mostrarTablero(tablero);
            System.out.println("G: guerrero, M: mago, E: enemigo, C: cofre, .: vacio");
            mostrarEstadisticas(escuadron, enemigos);

            // 4. turno del jugador: cada heroe vivo hace una accion
            turnoDelJugador();

            // 5. turno de los enemigos: atacan solos al heroe que tenga menos vida
            if (!llegoAlCofre && quedanEnemigosVivos(enemigos)) {
                turnoDeLosEnemigos();
            }
            recuperarManaDeLosMagos();

            // 6. limpieza de bajas: se quitan de las listas los que quedaron sin vida
            escuadron.removeIf(personaje -> !personaje.estaVivo());
            enemigos.removeIf(personaje -> !personaje.estaVivo());

            turno++;
        } while (!escuadron.isEmpty() && sumarVidaEscuadron() > 0 && !enemigos.isEmpty() && !llegoAlCofre);

        mostrarResultadoFinal();
        console.close();
    }

    public void inicializar() {
        escuadron.add(new Guerrero("Thorin", 120, 15, 0, 0, 10));
        escuadron.add(new Mago("Gandalf", 80, 25, 1, 1, 50));
        // el heroe principal es el primero del escuadron, es el que tiene que llegar al cofre
        heroePrincipal = escuadron.get(0);

        enemigos.add(new Enemigo("Goblin", 50, 12, 0, 4, 2));
        enemigos.add(new Enemigo("Orco", 70, 14, 4, 0, 4));
        enemigos.add(new Enemigo("Troll", 90, 16, 4, 4, 6));

        inventario.agregar(new Pocion("Pocion Pequena", CURACION_POCION_PEQUENA));
        inventario.agregar(new Pocion("Pocion Pequena", CURACION_POCION_PEQUENA));
        inventario.agregar(new Pocion("Pocion Grande", CURACION_POCION_GRANDE));
    }

    public void mostrarIntroduccion() {
        // con genericos no hay que castear, el compilador ya sabe que obtener() devuelve una Pocion
        Pocion primera = inventario.obtener(0);
        // sin genericos tocaria asi: Pocion primera = (Pocion) inventario.obtener(0);
        // inventario.agregar("espada"); -> no compila, el inventario solo acepta Pocion

        System.out.println(" -------- arena RPG: batalla en el tablero --------");
        System.out.printf("el escuadron tiene %d heroes y hay %d enemigos en el mapa%n", escuadron.size(), enemigos.size());
        System.out.printf("inventario: %d pociones, la primera es una %s (+%d PV)%n",
                inventario.cantidad(), primera.getNombre(), primera.getCuracion());
        System.out.println("para ganar: derrotar a todos los enemigos o llevar a " + heroePrincipal.getNombre() + " hasta el cofre [C]");
    }

    public void turnoDelJugador() {
        for (Personaje heroe : escuadron) {
            // si ya no quedan enemigos o ya llego al cofre no tiene sentido seguir con el siguiente heroe
            if (!quedanEnemigosVivos(enemigos) || llegoAlCofre) {
                break;
            }
            if (!heroe.estaVivo()) {
                continue;
            }

            boolean accionHecha = false;
            do {
                mostrarMenuHeroe(heroe);
                int opcion = leerNumero(console);

                switch (opcion) {
                    case OPCION_ATACAR:
                        accionHecha = atacarEnemigo(heroe);
                        break;
                    case OPCION_USAR_INVENTARIO:
                        accionHecha = usarPocion(heroe);
                        break;
                    case OPCION_MOVER:
                        accionHecha = moverHeroe(heroe);
                        break;
                    default:
                        System.out.printf("ERROR: opcion no valida, digite %d, %d o %d%n", OPCION_ATACAR, OPCION_USAR_INVENTARIO, OPCION_MOVER);
                }
            } while (!accionHecha);
        }
    }

    public void mostrarMenuHeroe(Personaje heroe) {
        System.out.println();
        System.out.printf("--- turno de %s (%d/%d PV) en [%d][%d] ---%n", heroe.getNombre(),
                heroe.getVida(), heroe.getVidaMaxima(), heroe.getPosicionX(), heroe.getPosicionY());
        System.out.printf("%d. Atacar%n", OPCION_ATACAR);
        System.out.printf("%d. Usar Inventario (%d pociones)%n", OPCION_USAR_INVENTARIO, inventario.cantidad());
        System.out.printf("%d. Mover en Tablero%n", OPCION_MOVER);
        System.out.print("elija una opcion: ");
    }

    // devuelve true si el ataque se hizo, false si eligio un enemigo que no se puede atacar
    public boolean atacarEnemigo(Personaje heroe) {
        System.out.println("enemigos:");
        for (int i = 0; i < enemigos.size(); i++) {
            Personaje enemigo = enemigos.get(i);
            if (enemigo.estaVivo()) {
                System.out.printf("%d. %s (%d/%d PV) en [%d][%d]%n", i + 1, enemigo.getNombre(),
                        enemigo.getVida(), enemigo.getVidaMaxima(), enemigo.getPosicionX(), enemigo.getPosicionY());
            }
        }
        System.out.print("elija el enemigo: ");
        int indice = leerNumero(console) - 1;

        // se valida el indice antes del get para que no salga IndexOutOfBoundsException
        if (indice < 0 || indice >= enemigos.size()) {
            System.out.println("ERROR: ese enemigo no existe");
            return false;
        }
        if (!enemigos.get(indice).estaVivo()) {
            System.out.println("ERROR: ese enemigo ya fue derrotado");
            return false;
        }

        Personaje objetivo = enemigos.get(indice);
        heroe.realizarAtaqueEspecial(objetivo);
        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " fue derrotado!!");
        }
        // se actualiza la matriz de una vez para que la baja se vea en el mapa
        actualizarTablero(tablero, escuadron, enemigos, COFRE_X, COFRE_Y);
        mostrarTablero(tablero);
        return true;
    }

    // devuelve true si el heroe se tomo una pocion
    public boolean usarPocion(Personaje heroe) {
        if (inventario.estaVacio()) {
            System.out.println("el inventario esta vacio, no quedan pociones");
            return false;
        }
        if (heroe.getVida() == heroe.getVidaMaxima()) {
            System.out.println(heroe.getNombre() + " ya tiene la vida llena, no hace falta la pocion");
            return false;
        }

        System.out.println("pociones:");
        for (int i = 0; i < inventario.cantidad(); i++) {
            Pocion pocion = inventario.obtener(i);
            System.out.printf("%d. %s (+%d PV)%n", i + 1, pocion.getNombre(), pocion.getCuracion());
        }
        System.out.print("elija la pocion: ");
        int indice = leerNumero(console) - 1;

        if (indice < 0 || indice >= inventario.cantidad()) {
            System.out.println("ERROR: esa pocion no existe");
            return false;
        }

        // se saca del inventario porque se gasta
        Pocion pocion = inventario.remover(indice);
        System.out.println(heroe.getNombre() + " se toma una " + pocion.getNombre());
        heroe.curar(pocion.getCuracion());
        return true;
    }

    // devuelve true si el heroe si se movio
    public boolean moverHeroe(Personaje heroe) {
        System.out.printf("direccion (%s: arriba, %s: abajo, %s: izquierda, %s: derecha): ", ARRIBA, ABAJO, IZQUIERDA, DERECHA);
        String direccion = console.nextLine().trim().toLowerCase();
        int nuevaX = heroe.getPosicionX();
        int nuevaY = heroe.getPosicionY();

        switch (direccion) {
            case ARRIBA:
                nuevaX = nuevaX - 1;
                break;
            case ABAJO:
                nuevaX = nuevaX + 1;
                break;
            case IZQUIERDA:
                nuevaY = nuevaY - 1;
                break;
            case DERECHA:
                nuevaY = nuevaY + 1;
                break;
            default:
                System.out.println("ERROR: direccion no valida");
                // si la direccion no era valida las coordenadas no cambian
                return false;
        }

        // primero se revisan los limites, si no tablero[5][..] lanza ArrayIndexOutOfBoundsException
        if (nuevaX < 0 || nuevaX >= tablero.length || nuevaY < 0 || nuevaY >= tablero[0].length) {
            System.out.println("no se puede salir del tablero");
            return false;
        }
        if (tablero[nuevaX][nuevaY] != CASILLA_VACIA && tablero[nuevaX][nuevaY] != SIMBOLO_COFRE) {
            System.out.println("esa casilla esta ocupada por [" + tablero[nuevaX][nuevaY] + "]");
            return false;
        }

        heroe.mover(nuevaX, nuevaY);
        System.out.printf("%s se mueve a [%d][%d]%n", heroe.getNombre(), nuevaX, nuevaY);
        actualizarTablero(tablero, escuadron, enemigos, COFRE_X, COFRE_Y);
        mostrarTablero(tablero);

        if (nuevaX == COFRE_X && nuevaY == COFRE_Y) {
            if (heroe == heroePrincipal) {
                System.out.println(heroe.getNombre() + " abre el cofre!!");
                llegoAlCofre = true;
            } else {
                System.out.println("solo " + heroePrincipal.getNombre() + " puede abrir el cofre");
            }
        }
        return true;
    }

    public void turnoDeLosEnemigos() {
        System.out.println();
        System.out.println("--- turno de los enemigos ---");
        for (Personaje enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }

            Personaje objetivo = buscarObjetivo(escuadron);
            // si devuelve null es porque ya no queda ningun heroe vivo
            if (objetivo == null) {
                break;
            }

            if (Math.random() < PROBABILIDAD_ATAQUE_ESPECIAL) {
                enemigo.realizarAtaqueEspecial(objetivo);
            } else {
                enemigo.atacar(objetivo);
            }

            if (!objetivo.estaVivo()) {
                System.out.println(objetivo.getNombre() + " cayo en combate!!");
            }
        }
    }

    // los magos recuperan un poco de mana al final del turno
    public void recuperarManaDeLosMagos() {
        for (Personaje heroe : escuadron) {
            if (heroe instanceof Mago && heroe.estaVivo()) {
                Mago mago = (Mago) heroe;
                mago.recuperarMana(MANA_POR_TURNO);
            }
        }
    }

    public int sumarVidaEscuadron() {
        int sumaVida = 0;
        for (Personaje heroe : escuadron) {
            sumaVida = sumaVida + heroe.getVida();
        }
        return sumaVida;
    }

    public void mostrarResultadoFinal() {
        actualizarTablero(tablero, escuadron, enemigos, COFRE_X, COFRE_Y);
        System.out.println();
        System.out.println(" -------- fin de la batalla --------");
        mostrarTablero(tablero);

        if (llegoAlCofre) {
            System.out.println("VICTORIA: " + heroePrincipal.getNombre() + " llego al cofre y se llevo el tesoro");
        } else if (enemigos.isEmpty()) {
            System.out.println("VICTORIA: todos los enemigos fueron eliminados");
        } else {
            System.out.println("DERROTA: el escuadron fue eliminado");
        }
        System.out.printf("turnos jugados: %d | heroes en pie: %d | enemigos en pie: %d | pociones sin usar: %d%n",
                turno - 1, escuadron.size(), enemigos.size(), inventario.cantidad());
    }

    // en la guia es public void, aca es static porque no usa el estado de la partida, solo la matriz que recibe
    public static void mostrarTablero(char[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print("[" + matriz[i][j] + "]");
            }
            System.out.println(); // Salto de linea por cada fila
        }
    }

    // se limpia toda la matriz con '.' y se vuelve a dibujar el cofre y los que siguen vivos,
    // asi ninguna celda queda vacia ('\0') y los muertos desaparecen del mapa en el momento
    public static void actualizarTablero(char[][] tablero, List<Personaje> escuadron, List<Personaje> enemigos, int cofreX, int cofreY) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = CASILLA_VACIA;
            }
        }

        tablero[cofreX][cofreY] = SIMBOLO_COFRE;

        for (Personaje enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                tablero[enemigo.getPosicionX()][enemigo.getPosicionY()] = enemigo.getSimbolo();
            }
        }

        for (Personaje heroe : escuadron) {
            if (heroe.estaVivo()) {
                tablero[heroe.getPosicionX()][heroe.getPosicionY()] = heroe.getSimbolo();
            }
        }
    }

    public static void mostrarEstadisticas(List<Personaje> escuadron, List<Personaje> enemigos) {
        System.out.println();
        System.out.printf("%-10s %-9s %-9s %-14s %-8s%n", "nombre", "tipo", "vida", "mana/escudo", "posicion");
        for (Personaje heroe : escuadron) {
            String extra;
            // se pregunta con instanceof antes de castear, si se castea un Guerrero a Mago sale ClassCastException
            if (heroe instanceof Mago) {
                Mago mago = (Mago) heroe;
                extra = "mana " + mago.getPuntosMana() + "/" + mago.getManaMaximo();
            } else if (heroe instanceof Guerrero) {
                Guerrero guerrero = (Guerrero) heroe;
                extra = "escudo " + guerrero.getFuerzaEscudo();
            } else {
                extra = "-";
            }
            mostrarFilaEstadistica(heroe, extra);
        }

        for (Personaje enemigo : enemigos) {
            String extra;
            if (enemigo instanceof Enemigo) {
                Enemigo enemigoConArmadura = (Enemigo) enemigo;
                extra = "armadura " + enemigoConArmadura.getArmadura();
            } else {
                extra = "-";
            }
            mostrarFilaEstadistica(enemigo, extra);
        }
    }

    public static void mostrarFilaEstadistica(Personaje personaje, String extra) {
        System.out.printf("%-10s %-9s %3d/%-5d %-14s [%d][%d]%n", personaje.getNombre(), personaje.getClass().getSimpleName(),
                personaje.getVida(), personaje.getVidaMaxima(), extra, personaje.getPosicionX(), personaje.getPosicionY());
    }

    // la "inteligencia" del enemigo: busca al heroe vivo con menos vida, si no queda ninguno devuelve null
    public static Personaje buscarObjetivo(List<Personaje> escuadron) {
        Personaje objetivo = null;
        for (Personaje heroe : escuadron) {
            if (heroe.estaVivo()) {
                if (objetivo == null || heroe.getVida() < objetivo.getVida()) {
                    objetivo = heroe;
                }
            }
        }
        return objetivo;
    }

    // las bajas se quitan al final del turno, entonces en la mitad del turno toca revisar si queda alguno vivo
    public static boolean quedanEnemigosVivos(List<Personaje> enemigos) {
        for (Personaje enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                return true;
            }
        }
        return false;
    }

    // lee un numero de la consola, si escribe letras devuelve NUMERO_INVALIDO y queda como opcion invalida
    public static int leerNumero(Scanner console) {
        int numero = NUMERO_INVALIDO;
        if (console.hasNextInt()) {
            numero = console.nextInt();
        }
        // se limpia lo que quedo en la linea (el enter o las letras) para que no afecte la siguiente lectura
        console.nextLine();
        return numero;
    }
}
