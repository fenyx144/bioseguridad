package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eRegEliminar {
    String _id;
    String _descripcion;

    public eRegEliminar(String _id, String _descripcion) {
        this._id = _id;
        this._descripcion = _descripcion;
    }
    public eRegEliminar() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_descripcion() {
        return _descripcion;
    }

    public void set_descripcion(String _descripcion) {
        this._descripcion = _descripcion;
    }
}
