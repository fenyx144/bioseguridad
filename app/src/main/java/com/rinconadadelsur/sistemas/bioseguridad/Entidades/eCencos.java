package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eCencos {
    private String c_cencos;
    private String c_nombre;

    public eCencos(String c_cencos, String c_nombre) {
        this.c_cencos = c_cencos;
        this.c_nombre = c_nombre;
    }
    public eCencos() {
    }

    public String getC_cencos() {
        return c_cencos;
    }

    public void setC_cencos(String c_cencos) {
        this.c_cencos = c_cencos;
    }

    public String getC_nombre() {
        return c_nombre;
    }

    public void setC_nombre(String c_nombre) {
        this.c_nombre = c_nombre;
    }
}
