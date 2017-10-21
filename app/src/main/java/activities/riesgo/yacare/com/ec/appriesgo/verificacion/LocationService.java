package activities.riesgo.yacare.com.ec.appriesgo.verificacion;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.NotificacionDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.general.MenuActivity;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.general.SplashActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class LocationService extends Service {


        private static final int TWO_MINUTES = 1000 * 60 * 2;
        public LocationManager locationManager;
        public MyLocationListener listener;
        public Location previousBestLocation = null;
        Context context;

        Intent intent;
        int counter = 0;

        @Override
        public void onCreate()
        {
            super.onCreate();
            //intent = new Intent(BROADCAST_ACTION);
            context = getApplicationContext();
        }

        @Override
        public void onStart(Intent intent, int startId)
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            listener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            if(hasGpsSensor(context)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }

        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }


    private boolean hasGpsSensor(Context context) {
        PackageManager packMan = context.getPackageManager();
        return packMan.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }



    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
        LocationService.this.stopSelf();
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }




    class MyLocationListener implements LocationListener
    {
        public  boolean ingresoZonaRiesgo = false;

        public  boolean ingresoZonaSegura = true;


        public void onLocationChanged(final Location loc){
            if(isBetterLocation(loc, previousBestLocation)) {
                previousBestLocation = loc;
                DatosAplicacion datosAplicacion = (DatosAplicacion) context;
                String zonaRiesgo = VariablesUtil.getInstance().verificarZonaRiesgoTotal(new LatLng(loc.getLatitude(), loc.getLongitude()), context);

//                RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context.getApplicationContext());
//                riesgoDataSource.open();
//                datosAplicacion.setRiesgoMonitoreo(riesgoDataSource.getRiesgoId("2"));
//                riesgoDataSource.close();
//               zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                if (!zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO)) {
                    ingresoZonaRiesgo = false;
                    if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_SEGURA) && !ingresoZonaSegura) {
                        ingresoZonaSegura = true;
                        //notificar ubicacion
                        NotificationManager mNotificationManager = (NotificationManager)
                                context.getSystemService(Context.NOTIFICATION_SERVICE);

                        Intent intent = new Intent(context, SplashActivity.class);
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                        intent.putExtra("mensaje", "Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");

                        intent.putExtra("titulo", "ZONA SEGURA");

                        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.not1);

                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0) ;
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.iconop)
                                        .setContentTitle("ZONA SEGURA")
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales."))
                                        .setSound(soundUri)
                                        .setContentText("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.")
                                        .setAutoCancel(true);
                        mBuilder.setContentIntent(contentIntent);

                        mNotificationManager.notify(1, mBuilder.build());

                        NotificacionDataSource notificacionDataSource = new NotificacionDataSource(context);
                        notificacionDataSource.open();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Notificacion notificacion = new Notificacion();
                        notificacion.setId(UUID.randomUUID().toString());
                        notificacion.setIdRiesgo(datosAplicacion.getRiesgoMonitoreo().getId());
                        notificacion.setFecha(dateFormat.format(date));
                        notificacion.setMensaje("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");
                        notificacion.setTitulo("ZONA SEGURA");

                        notificacionDataSource.createNotificacion(notificacion);
                        notificacionDataSource.close();

                        if ( datosAplicacion.getPrincipalActivity() != null) {

                            LayoutInflater layoutInflater = LayoutInflater.from(datosAplicacion.getPrincipalActivity());
                            View promptView = layoutInflater.inflate(R.layout.dialog_mensaje_verificar, null);
                            android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(datosAplicacion.getPrincipalActivity());
                            alertDialogBuilder.setView(promptView);

                            Typeface fontRegular = Typeface.createFromAsset(datosAplicacion.getPrincipalActivity().getAssets(), "Lato-Regular.ttf");

                            final TextView tituloDialog = (TextView) promptView.findViewById(R.id.tituloDialog);
                            final TextView leyendaDialog = (TextView) promptView.findViewById(R.id.leyendaDialog);
                            final ImageView imgDialog = (ImageView) promptView.findViewById(R.id.imgDialog);
                            final LinearLayout layoutDialog = (LinearLayout) promptView.findViewById(R.id.layoutDialog);

                            tituloDialog.setTypeface(fontRegular);
                            leyendaDialog.setTypeface(fontRegular);


                            alertDialogBuilder.setCancelable(false)
                                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });

                            // create an alert dialog
                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();

                            layoutDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            leyendaDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            tituloDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            imgDialog.setImageResource(R.drawable.zona_segura);
                            tituloDialog.setText("ZONA SEGURA");
                            leyendaDialog.setText("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");
                            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#33A948"));
                            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));


                            tituloDialog.setTextColor(Color.parseColor("#FFFFFF"));
                            imgDialog.setColorFilter(Color.parseColor("#FFFFFF"));
                            leyendaDialog.setTextColor(Color.parseColor("#FFFFFF"));
                        }else if ( datosAplicacion.getMenuActivity() != null){

                            LayoutInflater layoutInflater = LayoutInflater.from(datosAplicacion.getMenuActivity());
                            View promptView = layoutInflater.inflate(R.layout.dialog_mensaje_verificar, null);
                            android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(datosAplicacion.getMenuActivity());
                            alertDialogBuilder.setView(promptView);

                            Typeface fontRegular = Typeface.createFromAsset(datosAplicacion.getMenuActivity().getAssets(), "Lato-Regular.ttf");

                            final TextView tituloDialog = (TextView) promptView.findViewById(R.id.tituloDialog);
                            final TextView leyendaDialog = (TextView) promptView.findViewById(R.id.leyendaDialog);
                            final ImageView imgDialog = (ImageView) promptView.findViewById(R.id.imgDialog);
                            final LinearLayout layoutDialog = (LinearLayout) promptView.findViewById(R.id.layoutDialog);

                            tituloDialog.setTypeface(fontRegular);
                            leyendaDialog.setTypeface(fontRegular);


                            alertDialogBuilder.setCancelable(false)
                                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });

                            // create an alert dialog
                            AlertDialog alert = alertDialogBuilder.create();
                            alert.show();

                            layoutDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            leyendaDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            tituloDialog.setBackgroundColor(Color.parseColor("#33A948"));
                            imgDialog.setImageResource(R.drawable.zona_segura);
                            tituloDialog.setText("ZONA SEGURA");
                            leyendaDialog.setText("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");
                            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#33A948"));
                            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));


                            tituloDialog.setTextColor(Color.parseColor("#FFFFFF"));
                            imgDialog.setColorFilter(Color.parseColor("#FFFFFF"));
                            leyendaDialog.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }

                if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO) && !ingresoZonaRiesgo) {
                    ingresoZonaRiesgo = true;
                    ingresoZonaSegura = false;
                    //notificar ubicacion
                    NotificationManager mNotificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Intent intent = new Intent(context, SplashActivity.class);
//                    intent.setAction("android.intent.action.MAIN");
//                    intent.addCategory("android.intent.category.LAUNCHER");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("mensaje", "Usted se encuentra en zona de riesgo del " + datosAplicacion.getRiesgoMonitoreo().getNombreRiesgo()+  ". Manténgase informado por fuentes oficiales.");

                    intent.putExtra("titulo", "ZONA DE RIESGO");

                    Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.not1);

                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0) ;
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.iconop)
                                    .setContentTitle("ZONA DE RIESGO")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Usted se encuentra en zona de riesgo del " + datosAplicacion.getRiesgoMonitoreo().getNombreRiesgo()+  ". Manténgase informado por fuentes oficiales."))
                                    .setSound(soundUri)
                                    .setContentText("Usted se encuentra en zona de riesgo. Manténgase informado por fuentes oficiales.")
                                    .setAutoCancel(true);
                    mBuilder.setContentIntent(contentIntent);

                    mNotificationManager.notify(1, mBuilder.build());

                    NotificacionDataSource notificacionDataSource = new NotificacionDataSource(context);
                    notificacionDataSource.open();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Notificacion notificacion = new Notificacion();
                    notificacion.setId(UUID.randomUUID().toString());
                    notificacion.setIdRiesgo(datosAplicacion.getRiesgoMonitoreo().getId());
                    notificacion.setFecha(dateFormat.format(date));
                    notificacion.setMensaje("Usted se encuentra en zona de riesgo del " + datosAplicacion.getRiesgoMonitoreo().getNombreRiesgo()+  ". Manténgase informado por fuentes oficiales.");
                    notificacion.setTitulo("ZONA DE RIESGO");

                    notificacionDataSource.createNotificacion(notificacion);
                    notificacionDataSource.close();

                    if ( datosAplicacion.getPrincipalActivity() != null) {

                        LayoutInflater layoutInflater = LayoutInflater.from(datosAplicacion.getPrincipalActivity());
                        View promptView = layoutInflater.inflate(R.layout.dialog_mensaje_verificar, null);
                        android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(datosAplicacion.getPrincipalActivity());
                        alertDialogBuilder.setView(promptView);

                        Typeface fontRegular = Typeface.createFromAsset(datosAplicacion.getPrincipalActivity().getAssets(), "Lato-Regular.ttf");

                        final TextView tituloDialog = (TextView) promptView.findViewById(R.id.tituloDialog);
                        final TextView leyendaDialog = (TextView) promptView.findViewById(R.id.leyendaDialog);
                        final ImageView imgDialog = (ImageView) promptView.findViewById(R.id.imgDialog);
                        final LinearLayout layoutDialog = (LinearLayout) promptView.findViewById(R.id.layoutDialog);

                        tituloDialog.setTypeface(fontRegular);
                        leyendaDialog.setTypeface(fontRegular);


                        alertDialogBuilder.setCancelable(false)
                                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        // create an alert dialog
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();

                        TipoAlerta tipoAlerta = datosAplicacion.getRiesgoMonitoreo().getTipoAlerta();

                        layoutDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        leyendaDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        tituloDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        imgDialog.setImageResource(R.drawable.zona_riesgo);
                        tituloDialog.setText("ZONA DE RIESGO");
                        leyendaDialog.setText("Usted se encuentra en zona de riesgo del " + datosAplicacion.getRiesgoMonitoreo().getNombreRiesgo()+  ". Manténgase informado por fuentes oficiales.");
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        tituloDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        imgDialog.setColorFilter(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        tituloDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        leyendaDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                    }else if ( datosAplicacion.getMenuActivity() != null){
                        LayoutInflater layoutInflater = LayoutInflater.from(datosAplicacion.getMenuActivity());
                        View promptView = layoutInflater.inflate(R.layout.dialog_mensaje_verificar, null);
                        android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(datosAplicacion.getMenuActivity());
                        alertDialogBuilder.setView(promptView);

                        Typeface fontRegular = Typeface.createFromAsset(datosAplicacion.getMenuActivity().getAssets(), "Lato-Regular.ttf");

                        final TextView tituloDialog = (TextView) promptView.findViewById(R.id.tituloDialog);
                        final TextView leyendaDialog = (TextView) promptView.findViewById(R.id.leyendaDialog);
                        final ImageView imgDialog = (ImageView) promptView.findViewById(R.id.imgDialog);
                        final LinearLayout layoutDialog = (LinearLayout) promptView.findViewById(R.id.layoutDialog);

                        tituloDialog.setTypeface(fontRegular);
                        leyendaDialog.setTypeface(fontRegular);


                        alertDialogBuilder.setCancelable(false)
                                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        // create an alert dialog
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();

                        TipoAlerta tipoAlerta = datosAplicacion.getRiesgoMonitoreo().getTipoAlerta();

                        layoutDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        leyendaDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        tituloDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        imgDialog.setImageResource(R.drawable.zona_riesgo);
                        tituloDialog.setText("ZONA DE RIESGO");
                        leyendaDialog.setText("Usted se encuentra en zona de riesgo del " + datosAplicacion.getRiesgoMonitoreo().getNombreRiesgo()+  ". Manténgase informado por fuentes oficiales.");
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        tituloDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        imgDialog.setColorFilter(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        tituloDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        leyendaDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
                    }
               }




                if (datosAplicacion.getCuenta() == null) {
                    LocationService.this.stopSelf();

                }
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }


}