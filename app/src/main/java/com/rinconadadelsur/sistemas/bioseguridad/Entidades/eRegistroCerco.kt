package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegistroCerco {
    private var _codigo: String? = null
    private var _componente: String? = null
    private var _descripcion: String? = null

    constructor()

    constructor(_codigo: String?, _componente: String?, _descripcion: String) {
        this._codigo = _codigo
        this._componente = _componente
        this._descripcion = _descripcion
    }

    fun get_codigo(): String? {
        return _codigo
    }

    fun set_codigo(_codigo: String?) {
        this._codigo = _codigo
    }

    fun get_componente(): String? {
        return _componente
    }

    fun set_componente(_componente: String?) {
        this._componente = _componente
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
