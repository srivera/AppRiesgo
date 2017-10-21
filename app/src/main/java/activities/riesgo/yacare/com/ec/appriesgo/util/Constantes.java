package activities.riesgo.yacare.com.ec.appriesgo.util;

/**
 * Created by yacare on 24/09/2015.
 */
public class Constantes {

    //ESTADOS RIESGOS
    public static String ESTADO_ACT = "ACT";
    public static String ESTADO_INA = "INA";

    //ESTADOS NOTIFICACION
    public static String ESTADO_NO_LEIDO = "NOL";
    public static String ESTADO_LEIDO = "LEI";

    //TIPOS DE CAPAS
    public static String CAPA_ALBERGUES = "AL";
    public static String CAPA_PUNTO_SEGURO = "PS";
    public static String CAPA_RUTA_EVACUACION = "RE";
    public static String CAPA_CENTRO_SALUD = "CS";
    public static String CAPA_AMENAZA_VOLCANICA = "AV";

    //CODIGO RIESGO
    public static String CODIGO_RIESGO_COTOPAXI = "1";

    //NOMBRES DE CAPAS
    public static String CAPA_ALBERGUES_NOMBRE = "Albergues";
    public static String CAPA_PUNTO_SEGURO_NOMBRE = "Sitios Seguros";
    public static String CAPA_RUTA_EVACUACION_NOMBRE = "Rutas de Evacuación";
    public static String CAPA_CENTRO_SALUS_NOMBRE = "Centros de Salud";
    public static String CAPA_AMENAZA_VOLCANICA_NOMBRE = "Amenaza Volcánica";
    public static String CAPA_PUNTO_ENCUENTRO_NOMBRE = "Puntos de Encuentro";

    //Nompres de las preferencias
    public static String PREF_HORA_INICIO_SIMULACRO = "prefHoraInicioSimulacro";
    public static String PREF_HORA_FINAL_SIMULACRO = "prefHoraFinalSimulacro";
    public static String PREF_LATITUD_INICIAL = "prefLatInicioSimulacro";
    public static String PREF_LONGITUD_INICIAL = "prefLonInicioSimulacro";
    public static String PREF_LATITUD_FINAL = "prefLatFinalSimulacro";
    public static String PREF_LONGITUD_FINAL = "prefLonFinalSimulacro";
    public static String PREF_SIMULACRO_INICIADO = "prefSimulacroIniciado";

    public static String PREF_ULTIMA_NOTIFICACION = "prefSUltimaNotificacion";

    //Mensajes de alertas
    public static String CODIGO_MENSAJE_AMARILLA = "AMA";
    public static String CODIGO_MENSAJE_NARANJA = "NAR";
    public static String CODIGO_MENSAJE_ROJA = "ROJ";
    public static String CODIGO_MENSAJE_BLANCA = "BLA";
    public static String CODIGO_MENSAJE_VERDE= "VER";
    public static String CODIGO_MENSAJE_TEXTO = "MSJ";

    public static String IP_CORP = "ecuadorseguro.cloudapp.net";
public final static String PUERTO_JBOSS_CORP = "10083";
//    public static String IP_CORP = "10.98.117.107";
//    public final static String PUERTO_JBOSS_CORP = "8080";

    public final static String URL_GUARDAR_PERSONA = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?guardarPersona";
    public final static String URL_GUARDAR_UBICACION = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?guardarUbicacion";
    public final static String URL_OBTENER_RIESGO = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerRiesgoV1";
    public final static String URL_OBTENER_OFICIOS = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerOficios";
    public final static String URL_OBTENER_PERSONA = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerPersona";
    public final static String URL_GUARDAR_CONTACTO = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?guardarContacto";
    public final static String URL_OBTENER_CONTACTOS = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerContactos";
    public final static String URL_ENVIAR_MENSAJE = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?enviarMensaje";
    public final static String URL_OBTENER_NOTIFICACION= "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerNotificacion";
    public final static String URL_OBTENER_REFERENCIAS= "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerReferencias";
    public final static String URL_GUARDAR_EMERGENCIA = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?guardarEmergencia";
    public final static String URL_OBTENER_RIESGOS = "http://" + IP_CORP + ":"+ PUERTO_JBOSS_CORP +"/ytalk-web-1.0.0/json/servicioAlertaEcuadorJS?obtenerRiesgosV5";

    public final static String URL_NEW_EMERGENCY= "http://190.214.21.188:8082/ecuservicespublicTest/ecuserver.php?wsdl";


    //Zona de riesgo
    public static String CODIGO_ZONA_RIESGO = "2";
    public static String CODIGO_ZONA_SEGURA = "1";
    public static String CODIGO_ZONA_NO_DEFINIDA = "0";

    public static long TIEMPO_ACTUALIZACION_GPS =  1000 * 60 * 1;


    public static float DISTANCIA_ACTUALIZACION_GPS =  2;

    public static String CERTIFICADO_SEGURIDAD = "yacaretechnology";
}
