package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eRegEliminar {
    private var idValue: String? = null
    private var descripcionValue: String? = null

    constructor(id: String?, descripcion: String?) {
        idValue = id
        descripcionValue = descripcion
    }

    constructor()

    fun get_id(): String? = idValue

    fun set_id(value: String?) {
        idValue = value
    }

    fun get_descripcion(): String? = descripcionValue

    fun set_descripcion(value: String?) {
        descripcionValue = value
    }
}
