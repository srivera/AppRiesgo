package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.mapas.DirectionsJSONParser;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.RutasRetorno;

public class MapaActivity extends FragmentActivity implements LocationListener{

    private GoogleMap map;
    private ArrayList<LatLng> markerPoints;
    private DatosAplicacion datosAplicacion;
    private LocationManager locationManager;
    private String locationProvider;
    private Marker miUbicacion;

    private Boolean primeraVez = false;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public Location previousBestLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_prueba);
        primeraVez = false;
        datosAplicacion = (DatosAplicacion) getApplicationContext();

        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        if(map!=null){
            UiSettings mapSettings;
            mapSettings = map.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setMyLocationButtonEnabled(false);
            mapSettings.setMapToolbarEnabled(false);
//            initializeLocationManager();

//            CapaDataSource capaDataSource = new CapaDataSource(getApplicationContext());
//            capaDataSource.open();
//
//
//            String modo = "1";
//
//            String tipo = Constantes.CAPA_RUTA_EVACUACION;
//            if(modo.equals("1")) {
////                verRutaCaminando.setVisibility(View.GONE);
////                verRutaAuto.setVisibility(View.GONE);
//                //Mostrar todo
//                //albergues y punto seguro
//               ArrayList<Capa> capas = capaDataSource.getCapaByTipo(tipo);
//                LatLng points = null;
//                if(tipo.equals(Constantes.CAPA_PUNTO_SEGURO) || tipo.equals(Constantes.CAPA_ALBERGUES)) {
//                      for (Capa capa : capas) {
//                        String[] coordenadas = capa.getCoordenadas().split(",");
//                        if (coordenadas.length > 2) {
//                            points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
//                            MarkerOptions optionss = new MarkerOptions();
//                            optionss.position(points);
//                            if(tipo.equals(Constantes.CAPA_PUNTO_SEGURO)) {
//                                optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa.getNombre());
//                            }else{
//                                optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(capa.getNombre());
//                            }
//                            map.addMarker(optionss);
//                        }
//                    }
//
//                }else if(tipo.equals(Constantes.CAPA_RUTA_EVACUACION)){
//                    //Todas las Rutas
//                    capas = capaDataSource.getCapaByTipo(tipo);
//
//                    for(Capa capa : capas){
//                        boolean primerPunto = false;
//                        String[] coordenadas = capa.getCoordenadas().split(" ");
//                        LatLng[]  puntosRuta = new LatLng[coordenadas.length];
//                        int i = 0;
//                        for(String punto: coordenadas ){
//                            String[] coordenada = punto.split(",");
//                            puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
//
////                            if(!primerPunto){
////                                primerPunto = true;
////                                map.addMarker(new MarkerOptions()
////                                        .position(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])))
////                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
////                                        .title(capa.getId()));
////                            }
//
//                            //points = puntosRuta[i];
//                            i++;
//                        }
//
//                       Polyline line = map.addPolyline(new PolylineOptions()
//                                .add(puntosRuta)
//                                .width(4)
//                                .color(Color.GREEN));
//                    }
//
//
//                    capas = capaDataSource.getCapaByTipo(Constantes.CAPA_PUNTO_SEGURO);
//                    for (Capa capa : capas) {
//                        String[] coordenadas = capa.getCoordenadas().split(",");
//                        if (coordenadas.length > 2) {
//                            points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
//                            MarkerOptions optionss = new MarkerOptions();
//                            optionss.position(points);
//                            optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa.getNombre());
//                            map.addMarker(optionss);
//                        }
//                    }
//                }
//            }else{
//                if(tipo.equals(Constantes.CAPA_PUNTO_SEGURO) || tipo.equals(Constantes.CAPA_ALBERGUES)) {
//                    //Mostrar solo mi ubicacion y del punto seguro y la ruta
//                    Capa capa = (Capa) getIntent().getSerializableExtra("capa");
//                    String[] coordenadas = capa.getCoordenadas().split(",");
//                    LatLng points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
//                    //markerPoints.add(points);
//
//                    // Creating MarkerOptions
//                    MarkerOptions optionss = new MarkerOptions();
//
//                    // Setting the position of the marker
//                    optionss.position(points);
//
//                    /**
//                     * For the start location, the color of marker is GREEN and
//                     * for the end location, the color of marker is RED.
//                     */
//                    optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa.getNombre());
//
//                    // Add new marker to the Google Map Android API V2
//                    map.addMarker(optionss);
//
//                }else if(tipo.equals(Constantes.CAPA_RUTA_EVACUACION)) {
//                    //Ruta mas cercana
//                    Capa capa = (Capa) getIntent().getSerializableExtra("capa");
//                    String[] coordenadas = capa.getCoordenadas().split(" ");
//                    LatLng[] puntosRuta = new LatLng[coordenadas.length];
//                    int i = 0;
//                    for (String punto : coordenadas) {
//                        String[] coordenada = punto.split(",");
//                        puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
//                        i++;
//                    }
//                  Polyline line = map.addPolyline(new PolylineOptions()
//                          .add(puntosRuta)
//                          .width(4)
//                          .color(Color.GREEN));
//
//                }
//
//            }
//           //AMEBNAZA VOLCANICA
//            ArrayList<Capa> capas = capaDataSource.getCapaByTipo("AV");
//            dibujarLahar(capas);
//            capaDataSource.close();

        }
    }

    private void dibujarLahar(ArrayList<Capa> capas) {
        for (Capa capa : capas) {
            String[] coordenadas = capa.getCoordenadas().split(" ");
            LatLng[] puntosRuta = new LatLng[coordenadas.length];
            int i = 0;
            List<LatLng> puntosPoligono = new ArrayList<LatLng>();
            for (String puntoP : coordenadas) {
                String[] coordenada = puntoP.split(",");
                puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                puntosPoligono.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                i++;
            }

            LatLng[] puntosRutaH = null;
            if( capa.getHueco1() != null && !capa.getHueco1().isEmpty()) {
                capa.setPuntosRutaH(new ArrayList<LatLng[]>());

                String[] coordenadasH =  capa.getHueco1().split(" ");
                puntosRutaH = new LatLng[coordenadasH.length];
                i = 0;

                for (String puntoP : coordenadasH) {
                    String[] coordenadaH = puntoP.split(",");
                    puntosRutaH[i] = new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0]));
                    puntosPoligono.add(new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0])));
                    i++;
                }
                capa.getPuntosRutaH().add(puntosRutaH);

                if( capa.getHueco2() != null && !capa.getHueco2().isEmpty()) {
                    capa.setPuntosRutaH(new ArrayList<LatLng[]>());

                    coordenadasH =  capa.getHueco2().split(" ");
                    puntosRutaH = new LatLng[coordenadasH.length];
                    i = 0;

                    for (String puntoP : coordenadasH) {
                        String[] coordenadaH = puntoP.split(",");
                        puntosRutaH[i] = new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0]));
                        puntosPoligono.add(new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0])));
                        i++;
                    }
                    capa.getPuntosRutaH().add(puntosRutaH);

                    if( capa.getHueco3() != null && !capa.getHueco3().isEmpty()) {
                        capa.setPuntosRutaH(new ArrayList<LatLng[]>());

                        coordenadasH =  capa.getHueco3().split(" ");
                        puntosRutaH = new LatLng[coordenadasH.length];
                        i = 0;

                        for (String puntoP : coordenadasH) {
                            String[] coordenadaH = puntoP.split(",");
                            puntosRutaH[i] = new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0]));
                            puntosPoligono.add(new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0])));
                            i++;
                        }
                        capa.getPuntosRutaH().add(puntosRutaH);
                    }
                }
            }

            if(  capa.getPuntosRutaH() != null &&  capa.getPuntosRutaH().size() > 0) {

                PolygonOptions rectOptions1 = new PolygonOptions()
                        .add(puntosRuta).strokeColor(Color.RED)
                        .strokeWidth(2)
                        .fillColor(Color.argb(80, 238, 158, 158)) ;
                for(int k =0; k < capa.getPuntosRutaH().size(); k++){
                    rectOptions1.addHole(Arrays.asList(capa.getPuntosRutaH().get(k)));
                }
                Polygon polygon = map.addPolygon(rectOptions1);
            }else{
                PolygonOptions rectOptions1 = new PolygonOptions()
                        .add(puntosRuta).strokeColor(Color.RED)
                        .strokeWidth(2)
                        .fillColor(Color.argb(80, 238, 158, 158)) ;
                Polygon polygon = map.addPolygon(rectOptions1);
            }
        }
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest, String modo){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor +"&language=es" + modo;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception","error");
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


  // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                RutasRetorno rutasRetorno = parser.parse(jObject);
                routes = rutasRetorno.routes;
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//                location.setLatitude(-0.8518);
//        location.setLongitude(-78.61072);
        if(location != null){
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
               vermapa(location);
            }

        }else{
            // Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                this.locationManager.removeUpdates(this);
                primeraVez = true;
                new AlertDialog.Builder(MapaActivity.this)
                        .setTitle("INFORMACIÓN")
                        .setMessage("No se tiene acceso a su ubicación. Verifique el acceso a internet o al GPS y vuelva a ingresar a esta opción")
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create().show();

            }

        }
        this.locationManager.removeUpdates(this);
    }

    private void vermapa(Location location) {
        if (!primeraVez) {
            primeraVez = true;
            markerPoints = new ArrayList<LatLng>();
            LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
            markerPoints.add(punto);

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            map.moveCamera(center);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
            map.animateCamera(zoom);

            miUbicacion = map.addMarker(new MarkerOptions()
                    .position(punto)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Mi ubicación" + locationProvider.toString()));

            datosAplicacion.setUltimaUbicacion(location);
        } else {

            if(miUbicacion != null) {
                miUbicacion.remove();
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                miUbicacion = map.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Mi ubicación"));

//                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                        this.map.moveCamera(center);
            }else{
                primeraVez = true;
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                miUbicacion = map.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Mi ubicación"));

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                this.map.moveCamera(center);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                this.map.animateCamera(zoom);
            }

            markerPoints = new ArrayList<LatLng>();
            LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
            markerPoints.add(punto);
            datosAplicacion.setUltimaUbicacion(location);
        }
        this.locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    private void initializeLocationManager() {

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        this.locationProvider = locationManager.getBestProvider(criteria, false);

        Location location = locationManager.getLastKnownLocation(locationProvider);

        if(location != null) {
            vermapa(location);
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        onLocationChanged(location);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.locationManager.removeUpdates(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        this.locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {

        super.onResume();
       // if (!primeraVez) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
       // }
    }

}