package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eRegistroManto {

    private String _codigo;
    private String _elemento;
    private String _descripcion;

    public eRegistroManto(){

    }

    public eRegistroManto(String _codigo, String _elemento, String _descripcion) {
        this._codigo = _codigo;
        this._elemento= _elemento;
        this._descripcion = _descripcion;
    }

    public String get_codigo() {
        return _codigo;
    }

    public void set_codigo(String _codigo) {
        this._codigo = _codigo;
    }

    public String get_elemento() {
        return _elemento;
    }

    public void set_elemento(String _elemento) {
        this._elemento = _elemento;
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
