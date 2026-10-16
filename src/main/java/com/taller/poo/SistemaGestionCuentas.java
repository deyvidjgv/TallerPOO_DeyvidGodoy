package com.taller.poo;
import java.util.Scanner;

public class SistemaGestionCuentas {
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.75, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria();

        int opcion = 0;
        String numeroCuenta = "";
        double monto = 0;
        int posicion = -1;

        do {
            System.out.println(" -------- sistema de cuentas --------");
            System.out.println("1. Depositar en una cuenta");
            System.out.println("2. Retirar de una cuenta");
            System.out.println("3. Ver reporte consolidado");
            System.out.println("4. Salir");
            System.out.print("elija una opcion: ");
            opcion = console.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("digite el numero de cuenta: ");
                    numeroCuenta = console.next();

                    posicion = -1;
                    for (int i = 0; i < cuentas.length; i++) {
                        if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                            posicion = i;
                            break;
                        }
                    }

                    if (posicion == -1) {
                        System.out.printf("ERROR: la cuenta %s no existe%n", numeroCuenta);
                        break;
                    }

                    System.out.print("digite el monto a depositar: ");
                    monto = console.nextDouble();
                    if (monto <= 0) {
                        System.out.printf("ERROR: el monto %.2f no es valido, tiene que ser mayor a 0%n", monto);
                    } else if (cuentas[posicion].depositar(monto)) {
                        System.out.printf("deposito exitoso, nuevo saldo de la cuenta %s: $%.2f%n", numeroCuenta, cuentas[posicion].getSaldo());
                    } else {
                        System.out.printf("ERROR: no se pudo hacer el deposito%n");
                    }
                    break;
                case 2:
                    System.out.print("digite el numero de cuenta: ");
                    numeroCuenta = console.next();

                    posicion = -1;
                    for (int i = 0; i < cuentas.length; i++) {
                        if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                            posicion = i;
                            break;
                        }
                    }

                    if (posicion == -1) {
                        System.out.printf("ERROR: la cuenta %s no existe%n", numeroCuenta);
                        break;
                    }

                    System.out.print("digite el monto a retirar: ");
                    monto = console.nextDouble();
                    if (monto <= 0) {
                        System.out.printf("ERROR: el monto %.2f no es valido, tiene que ser mayor a 0%n", monto);
                    } else if (cuentas[posicion].retirar(monto)) {
                        System.out.printf("retiro exitoso, nuevo saldo de la cuenta %s: $%.2f%n", numeroCuenta, cuentas[posicion].getSaldo());
                    } else {
                        System.out.printf("ERROR: saldo insuficiente en la cuenta %s%n", numeroCuenta);
                    }
                    break;
                case 3:
                    StringBuilder reporte = new StringBuilder();
                    double saldoTotal = 0;

                    for (int i = 0; i < cuentas.length; i++) {
                        reporte.append(String.format("Cuenta: %s | Titular: %s | Saldo: $%.2f | Crédito: $%.2f%n",
                                cuentas[i].getNumeroCuenta(), cuentas[i].getTitular(), cuentas[i].getSaldo(), cuentas[i].getLimiteCredito()));
                        saldoTotal = saldoTotal + cuentas[i].getSaldo();
                    }

                    System.out.printf(" -------- reporte consolidado --------%n");
                    System.out.printf("%s", reporte);
                    System.out.printf("cuentas registradas: %d%n", cuentas.length);
                    System.out.printf("saldo total acumulado: $%.2f%n", saldoTotal);
                    break;
                case 4:
                    System.out.println("saliendo del sistema...");
                    break;
                default:
                    System.out.println("opcion no valida, intente de nuevo");
                    break;
            }

        } while (opcion != 4);
    }
}
