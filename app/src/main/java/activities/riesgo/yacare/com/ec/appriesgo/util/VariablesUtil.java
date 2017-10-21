package activities.riesgo.yacare.com.ec.appriesgo.util;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ProcesarRutaEmergenciaAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.NotificacionDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RutaEmergenciaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetallePuntosEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetalleRutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.RutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.general.SplashActivity;
import activities.riesgo.yacare.com.ec.appriesgo.mapas.DirectionsJSONParser;

import static java.lang.Math.*;

/**
 * Created by yacare on 24/09/2015.
 */
public class VariablesUtil {


    public static final int NOTIFICATION_ID = 1;

    public static boolean procesandoChat = false;


    public static boolean servicioMonitoreoActivo = false;

    public static boolean tareaMensajeOffLineActiva = false;

    public static final VariablesUtil intance = new VariablesUtil();

    public static ArrayList<String> direcciones = new ArrayList<String>();

    public static VariablesUtil getInstance() {
        return intance;
    }

    public static Double calcularDistancia(double latitudOrigen, double longitudOrigen, double latitudDestino, double longitudDestino) {
        Double distancia;

        double pi = 3.14159265358979323846;
        double deg2radMultiplier = pi / 180;
        double radius = 6378.137;
        double dlon = 0;

        latitudOrigen = latitudOrigen * deg2radMultiplier;
        longitudOrigen = longitudOrigen * deg2radMultiplier;
        latitudDestino = latitudDestino * deg2radMultiplier;
        longitudDestino = longitudDestino * deg2radMultiplier;

        dlon = longitudDestino - longitudOrigen;

        return acos(sin(latitudOrigen) * sin(latitudDestino)
                + cos(latitudOrigen) * cos(latitudDestino)
                * cos(dlon))
                * radius;
    }


    private PrincipalActivity principalActivity;
    private LocationManager locationManager;
    private Context context;

    private boolean hasGpsSensor(Context context) {
        PackageManager packMan = context.getPackageManager();
        return packMan.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    public Location retornarUbicacion1(Context context, PrincipalActivity principalActivity) {
        this.principalActivity = principalActivity;
        this.context = context;
        Location location = null;

        //Obtenemos una referencia al LocationManager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            // First get location from Network Provider
            if (isNetworkEnabled) {

                if (locationManager != null) {
                    if (principalActivity != null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    }
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled && hasGpsSensor(context)) {
                if (location == null) {
                    if (locationManager != null) {
                        if (principalActivity != null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        }
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }


        }


//        //TODO
//        location.setLatitude(-1.03247);
//       location.setLongitude(-78.59482);

        //       location.setLatitude(-1.07659);
        //       location.setLongitude(-78.57284);

//        location.setLatitude(-1.088035784201442);
//        location.setLongitude(-78.57378172543338);

//                        location.setLatitude(-0.92905);
//                        location.setLongitude(-78.62397);


        return location;
    }

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 minute


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
//            if (principalActivity != null) {
//                principalActivity.enviarUbicacion(location);
//            }
            locationManager.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    };


    public void enviarNotificacion(String titulo, String msg, NotificationManager mNotificationManager, Context ctx) {

        String[] mensajes = msg.split(";");
        String codigoMensaje = mensajes[0];
        String idNotificacion = mensajes[1];
        String idRiesgo = "1";
        if(mensajes.length > 4) {
             idRiesgo = mensajes[4];
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        //Buscar la notificacion

        NotificacionDataSource notificacionDataSource = new NotificacionDataSource(ctx);
        notificacionDataSource.open();
        Notificacion notificacionBusqueda = notificacionDataSource.getNotificacionId(idNotificacion);

        if (notificacionBusqueda == null) {
            Notificacion notificacion = new Notificacion();
            notificacion.setId(idNotificacion);
            notificacion.setIdRiesgo(idRiesgo);
            notificacion.setFecha(dateFormat.format(date));
            notificacion.setMensaje(mensajes[2]);
            notificacion.setTitulo(titulo);

            //Insert en Notificacion
            notificacionDataSource.createNotificacion(notificacion);
            notificacionDataSource.close();


            RiesgoDataSource riesgoDataSource = new RiesgoDataSource(ctx);
            riesgoDataSource.open();
            Riesgo riesgo = riesgoDataSource.getRiesgoId(idRiesgo);
            riesgoDataSource.close();


            Boolean actualizarPantalla = false;
            DatosAplicacion datosAplicacion = (DatosAplicacion) ctx.getApplicationContext();
//            if (datosAplicacion.getPrincipalActivity() != null && riesgo.getId().equals(datosAplicacion.getRiesgoActual().getId())) {
            if (datosAplicacion.getPrincipalActivity() != null || datosAplicacion.getMenuActivity() != null) {
                actualizarPantalla = true;
            }

            if (codigoMensaje.equals(Constantes.CODIGO_MENSAJE_AMARILLA) || codigoMensaje.equals(Constantes.CODIGO_MENSAJE_NARANJA)
                    || codigoMensaje.equals(Constantes.CODIGO_MENSAJE_ROJA)) {
                notifcarAlerta(titulo, mNotificationManager, ctx, mensajes, codigoMensaje, actualizarPantalla, datosAplicacion, notificacion, idRiesgo);

            } else if (codigoMensaje.equals(Constantes.CODIGO_MENSAJE_BLANCA)) {
//                RiesgoDataSource riesgoDataSource = new RiesgoDataSource(ctx);
//                riesgoDataSource.open();
//                //todo
//                Riesgo riesgo = riesgoDataSource.getRiesgoId(idRiesgo);
//                riesgo.setAlertaActual(Constantes.CODIGO_MENSAJE_BLANCA);
//                riesgo.setFechaAlerta(dateFormat.format(date));
//                riesgoDataSource.updateRiesgo(riesgo);
//                riesgoDataSource.close();

                TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(ctx);
                tipoAlertaDataSource.open();
                TipoAlerta tipoAlerta = tipoAlertaDataSource.getTipoAlertaByCodigo(Constantes.CODIGO_MENSAJE_AMARILLA, datosAplicacion.getRiesgoActual().getId());
                tipoAlerta.setLeyendaCorta(titulo);
                tipoAlertaDataSource.updateTipoAlerta(tipoAlerta);
                tipoAlertaDataSource.close();

                riesgo.setTipoAlerta(tipoAlerta);
                datosAplicacion.setRiesgoActual(riesgo);

                Intent intent = new Intent(ctx, SplashActivity.class);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");

                PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                        intent, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.drawable.iconop)
                                .setContentTitle(titulo)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(mensajes[2]))
                                .setSound(RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION))
                                .setContentText(mensajes[2])
                                .setAutoCancel(true);
                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


                if (actualizarPantalla) {
                    if( datosAplicacion.getPrincipalActivity() != null) {
                        datosAplicacion.getPrincipalActivity().actualizarAlertaPantalla(codigoMensaje);
                    }
                }


            } else if (codigoMensaje.equals(Constantes.CODIGO_MENSAJE_TEXTO)) {

                Intent intent = new Intent(ctx, SplashActivity.class);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.putExtra("mensaje", mensajes[2] );
                intent.putExtra("titulo", titulo);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.drawable.iconop)
                                .setContentTitle(titulo)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(mensajes[2]))
                                .setSound(soundUri)
                                .setContentText(mensajes[2])
                                .setAutoCancel(true);
                mBuilder.setContentIntent(contentIntent);

                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                if (actualizarPantalla && datosAplicacion.getPrincipalActivity() != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            datosAplicacion.getPrincipalActivity());
                    alertDialogBuilder.setTitle(titulo)
                            .setMessage(mensajes[2])
                            .setCancelable(false)
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                if (actualizarPantalla && datosAplicacion.getMenuActivity() != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            datosAplicacion.getMenuActivity());
                    alertDialogBuilder.setTitle(titulo)
                            .setMessage(mensajes[2])
                            .setCancelable(false)
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            Vibrator mVibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(1000);
        } else {
            notificacionDataSource.close();
        }
    }

    private void notifcarAlerta(String titulo, NotificationManager mNotificationManager, Context ctx, String[] mensajes, String codigoMensaje,
                                Boolean actualizarPantalla, DatosAplicacion datosAplicacion, Notificacion notificacion, String idRiesgo ) {



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();


        TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(ctx);
        tipoAlertaDataSource.open();
        TipoAlerta tipoAlerta = tipoAlertaDataSource.getTipoAlertaByCodigo(mensajes[0], idRiesgo);
//        tipoAlerta.setLeyendaCorta(titulo);
//        tipoAlertaDataSource.updateTipoAlerta(tipoAlerta);
        tipoAlertaDataSource.close();

        Location location = VariablesUtil.getInstance().retornarUbicacion1(ctx, null);
        String zonaRiesgo = Constantes.CODIGO_ZONA_NO_DEFINIDA;
        if (location != null) {
            LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
            zonaRiesgo = verificarZonaRiesgo(punto, ctx, idRiesgo);
        }

        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(ctx);
        riesgoDataSource.open();
        Riesgo riesgo = riesgoDataSource.getRiesgoId(idRiesgo);
        riesgo.setAlertaActual(mensajes[0]);
        riesgo.setFechaAlerta(dateFormat.format(date));
        riesgoDataSource.updateRiesgo(riesgo);
        riesgoDataSource.close();

        riesgo.setTipoAlerta(tipoAlerta);

        datosAplicacion.setRiesgoActual(riesgo);

        if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_SEGURA)) {
            //Zona segura

            Intent intent = new Intent(ctx, SplashActivity.class);
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");

            intent.putExtra("mensaje", mensajes[2]);
            intent.putExtra("titulo", titulo);


            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                    intent, 0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.iconop)
                            .setContentTitle(titulo)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(mensajes[2]))
                            .setSound(soundUri)
                            .setContentText(mensajes[2])
                            .setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(VariablesUtil.NOTIFICATION_ID, mBuilder.build());

            if (actualizarPantalla && datosAplicacion.getPrincipalActivity() != null) {
                datosAplicacion.getPrincipalActivity().actualizarAlertaPantalla(codigoMensaje);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        datosAplicacion.getPrincipalActivity());
                alertDialogBuilder.setTitle(titulo)
                        .setMessage(mensajes[2])
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if (actualizarPantalla && datosAplicacion.getMenuActivity() != null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        datosAplicacion.getMenuActivity());
                alertDialogBuilder.setTitle(titulo)
                        .setMessage(mensajes[2])
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            //Zona riesgo
            notificacion.setMensaje(mensajes[3]);

            //Insert en Notificacion
            NotificacionDataSource notificacionDataSource = new NotificacionDataSource(ctx);
            notificacionDataSource.open();
            notificacionDataSource.updateNotificacion(notificacion);
            notificacionDataSource.close();

            if ( codigoMensaje.equals(Constantes.CODIGO_MENSAJE_ROJA)) {

                Uri sound = Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.alerta2);

                Intent intent = new Intent(ctx, SplashActivity.class);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");

                intent.putExtra("mensaje", mensajes[3]);
                intent.putExtra("titulo", titulo);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                        intent, 0);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.drawable.iconop)
                                .setContentTitle(titulo)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(mensajes[3]))
                                .setContentText(mensajes[3])
                                .setAutoCancel(true);
                mBuilder.setSound(sound);
                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(VariablesUtil.NOTIFICATION_ID, mBuilder.build());

//                if (location != null) {
//                    ProcesarRutaEmergenciaAsyncTask procesarRutaEmergenciaAsyncTask = new ProcesarRutaEmergenciaAsyncTask(location, ctx, true);
//                    procesarRutaEmergenciaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }
            } else {

                Intent intent = new Intent(ctx, SplashActivity.class);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");

                 intent.putExtra("mensaje", mensajes[3] );
                intent.putExtra("titulo", titulo);


                PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                        intent, 0);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ctx)
                                .setSmallIcon(R.drawable.iconop)
                                .setContentTitle(titulo)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(mensajes[3]))
                                .setSound(soundUri)
                                .setContentText(mensajes[3])
                                .setAutoCancel(true);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(VariablesUtil.NOTIFICATION_ID, mBuilder.build());
            }
            if (actualizarPantalla && datosAplicacion.getPrincipalActivity() != null) {
                datosAplicacion.getPrincipalActivity().actualizarAlertaPantalla(codigoMensaje);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        datosAplicacion.getPrincipalActivity());
                alertDialogBuilder.setTitle(titulo)
                        .setMessage(mensajes[3])
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if (actualizarPantalla && datosAplicacion.getMenuActivity() != null) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        datosAplicacion.getMenuActivity());
                alertDialogBuilder.setTitle(titulo)
                        .setMessage(mensajes[3])
                        .setCancelable(false)
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }






    }


    public String verificarZonaRiesgo(LatLng punto, Context context, String idRiesgo) {
        //AMEBNAZA VOLCANICA
        DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();
        ArrayList<Capa> capas;
//        if (datosAplicacion.getLahares() == null) {
            CapaDataSource capaDataSource = new CapaDataSource(context.getApplicationContext());
            capaDataSource.open();
            capas = capaDataSource.getCapaByTipo(Constantes.CAPA_AMENAZA_VOLCANICA, idRiesgo);
            capaDataSource.close();
//        } else {
//            capas = datosAplicacion.getLahares();
//        }

        // 0 no se tiene ubicacion, 1 zona segura, 2 zona insegura
        String zonaRiesgo = Constantes.CODIGO_ZONA_NO_DEFINIDA;
        Boolean encontroZonaRiesgo = false;

        if (punto == null) {
            //No se pudo determinar la ubicacion
            zonaRiesgo = Constantes.CODIGO_ZONA_NO_DEFINIDA;
        }
        for (Capa capa : capas) {
            String[] coordenadas = capa.getCoordenadas().split(" ");
            int i = 0;
            List<LatLng> puntosPoligono = new ArrayList<LatLng>();
            for (String puntoP : coordenadas) {
                String[] coordenada = puntoP.split(",");
                puntosPoligono.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                i++;
            }

            if (punto != null && !encontroZonaRiesgo) {
                if (PolyUtil.containsLocation(punto, puntosPoligono, false)) {
                    //Area de riesgo
                    //Verificar si tiene huecos, y si el punto esta dentro de un hueco no es zona de riesgo
                    if (capa.getHueco1() != null && !capa.getHueco1().equals("")) {
                        String[] coordenadasH = capa.getHueco1().split(" ");
                        List<LatLng> puntosPoligonoH = new ArrayList<LatLng>();
                        for (String puntoP : coordenadasH) {
                            String[] coordenada = puntoP.split(",");
                            puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                        }
                        if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                            zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                        } else {
                            if (capa.getHueco2() != null && !capa.getHueco2().equals("")) {
                                coordenadasH = capa.getHueco2().split(" ");
                                puntosPoligonoH = new ArrayList<LatLng>();
                                for (String puntoP : coordenadasH) {
                                    String[] coordenada = puntoP.split(",");
                                    puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                }
                                if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                                    zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                                } else {
                                    if (capa.getHueco3() != null && !capa.getHueco3().equals("")) {
                                        coordenadasH = capa.getHueco3().split(" ");
                                        puntosPoligonoH = new ArrayList<LatLng>();
                                        for (String puntoP : coordenadasH) {
                                            String[] coordenada = puntoP.split(",");
                                            puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                        }
                                        if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                                            zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                                        } else {
                                            zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                                            encontroZonaRiesgo = true;
                                        }
                                    } else {
                                        zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                                        encontroZonaRiesgo = true;
                                    }
                                }
                            } else {
                                zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                                encontroZonaRiesgo = true;
                            }
                        }

                    } else {
                        zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                        encontroZonaRiesgo = true;
                    }
                } else {
                    //Area segura
                    zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                }

            }

        }
        return zonaRiesgo;
    }


    public String verificarZonaRiesgoTotal(LatLng punto, Context context) {
        //AMEBNAZA VOLCANICA
        ArrayList<Capa> capas;
        CapaDataSource capaDataSource = new CapaDataSource(context.getApplicationContext());
        capaDataSource.open();
        capas = capaDataSource.getAllCapaByTipo(Constantes.CAPA_AMENAZA_VOLCANICA);
//        capas = capaDataSource.getCapaByTipo(Constantes.CAPA_AMENAZA_VOLCANICA, "1");
        capaDataSource.close();

        // 0 no se tiene ubicacion, 1 zona segura, 2 zona insegura
        String zonaRiesgo = Constantes.CODIGO_ZONA_NO_DEFINIDA;
        Boolean encontroZonaRiesgo = false;

        DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();

        if (punto == null) {
            //No se pudo determinar la ubicacion
            zonaRiesgo = Constantes.CODIGO_ZONA_NO_DEFINIDA;
        }
        for (Capa capa : capas) {
            String[] coordenadas = capa.getCoordenadas().split(" ");
            int i = 0;
            List<LatLng> puntosPoligono = new ArrayList<LatLng>();
            for (String puntoP : coordenadas) {
                String[] coordenada = puntoP.split(",");
                puntosPoligono.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                i++;
            }

            if (punto != null && !encontroZonaRiesgo) {
                if (PolyUtil.containsLocation(punto, puntosPoligono, false)) {
                    //Area de riesgo
                    //Verificar si tiene huecos, y si el punto esta dentro de un hueco no es zona de riesgo
                    if (capa.getHueco1() != null && !capa.getHueco1().equals("")) {
                        String[] coordenadasH = capa.getHueco1().split(" ");
                        List<LatLng> puntosPoligonoH = new ArrayList<LatLng>();
                        for (String puntoP : coordenadasH) {
                            String[] coordenada = puntoP.split(",");
                            puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                        }
                        if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                            zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                        } else {
                            if (capa.getHueco2() != null && !capa.getHueco2().equals("")) {
                                coordenadasH = capa.getHueco2().split(" ");
                                puntosPoligonoH = new ArrayList<LatLng>();
                                for (String puntoP : coordenadasH) {
                                    String[] coordenada = puntoP.split(",");
                                    puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                }
                                if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                                    zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                                } else {
                                    if (capa.getHueco3() != null && !capa.getHueco3().equals("")) {
                                        coordenadasH = capa.getHueco3().split(" ");
                                        puntosPoligonoH = new ArrayList<LatLng>();
                                        for (String puntoP : coordenadasH) {
                                            String[] coordenada = puntoP.split(",");
                                            puntosPoligonoH.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                        }
                                        if (PolyUtil.containsLocation(punto, puntosPoligonoH, false)) {
                                            zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                                        } else {
                                            zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;

                                            RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context.getApplicationContext());
                                            riesgoDataSource.open();
                                            datosAplicacion.setRiesgoMonitoreo(riesgoDataSource.getRiesgoId(capa.getIdRiesgo()));
                                            riesgoDataSource.close();
                                            encontroZonaRiesgo = true;
                                        }
                                    } else {
                                        zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                                        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context.getApplicationContext());
                                        riesgoDataSource.open();
                                        datosAplicacion.setRiesgoMonitoreo(riesgoDataSource.getRiesgoId(capa.getIdRiesgo()));
                                        riesgoDataSource.close();
                                        encontroZonaRiesgo = true;
                                    }
                                }
                            } else {
                                zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                                RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context.getApplicationContext());
                                riesgoDataSource.open();
                                datosAplicacion.setRiesgoMonitoreo(riesgoDataSource.getRiesgoId(capa.getIdRiesgo()));
                                riesgoDataSource.close();
                                encontroZonaRiesgo = true;
                            }
                        }

                    } else {
                        zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context.getApplicationContext());
                        riesgoDataSource.open();
                        datosAplicacion.setRiesgoMonitoreo(riesgoDataSource.getRiesgoId(capa.getIdRiesgo()));
                        riesgoDataSource.close();
                        encontroZonaRiesgo = true;
                    }
                } else {
                    //Area segura
                    zonaRiesgo = Constantes.CODIGO_ZONA_SEGURA;
                }

            }

        }
        return zonaRiesgo;
    }
    public ArrayList<RutaEmergencia> buscarRutasCercanas(Location ubicacion, Context context, Boolean insertarTabla) {

        ArrayList<RutaEmergencia> rutasEmergencia = null;
        if (ubicacion != null) {
            DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();
            rutasEmergencia = new ArrayList<RutaEmergencia>();
            CapaDataSource capaDataSource = new CapaDataSource(context);
            capaDataSource.open();
            ArrayList<Capa> rutasEvacuacion = capaDataSource.getCapaByTipo(Constantes.CAPA_RUTA_EVACUACION,  datosAplicacion.getRiesgoActual().getId());
            capaDataSource.close();

            Capa capaUno = null;
            Capa capaDos = null;
            Capa capaTres = null;

            Double distanciaMenorUno = null;
            Double distanciaMenorDos = null;
            Double distanciaMenorTres = null;

            LatLng puntoMasCercaRutaUno = null;
            LatLng puntoMasCercaRutaDos = null;
            LatLng puntoMasCercaRutaTres = null;

            for (Capa capa : rutasEvacuacion) {
                String[] coordenadas = capa.getCoordenadas().split(" ");

                Double distanciaMenorRuta = null;
                LatLng puntoMasCercaRuta = null;

                for (String punto : coordenadas) {
                    String[] coordenada = punto.split(",");

                    double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacion.getLatitude(), ubicacion.getLongitude(),
                            Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));

                    if (distanciaMenorRuta == null || distanciaCalculada < distanciaMenorRuta) {
                        distanciaMenorRuta = distanciaCalculada;
                        puntoMasCercaRuta = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    }
                }

                if (distanciaMenorUno == null || distanciaMenorRuta < distanciaMenorUno) {
                    distanciaMenorUno = distanciaMenorRuta;
                    capaUno = capa;
                    puntoMasCercaRutaUno = puntoMasCercaRuta;
                }
            }
            RutaEmergencia rutaEmergencia = new RutaEmergencia();
            rutaEmergencia.setCapa(capaUno);
            rutaEmergencia.setIdCapa(capaUno.getId());
            rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
            rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
            rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaUno.latitude));
            rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaUno.longitude));
            rutaEmergencia.setDistancia(String.valueOf(distanciaMenorUno));
            rutaEmergencia.setId("1");
            rutasEmergencia.add(rutaEmergencia);


            for (Capa capa : rutasEvacuacion) {
                if (!capa.getId().equals(capaUno.getId())) {
                    String[] coordenadas = capa.getCoordenadas().split(" ");

                    Double distanciaMenorRuta = null;
                    LatLng puntoMasCercaRuta = null;

                    for (String punto : coordenadas) {

                        String[] coordenada = punto.split(",");

                        double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacion.getLatitude(), ubicacion.getLongitude(),
                                Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));

                        if (distanciaMenorRuta == null || distanciaCalculada < distanciaMenorRuta) {
                            distanciaMenorRuta = distanciaCalculada;
                            puntoMasCercaRuta = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                        }

                    }
                    if (distanciaMenorDos == null || distanciaMenorRuta < distanciaMenorDos) {
                        distanciaMenorDos = distanciaMenorRuta;
                        capaDos = capa;
                        puntoMasCercaRutaDos = puntoMasCercaRuta;
                    }
                }

            }

            rutaEmergencia = new RutaEmergencia();
            rutaEmergencia.setCapa(capaDos);
            rutaEmergencia.setIdCapa(capaDos.getId());
            rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
            rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
            rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaDos.latitude));
            rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaDos.longitude));
            rutaEmergencia.setDistancia(String.valueOf(distanciaMenorDos));
            rutaEmergencia.setId("2");
            rutasEmergencia.add(rutaEmergencia);


            for (Capa capa : rutasEvacuacion) {
                if (!capa.getId().equals(capaUno.getId()) && !capa.getId().equals(capaDos.getId())) {
                    String[] coordenadas = capa.getCoordenadas().split(" ");

                    Double distanciaMenorRuta = null;
                    LatLng puntoMasCercaRuta = null;

                    for (String punto : coordenadas) {
                        String[] coordenada = punto.split(",");

                        double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacion.getLatitude(), ubicacion.getLongitude(),
                                Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));

                        if (distanciaMenorRuta == null || distanciaCalculada < distanciaMenorRuta) {
                            distanciaMenorRuta = distanciaCalculada;
                            puntoMasCercaRuta = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                        }
                    }
                    if (distanciaMenorTres == null || distanciaMenorRuta < distanciaMenorTres) {
                        distanciaMenorTres = distanciaMenorRuta;
                        capaTres = capa;
                        puntoMasCercaRutaTres = puntoMasCercaRuta;
                    }
                }
            }


            rutaEmergencia = new RutaEmergencia();
            rutaEmergencia.setCapa(capaTres);
            rutaEmergencia.setIdCapa(capaTres.getId());
            rutaEmergencia.setLatitudOrigen(String.valueOf(ubicacion.getLatitude()));
            rutaEmergencia.setLongitudOrigen(String.valueOf(ubicacion.getLongitude()));
            rutaEmergencia.setLatitudDestino(String.valueOf(puntoMasCercaRutaTres.latitude));
            rutaEmergencia.setLongitudDestino(String.valueOf(puntoMasCercaRutaTres.longitude));
            rutaEmergencia.setDistancia(String.valueOf(distanciaMenorTres));
            rutaEmergencia.setId("3");
            rutasEmergencia.add(rutaEmergencia);


            if (insertarTabla && rutasEmergencia.size() > 0) {
                RutaEmergenciaDataSource rutaEmergenciaDataSource = new RutaEmergenciaDataSource(context);
                rutaEmergenciaDataSource.open();
                rutaEmergenciaDataSource.deleteAllRutaEmergencia();
                rutaEmergenciaDataSource.close();

                for (RutaEmergencia rutaEmergenciaC : rutasEmergencia) {
                    String url = getDirectionsUrl(new LatLng(Double.valueOf(rutaEmergenciaC.getLatitudOrigen()), Double.valueOf(rutaEmergenciaC.getLongitudOrigen())),
                            new LatLng(Double.valueOf(rutaEmergenciaC.getLatitudDestino()), Double.valueOf(rutaEmergenciaC.getLongitudDestino())));

                    DownloadTaskRutaEvacuacion downloadTask = new DownloadTaskRutaEvacuacion(rutaEmergenciaC, context);

                    downloadTask.execute(url);
                }
            }

        }


        return rutasEmergencia;
    }


    public Capa buscarSitioCercano(Location ubicacion, ArrayList<Capa> todosPuntos, ArrayList<Capa> puntosMasCercanos){
        Capa masCercano = null;
        Double distanciaMenor = null;
        for(Capa capa : todosPuntos){

            if(esNuevoPunto(capa, puntosMasCercanos)){
                String[] coordenada = capa.getCoordenadas().split(",");
                Double distanciaCalculada = VariablesUtil.calcularDistancia(ubicacion.getLatitude(), ubicacion.getLongitude(),
                        Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                if (distanciaMenor == null || distanciaCalculada < distanciaMenor) {
                    distanciaMenor = distanciaCalculada;
                    masCercano = capa;
                }

            }
         }



        return masCercano;
    }

    private Boolean esNuevoPunto(Capa punto, ArrayList<Capa> puntosMasCercanos ) {
        for(Capa capa : puntosMasCercanos){
            if(capa.getId().equals(punto.getId())) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Capa> buscarSitiosSegurosCercanos(Location ubicacion, Context context) {

        ArrayList<Capa> puntosCercanos = null;
        if (ubicacion != null) {
            DatosAplicacion datosAplicacion = (DatosAplicacion) context.getApplicationContext();
            CapaDataSource capaDataSource = new CapaDataSource(context);
            capaDataSource.open();
            ArrayList<Capa> rutasEvacuacion = capaDataSource.getCapaByTipo(Constantes.CAPA_PUNTO_SEGURO,  datosAplicacion.getRiesgoActual().getId());
            capaDataSource.close();

            puntosCercanos = new ArrayList<Capa>();
            puntosCercanos.add(buscarSitioCercano(ubicacion, rutasEvacuacion, puntosCercanos));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion, rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));
            puntosCercanos.add(buscarSitioCercano(ubicacion,rutasEvacuacion, puntosCercanos ));

        }
        return puntosCercanos;
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=es" + "&mode=walking";
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }


    // Fetches data from url passed
    private class DownloadTaskRutaEvacuacion extends AsyncTask<String, Void, String> {

        RutaEmergencia rutaEmergencia;
        Context context;

        public DownloadTaskRutaEvacuacion(RutaEmergencia rutaEmergencia, Context context) {
            this.rutaEmergencia = rutaEmergencia;
            this.context = context;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTaskRutaEvacuacion parserTask = new ParserTaskRutaEvacuacion(rutaEmergencia, context);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        /**
         * A method to download json data from url
         */
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                // Connecting to url
                urlConnection.connect();
                // Reading data from url
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (Exception e) {
                Log.d("Exception", "error");
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTaskRutaEvacuacion extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        RutaEmergencia rutaEmergencia;
        Context context;
        RutasRetorno rutasRetorno;

        public ParserTaskRutaEvacuacion(RutaEmergencia rutaEmergencia, Context context) {
            this.rutaEmergencia = rutaEmergencia;
            this.context = context;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                rutasRetorno = parser.parse(jObject);
                routes = rutasRetorno.routes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<DetallePuntosEmergencia> listaPuntos = new ArrayList<DetallePuntosEmergencia>();
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    DetallePuntosEmergencia detallePuntosEmergencia = new DetallePuntosEmergencia();
                    detallePuntosEmergencia.setLatitud(String.valueOf(lat));
                    detallePuntosEmergencia.setLongitud(String.valueOf(lng));
                    detallePuntosEmergencia.setIdRuta(rutaEmergencia.getId());
                    listaPuntos.add(detallePuntosEmergencia);
                }

            }

            rutaEmergencia.setDetallePuntos(listaPuntos);
            // Drawing polyline in the Google Map for the i-th route

            ArrayList<DetalleRutaEmergencia> listaRutas = new ArrayList<DetalleRutaEmergencia>();
            for (String texto : rutasRetorno.direcciones) {
                DetalleRutaEmergencia detalleRutaEmergencia = new DetalleRutaEmergencia();
                detalleRutaEmergencia.setInstruccion(texto.toString());
                detalleRutaEmergencia.setIdRuta(rutaEmergencia.getId());
                listaRutas.add(detalleRutaEmergencia);
            }
            rutaEmergencia.setDetalleRuta(listaRutas);

            RutaEmergenciaDataSource rutaEmergenciaDataSource = new RutaEmergenciaDataSource(context);
            rutaEmergenciaDataSource.open();
            rutaEmergenciaDataSource.createRutaEmergencia(rutaEmergencia);
            rutaEmergenciaDataSource.close();

        }
    }

    public static boolean verificarConexionIternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE);
        NetworkInfo.State mobile = NetworkInfo.State.DISCONNECTED;
        if (mobileInfo != null) {
            mobile = mobileInfo.getState();
        }
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State wifi = NetworkInfo.State.DISCONNECTED;
        if (wifiInfo != null) {
            wifi = wifiInfo.getState();
        }
        boolean dataOnWifiOnly = (Boolean) PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(
                        "data_wifi_only", true);
        if ((!dataOnWifiOnly && (mobile.equals(NetworkInfo.State.CONNECTED) || wifi
                .equals(NetworkInfo.State.CONNECTED)))
                || (dataOnWifiOnly && wifi.equals(NetworkInfo.State.CONNECTED))) {
            return false;
        } else {
            return true;
        }
    }

    public static String generarMD5(String cadena) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.reset();

        m.update(cadena.getBytes());

        byte[] digest = m.digest();

        BigInteger bigInt = new BigInteger(1, digest);

        String hashtext = bigInt.toString(16);

        while (hashtext.length() < 32) {

            hashtext = "0" + hashtext;

        }
        return hashtext;
    }

}
