package com.taller.poo;

/*
 * Wrappers vs primitivos:
 * Un primitivo (int, double, boolean) guarda el valor directo y ocupa solo lo que pesa el tipo
 * (int 4 bytes, double 8 bytes, boolean 1 byte aprox). Un Wrapper (Integer, Double, Boolean) es un
 * objeto: el atributo guarda una referencia y el valor vive en el heap con su encabezado de objeto,
 * entonces gasta mas memoria (un Double ocupa unos 16 bytes mas la referencia).
 * La ventaja del Wrapper es que admite null, sirve para decir "este dato no se conoce todavia".
 * La desventaja es que si el valor esta en null y se opera con el (unboxing) lanza NullPointerException.
 * Un primitivo nunca es null: si no se asigna toma su valor por defecto (0, 0.0, false).
 */
public class Vehiculo {

    private String marcaVehiculo;
    private String modeloVehiculo;
    // Double: el kilometraje puede no conocerse (por ejemplo un carro usado sin registro), con el
    // Wrapper se puede dejar en null y distinguirlo de un 0.0 que si es un carro nuevo
    private Double kilometrajeActual;
    // int: la velocidad maxima siempre existe para un vehiculo y es un numero entero,
    // no necesita null y el primitivo gasta menos memoria
    private int velocidadMax;
    // boolean: el motor solo puede estar encendido o apagado, no hay un tercer estado
    // y el valor por defecto false (apagado) tiene sentido
    private boolean encendido;

    // valores por defecto que pide la guia para el constructor sin parametros
    private static final String MARCA_POR_DEFECTO = "Genérico";
    private static final String MODELO_POR_DEFECTO = "Sin Modelo";
    private static final double KILOMETRAJE_INICIAL = 0.0;
    private static final int VELOCIDAD_MAX_POR_DEFECTO = 120;

    public Vehiculo() {
        this.marcaVehiculo = MARCA_POR_DEFECTO;
        this.modeloVehiculo = MODELO_POR_DEFECTO;
        this.kilometrajeActual = KILOMETRAJE_INICIAL;
        this.velocidadMax = VELOCIDAD_MAX_POR_DEFECTO;
        this.encendido = false;
    }

    public Vehiculo(String marcaVehiculo, String modeloVehiculo, Double kilometrajeActual, int velocidadMax, boolean encendido) {
        this.marcaVehiculo = marcaVehiculo;
        this.modeloVehiculo = modeloVehiculo;
        this.kilometrajeActual = kilometrajeActual;
        this.velocidadMax = velocidadMax;
        this.encendido = encendido;
    }

    public String getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }

    public Double getKilometrajeActual() {
        return kilometrajeActual;
    }

    public void setKilometrajeActual(Double kilometrajeActual) {
        this.kilometrajeActual = kilometrajeActual;
    }

    public int getVelocidadMax() {
        return velocidadMax;
    }

    public void setVelocidadMax(int velocidadMax) {
        this.velocidadMax = velocidadMax;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public void setEncendido(boolean encendido) {
        this.encendido = encendido;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "marcaVehiculo='" + marcaVehiculo + '\'' +
                ", modeloVehiculo='" + modeloVehiculo + '\'' +
                ", kilometrajeActual=" + kilometrajeActual +
                ", velocidadMax=" + velocidadMax +
                ", encendido=" + encendido +
                '}';
    }

}
