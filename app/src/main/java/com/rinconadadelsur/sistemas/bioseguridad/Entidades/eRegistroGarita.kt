package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegistroGarita {
    private var _codigo: String? = null
    private var _anomalia: String? = null
    private var _descripcion: String? = null

    constructor()

    constructor(_codigo: String?, _anomalia: String?, _descripcion: String) {
        this._codigo = _codigo
        this._anomalia = _anomalia
        this._descripcion = _descripcion
    }

    fun get_codigo(): String? {
        return _codigo
    }

    fun set_codigo(_codigo: String?) {
        this._codigo = _codigo
    }

    fun get_anomalia(): String? {
        return _anomalia
    }

    fun set_anomalia(_anomalia: String?) {
        this._anomalia = _anomalia
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
