package com.taller.poo;

public class Principal {
    public static void main(String[] args) {

        // vehiculos: uno con el constructor por defecto y dos con el parametrizado
        Vehiculo vehiculo1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo vehiculo2 = new Vehiculo();
        Vehiculo vehiculo3 = new Vehiculo("Mazda", "CX-5", 32000.0, 200, false);

        // estudiantes
        Estudiante estudiante1 = new Estudiante("1090123456", "Laura Martinez", 4.35, true);
        Estudiante estudiante2 = new Estudiante("1090654321", "Andres Rojas", 3.8, true);
        Estudiante estudiante3 = new Estudiante("1090111222", "Sofia Castro", 2.95, false);

        // cuentas: una con cada constructor
        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.75, 500.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();

        // operaciones que cambian el estado de las cuentas
        System.out.printf(" -------- operaciones --------%n");
        System.out.printf("deposito de 300.00 en la cuenta %s: %b%n", cuenta1.getNumeroCuenta(), cuenta1.depositar(300.0));
        System.out.printf("retiro de 50.00 en la cuenta %s: %b%n", cuenta1.getNumeroCuenta(), cuenta1.retirar(50.0));
        System.out.printf("deposito de 50.00 en la cuenta %s: %b%n", cuenta2.getNumeroCuenta(), cuenta2.depositar(50.0));
        System.out.printf("deposito de -20.00 en la cuenta %s: %b%n", cuenta2.getNumeroCuenta(), cuenta2.depositar(-20.0));
        System.out.printf("retiro de 20.00 en la cuenta %s: %b%n", cuenta3.getNumeroCuenta(), cuenta3.retirar(20.0));

        // metodos estaticos, se llaman con el nombre de la clase sin hacer new
        double euros = ConversorMoneda.convertirDolaresAEuros(cuenta1.getSaldo(), 0.92);
        System.out.printf("saldo de la cuenta %s en euros: %s%n", cuenta1.getNumeroCuenta(), ConversorMoneda.formatearMoneda(euros));
        System.out.printf("conversion con tasa invalida: %s%n", ConversorMoneda.formatearMoneda(ConversorMoneda.convertirDolaresAEuros(100.0, 0)));

        System.out.printf("%n=== REPORTE DE VEHÍCULOS ===%n");
        System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                1, vehiculo1.getMarcaVehiculo(), vehiculo1.getModeloVehiculo(), vehiculo1.getKilometrajeActual(), vehiculo1.getVelocidadMax(), vehiculo1.isEncendido());
        System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                2, vehiculo2.getMarcaVehiculo(), vehiculo2.getModeloVehiculo(), vehiculo2.getKilometrajeActual(), vehiculo2.getVelocidadMax(), vehiculo2.isEncendido());
        System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                3, vehiculo3.getMarcaVehiculo(), vehiculo3.getModeloVehiculo(), vehiculo3.getKilometrajeActual(), vehiculo3.getVelocidadMax(), vehiculo3.isEncendido());

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        System.out.printf("%s%n", estudiante1.obtenerReporteAcademicos());
        System.out.printf("%s%n", estudiante2.obtenerReporteAcademicos());
        System.out.printf("%s%n", estudiante3.obtenerReporteAcademicos());

        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta1.getNumeroCuenta(), cuenta1.getTitular(), cuenta1.getSaldo(), cuenta1.getLimiteCredito());
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta2.getNumeroCuenta(), cuenta2.getTitular(), cuenta2.getSaldo(), cuenta2.getLimiteCredito());
        System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n",
                cuenta3.getNumeroCuenta(), cuenta3.getTitular(), cuenta3.getSaldo(), cuenta3.getLimiteCredito());
    }
}
