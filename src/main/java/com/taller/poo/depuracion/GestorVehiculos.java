package com.taller.poo.depuracion;

public class GestorVehiculos {

    public void encenderMotor() {
        System.out.println("El motor del vehículo ha sido encendido.");
    }

    public static void main(String[] args) {
        // correccion: main es static y no tiene un objeto, hay que crear uno para llamar al metodo de instancia
        GestorVehiculos gestor = new GestorVehiculos();
        gestor.encenderMotor();
    }
}
