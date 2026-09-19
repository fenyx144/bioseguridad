package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eRegistroCerco {

    private String _codigo;
    private String _componente;
    private String _descripcion;

    public eRegistroCerco(){

    }

    public eRegistroCerco(String _codigo, String _componente, String _descripcion) {
        this._codigo = _codigo;
        this._componente = _componente;
        this._descripcion = _descripcion;
    }

    public String get_codigo() {
        return _codigo;
    }

    public void set_codigo(String _codigo) {
        this._codigo = _codigo;
    }

    public String get_componente() {
        return _componente;
    }

    public void set_componente(String _componente) {
        this._componente = _componente;
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
