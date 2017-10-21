package activities.riesgo.yacare.com.ec.appriesgo.verificacion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.LeyendaMapaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.PuntosSegurosFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class MapaFragment extends Fragment {

    private MapView mMapView;

    private Button verRutasEvacuacion;

    private Button monitorearRuta;

    private ImageButton tipoMapa;

    private ImageButton ayuda;

    private GoogleMap googleMap;

    private DatosAplicacion datosAplicacion;
    private ArrayList<Capa> capas;

    private Boolean avisoZonaRiesgo = false;

    private Marker miUbicacion;

    private Boolean primeraVez = false;

    private Boolean estaCentrado = false;

    private Boolean estaNavegando = true;

    private ImageButton navegar;

    private float currentZoom = -1;
    private  View view;


    private Bundle savedInstanceState;


    private boolean hasGpsSensor() {
        PackageManager packMan = getActivity().getPackageManager();
        return packMan.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mapa_fragment, container, false);
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

    int PETICION_PERMISO_LOCALIZACION = 1;

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
        datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("VERIFICAR");
            getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
        }


        //AMEBNAZA VOLCANICA
        if (datosAplicacion.getLahares() == null) {
            CapaDataSource capaDataSource = new CapaDataSource(getActivity().getApplicationContext());
            capaDataSource.open();
            capas = capaDataSource.getCapaByTipo(Constantes.CAPA_AMENAZA_VOLCANICA, datosAplicacion.getRiesgoActual().getId());
            capaDataSource.close();
        } else {
            capas = datosAplicacion.getLahares();
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

        verRutasEvacuacion = (Button) view.findViewById(R.id.verRutasEvacuacion);
        verRutasEvacuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    PuntosSegurosFragment fragment = new PuntosSegurosFragment();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                }
            }
        });

        monitorearRuta = (Button) view.findViewById(R.id.monitorearRuta);
        if (VariablesUtil.servicioMonitoreoActivo) {
            monitorearRuta.setText("Detener");
        }
        monitorearRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    if (!VariablesUtil.servicioMonitoreoActivo) {
                        monitorearRuta.setText("Detener");
                        VariablesUtil.servicioMonitoreoActivo = true;

                        getActivity().startService(new Intent(getActivity(), LocationService.class));

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("INFORMACIÓN")
                                .setMessage("El sistema le notificará cuando se encuentre en zona de riesgo")
                                .setCancelable(false)
                                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }

                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        monitorearRuta.setText("Monitorear Ruta");
                        VariablesUtil.servicioMonitoreoActivo = false;
                        getActivity().stopService(new Intent(getActivity(), LocationService.class));

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("INFORMACIÓN")
                                .setMessage("El monitoreo de ruta se ha detenido")
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
                        googleMap.setMyLocationEnabled(false);
                        estaNavegando = false;
                        navegar.setBackgroundResource(R.drawable.brujulaverde);
                    }
                }
            }
        });

        primeraVez = false;
        currentZoom = -1;
        mMapView = (MapView) view.findViewById(R.id.mapview);

        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;

                    if (map != null) {
                        estaNavegando = true;
                        map.setMyLocationEnabled(true);
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        MapsInitializer.initialize(getActivity());
                        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
                        mMapView.onResume();
                        UiSettings mapSettings;
                        mapSettings = googleMap.getUiSettings();
                        mapSettings.setZoomControlsEnabled(true);
                        mapSettings.setMyLocationButtonEnabled(false);
                        mapSettings.setMapToolbarEnabled(false);
                        mapSettings.setCompassEnabled(true);
                        dibujarLahar();
                        currentZoom = -1;


                        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition position) {
                                if (position.zoom != currentZoom) {
                                    currentZoom = position.zoom;  // here you get zoom level
                                }
                            }
                        });
                        if (datosAplicacion.cameraPosition != null && datosAplicacion.getRiesgoActual().getId().equals("2")) {
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(datosAplicacion.cameraPosition));
                            datosAplicacion.cameraPosition = null;
                            googleMap.setMyLocationEnabled(false);
                            estaNavegando = false;
                        }

                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("INFORMACIÓN")
                                .setMessage("Mapa null")
                                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                }).create().show();

                        if (!hasGpsSensor()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("INFORMACIÓN")
                                    .setMessage("No tiene gps")
                                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    }).create().show();
                        }


                    }
                }
            });
        }


        if (datosAplicacion.getDetalleAlertaFragment() != null) {
            InicioFragment fragment = new InicioFragment();
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(datosAplicacion.getDetalleAlertaFragment());
            transaction.commit();

            TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getActivity().getApplicationContext());
            tipoAlertaDataSource.open();
            TipoAlerta tipoAlerta1 = tipoAlertaDataSource.getTipoAlertaByCodigo(datosAplicacion.getRiesgoActual().getAlertaActual(), datosAplicacion.getRiesgoActual().getId());
            tipoAlertaDataSource.close();

            datosAplicacion.getPrincipalActivity().btnTipoALerta.setEnabled(true);
            datosAplicacion.getPrincipalActivity().btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta1.getCodigoColorOscuro()));
            datosAplicacion.getPrincipalActivity().btnTipoALerta.setText(">");
        }
    }



    private void dibujarLahar() {
        for (Capa capa : capas) {
            Log.d("Capa ", capa.getId());
            String[] coordenadas = capa.getCoordenadas().split(" ");
            LatLng[] puntosRuta = new LatLng[coordenadas.length];
            int i = 0;
            List<LatLng> puntosPoligono = new ArrayList<LatLng>();
            for (String puntoP : coordenadas) {
                Log.d("Capa ", capa.getId() + " / " + puntoP);
                String[] coordenada = puntoP.split(",");
                if(coordenada.length > 1) {
                    puntosRuta[i] = new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0]));
                    puntosPoligono.add(new LatLng(Double.valueOf(coordenada[1]), Double.valueOf(coordenada[0])));
                }
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

            if (googleMap != null) {
//                if (!capa.getId().equals("AVT3") && !capa.getId().equals("AVT4") && !capa.getId().equals("AVT5") ) {
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
                    if (capa.getDireccion() == null || capa.getDireccion().equals("")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.RED)
                                .strokeWidth(1)
                                .fillColor(colorLahar);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("N")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLACK)

                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("V")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.parseColor("#056905"))
                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    } else if (capa.getDireccion().equals("A")) {
                        PolygonOptions rectOptions1 = new PolygonOptions()
                                .add(puntosRuta).strokeColor(Color.BLUE)
                                .strokeWidth(5);
                        Polygon polygon = googleMap.addPolygon(rectOptions1);
                    }
                }
            }
//            }
        }
    }


    protected void mostrarMensajeVerificar(String zonaRiesgo, Location location) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.moveCamera(center);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);


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
        if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_SEGURA)) {
            //Zona Segura
            layoutDialog.setBackgroundColor(Color.parseColor("#33A948"));
            leyendaDialog.setBackgroundColor(Color.parseColor("#33A948"));
            tituloDialog.setBackgroundColor(Color.parseColor("#33A948"));
            imgDialog.setImageResource(R.drawable.zona_segura);
            tituloDialog.setText("ZONA SEGURA");
            leyendaDialog.setText("Usted se encuentra en zona segura. Manténgase informado por fuentes oficiales.");
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#33A948"));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"));
        } else if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_RIESGO)) {
            //Zona Riesgo
            avisoZonaRiesgo = true;
            TipoAlerta tipoAlerta = datosAplicacion.getRiesgoActual().getTipoAlerta();

            layoutDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            leyendaDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            tituloDialog.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            imgDialog.setImageResource(R.drawable.zona_riesgo);
            tituloDialog.setText("ZONA RIESGO");
            leyendaDialog.setText("Usted se encuentra en zona de riesgo. Manténgase informado por fuentes oficiales.");
            tituloDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
            imgDialog.setColorFilter(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
            leyendaDialog.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));

        } else if (zonaRiesgo.equals(Constantes.CODIGO_ZONA_NO_DEFINIDA)) {
            //Sin ubicacion
            layoutDialog.setBackgroundColor(Color.parseColor("#FFFFFF"));
            leyendaDialog.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tituloDialog.setBackgroundColor(Color.parseColor("#FFFFFF"));
            leyendaDialog.setTextColor(Color.parseColor("#000000"));
            tituloDialog.setTextColor(Color.parseColor("#000000"));
            tituloDialog.setText("SIN SEÑAL");
            leyendaDialog.setText("No fue posible obtener su ubicación. Revise la señal de su gps. ");
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.moveCamera(center);
        zoom = CameraUpdateFactory.zoomTo(14);
        googleMap.animateCamera(zoom);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleMap != null && googleMap.isMyLocationEnabled()) {
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
        if (estaNavegando && googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleMap != null && googleMap.isMyLocationEnabled()) {
            googleMap.setMyLocationEnabled(false);
        }
    }

    GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
//            location.setLatitude(-0.92905);
//            location.setLongitude(-78.62397);
            if (googleMap != null) {
                if (location != null && getActivity() != null && getActivity().getApplicationContext() != null) {

                    if (miUbicacion != null) {
                        miUbicacion.remove();
                    }


                    String zonaRiesgo = VariablesUtil.getInstance().verificarZonaRiesgo(new LatLng(location.getLatitude(), location.getLongitude()), getActivity().getApplicationContext(), datosAplicacion.getRiesgoActual().getId());
                    if (!primeraVez) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        googleMap.moveCamera(center);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                        googleMap.animateCamera(zoom);
                        datosAplicacion.setUltimaUbicacion(location);
                        mostrarMensajeVerificar(zonaRiesgo, location);
                        primeraVez = true;

                    } else if (currentZoom < 3) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        googleMap.moveCamera(center);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
                        googleMap.animateCamera(zoom);
                        estaCentrado = true;

                    } else {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).bearing(location.getBearing()).zoom(currentZoom).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                        googleMap.animateCamera(cameraUpdate);

                    }


                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    miUbicacion = googleMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            .title("Mi ubicación"));

                    datosAplicacion.setUltimaUbicacion(location);

                }
            }
        }
    };
}