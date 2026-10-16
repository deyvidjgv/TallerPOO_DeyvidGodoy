package com.taller.poo;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    /*
     * PREGUNTAS DE ANÁLISIS:
     * 1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto al declarar 'saldo' como double
     * primitivo frente a 'limiteCredito' como Wrapper Double?
     * R/ 'saldo' guarda el numero directo dentro del objeto (8 bytes) y si no se le asigna nada queda en 0.0.
     * 'limiteCredito' es una referencia a un objeto Double que vive aparte en el heap, gasta mas memoria
     * (la referencia mas el objeto con su encabezado) y si no se le asigna nada queda en null, o sea
     * no apunta a ningun objeto.
     *
     * 2. ¿Por qué una operación aritmética sobre un atributo no inicializado de tipo Double provoca una
     * NullPointerException, mientras que sobre un double primitivo opera con 0.0?
     * R/ Para operar con un Double, Java lo convierte a double por debajo (unboxing) llamando a
     * limiteCredito.doubleValue(). Si limiteCredito es null no hay objeto sobre el cual llamar ese metodo
     * y salta la NullPointerException. El double primitivo no tiene que convertirse, ya tiene su valor
     * 0.0 por defecto y la operacion se hace normal.
     */

    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.titular = "Sin Nombre";
        this.saldo = 0.0;
        this.limiteCredito = 0.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;
        this.limiteCredito = 100.0;
    }

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo = saldo + monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        double disponible = saldo;
        // si el limite de credito es null no se suma, asi se evita la NullPointerException
        if (limiteCredito != null) {
            disponible = saldo + limiteCredito;
        }

        if (monto <= 0) {
            return false;
        } else if (monto > disponible) {
            return false;
        } else {
            saldo = saldo - monto;
            return true;
        }
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(Double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    @Override
    public String toString() {
        return "CuentaBancaria{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", titular='" + titular + '\'' +
                ", saldo=" + saldo +
                ", limiteCredito=" + limiteCredito +
                '}';
    }
}
