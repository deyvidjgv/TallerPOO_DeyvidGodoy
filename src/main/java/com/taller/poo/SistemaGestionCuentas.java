package com.taller.poo;
import java.util.Scanner;

public class SistemaGestionCuentas {

    // opciones del menu, asi el switch no queda con numeros sueltos
    private static final int OPCION_DEPOSITAR = 1;
    private static final int OPCION_RETIRAR = 2;
    private static final int OPCION_REPORTE = 3;
    private static final int OPCION_SALIR = 4;
    private static final int OPCION_INVALIDA = -1;

    private static final int CANTIDAD_CUENTAS = 3;
    // valor que devuelve buscarCuenta cuando el numero no esta en el arreglo
    private static final int CUENTA_NO_ENCONTRADA = -1;
    private static final double MONTO_MINIMO = 0.0;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        CuentaBancaria[] cuentas = crearCuentas();
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(console);

            switch (opcion) {
                case OPCION_DEPOSITAR:
                    depositar(console, cuentas);
                    break;
                case OPCION_RETIRAR:
                    retirar(console, cuentas);
                    break;
                case OPCION_REPORTE:
                    mostrarReporte(cuentas);
                    break;
                case OPCION_SALIR:
                    System.out.printf("saliendo del sistema...%n");
                    break;
                default:
                    System.out.printf("opcion no valida, intente de nuevo%n");
                    break;
            }

        } while (opcion != OPCION_SALIR);

        console.close();
    }

    // arreglo de tamaño fijo pre-poblado, una cuenta con cada constructor
    public static CuentaBancaria[] crearCuentas() {
        CuentaBancaria[] cuentas = new CuentaBancaria[CANTIDAD_CUENTAS];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.75, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria();
        return cuentas;
    }

    public static void mostrarMenu() {
        System.out.printf(" -------- sistema de cuentas --------%n");
        System.out.printf("%d. Depositar en una cuenta%n", OPCION_DEPOSITAR);
        System.out.printf("%d. Retirar de una cuenta%n", OPCION_RETIRAR);
        System.out.printf("%d. Ver reporte consolidado%n", OPCION_REPORTE);
        System.out.printf("%d. Salir%n", OPCION_SALIR);
        System.out.printf("elija una opcion: ");
    }

    public static void depositar(Scanner console, CuentaBancaria[] cuentas) {
        int posicion = pedirCuenta(console, cuentas);
        if (posicion == CUENTA_NO_ENCONTRADA) {
            return;
        }

        double monto = pedirMonto(console, "depositar");
        if (monto <= MONTO_MINIMO) {
            System.out.printf("ERROR: el monto %.2f no es valido, tiene que ser mayor a 0%n", monto);
        } else if (cuentas[posicion].depositar(monto)) {
            System.out.printf("deposito exitoso, nuevo saldo de la cuenta %s: $%.2f%n",
                    cuentas[posicion].getNumeroCuenta(), cuentas[posicion].getSaldo());
        } else {
            System.out.printf("ERROR: no se pudo hacer el deposito%n");
        }
    }

    public static void retirar(Scanner console, CuentaBancaria[] cuentas) {
        int posicion = pedirCuenta(console, cuentas);
        if (posicion == CUENTA_NO_ENCONTRADA) {
            return;
        }

        double monto = pedirMonto(console, "retirar");
        if (monto <= MONTO_MINIMO) {
            System.out.printf("ERROR: el monto %.2f no es valido, tiene que ser mayor a 0%n", monto);
        } else if (cuentas[posicion].retirar(monto)) {
            System.out.printf("retiro exitoso, nuevo saldo de la cuenta %s: $%.2f%n",
                    cuentas[posicion].getNumeroCuenta(), cuentas[posicion].getSaldo());
        } else {
            System.out.printf("ERROR: saldo insuficiente en la cuenta %s%n", cuentas[posicion].getNumeroCuenta());
        }
    }

    // pide el numero de cuenta y lo busca, si no existe muestra el error y devuelve CUENTA_NO_ENCONTRADA
    public static int pedirCuenta(Scanner console, CuentaBancaria[] cuentas) {
        System.out.printf("digite el numero de cuenta: ");
        String numeroCuenta = console.next();

        int posicion = buscarCuenta(cuentas, numeroCuenta);
        if (posicion == CUENTA_NO_ENCONTRADA) {
            System.out.printf("ERROR: la cuenta %s no existe%n", numeroCuenta);
        }
        return posicion;
    }

    // la busqueda queda en un solo metodo y la usan depositar y retirar
    public static int buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return i;
            }
        }
        return CUENTA_NO_ENCONTRADA;
    }

    public static double pedirMonto(Scanner console, String operacion) {
        System.out.printf("digite el monto a %s: ", operacion);
        // si no escribe un numero se descarta y queda un monto invalido
        if (console.hasNextDouble()) {
            return console.nextDouble();
        }
        console.next();
        return MONTO_MINIMO;
    }

    public static void mostrarReporte(CuentaBancaria[] cuentas) {
        StringBuilder reporte = new StringBuilder();
        double saldoTotal = 0;

        for (CuentaBancaria cuenta : cuentas) {
            reporte.append(String.format("Cuenta: %s | Titular: %s | Saldo: $%.2f | Crédito: $%.2f%n",
                    cuenta.getNumeroCuenta(), cuenta.getTitular(), cuenta.getSaldo(), cuenta.getLimiteCredito()));
            saldoTotal = saldoTotal + cuenta.getSaldo();
        }

        System.out.printf(" -------- reporte consolidado --------%n");
        System.out.printf("%s", reporte);
        System.out.printf("cuentas registradas: %d%n", cuentas.length);
        System.out.printf("saldo total acumulado: $%.2f%n", saldoTotal);
    }

    // si no escribe un numero se descarta lo que escribio y queda una opcion invalida
    public static int leerEntero(Scanner console) {
        if (console.hasNextInt()) {
            return console.nextInt();
        }
        console.next();
        return OPCION_INVALIDA;
    }
}
