package activities.riesgo.yacare.com.ec.appriesgo.general;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class ActualizarRiesgoService extends Service {


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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
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
        ActualizarRiesgoService.this.stopSelf();
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
        public void onLocationChanged(final Location loc){
            if(isBetterLocation(loc, previousBestLocation)) {
                previousBestLocation = loc;
                DatosAplicacion datosAplicacion = (DatosAplicacion) context;
                String zonaRiesgo = VariablesUtil.getInstance().verificarZonaRiesgo(new LatLng(loc.getLatitude(), loc.getLongitude()), context, datosAplicacion.getRiesgoActual().getId());

                //zonaRiesgo = Constantes.CODIGO_ZONA_RIESGO;
                if (!zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO)) {
                    ingresoZonaRiesgo = false;
                }

                if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO) && !ingresoZonaRiesgo) {
                    ingresoZonaRiesgo = true;
                    //notificar ubicacion
                    NotificationManager mNotificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);

                    Intent intent = new Intent(context, SplashActivity.class);
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.LAUNCHER");

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT) ;
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.iconop)
                                    .setContentTitle("ZONA DE RIESGO")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Usted se encuentra dentro de la zona de riesgo, revise su ubicación utilizando los mapas del sistema"))
                                    .setSound(soundUri)
                                    .setContentText("Usted se encuentra dentro de la zona de riesgo, revise su ubicación utilizando los mapas del sistema")
                                    .setAutoCancel(true);
                    mBuilder.setContentIntent(contentIntent);

                    mNotificationManager.notify(1, mBuilder.build());


                    if (datosAplicacion.getCuenta() == null) {
                        ActualizarRiesgoService.this.stopSelf();
                    }

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