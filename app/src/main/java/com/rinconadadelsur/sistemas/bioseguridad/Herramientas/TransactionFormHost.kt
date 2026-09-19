package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

/** Fragmentos de registro (Garita, Cerco, Fómites) que pueden mostrar resumen y guardar. */
interface TransactionFormHost {
    fun formTitle(): String
    fun summaryLines(): List<Pair<String, String>>
    fun performSave(): Boolean
}
