package com.taller.poo.depuracion;

public class Vehiculo {
    private int velocidadMaxima;
    private boolean encendido;

    public Vehiculo(int velocidadMaxima, boolean encendido) {
        // correccion: sin this el parametro se asignaba a si mismo y los atributos quedaban en 0 y false
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public void imprimirEstado() {
        System.out.printf("Velocidad Máxima: %d | Encendido: %b%n", velocidadMaxima, encendido);
    }
}
