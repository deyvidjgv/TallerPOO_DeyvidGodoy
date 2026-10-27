package com.taller.poo;

public class Principal {

    // tasa de dolares a euros que se usa en la conversion y una invalida para probar la validacion
    private static final double TASA_DOLAR_A_EURO = 0.92;
    private static final double TASA_INVALIDA = 0;
    private static final double MONTO_CONVERSION_PRUEBA = 100.0;

    // montos de las operaciones de prueba: uno negativo y un retiro sin saldo para ver que se rechazan
    private static final double DEPOSITO_GRANDE = 300.0;
    private static final double RETIRO_NORMAL = 50.0;
    private static final double DEPOSITO_PEQUENO = 50.0;
    private static final double DEPOSITO_NEGATIVO = -20.0;
    private static final double RETIRO_SIN_SALDO = 20.0;

    private static final String FORMATO_VEHICULO = "[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n";
    private static final String FORMATO_CUENTA = "Cuenta: %s | Titular: %s | Saldo Final: $%.2f | Crédito: $%.2f%n";

    public static void main(String[] args) {
        Vehiculo[] vehiculos = crearVehiculos();
        Estudiante[] estudiantes = crearEstudiantes();
        CuentaBancaria[] cuentas = crearCuentas();

        realizarOperaciones(cuentas);
        probarConversor(cuentas[0]);

        mostrarReporteVehiculos(vehiculos);
        mostrarReporteEstudiantes(estudiantes);
        mostrarReporteCuentas(cuentas);
    }

    // uno con el constructor por defecto y dos con el parametrizado
    public static Vehiculo[] crearVehiculos() {
        Vehiculo vehiculo1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo vehiculo2 = new Vehiculo();
        Vehiculo vehiculo3 = new Vehiculo("Mazda", "CX-5", 32000.0, 200, false);
        return new Vehiculo[]{vehiculo1, vehiculo2, vehiculo3};
    }

    public static Estudiante[] crearEstudiantes() {
        Estudiante estudiante1 = new Estudiante("1090123456", "Laura Martinez", 4.35, true);
        Estudiante estudiante2 = new Estudiante("1090654321", "Andres Rojas", 3.8, true);
        Estudiante estudiante3 = new Estudiante("1090111222", "Sofia Castro", 2.95, false);
        return new Estudiante[]{estudiante1, estudiante2, estudiante3};
    }

    // una cuenta con cada constructor
    public static CuentaBancaria[] crearCuentas() {
        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.75, 500.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();
        return new CuentaBancaria[]{cuenta1, cuenta2, cuenta3};
    }

    // operaciones que cambian el estado de las cuentas, incluye un deposito negativo y un retiro sin saldo
    public static void realizarOperaciones(CuentaBancaria[] cuentas) {
        System.out.printf(" -------- operaciones --------%n");
        depositar(cuentas[0], DEPOSITO_GRANDE);
        retirar(cuentas[0], RETIRO_NORMAL);
        depositar(cuentas[1], DEPOSITO_PEQUENO);
        depositar(cuentas[1], DEPOSITO_NEGATIVO);
        retirar(cuentas[2], RETIRO_SIN_SALDO);
    }

    public static void depositar(CuentaBancaria cuenta, double monto) {
        System.out.printf("deposito de %.2f en la cuenta %s: %b%n", monto, cuenta.getNumeroCuenta(), cuenta.depositar(monto));
    }

    public static void retirar(CuentaBancaria cuenta, double monto) {
        System.out.printf("retiro de %.2f en la cuenta %s: %b%n", monto, cuenta.getNumeroCuenta(), cuenta.retirar(monto));
    }

    // metodos estaticos, se llaman con el nombre de la clase sin hacer new
    public static void probarConversor(CuentaBancaria cuenta) {
        double euros = ConversorMoneda.convertirDolaresAEuros(cuenta.getSaldo(), TASA_DOLAR_A_EURO);
        System.out.printf("saldo de la cuenta %s en euros: %s%n", cuenta.getNumeroCuenta(), ConversorMoneda.formatearMoneda(euros));
        System.out.printf("conversion con tasa invalida: %s%n",
                ConversorMoneda.formatearMoneda(ConversorMoneda.convertirDolaresAEuros(MONTO_CONVERSION_PRUEBA, TASA_INVALIDA)));
    }

    public static void mostrarReporteVehiculos(Vehiculo[] vehiculos) {
        System.out.printf("%n=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo vehiculo = vehiculos[i];
            System.out.printf(FORMATO_VEHICULO, i + 1, vehiculo.getMarcaVehiculo(), vehiculo.getModeloVehiculo(),
                    vehiculo.getKilometrajeActual(), vehiculo.getVelocidadMax(), vehiculo.isEncendido());
        }
    }

    public static void mostrarReporteEstudiantes(Estudiante[] estudiantes) {
        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        for (Estudiante estudiante : estudiantes) {
            System.out.printf("%s%n", estudiante.obtenerReporteAcademicos());
        }
    }

    public static void mostrarReporteCuentas(CuentaBancaria[] cuentas) {
        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        for (CuentaBancaria cuenta : cuentas) {
            System.out.printf(FORMATO_CUENTA, cuenta.getNumeroCuenta(), cuenta.getTitular(), cuenta.getSaldo(), cuenta.getLimiteCredito());
        }
    }
}
