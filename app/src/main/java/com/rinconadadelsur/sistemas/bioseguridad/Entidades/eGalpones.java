package com.rinconadadelsur.sistemas.bioseguridad.Entidades;

public class eGalpones {
    private String _tcencos;
    private String _tcodint;

    public eGalpones(){
    }

    public eGalpones(String _tcencos, String _tcodint) {
        this._tcencos = _tcencos;
        this._tcodint = _tcodint;
    }

    public String get_tcencos() {
        return _tcencos;
    }

    public void set_tcencos(String _tcencos) {
        this._tcencos = _tcencos;
    }

    public String get_tcodint() {
        return _tcodint;
    }

    public void set_tcodint(String _tcodint) {
        this._tcodint = _tcodint;
    }

}
