package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegistroManto {
    private var _codigo: String? = null
    private var _elemento: String? = null
    private var _descripcion: String? = null

    constructor()

    constructor(_codigo: String?, _elemento: String?, _descripcion: String) {
        this._codigo = _codigo
        this._elemento = _elemento
        this._descripcion = _descripcion
    }

    fun get_codigo(): String? {
        return _codigo
    }

    fun set_codigo(_codigo: String?) {
        this._codigo = _codigo
    }

    fun get_elemento(): String? {
        return _elemento
    }

    fun set_elemento(_elemento: String?) {
        this._elemento = _elemento
    }

    fun get_descripcion(): String {
        return _descripcion!!
    }

    fun set_descripcion(_descripcion: String) {
        this._descripcion = _descripcion
    }

    override fun toString(): String {
        return _descripcion!!
    }
}
