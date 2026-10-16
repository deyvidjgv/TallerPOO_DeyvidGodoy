package com.taller.poo.depuracion;

import com.taller.poo.Estudiante;

public class PruebaEstudiante {
    public static void main(String[] args) {
        // correccion: la variable estaba declarada pero nunca se creo el objeto con new
        Estudiante estudiante1 = new Estudiante("1090123456", "Laura Martinez", 4.35, true);

        String reporte = estudiante1.obtenerReporteAcademicos();
        System.out.println(reporte);
    }
}
