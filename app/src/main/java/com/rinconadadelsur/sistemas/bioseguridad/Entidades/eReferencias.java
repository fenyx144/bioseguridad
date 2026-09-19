package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eReferencias {
    private String _codigo;
    private String _descripcion;

    public eReferencias(){

    }

    public eReferencias(String _codigo, String _descripcion) {
        this._codigo = _codigo;
        this._descripcion = _descripcion;
    }

    public String get_codigo() {
        return _codigo;
    }

    public void set_codigo(String _codigo) {
        this._codigo = _codigo;
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
