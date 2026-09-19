package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eRegistroFomites {

    private String _codigo;
    private String _depredador;
    private String _tipo;
    private String _observacion;

    public eRegistroFomites(){

    }

    public eRegistroFomites(String _codigo, String _depredador, String _tipo, String _observacion) {
        this._codigo = _codigo;
        this._depredador = _depredador;
        this._tipo = _tipo;
        this._observacion = _observacion;
    }

    public String get_codigo() {
        return _codigo;
    }

    public void set_codigo(String _codigo) {
        this._codigo = _codigo;
    }

    public String get_depredador() {
        return _depredador;
    }

    public void set_depredador(String _depredador) {
        this._depredador = _depredador;
    }

    public String get_tipo() {
        return _tipo;
    }

    public void set_tipo(String _tipo) {
        this._tipo = _tipo;
    }

    public String get_observacion() {
        return _observacion;
    }

    public void set_observacion(String _observacion) {
        this._observacion = _observacion;
    }

    @Override
    public String toString() {
        return _observacion;
    }
}
