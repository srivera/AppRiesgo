package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;

public class MapaPuntosFragment extends Fragment {

    private GoogleMap map;
    private ArrayList<LatLng> markerPoints;
    private Button regresar;
    private DatosAplicacion datosAplicacion;
    private ImageButton tipoMapa;

    private Marker miUbicacion;

    private Boolean primeraVez = false;

    private MapView mMapView;

    private ImageButton ayuda;


    public Double longitudCercana;
    public Double latitudCercana;

    private ArrayList<Capa> todasRutasEvacuacion;

    private ArrayList<Capa> rutasRelacionadas;

    private Bundle savedInstanceState;

    private  View view;

    int PETICION_PERMISO_LOCALIZACION = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mapa, container, false);
        this.savedInstanceState = savedInstanceState;

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            mostrarPantalla();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                mostrarPantalla();

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e("permiso", "Permiso denegado");
            }
        }
    }
    private void mostrarPantalla() {
        primeraVez = false;
        datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

        tipoMapa = (ImageButton) view.findViewById(R.id.tipoMapa);
        tipoMapa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else {
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
            }
        });

        regresar = (Button) view.findViewById(R.id.btnRegresarMapa);
        regresar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PuntosSegurosFragment fragment = new PuntosSegurosFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("MAPAS OFICIALES");
            getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
        }

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        ayuda = (ImageButton) view.findViewById(R.id.ayuda);
        if (datosAplicacion.getRiesgoActual().getId().equals("1")) {
            ayuda.setVisibility(View.INVISIBLE);
        } else {
            ayuda.setVisibility(View.VISIBLE);
            ayuda.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    datosAplicacion.cameraPosition = map.getCameraPosition();
                    LeyendaMapaFragment fragment = new LeyendaMapaFragment();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                }
            });
        }

        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Boolean moverCamara = true;
                if (getActivity() != null) {
                    MapsInitializer.initialize(getActivity());
                    map.setMyLocationEnabled(true);

                    googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
                    mMapView.onResume();// needed to get the map to display immediately
                    UiSettings mapSettings;
                    mapSettings = map.getUiSettings();
                    mapSettings.setZoomControlsEnabled(true);
                    mapSettings.setMyLocationButtonEnabled(false);
                    mapSettings.setMapToolbarEnabled(false);
                    mapSettings.setCompassEnabled(true);
                    if (map != null) {
                        CapaDataSource capaDataSource = new CapaDataSource(getActivity().getApplicationContext());
                        capaDataSource.open();
                        if (datosAplicacion.modo.equals("1")) {
                            //albergues y punto seguro
                            ArrayList<Capa> capas = capaDataSource.getCapaByTipo(datosAplicacion.tipo, datosAplicacion.getRiesgoActual().getId());
                            LatLng points = null;
                            if (datosAplicacion.tipo.equals(Constantes.CAPA_PUNTO_SEGURO) || datosAplicacion.tipo.equals(Constantes.CAPA_ALBERGUES) || datosAplicacion.tipo.equals(Constantes.CAPA_CENTRO_SALUD)) {
                                for (Capa capa : capas) {
                                    String[] coordenadas = capa.getCoordenadas().split(",");
                                    if (coordenadas.length > 2) {
                                        points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));

                                        if(moverCamara) {
                                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0])));
                                            map.moveCamera(center);

                                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                                            map.animateCamera(zoom);
                                            moverCamara = false;
                                        }

                                        MarkerOptions optionss = new MarkerOptions();
                                        optionss.position(points);
                                        if (datosAplicacion.tipo.equals(Constantes.CAPA_PUNTO_SEGURO)) {
                                            optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(capa.getNombre());
                                        } else {
                                            optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(capa.getNombre());
                                        }
                                        map.addMarker(optionss);
                                    }
                                }

                            } else if (datosAplicacion.tipo.equals(Constantes.CAPA_RUTA_EVACUACION)) {
                                //Todas las Rutas
                                capas = capaDataSource.getCapaByTipo(datosAplicacion.tipo, datosAplicacion.getRiesgoActual().getId());

                                for (Capa capa : capas) {
                                    boolean primerPunto = false;
                                    String[] coordenadas = capa.getCoordenadas().split(" ");
                                    LatLng[] puntosRuta = new LatLng[coordenadas.length];
                                    int i = 0;
                                    String[] coordenada = null;
                                    for (String punto : coordenadas) {
                                        coordenada = punto.split(",");
                                        puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                                        i++;
                                        if(moverCamara) {
                                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                            map.moveCamera(center);

                                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                                            map.animateCamera(zoom);
                                            moverCamara = false;
                                        }


//                                    if (capa.getId().equals("RE281")) {
//                                        primerPunto = true;
//                                        map.addMarker(new MarkerOptions()
//                                                .position(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])))
//                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                                                .title(capa.getId()));
//                                    }
                                    }
//                                if (capa.getId().equals("RE281")) {
//                                    map.addMarker(new MarkerOptions()
//                                            .position(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])))
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
//                                            .title(capa.getId()));
//                                }

                                    Polyline line = map.addPolyline(new PolylineOptions()
                                            .add(puntosRuta)
                                            .width(4)
                                            .color(Color.GREEN));
                                }

                               // if (datosAplicacion.getRiesgoActual().getId().equals("1")) {
                                    capas = capaDataSource.getCapaByTipo(Constantes.CAPA_PUNTO_SEGURO, datosAplicacion.getRiesgoActual().getId());
                                    for (Capa capa : capas) {
                                        String[] coordenadas = capa.getCoordenadas().split(",");
                                        if (coordenadas.length > 2) {
                                            points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
                                            MarkerOptions optionss = new MarkerOptions();
                                            optionss.position(points);
                                            optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(capa.getNombre());
                                            map.addMarker(optionss);
                                        }
                                    }
                             //   }
                            }
                        } else {
                            if (datosAplicacion.tipo.equals(Constantes.CAPA_PUNTO_SEGURO) || datosAplicacion.tipo.equals(Constantes.CAPA_ALBERGUES) || datosAplicacion.tipo.equals(Constantes.CAPA_CENTRO_SALUD)) {
                                //Mostrar solo mi ubicacion y del punto seguro y la ruta
                                String[] coordenadas = datosAplicacion.capa.getCoordenadas().split(",");
                                LatLng points = new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0]));
                                //markerPoints.add(points);
                                if(moverCamara) {
                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(coordenadas[1]), Double.valueOf(coordenadas[0])));
                                    map.moveCamera(center);

                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                                    map.animateCamera(zoom);
                                    moverCamara = false;
                                }
                                // Creating MarkerOptions
                                MarkerOptions optionss = new MarkerOptions();

                                // Setting the position of the marker
                                optionss.position(points);

                                /**
                                 * For the start location, the color of marker is GREEN and
                                 * for the end location, the color of marker is RED.
                                 */
                                optionss.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(datosAplicacion.capa.getNombre());

                                // Add new marker to the Google Map Android API V2
                                map.addMarker(optionss);

                            } else if (datosAplicacion.tipo.equals(Constantes.CAPA_RUTA_EVACUACION)) {
                                //Ruta mas cercana

                                String[] coordenadas = datosAplicacion.capa.getCoordenadas().split(" ");
                                LatLng[] puntosRuta = new LatLng[coordenadas.length];
                                int i = 0;
                                for (String punto : coordenadas) {
                                    String[] coordenada = punto.split(",");
                                    puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                                    i++;

                                    if(moverCamara) {
                                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                                        map.moveCamera(center);

                                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                                        map.animateCamera(zoom);
                                        moverCamara = false;
                                    }
                                }

                                todasRutasEvacuacion = capaDataSource.getCapaByTipo(Constantes.CAPA_RUTA_EVACUACION, datosAplicacion.getRiesgoActual().getId());


                                rutasRelacionadas = new ArrayList<Capa>();
                                rutasRelacionadas.add(datosAplicacion.capa);
                                buscarRutasRelacionadas(datosAplicacion.capa);


                                for (Capa capa : rutasRelacionadas) {
                                    coordenadas = capa.getCoordenadas().split(" ");
                                    puntosRuta = new LatLng[coordenadas.length];
                                    i = 0;
                                    for (String puntoRuta : coordenadas) {
                                        String[] coordenada = puntoRuta.split(",");
                                        puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                                        i++;
                                    }
                                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                                            .add(puntosRuta)
                                            .width(4)
                                            .color(Color.GREEN));


                                }

                            }

                        }
                        //AMEBNAZA VOLCANICA
                        ArrayList<Capa> capas = capaDataSource.getCapaByTipo("AV", datosAplicacion.getRiesgoActual().getId());
                        dibujarLahar(capas);
                        capaDataSource.close();

                        if (datosAplicacion.cameraPosition != null && datosAplicacion.getRiesgoActual().getId().equals("2")) {
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(datosAplicacion.cameraPosition));
                            datosAplicacion.cameraPosition = null;
                            map.setMyLocationEnabled(false);
                        }
                    }
                }

            }
        });
    }

    private void buscarRutasRelacionadas(Capa rutaEmergencia) {
        String[] coordenadas = rutaEmergencia.getCoordenadas().split(" ");
        String coordenadaInicial = null;
        String coordenadaFinal = null;
        for (int i = 0; i < coordenadas.length; i++) {
            if (i == 0) {
                coordenadaInicial = coordenadas[i];
            } else {
                coordenadaFinal = coordenadas[i];
            }
        }

        for (Capa capa : todasRutasEvacuacion) {
            if (!capa.getId().equals(rutaEmergencia.getId())) {
                if (esNuevaRuta(capa)) {
                    if (capa.getCoordenadas().contains(coordenadaInicial)) {
                        rutasRelacionadas.add(capa);
                        buscarRutasRelacionadas(capa);
                    }

                    if (capa.getCoordenadas().contains(coordenadaFinal)) {
                        rutasRelacionadas.add(capa);
                        buscarRutasRelacionadas(capa);
                    }
                }
            }
        }
    }

    private Boolean esNuevaRuta(Capa ruta) {
        for (Capa capa : rutasRelacionadas) {
            if (capa.getId().equals(ruta.getId())) {
                return false;
            }
        }
        return true;
    }


    private void dibujarLahar(ArrayList<Capa> capas) {

//        LatLngBounds newarkBounds = new LatLngBounds(
//                new LatLng(-5.128646755012045, -81.36025657019827),       // South west corner
//                new LatLng(2.047130813774082, -77.17155016801689));      // North east corner
//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapDescriptorFactory.fromResource(R.drawable.mapa))
//                .positionFromBounds(newarkBounds);
//        map.addGroundOverlay(newarkMap);


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
            if (capa.getHueco1() != null && !capa.getHueco1().isEmpty()) {
                capa.setPuntosRutaH(new ArrayList<LatLng[]>());

                String[] coordenadasH = capa.getHueco1().split(" ");
                puntosRutaH = new LatLng[coordenadasH.length];
                i = 0;

                for (String puntoP : coordenadasH) {
                    String[] coordenadaH = puntoP.split(",");
                    puntosRutaH[i] = new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0]));
                    puntosPoligono.add(new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0])));
                    i++;
                }
                capa.getPuntosRutaH().add(puntosRutaH);

                if (capa.getHueco2() != null && !capa.getHueco2().isEmpty()) {

                    coordenadasH = capa.getHueco2().split(" ");
                    puntosRutaH = new LatLng[coordenadasH.length];
                    i = 0;

                    for (String puntoP : coordenadasH) {
                        String[] coordenadaH = puntoP.split(",");
                        puntosRutaH[i] = new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0]));
                        puntosPoligono.add(new LatLng(Double.valueOf(coordenadaH[1]), Double.valueOf(coordenadaH[0])));
                        i++;
                    }
                    capa.getPuntosRutaH().add(puntosRutaH);

                    if (capa.getHueco3() != null && !capa.getHueco3().isEmpty()) {

                        coordenadasH = capa.getHueco3().split(" ");
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

            int colorLahar = Color.argb(100, 208, 150, 149); //#D09695
            if (capa.getId().equals("AVT8")) {
                colorLahar = Color.argb(100, 208, 150, 149);//#D09695
            } else if (capa.getId().equals("AVT7")) {
                colorLahar = Color.argb(100, 211, 177, 176);//#D3B1B0
            } else if (capa.getId().equals("AVT6")) {
                colorLahar = Color.argb(100, 215, 200, 203);//#D7C8CB
            }
            if (capa.getPuntosRutaH() != null && capa.getPuntosRutaH().size() > 0) {

                PolygonOptions rectOptions1 = new PolygonOptions()
                        .add(puntosRuta).strokeColor(Color.RED)
                        .strokeWidth(1)
                        .fillColor(colorLahar);
                for (int k = 0; k < capa.getPuntosRutaH().size(); k++) {
                    rectOptions1.addHole(Arrays.asList(capa.getPuntosRutaH().get(k)));
                }
                Polygon polygon = map.addPolygon(rectOptions1);
            } else {
                if (capa.getPuntosRutaH() != null && capa.getPuntosRutaH().size() > 0) {

                    PolygonOptions rectOptions1 = new PolygonOptions()
                            .add(puntosRuta).strokeColor(Color.RED)
                            .strokeWidth(1)
                            .fillColor(colorLahar);
                    for (int k = 0; k < capa.getPuntosRutaH().size(); k++) {
                        rectOptions1.addHole(Arrays.asList(capa.getPuntosRutaH().get(k)));
                    }
                    Polygon polygon = map.addPolygon(rectOptions1);
                } else {
                    if (capa.getDireccion() == null || capa.getDireccion().equals("")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.RED)
                                .strokeWidth(2)
                                .fillColor(Color.argb(40, 238, 158, 158));
                        Polygon polygon = map.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("N")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLACK)

                                .strokeWidth(5);
                        Polygon polygon = map.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("V")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.parseColor("#056905"))
                                .strokeWidth(5);
                        Polygon polygon = map.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("A")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLUE)
                                .strokeWidth(5);
                        Polygon polygon = map.addPolygon(rectOptions1);
                    }
                }
//                PolygonOptions rectOptions1 = new PolygonOptions()
//                        .add(puntosRuta).strokeColor(Color.RED)
//                        .strokeWidth(2)
//                        .fillColor(Color.argb(80, 238, 158, 158));
//                Polygon polygon = map.addPolygon(rectOptions1);
            }
        }
    }


    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
//                        location.setLatitude(-0.92905);
//                        location.setLongitude(-78.62397);
            vermapa(location);
        }
    };


    private void vermapa(Location location) {
        if (map != null) {
            if (!primeraVez) {
                primeraVez = true;
                markerPoints = new ArrayList<LatLng>();
                LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
                markerPoints.add(punto);

//                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                map.moveCamera(center);
//
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//                map.animateCamera(zoom);

                miUbicacion = map.addMarker(new MarkerOptions()
                        .position(punto)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Mi ubicación"));

                datosAplicacion.setUltimaUbicacion(location);
            } else {

                if (miUbicacion != null) {
                    miUbicacion.remove();
                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    miUbicacion = map.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Mi ubicación"));

//                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                        this.map.moveCamera(center);
                } else {
                    primeraVez = true;
                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    miUbicacion = map.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Mi ubicación"));

//                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                    this.map.moveCamera(center);
//                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//                    this.map.animateCamera(zoom);
                }

                markerPoints = new ArrayList<LatLng>();
                LatLng punto = new LatLng(location.getLatitude(), location.getLongitude());
                markerPoints.add(punto);
                datosAplicacion.setUltimaUbicacion(location);
            }
            map.setMyLocationEnabled(false);


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}