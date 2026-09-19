package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegEliminar {
    var _id: String? = null
    var _descripcion: String? = null

    constructor(_id: String?, _descripcion: String?) {
        this._id = _id
        this._descripcion = _descripcion
    }

    constructor()

    fun get_id(): String? {
        return _id
    }

    fun set_id(_id: String?) {
        this._id = _id
    }

    fun get_descripcion(): String? {
        return _descripcion
    }

    fun set_descripcion(_descripcion: String?) {
        this._descripcion = _descripcion
    }
}
