package activities.riesgo.yacare.com.ec.appriesgo.evacuacion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import activities.riesgo.yacare.com.ec.appriesgo.adapter.RutaArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetallePuntosEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetalleRutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.RutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.LeyendaMapaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.mapas.DirectionsJSONParser;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.RutasRetorno;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;



public class EvacuacionFragment extends Fragment {

    private ImageButton tipoMapa;

    private MapView mMapView;

    private GoogleMap googleMap;

    private DatosAplicacion datosAplicacion;

    private ListView listView;

    private RutaArrayAdapter rutas;

    private Button verRutaUno;
    private Button verRutaDos;
    private Button verRutaTres;

    private Button btnRegresarEvacuacion;

    private ImageButton abrirChat;

    private ImageButton ayuda;

    private ArrayList<RutaEmergencia> rutasEmergencia;

    private ArrayList<Capa> puntosCercanos;

    private Riesgo riesgo;

    Boolean modoDesconectado = false;

    private Location location;

    private int rutaActiva;

    private Marker miUbicacion;

    private String zonaRiesgo;

    private View view;

    Boolean mostroMensajeSinInternet = false;

    private Boolean primeraVez = false;

    private ArrayList<Capa> todasRutasEvacuacion;

    private ArrayList<Capa> rutasRelacionadas;

    private Boolean estaNavegando = true;

    private ImageButton navegar;

    private float currentZoom = -1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_evacuacion, container, false);

        verRutaUno = (Button) view.findViewById(R.id.verRutaUno);
        verRutaDos = (Button) view.findViewById(R.id.verRutaDos);
        verRutaTres = (Button) view.findViewById(R.id.verRutaTres);

        primeraVez = false;


        datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
        listView = (ListView) view.findViewById(android.R.id.list);
        riesgo = datosAplicacion.getRiesgoActual();
        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                estaNavegando = true;

                googleMap = map;
                googleMap.setMyLocationEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                MapsInitializer.initialize(getActivity());
                googleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                mMapView.onResume();// needed to get the map to display immediately
                UiSettings mapSettings;
                mapSettings = googleMap.getUiSettings();
                mapSettings.setZoomControlsEnabled(true);
                mapSettings.setMyLocationButtonEnabled(false);
                mapSettings.setMapToolbarEnabled(false);
                mapSettings.setCompassEnabled(true);
                cargarEvacuacion(view);

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        if (position.zoom != currentZoom) {
                            currentZoom = position.zoom;  // here you get zoom level
                        }
                    }
                });

            }
        });


        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("EVACUACIÓN");
            getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
        }

        if (getParentFragment() instanceof SimulacroContainerFragment) {
            btnRegresarEvacuacion = (Button) view.findViewById(R.id.btnRegresarEvacuacion);
            btnRegresarEvacuacion.setVisibility(View.VISIBLE);
            btnRegresarEvacuacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimulacroFragment fragment = new SimulacroFragment();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                }
            });

        }

        ayuda = (ImageButton) view.findViewById(R.id.ayuda);
        if (datosAplicacion.getRiesgoActual().getId().equals("1")) {
            ayuda.setVisibility(View.INVISIBLE);
        } else {
            ayuda.setVisibility(View.VISIBLE);
            ayuda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datosAplicacion.cameraPosition = googleMap.getCameraPosition();
                    LeyendaMapaFragment fragment = new LeyendaMapaFragment();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                }
            });
        }
        tipoMapa = (ImageButton) view.findViewById(R.id.tipoMapa);
        tipoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    if (googleMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
            }
        });

        navegar = (ImageButton) view.findViewById(R.id.navegar);
        navegar.setBackgroundResource(R.drawable.brujularoja);
        navegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    if (!estaNavegando) {
                        if (miUbicacion != null) {
                            miUbicacion.remove();
                        }
                        googleMap.setMyLocationEnabled(true);
                        estaNavegando = true;
                        navegar.setBackgroundResource(R.drawable.brujularoja);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("INFORMACIÓN")
                                .setMessage("Esta opción permite actualizar su ubicación en el mapa, por lo que requiere encender el GPS")
                                .setCancelable(false)
                                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }

                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        if (miUbicacion != null) {
                            miUbicacion.remove();
                        }
                        LatLng currentPosition = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
                        miUbicacion = googleMap.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title("Mi ubicación"));
                        estaNavegando = false;
                        navegar.setBackgroundResource(R.drawable.brujulaverde);
                        googleMap.setMyLocationEnabled(false);
                    }
                }
            }
        });


        if (datosAplicacion.getDetalleAlertaFragment() != null) {
            InicioFragment fragment = new InicioFragment();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(datosAplicacion.getDetalleAlertaFragment());
            transaction.commit();

            TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getActivity().getApplicationContext());
            tipoAlertaDataSource.open();
            TipoAlerta tipoAlerta1 = tipoAlertaDataSource.getTipoAlertaByCodigo(datosAplicacion.getRiesgoActual().getAlertaActual(),  datosAplicacion.getRiesgoActual().getId() );
            tipoAlertaDataSource.close();

            datosAplicacion.getPrincipalActivity().btnTipoALerta.setEnabled(true);
            datosAplicacion.getPrincipalActivity().btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta1.getCodigoColorOscuro()));
            datosAplicacion.getPrincipalActivity().btnTipoALerta.setText(">");
        }

        CapaDataSource capaDataSource = new CapaDataSource(getActivity().getApplicationContext());
        capaDataSource.open();
        todasRutasEvacuacion = capaDataSource.getCapaByTipo(Constantes.CAPA_RUTA_EVACUACION,  datosAplicacion.getRiesgoActual().getId());
        capaDataSource.close();

        return view;
    }

    private void cargarEvacuacion(View view) {

        verRutaUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    verRutaUno.setBackgroundColor(Color.parseColor("#33a948"));
                    verRutaDos.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    verRutaTres.setBackgroundColor(Color.parseColor("#A0A0A0"));

                    rutaActiva = 0;
                    if (rutasEmergencia != null && rutasEmergencia.size() == 3) {
                        if (modoDesconectado) {
                            dibujarRutaSinSenal(rutasEmergencia.get(0));
                        } else {
                            for(RutaEmergencia emergencia :rutasEmergencia ){
                                if(emergencia.getOrden().equals("1")){
                                    dibujarRuta(emergencia);
                                    break;
                                }
                            }
//                            dibujarRuta(rutasEmergencia.get(0));
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("Información")
                                .setMessage("Se está calculando su ubicación y si tiene rutas de evacuación. Espere un momento.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        verRutaDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    verRutaUno.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    verRutaDos.setBackgroundColor(Color.parseColor("#33a948"));
                    verRutaTres.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    rutaActiva = 1;
                    if (rutasEmergencia != null && rutasEmergencia.size() == 3) {
                        if (modoDesconectado) {
                            dibujarRutaSinSenal(rutasEmergencia.get(1));
                        } else {
//                            dibujarRuta(rutasEmergencia.get(1));
                            for(RutaEmergencia emergencia :rutasEmergencia ){
                                if(emergencia.getOrden().equals("2")){
                                    dibujarRuta(emergencia);
                                    break;
                                }
                            }
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("Información")
                                .setMessage("Se está calculando su ubicación y si tiene rutas de evacuación. Espere un momento.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        verRutaTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    verRutaUno.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    verRutaDos.setBackgroundColor(Color.parseColor("#A0A0A0"));
                    verRutaTres.setBackgroundColor(Color.parseColor("#33a948"));
                    rutaActiva = 2;
                    if (rutasEmergencia != null && rutasEmergencia.size() == 3) {
                        if (modoDesconectado) {
                            dibujarRutaSinSenal(rutasEmergencia.get(2));
                        } else {
//                            dibujarRuta(rutasEmergencia.get(2));
                            for(RutaEmergencia emergencia :rutasEmergencia ){
                                if(emergencia.getOrden().equals("3")){
                                    dibujarRuta(emergencia);
                                    break;
                                }
                            }
                        }
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("Información")
                                .setMessage("Se está calculando su ubicación y si tiene rutas de evacuación. Espere un momento.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        abrirChat = (ImageButton) view.findViewById(R.id.abrirChat);
        abrirChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatBlueToothActivity fragment = new ChatBlueToothActivity();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });

        dibujarElLahar();


    }

    private void dibujarElLahar() {
        if (googleMap != null) {
            ArrayList<Capa> capas;
            if (datosAplicacion.getLahares() == null) {
                CapaDataSource capaDataSource = new CapaDataSource(getActivity().getApplicationContext());
                capaDataSource.open();
                capas = capaDataSource.getCapaByTipo("AV",  datosAplicacion.getRiesgoActual().getId());
                capaDataSource.close();
            } else {
                capas = datosAplicacion.getLahares();
            }


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
                    Polygon polygon = googleMap.addPolygon(rectOptions1);
                } else {
                    if(capa.getDireccion() == null || capa.getDireccion().equals("")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.RED)
                                .strokeWidth(1)
                                .fillColor(colorLahar);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    }else  if(capa.getDireccion().equals("N")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLACK)

                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    }else  if(capa.getDireccion().equals("V")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.parseColor("#056905"))
                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    }else  if(capa.getDireccion().equals("A")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLUE)
                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    }

//                    PolygonOptions rectOptions1 = new PolygonOptions()
//                            .add(puntosRuta).strokeColor(Color.RED)
//                            .strokeWidth(2)
//                            .fillColor(Color.argb(80, 238, 158, 158));
//                    Polygon polygon = googleMap.addPolygon(rectOptions1);
                }
            }
        }

    }


    private void dibujarRutaSinSenal(RutaEmergencia rutaEmergencia) {
        if (googleMap != null) {
            googleMap.clear();

            rutasRelacionadas = new ArrayList<Capa>();
            rutasRelacionadas.add(rutaEmergencia.getCapa());

            buscarRutasRelacionadas(rutaEmergencia.getCapa());

            dibujarElLahar();

            if (!estaNavegando) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                miUbicacion = googleMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Mi ubicación"));
            }

            if(rutaEmergencia.getDetalleRuta() != null) {
                ArrayList<String> instrucciones = new ArrayList<String>();
                for (DetalleRutaEmergencia detalleRutaEmergencia : rutaEmergencia.getDetalleRuta()) {
                    instrucciones.add(detalleRutaEmergencia.getInstruccion());
                }
                rutas = new RutaArrayAdapter(getActivity(), instrucciones);
                listView.setAdapter(rutas);
            }

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            lineOptions = new PolylineOptions();

            if(rutaEmergencia.getDetallePuntos() != null) {
                for (DetallePuntosEmergencia puntosEmergencia : rutaEmergencia.getDetallePuntos()) {

                    double lat = Double.parseDouble(puntosEmergencia.getLatitud());
                    double lng = Double.parseDouble(puntosEmergencia.getLongitud());
                    LatLng position = new LatLng(lat, lng);
                    lineOptions.add(position);
                }
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);
                googleMap.addPolyline(lineOptions);
            }

//            Capa capa = (Capa) rutaEmergencia.getCapa();
//            String[] coordenadas = capa.getCoordenadas().split(" ");
//            LatLng[] puntosRuta = new LatLng[coordenadas.length];
//            int i = 0;
//            for (String puntoRuta : coordenadas) {
//                String[] coordenada = puntoRuta.split(",");
//                puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
//                i++;
//            }
//            Polyline line = googleMap.addPolyline(new PolylineOptions()
//                    .add(puntosRuta)
//                    .width(4)
//                    .color(Color.GREEN));

            for (Capa capa : rutasRelacionadas) {
                String[] coordenadas = capa.getCoordenadas().split(" ");
                LatLng[] puntosRuta = new LatLng[coordenadas.length];
                int i = 0;
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
            if (puntosCercanos != null && puntosCercanos.size() > 0) {
                for (Capa capa1 : puntosCercanos) {
                    String[] coordenada = capa1.getCoordenadas().split(",");
                    LatLng points1 = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    MarkerOptions optionss1 = new MarkerOptions();
                    optionss1.position(points1);
                    optionss1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa1.getNombre());

                    googleMap.addMarker(optionss1);
                }

            }
        }
    }


    private void dibujarRuta(RutaEmergencia rutaEmergencia) {
        if (googleMap != null) {
            googleMap.clear();

            rutasRelacionadas = new ArrayList<Capa>();
            rutasRelacionadas.add(rutaEmergencia.getCapa());

            buscarRutasRelacionadas(rutaEmergencia.getCapa());

            dibujarElLahar();

            if (!estaNavegando) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                miUbicacion = googleMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Mi ubicación" + rutaEmergencia.getDistanciaTramos()));
            }

            ArrayList<String> instrucciones = new ArrayList<String>();
            if (rutaEmergencia.getDetalleRuta() != null) {
                for (DetalleRutaEmergencia detalleRutaEmergencia : rutaEmergencia.getDetalleRuta()) {
                    instrucciones.add(detalleRutaEmergencia.getInstruccion());
                }
                rutas = new RutaArrayAdapter(getActivity(), instrucciones);
                listView.setAdapter(rutas);
            }

            PolylineOptions lineOptions = null;

            lineOptions = new PolylineOptions();

            if (rutaEmergencia.getDetallePuntos() != null) {
                for (DetallePuntosEmergencia puntosEmergencia : rutaEmergencia.getDetallePuntos()) {

                    double lat = Double.parseDouble(puntosEmergencia.getLatitud());
                    double lng = Double.parseDouble(puntosEmergencia.getLongitud());
                    LatLng position = new LatLng(lat, lng);
                    lineOptions.add(position);
                }
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);
                googleMap.addPolyline(lineOptions);
            }

            for (Capa capa : rutasRelacionadas) {
                String[] coordenadas = capa.getCoordenadas().split(" ");
                LatLng[] puntosRuta = new LatLng[coordenadas.length];
                int i = 0;
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
            if (puntosCercanos != null && puntosCercanos.size() > 0) {
                for (Capa capa : puntosCercanos) {
                    String[] coordenada = capa.getCoordenadas().split(",");
                    LatLng points = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    MarkerOptions optionss1 = new MarkerOptions();
                    optionss1.position(points);
                    optionss1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(capa.getNombre());

                    googleMap.addMarker(optionss1);


                }
            }
        }

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

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&language=es" + "&mode=walking";
        if(datosAplicacion.getRiesgoActual().getId().equals("1")){
            parameters = parameters + "&mode=walking";
        }
       String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
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


    // Fetches data from url passed
    private class DownloadTaskEvacuacion extends AsyncTask<String, Void, String> {

        int indice;

        public DownloadTaskEvacuacion(int indice) {
            this.indice = indice;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
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
            ParserTaskEvacuacion parserTask = new ParserTaskEvacuacion(indice);
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTaskEvacuacion extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        int indice;
        RutasRetorno rutasRetorno;

        public ParserTaskEvacuacion(int indice) {
            this.indice = indice;
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
            if (result != null && result.size() > 0) {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;

                ArrayList<DetallePuntosEmergencia> listaPuntos = new ArrayList<DetallePuntosEmergencia>();
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                        DetallePuntosEmergencia detallePuntosEmergencia = new DetallePuntosEmergencia();
                        detallePuntosEmergencia.setLatitud(String.valueOf(lat));
                        detallePuntosEmergencia.setLongitud(String.valueOf(lng));
                        detallePuntosEmergencia.setIdRuta(rutasEmergencia.get(indice).getId());
                        listaPuntos.add(detallePuntosEmergencia);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(4);
                    lineOptions.color(Color.GREEN);
                }

                rutasEmergencia.get(indice).setDetallePuntos(listaPuntos);
                // Drawing polyline in the Google Map for the i-th route
                googleMap.addPolyline(lineOptions);

                Double distanciaTramos = 0.0;
                ArrayList<DetalleRutaEmergencia> listaRutas = new ArrayList<DetalleRutaEmergencia>();
                for (String texto : rutasRetorno.direcciones) {
                    DetalleRutaEmergencia detalleRutaEmergencia = new DetalleRutaEmergencia();
                    detalleRutaEmergencia.setInstruccion(texto.toString());
                    detalleRutaEmergencia.setIdRuta(rutasEmergencia.get(indice).getId());
                    listaRutas.add(detalleRutaEmergencia);
                    String[] arreglo = texto.toString().split(";");
                    String[] distanciaMedida = arreglo[1].toString().split(" ");
                    if(distanciaMedida[1].toUpperCase().equals("KM")){
                        distanciaTramos = distanciaTramos + Double.valueOf(distanciaMedida[0].replace(",", "."));
                    }else{
                        distanciaTramos = distanciaTramos + Double.valueOf(distanciaMedida[0])/1000;
                    }
                }
                rutasEmergencia.get(indice).setDistanciaTramos(distanciaTramos);
                rutasEmergencia.get(indice).setDetalleRuta(listaRutas);

                if(indice == 2){
                    if(rutasEmergencia.get(0).getDistanciaTramos() < rutasEmergencia.get(1).getDistanciaTramos() && rutasEmergencia.get(0).getDistanciaTramos() < rutasEmergencia.get(2).getDistanciaTramos()){
                        rutasEmergencia.get(0).setOrden("1");
                        if(rutasEmergencia.get(1).getDistanciaTramos() < rutasEmergencia.get(2).getDistanciaTramos()){
                            rutasEmergencia.get(1).setOrden("2");
                            rutasEmergencia.get(2).setOrden("3");
                        }else{
                            rutasEmergencia.get(1).setOrden("3");
                            rutasEmergencia.get(2).setOrden("2");
                        }
                    }else if(rutasEmergencia.get(1).getDistanciaTramos() < rutasEmergencia.get(0).getDistanciaTramos() && rutasEmergencia.get(1).getDistanciaTramos() < rutasEmergencia.get(2).getDistanciaTramos()){
                        rutasEmergencia.get(1).setOrden("1");
                        if(rutasEmergencia.get(0).getDistanciaTramos() < rutasEmergencia.get(2).getDistanciaTramos()){
                            rutasEmergencia.get(0).setOrden("2");
                            rutasEmergencia.get(2).setOrden("3");
                        }else{
                            rutasEmergencia.get(0).setOrden("3");
                            rutasEmergencia.get(2).setOrden("2");
                        }
                    }else{
                        rutasEmergencia.get(2).setOrden("1");
                        if(rutasEmergencia.get(0).getDistanciaTramos() < rutasEmergencia.get(1).getDistanciaTramos()){
                            rutasEmergencia.get(0).setOrden("2");
                            rutasEmergencia.get(1).setOrden("3");
                        }else{
                            rutasEmergencia.get(0).setOrden("3");
                            rutasEmergencia.get(1).setOrden("2");
                        }
                    }
                    for(RutaEmergencia emergencia :rutasEmergencia ){
                        if(emergencia.getOrden().equals("1")){
                            dibujarRuta(emergencia);
                            break;
                        }
                    }

                }



            } else {
                modoDesconectado = true;
                dibujarRuta(rutasEmergencia.get(0));
                if (!mostroMensajeSinInternet) {
                    dibujarRuta(rutasEmergencia.get(0));
                    mostroMensajeSinInternet = true;

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder.setTitle("Información")
                            .setMessage("Su celular no tiene acceso a internet ")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }
    }


    private void mostrarEvacuacion() {
        if (!zonaRiesgo.equals(Constantes.CODIGO_ZONA_SEGURA)) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            googleMap.moveCamera(center);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
            googleMap.animateCamera(zoom);
            if (miUbicacion != null) {
                miUbicacion.remove();
            }
            miUbicacion = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Mi ubicación"));

            datosAplicacion.setUltimaUbicacion(location);


            rutasEmergencia = VariablesUtil.getInstance().buscarRutasCercanas(location, getActivity().getApplicationContext(), false);
            puntosCercanos = VariablesUtil.getInstance().buscarSitiosSegurosCercanos(location, getActivity().getApplicationContext());

            int i = 0;
            for (RutaEmergencia rutaEmergencia : rutasEmergencia) {
                String url = getDirectionsUrl(new LatLng(Double.valueOf(rutaEmergencia.getLatitudOrigen()), Double.valueOf(rutaEmergencia.getLongitudOrigen())),
                        new LatLng(Double.valueOf(rutaEmergencia.getLatitudDestino()), Double.valueOf(rutaEmergencia.getLongitudDestino())));

                DownloadTaskEvacuacion downloadTask = new DownloadTaskEvacuacion(i);

                downloadTask.execute(url);
                i++;
            }
            rutaActiva = 0;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder.setTitle("Advertencia")
                    .setMessage("Como información para el usuario se comunica que las líneas verdes corresponden a rutas de evacuación oficiales, mientras que las azules son instrucciones de referencia para llegar a una ruta de evacuación cercana caminando y fue obtenida utilizando los servicios de Google Maps. Revise su entorno y la información oficial.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            RelativeLayout layoutMapa = (RelativeLayout) view.findViewById(R.id.layoutMapa);
            layoutMapa.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1F));
            verRutaUno.setVisibility(View.GONE);
            verRutaDos.setVisibility(View.GONE);
            verRutaTres.setVisibility(View.GONE);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            googleMap.moveCamera(center);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
            googleMap.animateCamera(zoom);

            if (miUbicacion != null) {
                miUbicacion.remove();
            }
            miUbicacion = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Mi ubicación" ));

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View promptView = layoutInflater.inflate(R.layout.dialog_mensaje_verificar, null);
            android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptView);

            Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

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

            //Zona Segura
            layoutDialog.setBackgroundColor(Color.parseColor("#33A948"));
            leyendaDialog.setBackgroundColor(Color.parseColor("#33A948"));
            tituloDialog.setBackgroundColor(Color.parseColor("#33A948"));
            imgDialog.setImageResource(R.drawable.zona_segura);
            tituloDialog.setText("ZONA SEGURA");
            leyendaDialog.setText(" Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#33A948"));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));
            datosAplicacion.setUltimaUbicacion(location);

        }
    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(estaNavegando && googleMap != null) {
//            googleMap.setMyLocationEnabled(false);
//        }
//        if (null != mMapView)
//            mMapView.onDestroy();
//
//    }
//
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        if (null != mMapView)
//            mMapView.onLowMemory();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(estaNavegando && googleMap != null) {
//            googleMap.setMyLocationEnabled(true);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(estaNavegando && googleMap != null) {
//            googleMap.setMyLocationEnabled(false);
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(googleMap != null && googleMap.isMyLocationEnabled()) {
            googleMap.setMyLocationEnabled(false);
        }
        if (null != mMapView)
            mMapView.onDestroy();

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (null != mMapView)
            mMapView.onLowMemory();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(estaNavegando && googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(googleMap != null && googleMap.isMyLocationEnabled()) {
            googleMap.setMyLocationEnabled(false);
        }
    }

    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location1) {
            location = location1;
            if (googleMap != null) {
                if (location != null) {
                    datosAplicacion.setUltimaUbicacion(location);

                    if (estaNavegando && !primeraVez) {
//                        location.setLatitude(-0.281750);
//                        location.setLongitude(-78.451067);
                        primeraVez = true;
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        googleMap.moveCamera(center);

                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                        googleMap.animateCamera(zoom);

                        zonaRiesgo = VariablesUtil.getInstance().verificarZonaRiesgo(new LatLng(location.getLatitude(), location.getLongitude()), getActivity().getApplicationContext(), datosAplicacion.getRiesgoActual().getId());
                        mostrarEvacuacion();
                    }else if(currentZoom < 3) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).bearing(location.getBearing()).zoom(14).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.animateCamera(cameraUpdate);


                    }else{
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).bearing(location.getBearing()).zoom(currentZoom).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.animateCamera(cameraUpdate);
                    }




                    if (miUbicacion != null) {
                        miUbicacion.remove();
                    }
                    miUbicacion = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Mi ubicación" ));

                    if (datosAplicacion.cameraPosition != null && datosAplicacion.getRiesgoActual().getId().equals("2")) {
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(datosAplicacion.cameraPosition));
                        datosAplicacion.cameraPosition = null;
                        googleMap.setMyLocationEnabled(false);
                        estaNavegando = false;
                    }
                }

            }
        }
    };

}
