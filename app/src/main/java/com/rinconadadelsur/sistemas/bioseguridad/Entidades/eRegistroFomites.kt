package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegistroFomites {
    private var _codigo: String? = null
    private var _depredador: String? = null
    private var _tipo: String? = null
    private var _observacion: String? = null

    constructor()

    constructor(_codigo: String?, _depredador: String?, _tipo: String?, _observacion: String) {
        this._codigo = _codigo
        this._depredador = _depredador
        this._tipo = _tipo
        this._observacion = _observacion
    }

    fun get_codigo(): String? {
        return _codigo
    }

    fun set_codigo(_codigo: String?) {
        this._codigo = _codigo
    }

    fun get_depredador(): String? {
        return _depredador
    }

    fun set_depredador(_depredador: String?) {
        this._depredador = _depredador
    }

    fun get_tipo(): String? {
        return _tipo
    }

    fun set_tipo(_tipo: String?) {
        this._tipo = _tipo
    }

    fun get_observacion(): String {
        return _observacion!!
    }

    fun set_observacion(_observacion: String) {
        this._observacion = _observacion
    }

    override fun toString(): String {
        return _observacion!!
    }
}
