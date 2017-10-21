package activities.riesgo.yacare.com.ec.appriesgo.mapas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.util.RutasRetorno;

public class UbicacionActivity extends FragmentActivity {

    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    Button button;
    Button script;
    Button zona;
    DatosAplicacion datosAplicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        datosAplicacion = (DatosAplicacion) getApplicationContext();

        button= (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(UbicacionActivity.this, TextoActivity.class);
                startActivity(i);

            }
        });

//        zona= (Button) findViewById(R.id.button3);
//        zona.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Location myLocation = map.getMyLocation();
//                LatLng miUbicacion = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//                Boolean contiene = false;
//                for(Polygon polygon: datosAplicacion.getLahares()){
////                    if(PolyUtil.containsLocation(miUbicacion, polygon.getPoints(), false)){
////                        contiene = true;
////
////                        break;
////                    }
//                }
//
//
//            }
//        });

        script = (Button) findViewById(R.id.button2);
        script.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try
                {
                    File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, "riesgos.sql");
                    FileWriter writer = new FileWriter(gpxfile);


                    //Cargar coordenadas
                    CapaDataSource capaDataSource = new CapaDataSource(getApplicationContext());
                    capaDataSource.open();

                    //AMENAZA VOLCANICA
//                    ArrayList<Capa> capas = capaDataSource.getCapaByTipo("AV");
//
//
//                    for (Capa capa : capas) {
//                        String[] coordenadas = capa.getCoordenadas().split(" ");
//                        int i = 10;
//                        for (String punto : coordenadas) {
//                            String[] coordenada = punto.split(",");
//                            writer.append("INSERT INTO COORDENADA (idcoordenada, idcapa, latitud, longitud) values('" + capa.getId() + String.valueOf(i) + "','"
//                                    + capa.getId() + "','" + coordenada[1] + "','" + coordenada[0] + "');");
//
//                            i++;
//                        }
//
//                    }

//                    //RUTA DE EVACUACION
//                    capas = capaDataSource.getCapaByTipo("RE");
//                    for (Capa capa : capas) {
//                        String[] coordenadas = capa.getCoordenadas().split(" ");
//                        int i = 50;
//                        for (String punto : coordenadas) {
//                            String[] coordenada = punto.split(",");
//                            writer.append("INSERT INTO COORDENADA (idcoordenada, idcapa, latitud, longitud) values('" + capa.getId() + String.valueOf(i) + "','"
//                                    + capa.getId() + "','" + coordenada[1] + "','" + coordenada[0] + "');");
//                            i++;
//                        }
//                    }
//
//                    //VIAS COLECTORAS
//                    capas = capaDataSource.getCapaByTipo("VC");
//                    for (Capa capa : capas) {
//                        String[] coordenadas = capa.getCoordenadas().split(" ");
//                        int i = 100;
//                        for (String punto : coordenadas) {
//                            String[] coordenada = punto.split(",");
//                            writer.append("INSERT INTO COORDENADA (idcoordenada, idcapa, latitud, longitud) values('" + capa.getId() + String.valueOf(i) + "','"
//                                    + capa.getId() + "','" + coordenada[1] + "','" + coordenada[0] + "'); ");
//                            i++;
//                        }
//                    }


                    ArrayList<Capa> rutas = capaDataSource.getCapaByTipo("RE",  datosAplicacion.getRiesgoActual().getId());

                    ArrayList<Capa> puntosSeguros = capaDataSource.getCapaByTipo("PS",  datosAplicacion.getRiesgoActual().getId());

                    String puntos = "";
                    for(Capa ruta : rutas){
                        puntos = " ";
                        for(Capa puntoSeguro: puntosSeguros){
                            if(ruta.getCoordenadas().contains(puntoSeguro.getCoordenadas())){
                                puntos = puntoSeguro.getId() + ",";
                            }
                        }
                        if(!puntos.equals(" ")){
                            writer.append("UPDATE CAPA SET DIRECCION = '" + puntos  + "' WHERE IDCAPA = '"  + ruta.getId()  +"';" );
                        }
                    }




                    capaDataSource.close();
                    writer.flush();
                    writer.close();

                }
                catch(IOException e) {
                    e.printStackTrace();

                }


            }
        });

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        if(map!=null){

            // Enable MyLocation Button in the Map
            map.setMyLocationEnabled(true);

//            // Setting onclick event listener for the map
//            map.setOnMapClickListener(new OnMapClickListener() {
//
//                @Override
//                public void onMapClick(LatLng point) {
//
//
//
//                    // Already two locations
//                    if(markerPoints.size()>1){
//                        markerPoints.clear();
//                        map.clear();
//                    }
//
//                    // Adding new item to the ArrayList
//                    markerPoints.add(point);
//
//                    // Creating MarkerOptions
//                    MarkerOptions options = new MarkerOptions();
//
//                    // Setting the position of the marker
//                    options.position(point);
//
//                    /**
//                     * For the start location, the color of marker is GREEN and
//                     * for the end location, the color of marker is RED.
//                     */
//                    if(markerPoints.size()==1){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    }else if(markerPoints.size()==2){
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    }
//
//
//                    // Add new marker to the Google Map Android API V2
//                    map.addMarker(options);
//
//                    // Checks, whether start and end locations are captured
//                    if(markerPoints.size() >= 2){
//                        LatLng origin = markerPoints.get(0);
//                        LatLng dest = markerPoints.get(1);
//
//                        // Getting URL to the Google Directions API
//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//                        // Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
//
//                        LatLng points = new LatLng(-0.91714, -78.63404);
//
//                        markerPoints.add(points);
//
//                        // Creating MarkerOptions
//                        MarkerOptions optionss = new MarkerOptions();
//
//                        // Setting the position of the marker
//                        optionss.position(points);
//
//                        /**
//                         * For the start location, the color of marker is GREEN and
//                         * for the end location, the color of marker is RED.
//                         */
//                        optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("SITIO SEGURO");
//
//                        // Add new marker to the Google Map Android API V2
//                        map.addMarker(optionss);
//                    }
//
//                }
//            });

            // Instantiates a new Polygon object and adds points to define a rectangle
            //,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0
            //,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0 ,,0.0
//            PolygonOptions rectOptions = new PolygonOptions()
//                    .add(new LatLng( -0.925729, -78.6276057),
//                            new LatLng( -0.9287755999999999,-78.62597490000002),
//                            new LatLng( -0.9297196, -78.6229279),
//                            new LatLng(-0.9270592000000001, -78.6220696),
//                            new LatLng( -0.9262009999999999, -78.6240008),
//                            new LatLng( -0.9241843000000001, -78.6240866),
//                            new LatLng( -0.9237127999999999, -78.6269188),
//                            new LatLng( -0.925729, -78.6276057)).strokeColor(Color.DKGRAY)
//                    .fillColor(Color.GRAY);
//
//            // Get back the mutable Polygon
//            Polygon polygon = map.addPolygon(rectOptions);


            //,,0.0 ,,0.0 ,0.0

//            Polyline line = map.addPolyline(new PolylineOptions()
//                    .add(new LatLng(-0.9171476, -78.6340427), new LatLng(-0.9220822000000001, -78.63249780000001), new LatLng(-0.9243135000000001, -78.6317253))
//                    .width(5)
//                    .color(Color.GREEN));

            CapaDataSource capaDataSource = new CapaDataSource(getApplicationContext());
            capaDataSource.open();

            //albergues
            ArrayList<Capa> capas = capaDataSource.getCapaByTipo("AL",  datosAplicacion.getRiesgoActual().getId());

            for(Capa capa : capas){

                String[] coordenadas = capa.getCoordenadas().split(",");
                if(coordenadas.length > 2) {
                    LatLng points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
                    markerPoints.add(points);

                    // Creating MarkerOptions
                    MarkerOptions optionss = new MarkerOptions();

                    // Setting the position of the marker
                    optionss.position(points);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("ALBERGUE");

                    // Add new marker to the Google Map Android API V2
                    map.addMarker(optionss);
                }



            }


            //AMEBNAZA VOLCANICA
            capas = capaDataSource.getCapaByTipo("AV",  datosAplicacion.getRiesgoActual().getId());

            for(Capa capa : capas){

                String[] coordenadas = capa.getCoordenadas().split(" ");
                LatLng[]  puntosRuta = new LatLng[coordenadas.length];
                int i = 0;
                for(String punto: coordenadas ){
                    String[] coordenada = punto.split(",");
                    puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    i++;
                }
                PolygonOptions rectOptions1 = new PolygonOptions()
                        .add(puntosRuta).strokeColor(Color.DKGRAY)
                        .fillColor(Color.GRAY);

                // Get back the mutable Polygon
                Polygon polygon = map.addPolygon(rectOptions1);
//                datosAplicacion.getLahares().add(polygon);

            }

            //punto seguro
            capas = capaDataSource.getCapaByTipo("PS",  datosAplicacion.getRiesgoActual().getId());

            for(Capa capa : capas) {

                String[] coordenadas = capa.getCoordenadas().split(",");
                if (coordenadas.length > 2) {
                    LatLng points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
                    markerPoints.add(points);

                    // Creating MarkerOptions
                    MarkerOptions optionss = new MarkerOptions();

                    // Setting the position of the marker
                    optionss.position(points);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa.getNombre());

                    // Add new marker to the Google Map Android API V2
                    map.addMarker(optionss);
                }
            }
                //Ruta de evacuacion
                capas = capaDataSource.getCapaByTipo("RE",  datosAplicacion.getRiesgoActual().getId());

                for(Capa capa : capas){

                    String[] coordenadas = capa.getCoordenadas().split(" ");
                    LatLng[]  puntosRuta = new LatLng[coordenadas.length];
                    int i = 0;
                    for(String punto: coordenadas ){
                        String[] coordenada = punto.split(",");
                        puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                        i++;
                    }

                    Polyline line = map.addPolyline(new PolylineOptions()
                            .add(puntosRuta)
                            .width(5)
                            .color(Color.GREEN));

                }

            //VIAS COLECTORAS
            capas = capaDataSource.getCapaByTipo("VC",  datosAplicacion.getRiesgoActual().getId());

            for(Capa capa : capas){

                String[] coordenadas = capa.getCoordenadas().split(" ");
                LatLng[]  puntosRuta = new LatLng[coordenadas.length];
                int i = 0;
                for(String punto: coordenadas ){
                    String[] coordenada = punto.split(",");
                    puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    i++;
                }

                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(puntosRuta)
                        .width(5)
                        .color(Color.MAGENTA));

            }

            capaDataSource.close();
            //Boolean contiene = PolyUtil.containsLocation(point, rectOptions.getPoints(), false);
//			Log.d("poligono", "adentro: " + contiene);
//
//			point = new LatLng(-0.65277, -78.45907);
//			contiene = PolyUtil.containsLocation(point, rectOptions.getPoints(), false);
//			Log.d("poligono", "limite: " + contiene);
//
//			point = new LatLng(-0.6595, -78.46607);
//			contiene = PolyUtil.containsLocation(point, rectOptions.getPoints(), false);
//			Log.d("poligono", "afuera: " + contiene);
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor +"&language=es";

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
                RutasRetorno rutasRetorno =  parser.parse(jObject);
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
                lineOptions.width(2);
                lineOptions.color(Color.RED);

            }

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }
}