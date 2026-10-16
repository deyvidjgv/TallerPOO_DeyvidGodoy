package com.taller.poo.depuracion;

public class CuentaBancaria {
    private String numeroCuenta;
    private double saldo;

    // correccion: se quito el void, un constructor no lleva tipo de retorno
    public CuentaBancaria(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }
}
