package com.rinconadadelsur.sistemas.bioseguridad.DataBase

object dbEstructura {
    var miBaseDatos: String = "bd_Bioseguridad"

    /*<!-- TODO: TABLAS UTILIZADAS EN EL LOGIN -->*/ //todo:Variables de TABLA CLV
    const val TABLA_CLV: String = "t_clv"
    const val Campo_uCodClv: String = "c_codigo"
    const val Campo_uNomClv: String = "c_nombre"

    val CREATE_TABLA_CLV: String = "CREATE TABLE " + TABLA_CLV +
            " (" + Campo_uCodClv + " TEXT," + Campo_uNomClv + " TEXT)"

    //todo:Variables de TABLA ANDROID
    const val t_android: String = "t_android"
    const val c_aCodigo: String = "c_codigo"
    const val c_aSerie: String = "c_serie"
    const val c_aFecha: String = "c_fecha"
    const val c_aEstado: String = "c_estado"

    val CREATE_TABLA_ANDROID: String = "CREATE TABLE " + t_android +
            " (" + c_aCodigo + " TEXT," + c_aSerie + " TEXT," + c_aFecha + " TEXT," + c_aEstado + " TEXT)"


    //todo:Variables de TABLA USUARIOS
    const val t_usuarios: String = "t_usuarios"
    const val c_uCodigo: String = "c_codigo"
    const val c_uNombre: String = "c_nombre"
    const val c_uDni: String = "c_dni"
    const val c_uPassword: String = "c_password"
    const val c_uPermiso: String = "c_permiso"

    val CREATE_TABLA_USUARIOS: String = "CREATE TABLE " + t_usuarios +
            " (" + c_uCodigo + " TEXT," + c_uNombre + " TEXT," + c_uDni + " TEXT," + c_uPassword + " TEXT," + c_uPermiso + " TEXT)"

    /*<!-- TODO: Variables de TABLA TCENCOS  -->*/
    const val t_TCencos: String = "t_tcencos"
    const val c_tcCodigo: String = "c_codigo" //0
    const val c_tcNombre: String = "c_nombre" //1

    val Create_t_TCencos: String = "CREATE TABLE " + t_TCencos +
            " (" + c_tcCodigo + " TEXT," + c_tcNombre + " TEXT)"

    /*<!-- TODO: TABLAS UTILIZADAS EN LA IMPORTACION DE DATOS -->*/ /*<!-- TODO: VARIABLES DE TABLA CENCOS -->*/
    const val t_Cencos: String = "t_cencos"
    const val c_cCodigo: String = "c_codigo"
    const val c_cNombre: String = "c_nombre"

    val Create_t_Cencos: String = "CREATE TABLE " + t_Cencos +
            " (" + c_cCodigo + " TEXT," + c_cNombre + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA ANOMALIAGARITA -->*/
    const val t_AnomaliaGarita: String = "t_anomaliagarita"
    const val c_anCodigo: String = "c_codigo"
    const val c_anDescripcion: String = "c_descripcion"

    val Create_t_AnomaliaGarita: String = "CREATE TABLE " + t_AnomaliaGarita +
            " (" + c_anCodigo + " TEXT," + c_anDescripcion + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA REFERENCIAFOM -->*/
    const val t_RefFomites: String = "t_referenciafom"
    const val c_rfCodigo: String = "c_codigo"
    const val c_rfDescripcion: String = "c_descripcion"

    val Create_t_RefFomites: String = "CREATE TABLE " + t_RefFomites +
            " (" + c_rfCodigo + " TEXT," + c_rfDescripcion + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA PROCESOFOMITES -->*/
    const val t_ProcesoFomites: String = "t_procesofomites"
    const val c_pCodigo: String = "c_codigo"
    const val c_pDescripcion: String = "c_descripcion"

    val Create_t_ProcesoFomites: String = "CREATE TABLE " + t_ProcesoFomites +
            " (" + c_pCodigo + " TEXT," + c_pDescripcion + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA TIPOFOMITES -->*/
    const val t_TipoFomites: String = "t_tipofomites"
    const val c_tCodigo: String = "c_codigo"
    const val c_tDescripcion: String = "c_descripcion"

    val Create_t_TipoFomites: String = "CREATE TABLE " + t_TipoFomites +
            " (" + c_tCodigo + " TEXT," + c_tDescripcion + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA CENCOSGALPON -->*/
    const val t_CencosGalpon: String = "t_regcencosgalpones"
    const val c_cgCodigo: String = "c_tcencos"
    const val c_cgGalpon: String = "c_tcodint"

    val Create_t_CcosGalpon: String = "CREATE TABLE " + t_CencosGalpon +
            " (" + c_cgCodigo + " TEXT," + c_cgGalpon + " TEXT)"


    /*<!-- TODO: TABLAS UTILIZADAS EN EL REGISTRO DE DATOS -->*/ /*<!-- TODO: VARIABLES DE TABLA RGARITA -->*/
    const val t_RGarita: String = "t_rgarita"
    const val c_rgId: String = "c_id"
    const val c_rgUsuario: String = "c_tuser"
    const val c_rgDate: String = "c_tdate"
    const val c_rgTime: String = "c_ttime"

    const val c_rgFecha: String = "c_fecha"
    const val c_rgCencos: String = "c_cencos"
    const val c_rgNomColaborador: String = "c_nomcolaborador"
    const val c_rgTurno: String = "c_turno"
    const val c_rgNumFiltro: String = "c_numfiltro"
    const val c_rgAnomalia: String = "c_anomalia"
    const val c_rgHora: String = "c_hora"
    const val c_rgDescripcion: String = "c_descripcion"
    const val c_rgAgenteCausal: String = "c_agentecausal"

    val Create_t_RGarita: String = "CREATE TABLE " + t_RGarita +
            " (" + c_rgId + " TEXT," + c_rgUsuario + " TEXT," + c_rgDate +
            " TEXT," + c_rgTime + " TEXT," + c_rgFecha + " TEXT," + c_rgCencos +
            " TEXT," + c_rgNomColaborador + " TEXT," + c_rgTurno + " TEXT," + c_rgNumFiltro +
            " TEXT," + c_rgAnomalia + " TEXT," + c_rgHora + " TEXT," + c_rgDescripcion +
            " TEXT," + c_rgAgenteCausal + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA RMANTENIMIENTO -->*/
    const val t_RMantenimiento: String = "t_rmantenimiento"
    const val c_rmId: String = "c_id"
    const val c_rmUsuario: String = "c_tuser"
    const val c_rmDate: String = "c_tdate"
    const val c_rmTime: String = "c_ttime"

    const val c_rmFecha: String = "c_fecha"
    const val c_rmCencos: String = "c_cencos"
    const val c_rmTurno: String = "c_turno"
    const val c_rmElemento: String = "c_elemento"
    const val c_rmFallo: String = "c_fallo"
    const val c_rmDescripcion: String = "c_descripcion"
    const val c_rmNomColaborador: String = "c_nomcolaborador"

    val Create_t_RMantenimiento: String = "CREATE TABLE " + t_RMantenimiento +
            " (" + c_rmId + " TEXT," + c_rmUsuario + " TEXT," + c_rmDate +
            " TEXT," + c_rmTime + " TEXT," + c_rmFecha + " TEXT," + c_rmCencos +
            " TEXT," + c_rmTurno + " TEXT," + c_rmElemento + " TEXT," + c_rmFallo +
            " TEXT," + c_rmDescripcion + " TEXT," + c_rmNomColaborador + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA RCERCOELECTRICO -->*/
    const val t_RCercoElectrico: String = "t_rcercoelectrico"
    const val c_rcId: String = "c_id"
    const val c_rcUsuario: String = "c_tuser"
    const val c_rcDate: String = "c_tdate"
    const val c_rcTime: String = "c_ttime"

    const val c_rcFecha: String = "c_fecha"
    const val c_rcCencos: String = "c_cencos"
    const val c_rcTurno: String = "c_turno"
    const val c_rcEstadoDerecho: String = "c_estadoderecho"
    const val c_rcDescEstadoDer: String = "c_descestadoder"
    const val c_rcEstadoIzquierdo: String = "c_estadoizquierdo"
    const val c_rcDescEstadoIzq: String = "c_descestadoizq"
    const val c_rcNomEvaluador: String = "c_nomevaluador"
    const val c_rcHoraInicio: String = "c_horainicio"
    const val c_rcHoraFinal: String = "c_horafinal"
    const val c_rcComponente: String = "c_componente"
    const val c_rcFallo: String = "c_fallo"
    const val c_rcDescripcion: String = "c_descripcion"

    val Create_t_RCercoElectrico: String = "CREATE TABLE " + t_RCercoElectrico +
            " (" + c_rcId + " TEXT," + c_rcUsuario + " TEXT," + c_rcDate +
            " TEXT," + c_rcTime + " TEXT," + c_rcFecha + " TEXT," + c_rcCencos +
            " TEXT," + c_rcTurno + " TEXT," + c_rcEstadoDerecho + " TEXT," + c_rcDescEstadoDer +
            " TEXT," + c_rcEstadoIzquierdo + " TEXT," + c_rcDescEstadoIzq + " TEXT," + c_rcNomEvaluador +
            " TEXT," + c_rcHoraInicio + " TEXT," + c_rcHoraFinal + " TEXT," + c_rcComponente +
            " TEXT," + c_rcFallo + " TEXT," + c_rcDescripcion + " TEXT)"

    /*<!-- TODO: VARIABLES DE TABLA RFOMITES -->*/
    const val t_RFomites: String = "t_rfomites"
    const val c_rfId: String = "c_id"
    const val c_rfUsuario: String = "c_tuser"
    const val c_rfDate: String = "c_tdate"
    const val c_rfTime: String = "c_ttime"

    const val c_rfFecha: String = "c_fecha"
    const val c_rfCencos: String = "c_cencos"
    const val c_rfTurno: String = "c_turno"
    const val c_rfReferencia: String = "c_referencia"
    const val c_rfProceso: String = "c_proceso"
    const val c_rfNomColaborador: String = "c_nomcolaborador"
    const val c_rfDepredador: String = "c_depredador"
    const val c_rfTipo: String = "c_tipo"
    const val c_rfCantidad: String = "c_cantidad"
    const val c_rfHora: String = "c_hora"
    const val c_rfDentroCerco: String = "c_dentrocerco"
    const val c_rfObservacion: String = "c_observacion"

    val Create_t_RFomites: String = "CREATE TABLE " + t_RFomites +
            " (" + c_rfId + " TEXT," + c_rfUsuario + " TEXT," + c_rfDate +
            " TEXT," + c_rfTime + " TEXT," + c_rfFecha + " TEXT," + c_rfCencos +
            " TEXT," + c_rfTurno + " TEXT," + c_rfReferencia + " TEXT," + c_rfProceso +
            " TEXT," + c_rfNomColaborador + " TEXT," + c_rfDepredador + " TEXT," + c_rfTipo +
            " TEXT," + c_rfCantidad + " TEXT," + c_rfHora + " TEXT," + c_rfDentroCerco +
            " TEXT," + c_rfObservacion + " TEXT)"
}