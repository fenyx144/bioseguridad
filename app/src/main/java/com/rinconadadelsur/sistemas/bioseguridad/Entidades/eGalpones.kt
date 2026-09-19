package com.rinconadadelsur.sistemas.bioseguridad.Entidades

class eGalpones {
    private var _tcencos: String? = null
    private var _tcodint: String? = null

    constructor()

    constructor(_tcencos: String?, _tcodint: String?) {
        this._tcencos = _tcencos
        this._tcodint = _tcodint
    }

    fun get_tcencos(): String? {
        return _tcencos
    }

    fun set_tcencos(_tcencos: String?) {
        this._tcencos = _tcencos
    }

    fun get_tcodint(): String? {
        return _tcodint
    }

    fun set_tcodint(_tcodint: String?) {
        this._tcodint = _tcodint
    }
}
