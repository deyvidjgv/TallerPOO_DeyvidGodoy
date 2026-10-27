package co.edu.puj.taller8.principal;

import co.edu.puj.taller8.modelo.equipo.Arma;
import co.edu.puj.taller8.modelo.grupo.Escuadron;
import co.edu.puj.taller8.modelo.grupo.Gremio;
import co.edu.puj.taller8.modelo.personaje.Personaje;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AplicacionArenaRPG {

    // opciones del menu principal
    private static final int OPCION_CREAR_PERSONAJE = 1;
    private static final int OPCION_CREAR_GREMIO = 2;
    private static final int OPCION_RECLUTAR = 3;
    private static final int OPCION_EQUIPAR_ARMA = 4;
    private static final int OPCION_MOSTRAR_ESTADO = 5;
    // estas tres no estan en la plantilla, se agregaron para probar la bidireccional,
    // el desequipar y la agregacion, por eso salir paso al 9
    private static final int OPCION_UNIR_A_GREMIO = 6;
    private static final int OPCION_DESEQUIPAR_ARMA = 7;
    private static final int OPCION_GESTIONAR_ESCUADRON = 8;
    private static final int OPCION_SALIR = 9;

    // submenu de la opcion 8
    private static final int OPCION_EXPULSAR = 1;
    private static final int OPCION_DISOLVER = 2;

    // en la lista de escuadrones el 0 es para crear uno nuevo
    private static final int CREAR_ESCUADRON_NUEVO = 0;
    // lo que devuelve leerNumero cuando escriben letras
    private static final int NUMERO_INVALIDO = -1;
    private static final String NOMBRE_ARMA_POR_DEFECTO = "Espada";

    // datos de las pruebas de relaciones
    private static final int NIVEL_ARAGORN = 3;
    private static final int NIVEL_LEGOLAS = 4;
    private static final int NIVEL_GIMLI = 2;
    private static final int DANIO_ANDURIL = 12;
    private static final int DANIO_PRUEBA = 50;
    private static final int HECHIZO_BARATO = 20;
    private static final int HECHIZO_CARO = 100;
    private static final int CURACION_PRUEBA = 30;

    private final Scanner scanner = new Scanner(System.in);
    // colecciones globales: todos los personajes creados, los gremios y los escuadrones.
    // un personaje "libre" es el que no esta en ningun escuadron, pero igual sigue en la lista de personajes
    // porque por la agregacion no depende del escuadron
    private final List<Personaje> personajes = new ArrayList<>();
    private final List<Gremio> gremios = new ArrayList<>();
    private final List<Escuadron> escuadrones = new ArrayList<>();

    public static void main(String[] args) {
        new AplicacionArenaRPG().ejecutar();
    }

    public void ejecutar() {
        System.out.println(" -------- arena de heroes RPG: relaciones entre clases --------");
        System.out.print("desea correr primero las pruebas de las relaciones? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            pruebasDeRelaciones();
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerNumero(scanner);

            switch (opcion) {
                case OPCION_CREAR_PERSONAJE:
                    crearPersonaje();
                    break;
                case OPCION_CREAR_GREMIO:
                    crearGremio();
                    break;
                case OPCION_RECLUTAR:
                    reclutarEnEscuadron();
                    break;
                case OPCION_EQUIPAR_ARMA:
                    equiparArma();
                    break;
                case OPCION_MOSTRAR_ESTADO:
                    mostrarEstadoDetallado();
                    break;
                case OPCION_UNIR_A_GREMIO:
                    unirAGremio();
                    break;
                case OPCION_DESEQUIPAR_ARMA:
                    desequiparArma();
                    break;
                case OPCION_GESTIONAR_ESCUADRON:
                    gestionarEscuadron();
                    break;
                case OPCION_SALIR:
                    System.out.println("Cerrando la Arena de Heroes...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != OPCION_SALIR);

        scanner.close();
    }

    public void mostrarMenu() {
        System.out.println("\n=== MENU ARENA DE HEROES RPG ===");
        System.out.printf("%d. Crear Personaje%n", OPCION_CREAR_PERSONAJE);
        System.out.printf("%d. Crear Gremio%n", OPCION_CREAR_GREMIO);
        System.out.printf("%d. Reclutar Personaje a Escuadron%n", OPCION_RECLUTAR);
        System.out.printf("%d. Equipar Arma a Personaje%n", OPCION_EQUIPAR_ARMA);
        System.out.printf("%d. Mostrar Estado Detallado (Personaje y Corazon RPG)%n", OPCION_MOSTRAR_ESTADO);
        System.out.printf("%d. Unir Personaje a Gremio%n", OPCION_UNIR_A_GREMIO);
        System.out.printf("%d. Desequipar Arma%n", OPCION_DESEQUIPAR_ARMA);
        System.out.printf("%d. Expulsar Personaje / Disolver Escuadron%n", OPCION_GESTIONAR_ESCUADRON);
        System.out.printf("%d. Salir%n", OPCION_SALIR);
        System.out.print("Seleccione una opcion: ");
    }

    public void crearPersonaje() {
        String nombre = pedirNombre("nombre del personaje: ", "Heroe" + (personajes.size() + 1));
        System.out.print("nivel (1 o mas): ");
        int nivel = leerNumero(scanner);

        // el corazon no se crea aca, lo crea el constructor de Personaje (composicion)
        Personaje personaje = new Personaje(nombre, nivel);
        personajes.add(personaje);
        System.out.printf("se creo a %s de nivel %d con %d de salud y %d de mana%n",
                personaje.getNombre(), personaje.getNivel(), personaje.getSalud(), personaje.getMana());
    }

    public void crearGremio() {
        String nombre = pedirNombre("nombre del gremio: ", "Gremio" + (gremios.size() + 1));
        Gremio gremio = new Gremio(nombre);
        gremios.add(gremio);
        System.out.println("se creo el gremio " + gremio.getNombre());

        if (!personajes.isEmpty()) {
            System.out.print("desea inscribir un personaje de una vez? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                Personaje personaje = elegirPersonaje(scanner, personajes);
                if (personaje != null) {
                    personaje.unirseAGremio(gremio);
                }
            }
        }
    }

    public void reclutarEnEscuadron() {
        if (personajes.isEmpty()) {
            System.out.printf("primero tiene que crear un personaje (opcion %d)%n", OPCION_CREAR_PERSONAJE);
            return;
        }

        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }

        Escuadron actual = buscarEscuadron(escuadrones, personaje);
        if (actual != null) {
            System.out.printf("%s ya esta en el escuadron %s, primero hay que expulsarlo (opcion %d)%n",
                    personaje.getNombre(), actual.getNombre(), OPCION_GESTIONAR_ESCUADRON);
            return;
        }

        Escuadron escuadron = elegirOCrearEscuadron();
        if (escuadron != null) {
            // agregacion: el personaje ya existia, el escuadron solo guarda la referencia
            escuadron.reclutarPersonaje(personaje);
        }
    }

    // devuelve el escuadron elegido, uno nuevo si elige CREAR_ESCUADRON_NUEVO o null si el numero no existe
    public Escuadron elegirOCrearEscuadron() {
        System.out.println("escuadrones:");
        System.out.printf("%d. crear un escuadron nuevo%n", CREAR_ESCUADRON_NUEVO);
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("%d. %s (%d miembros)%n", i + 1, escuadrones.get(i).getNombre(), escuadrones.get(i).getCantidadMiembros());
        }
        System.out.print("elija el escuadron: ");
        int indice = leerNumero(scanner);

        if (indice == CREAR_ESCUADRON_NUEVO) {
            String nombre = pedirNombre("nombre del escuadron: ", "Escuadron" + (escuadrones.size() + 1));
            Escuadron escuadron = new Escuadron(nombre);
            escuadrones.add(escuadron);
            return escuadron;
        }
        if (indice >= 1 && indice <= escuadrones.size()) {
            return escuadrones.get(indice - 1);
        }
        System.out.println("ERROR: ese escuadron no existe");
        return null;
    }

    public void equiparArma() {
        if (personajes.isEmpty()) {
            System.out.printf("primero tiene que crear un personaje (opcion %d)%n", OPCION_CREAR_PERSONAJE);
            return;
        }

        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }

        String nombre = pedirNombre("nombre del arma: ", NOMBRE_ARMA_POR_DEFECTO);
        System.out.print("danio base del arma: ");
        int danio = leerNumero(scanner);

        // asociacion unidireccional: el arma se crea afuera y se le pasa al personaje
        Arma arma = new Arma(nombre, danio);
        personaje.equiparArma(arma);
        System.out.printf("ataque total de %s: %d%n", personaje.getNombre(), personaje.calcularAtaqueTotal());
    }

    public void mostrarEstadoDetallado() {
        if (personajes.isEmpty()) {
            System.out.println("no hay personajes creados");
            return;
        }

        mostrarTablaPersonajes(personajes, escuadrones);

        System.out.print("numero del personaje para ver el detalle (0 para seguir): ");
        int indice = leerNumero(scanner);
        if (indice >= 1 && indice <= personajes.size()) {
            System.out.println();
            System.out.println(personajes.get(indice - 1).toString());
        }

        mostrarTablaEscuadrones(escuadrones);

        System.out.println();
        System.out.println(" -------- gremios --------");
        if (gremios.isEmpty()) {
            System.out.println("no hay gremios creados");
        }
        for (Gremio gremio : gremios) {
            System.out.println(gremio.toString());
        }
    }

    public void unirAGremio() {
        if (personajes.isEmpty() || gremios.isEmpty()) {
            System.out.println("tiene que haber por lo menos un personaje y un gremio");
            return;
        }

        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje == null) {
            return;
        }

        System.out.println("gremios:");
        for (int i = 0; i < gremios.size(); i++) {
            System.out.printf("%d. %s (%d miembros)%n", i + 1, gremios.get(i).getNombre(), gremios.get(i).getCantidadMiembros());
        }
        System.out.print("elija el gremio: ");
        int indice = leerNumero(scanner);

        if (indice < 1 || indice > gremios.size()) {
            System.out.println("ERROR: ese gremio no existe");
            return;
        }

        Gremio gremio = gremios.get(indice - 1);
        personaje.unirseAGremio(gremio);

        // se revisan los dos lados para comprobar que quedaron sincronizados
        System.out.printf("%s.getGremio() = %s | %s.getMiembros() contiene a %s: %b%n",
                personaje.getNombre(), personaje.getGremio().getNombre(), gremio.getNombre(),
                personaje.getNombre(), gremio.getMiembros().contains(personaje));
    }

    public void desequiparArma() {
        if (personajes.isEmpty()) {
            System.out.println("no hay personajes creados");
            return;
        }

        Personaje personaje = elegirPersonaje(scanner, personajes);
        if (personaje != null) {
            personaje.desequiparArma();
            System.out.printf("ataque total de %s: %d%n", personaje.getNombre(), personaje.calcularAtaqueTotal());
        }
    }

    public void gestionarEscuadron() {
        if (escuadrones.isEmpty()) {
            System.out.println("no hay escuadrones creados");
            return;
        }

        System.out.println("escuadrones:");
        for (int i = 0; i < escuadrones.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, escuadrones.get(i).toString());
        }
        System.out.print("elija el escuadron: ");
        int indice = leerNumero(scanner);

        if (indice < 1 || indice > escuadrones.size()) {
            System.out.println("ERROR: ese escuadron no existe");
            return;
        }
        Escuadron escuadron = escuadrones.get(indice - 1);

        System.out.printf("%d. Expulsar un personaje%n", OPCION_EXPULSAR);
        System.out.printf("%d. Disolver el escuadron%n", OPCION_DISOLVER);
        System.out.print("elija una opcion: ");
        int opcionEscuadron = leerNumero(scanner);

        if (opcionEscuadron == OPCION_EXPULSAR) {
            if (escuadron.getCantidadMiembros() == 0) {
                System.out.println("el escuadron esta vacio");
            } else {
                Personaje personaje = elegirPersonaje(scanner, escuadron.getMiembros());
                if (personaje != null) {
                    escuadron.expulsarPersonaje(personaje);
                }
            }
        } else if (opcionEscuadron == OPCION_DISOLVER) {
            escuadron.disolver();
            escuadrones.remove(escuadron);
            // agregacion: se borro el escuadron pero los personajes siguen en la lista general
            System.out.printf("siguen existiendo %d personajes en la arena%n", personajes.size());
        } else {
            System.out.println("ERROR: opcion no valida");
        }
    }

    // pide un nombre, si lo deja vacio se usa el que viene por defecto
    public String pedirNombre(String mensaje, String nombrePorDefecto) {
        System.out.print(mensaje);
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            return nombrePorDefecto;
        }
        return nombre;
    }

    // muestra la lista numerada y devuelve el personaje elegido, o null si el numero no existe
    public static Personaje elegirPersonaje(Scanner scanner, List<Personaje> lista) {
        System.out.println("personajes:");
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d. %s (nivel %d)%n", i + 1, lista.get(i).getNombre(), lista.get(i).getNivel());
        }
        System.out.print("elija el personaje: ");
        int indice = leerNumero(scanner);

        // se valida antes del get para que no salga IndexOutOfBoundsException
        if (indice < 1 || indice > lista.size()) {
            System.out.println("ERROR: ese personaje no existe");
            return null;
        }
        return lista.get(indice - 1);
    }

    // devuelve el escuadron donde esta el personaje, o null si esta libre
    public static Escuadron buscarEscuadron(List<Escuadron> escuadrones, Personaje personaje) {
        for (Escuadron escuadron : escuadrones) {
            if (escuadron.contiene(personaje)) {
                return escuadron;
            }
        }
        return null;
    }

    public static void mostrarTablaPersonajes(List<Personaje> personajes, List<Escuadron> escuadrones) {
        System.out.println();
        System.out.println(" -------- personajes --------");
        System.out.printf("%-3s %-12s %-6s %-10s %-9s %-14s %-7s %-12s %-12s%n",
                "#", "nombre", "nivel", "salud", "mana", "arma", "ataque", "gremio", "escuadron");

        for (int i = 0; i < personajes.size(); i++) {
            Personaje personaje = personajes.get(i);
            String arma;
            String gremio;
            String escuadron;

            // las referencias que pueden ser null se revisan antes de pedirles el nombre
            if (personaje.getArmaEquipada() != null) {
                arma = personaje.getArmaEquipada().getNombre();
            } else {
                arma = "desarmado";
            }

            if (personaje.getGremio() != null) {
                gremio = personaje.getGremio().getNombre();
            } else {
                gremio = "-";
            }

            Escuadron escuadronDelPersonaje = buscarEscuadron(escuadrones, personaje);
            if (escuadronDelPersonaje != null) {
                escuadron = escuadronDelPersonaje.getNombre();
            } else {
                escuadron = "libre";
            }

            System.out.printf("%-3d %-12s %-6d %4d/%-5d %3d/%-5d %-14s %-7d %-12s %-12s%n",
                    i + 1, personaje.getNombre(), personaje.getNivel(), personaje.getSalud(), personaje.getSaludMaxima(),
                    personaje.getMana(), personaje.getManaMaximo(), arma, personaje.calcularAtaqueTotal(), gremio, escuadron);
        }
    }

    public static void mostrarTablaEscuadrones(List<Escuadron> escuadrones) {
        System.out.println();
        System.out.println(" -------- escuadrones --------");
        if (escuadrones.isEmpty()) {
            System.out.println("no hay escuadrones creados");
            return;
        }

        System.out.printf("%-15s %-9s %-13s %-12s%n", "nombre", "miembros", "ataque total", "salud total");
        for (Escuadron escuadron : escuadrones) {
            int ataqueTotal = 0;
            int saludTotal = 0;
            for (Personaje miembro : escuadron.getMiembros()) {
                ataqueTotal = ataqueTotal + miembro.calcularAtaqueTotal();
                saludTotal = saludTotal + miembro.getSalud();
            }
            System.out.printf("%-15s %-9d %-13d %-12d%n", escuadron.getNombre(), escuadron.getCantidadMiembros(), ataqueTotal, saludTotal);
        }
        for (Escuadron escuadron : escuadrones) {
            System.out.println(escuadron.toString());
        }
    }

    // lee un numero, si escribe letras devuelve NUMERO_INVALIDO y queda como opcion invalida
    public static int leerNumero(Scanner scanner) {
        int numero = NUMERO_INVALIDO;
        if (scanner.hasNextInt()) {
            numero = scanner.nextInt();
        }
        // se limpia el resto de la linea para que el siguiente nextLine() no lea un enter vacio
        scanner.nextLine();
        return numero;
    }

    // pruebas de cada relacion sin tener que usar el menu
    public static void pruebasDeRelaciones() {
        System.out.println();
        System.out.println(" -------- prueba 1: asociacion unidireccional (Personaje -> Arma) --------");
        Personaje aragorn = new Personaje("Aragorn", NIVEL_ARAGORN);
        int ataqueSinArma = aragorn.calcularAtaqueTotal();
        System.out.printf("desarmado, ataque total: %d (nivel %d * %d)%n", ataqueSinArma, NIVEL_ARAGORN, Personaje.ATAQUE_POR_NIVEL);
        aragorn.desequiparArma(); // no tiene arma, no se cae porque se revisa el null
        Arma anduril = new Arma("Anduril", DANIO_ANDURIL);
        aragorn.equiparArma(anduril);
        System.out.printf("con arma, ataque total: %d (%d + %d)%n", aragorn.calcularAtaqueTotal(), ataqueSinArma, DANIO_ANDURIL);
        aragorn.desequiparArma();
        System.out.printf("el arma sigue existiendo sola: %s%n", anduril.toString());

        System.out.println();
        System.out.println(" -------- prueba 2: asociacion bidireccional (Personaje <-> Gremio) --------");
        Gremio montaraces = new Gremio("Montaraces");
        Gremio comunidad = new Gremio("Comunidad");
        Personaje legolas = new Personaje("Legolas", NIVEL_LEGOLAS);

        // desde el lado del personaje
        aragorn.unirseAGremio(montaraces);
        System.out.printf("aragorn.getGremio() = %s | montaraces contiene a aragorn: %b%n",
                aragorn.getGremio().getNombre(), montaraces.getMiembros().contains(aragorn));

        // desde el lado del gremio
        comunidad.agregarMiembro(legolas);
        System.out.printf("legolas.getGremio() = %s | comunidad contiene a legolas: %b%n",
                legolas.getGremio().getNombre(), comunidad.getMiembros().contains(legolas));

        // cambio de gremio: tiene que salir del anterior
        comunidad.agregarMiembro(aragorn);
        System.out.printf("aragorn.getGremio() = %s | montaraces tiene %d miembros | comunidad tiene %d%n",
                aragorn.getGremio().getNombre(), montaraces.getCantidadMiembros(), comunidad.getCantidadMiembros());
        aragorn.unirseAGremio(comunidad); // ya esta, no se duplica
        System.out.println(comunidad.toString());

        // los dos toString se pueden imprimir sin StackOverflowError
        System.out.println(aragorn.toString());

        System.out.println();
        System.out.println(" -------- prueba 3: agregacion (Escuadron o-- Personaje) --------");
        Escuadron vanguardia = new Escuadron("Vanguardia");
        vanguardia.reclutarPersonaje(aragorn);
        vanguardia.reclutarPersonaje(legolas);
        vanguardia.reclutarPersonaje(aragorn); // repetido
        vanguardia.reclutarPersonaje(null);
        System.out.println(vanguardia.toString());
        vanguardia.disolver();
        vanguardia = null;
        // el escuadron ya no existe pero los personajes siguen intactos
        System.out.printf("despues de borrar el escuadron: %s sigue con %d de salud y en el gremio %s%n",
                aragorn.getNombre(), aragorn.getSalud(), aragorn.getGremio().getNombre());

        System.out.println();
        System.out.println(" -------- prueba 4: composicion (Personaje *-- CorazonRPG) --------");
        Personaje gimli = new Personaje("Gimli", NIVEL_GIMLI);
        System.out.println("corazon de Gimli al crearlo: " + gimli.reporteCorazon());
        gimli.recibirDanio(DANIO_PRUEBA);
        gimli.lanzarHechizo(HECHIZO_BARATO);
        gimli.lanzarHechizo(HECHIZO_CARO);
        gimli.curar(CURACION_PRUEBA);
        System.out.println("corazon de Gimli: " + gimli.reporteCorazon());
        // esto no compila porque no existe el setter, el corazon no se puede reemplazar desde afuera:
        // gimli.setCorazon(new CorazonRPG(999, 999));
        // error: cannot find symbol  symbol: method setCorazon(CorazonRPG)
        // y tampoco se puede crear un corazon suelto porque su constructor no es public:
        // new CorazonRPG(999, 999);
        // error: CorazonRPG(int,int) is not public in CorazonRPG; cannot be accessed from outside package
        System.out.println();
    }
}
