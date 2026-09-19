package com.rinconadadelsur.sistemas.bioseguridad.DataBase;

public class dbEstructura {
    public static String miBaseDatos = "bd_Bioseguridad";

    /*<!-- TODO: TABLAS UTILIZADAS EN EL LOGIN -->*/

    //todo:Variables de TABLA CLV
    public static final String TABLA_CLV="t_clv";
    public static final String Campo_uCodClv="c_codigo";
    public static final String Campo_uNomClv="c_nombre";

    public static final String CREATE_TABLA_CLV="CREATE TABLE "+TABLA_CLV+
            " ("+Campo_uCodClv+" TEXT,"+Campo_uNomClv+" TEXT)";
    //todo:Variables de TABLA ANDROID
    public static final String t_android = "t_android";
    public static final String c_aCodigo = "c_codigo";
    public static final String c_aSerie = "c_serie";
    public static final String c_aFecha = "c_fecha";
    public static final String c_aEstado = "c_estado";

    public static final String CREATE_TABLA_ANDROID = "CREATE TABLE " + t_android +
            " (" + c_aCodigo + " TEXT," + c_aSerie + " TEXT," + c_aFecha + " TEXT," + c_aEstado + " TEXT)";



    //todo:Variables de TABLA USUARIOS
    public static final String t_usuarios = "t_usuarios";
    public static final String c_uCodigo = "c_codigo";
    public static final String c_uNombre = "c_nombre";
    public static final String c_uDni = "c_dni";
    public static final String c_uPassword = "c_password";
    public static final String c_uPermiso = "c_permiso";

    public static final String CREATE_TABLA_USUARIOS = "CREATE TABLE " + t_usuarios +
            " (" + c_uCodigo + " TEXT," + c_uNombre + " TEXT," + c_uDni + " TEXT," + c_uPassword + " TEXT,"+c_uPermiso+" TEXT)";

    /*<!-- TODO: Variables de TABLA TCENCOS  -->*/
    public static final String t_TCencos = "t_tcencos";
    public static final String c_tcCodigo = "c_codigo";//0
    public static final String c_tcNombre = "c_nombre";//1

    public static final String Create_t_TCencos = "CREATE TABLE " + t_TCencos +
            " (" + c_tcCodigo + " TEXT," + c_tcNombre + " TEXT)";

    /*<!-- TODO: TABLAS UTILIZADAS EN LA IMPORTACION DE DATOS -->*/
    /*<!-- TODO: VARIABLES DE TABLA CENCOS -->*/
    public static final String t_Cencos = "t_cencos";
    public static final String c_cCodigo = "c_codigo";
    public static final String c_cNombre = "c_nombre";

    public static final String Create_t_Cencos = "CREATE TABLE " + t_Cencos +
            " (" + c_cCodigo + " TEXT," + c_cNombre + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA ANOMALIAGARITA -->*/
    public static final String t_AnomaliaGarita = "t_anomaliagarita";
    public static final String c_anCodigo= "c_codigo";
    public static final String c_anDescripcion = "c_descripcion";

    public static final String Create_t_AnomaliaGarita= "CREATE TABLE " + t_AnomaliaGarita+
            " (" + c_anCodigo + " TEXT," + c_anDescripcion + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA REFERENCIAFOM -->*/
    public static final String t_RefFomites = "t_referenciafom";
    public static final String c_rfCodigo= "c_codigo";
    public static final String c_rfDescripcion = "c_descripcion";

    public static final String Create_t_RefFomites= "CREATE TABLE " + t_RefFomites+
            " (" + c_rfCodigo + " TEXT," + c_rfDescripcion + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA PROCESOFOMITES -->*/
    public static final String t_ProcesoFomites = "t_procesofomites";
    public static final String c_pCodigo= "c_codigo";
    public static final String c_pDescripcion = "c_descripcion";

    public static final String Create_t_ProcesoFomites= "CREATE TABLE " + t_ProcesoFomites+
            " (" + c_pCodigo + " TEXT," + c_pDescripcion + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA TIPOFOMITES -->*/
    public static final String t_TipoFomites = "t_tipofomites";
    public static final String c_tCodigo= "c_codigo";
    public static final String c_tDescripcion = "c_descripcion";

    public static final String Create_t_TipoFomites= "CREATE TABLE " + t_TipoFomites+
            " (" + c_tCodigo + " TEXT," + c_tDescripcion + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA CENCOSGALPON -->*/
    public static final String t_CencosGalpon="t_regcencosgalpones";
    public static final String c_cgCodigo="c_tcencos";
    public static final String c_cgGalpon="c_tcodint";

    public static final String Create_t_CcosGalpon = "CREATE TABLE "+t_CencosGalpon +
            " ("+c_cgCodigo+" TEXT,"+c_cgGalpon+" TEXT)";


    /*<!-- TODO: TABLAS UTILIZADAS EN EL REGISTRO DE DATOS -->*/
    /*<!-- TODO: VARIABLES DE TABLA RGARITA -->*/
    public static final String t_RGarita = "t_rgarita";
    public static final String c_rgId= "c_id";
    public static final String c_rgUsuario= "c_tuser";
    public static final String c_rgDate= "c_tdate";
    public static final String c_rgTime= "c_ttime";

    public static final String c_rgFecha= "c_fecha";
    public static final String c_rgCencos = "c_cencos";
    public static final String c_rgNomColaborador = "c_nomcolaborador";
    public static final String c_rgTurno= "c_turno";
    public static final String c_rgNumFiltro= "c_numfiltro";
    public static final String c_rgAnomalia= "c_anomalia";
    public static final String c_rgHora= "c_hora";
    public static final String c_rgDescripcion= "c_descripcion";
    public static final String c_rgAgenteCausal= "c_agentecausal";

    public static final String Create_t_RGarita = "CREATE TABLE " + t_RGarita +
            " (" + c_rgId + " TEXT," + c_rgUsuario + " TEXT," + c_rgDate +
            " TEXT," + c_rgTime + " TEXT," + c_rgFecha + " TEXT," + c_rgCencos +
            " TEXT," + c_rgNomColaborador + " TEXT," + c_rgTurno + " TEXT," + c_rgNumFiltro +
            " TEXT," + c_rgAnomalia + " TEXT," + c_rgHora + " TEXT," + c_rgDescripcion +
            " TEXT," + c_rgAgenteCausal + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA RMANTENIMIENTO -->*/
    public static final String t_RMantenimiento = "t_rmantenimiento";
    public static final String c_rmId = "c_id";
    public static final String c_rmUsuario = "c_tuser";
    public static final String c_rmDate = "c_tdate";
    public static final String c_rmTime = "c_ttime";

    public static final String c_rmFecha = "c_fecha";
    public static final String c_rmCencos = "c_cencos";
    public static final String c_rmTurno = "c_turno";
    public static final String c_rmElemento = "c_elemento";
    public static final String c_rmFallo = "c_fallo";
    public static final String c_rmDescripcion = "c_descripcion";
    public static final String c_rmNomColaborador = "c_nomcolaborador";

    public static final String Create_t_RMantenimiento = "CREATE TABLE " + t_RMantenimiento +
            " (" + c_rmId + " TEXT," + c_rmUsuario + " TEXT," + c_rmDate +
            " TEXT," + c_rmTime + " TEXT," + c_rmFecha + " TEXT," + c_rmCencos +
            " TEXT," + c_rmTurno + " TEXT," + c_rmElemento + " TEXT," + c_rmFallo +
            " TEXT," + c_rmDescripcion + " TEXT," + c_rmNomColaborador + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA RCERCOELECTRICO -->*/
    public static final String t_RCercoElectrico = "t_rcercoelectrico";
    public static final String c_rcId= "c_id";
    public static final String c_rcUsuario = "c_tuser";
    public static final String c_rcDate = "c_tdate";
    public static final String c_rcTime = "c_ttime";

    public static final String c_rcFecha= "c_fecha";
    public static final String c_rcCencos = "c_cencos";
    public static final String c_rcTurno= "c_turno";
    public static final String c_rcEstadoDerecho= "c_estadoderecho";
    public static final String c_rcDescEstadoDer= "c_descestadoder";
    public static final String c_rcEstadoIzquierdo= "c_estadoizquierdo";
    public static final String c_rcDescEstadoIzq= "c_descestadoizq";
    public static final String c_rcNomEvaluador= "c_nomevaluador";
    public static final String c_rcHoraInicio= "c_horainicio";
    public static final String c_rcHoraFinal= "c_horafinal";
    public static final String c_rcComponente= "c_componente";
    public static final String c_rcFallo= "c_fallo";
    public static final String c_rcDescripcion= "c_descripcion";

    public static final String Create_t_RCercoElectrico = "CREATE TABLE " + t_RCercoElectrico +
            " (" + c_rcId + " TEXT," + c_rcUsuario + " TEXT," + c_rcDate +
            " TEXT," + c_rcTime + " TEXT," + c_rcFecha + " TEXT," + c_rcCencos +
            " TEXT," + c_rcTurno + " TEXT," + c_rcEstadoDerecho + " TEXT," + c_rcDescEstadoDer +
            " TEXT," + c_rcEstadoIzquierdo + " TEXT," + c_rcDescEstadoIzq + " TEXT," + c_rcNomEvaluador +
            " TEXT," + c_rcHoraInicio + " TEXT," + c_rcHoraFinal + " TEXT," + c_rcComponente +
            " TEXT," + c_rcFallo + " TEXT," + c_rcDescripcion + " TEXT)";

    /*<!-- TODO: VARIABLES DE TABLA RFOMITES -->*/
    public static final String t_RFomites = "t_rfomites";
    public static final String c_rfId= "c_id";
    public static final String c_rfUsuario = "c_tuser";
    public static final String c_rfDate = "c_tdate";
    public static final String c_rfTime = "c_ttime";

    public static final String c_rfFecha= "c_fecha";
    public static final String c_rfCencos = "c_cencos";
    public static final String c_rfTurno= "c_turno";
    public static final String c_rfReferencia= "c_referencia";
    public static final String c_rfProceso= "c_proceso";
    public static final String c_rfNomColaborador= "c_nomcolaborador";
    public static final String c_rfDepredador= "c_depredador";
    public static final String c_rfTipo= "c_tipo";
    public static final String c_rfCantidad= "c_cantidad";
    public static final String c_rfHora= "c_hora";
    public static final String c_rfDentroCerco= "c_dentrocerco";
    public static final String c_rfObservacion="c_observacion";

    public static final String Create_t_RFomites= "CREATE TABLE " + t_RFomites+
            " (" + c_rfId + " TEXT," + c_rfUsuario + " TEXT," + c_rfDate +
            " TEXT," + c_rfTime + " TEXT," + c_rfFecha + " TEXT," + c_rfCencos +
            " TEXT," + c_rfTurno + " TEXT," + c_rfReferencia + " TEXT," + c_rfProceso +
            " TEXT," + c_rfNomColaborador + " TEXT," + c_rfDepredador+ " TEXT," + c_rfTipo +
            " TEXT," + c_rfCantidad + " TEXT," + c_rfHora + " TEXT," + c_rfDentroCerco +
            " TEXT," + c_rfObservacion + " TEXT)";

}