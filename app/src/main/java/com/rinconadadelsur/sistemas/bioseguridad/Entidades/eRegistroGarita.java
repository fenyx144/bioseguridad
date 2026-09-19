package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eRegistroGarita {

    private String _codigo;
    private String _anomalia;
    private String _descripcion;

    public eRegistroGarita(){

    }

    public eRegistroGarita(String _codigo, String _anomalia, String _descripcion ) {
        this._codigo = _codigo;
        this._anomalia = _anomalia;
        this._descripcion = _descripcion;
    }

    public String get_codigo() {
        return _codigo;
    }

    public void set_codigo(String _codigo) {
        this._codigo = _codigo;
    }

    public String get_anomalia() {
        return _anomalia;
    }

    public void set_anomalia(String _anomalia) {
        this._anomalia = _anomalia;
    }

    public String get_descripcion() {
        return _descripcion;
    }

    public void set_descripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }

    @Override
    public String toString() {
        return _descripcion;
    }
}
