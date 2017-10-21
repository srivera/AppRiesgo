package activities.riesgo.yacare.com.ec.appriesgo.general;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.alerta.AlertaContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.alerta.DetalleAlertaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerRiesgoTimer;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.evacuacion.EvacuacionContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.verificacion.VerificarContainerFragment;


public class PrincipalActivity extends FragmentActivity {

    private FragmentTabHost tabHost;
    private DatosAplicacion datosAplicacion;


    public Button btnTipoALerta;

    Timer time;

    public String fechaIncidente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);
        datosAplicacion = (DatosAplicacion) getApplicationContext();

        if (getActionBar() != null) {
            String nombreRiesgo = datosAplicacion.getRiesgoActual().getNombreRiesgo();
            if(datosAplicacion.getRiesgoActual().getId().equals("4")) {
                //Tsunami
                getActionBar().setTitle(nombreRiesgo);
                getActionBar().setIcon(R.drawable.tsugris);
            }else if(datosAplicacion.getRiesgoActual().getId().equals("5")) {
                getActionBar().setTitle("ECUADOR MÁS SEGURO");
                getActionBar().setSubtitle(null);
                getActionBar().setIcon(R.drawable.maleta);
                TextView leyendaAlerta = (TextView) findViewById(R.id.subtituloAlerta);
                leyendaAlerta.setText("Descarga el Plan");
            }else{
                getActionBar().setTitle(nombreRiesgo.substring(0, nombreRiesgo.indexOf(" ")));
                getActionBar().setSubtitle(nombreRiesgo.substring(nombreRiesgo.indexOf(" "), nombreRiesgo.length()));
                if (datosAplicacion.getRiesgoActual().getId().equals("3")) {
                    getActionBar().setIcon(R.drawable.fenomenonino);
                } else{
                    getActionBar().setIcon(R.drawable.volcan);
                }
            }
        }

        TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getApplicationContext());
        tipoAlertaDataSource.open();
        datosAplicacion.getRiesgoActual().setTipoAlerta(tipoAlertaDataSource.getTipoAlertaByCodigo(datosAplicacion.getRiesgoActual().getAlertaActual(), datosAplicacion.getRiesgoActual().getId()));
        tipoAlertaDataSource.close();

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(null,
                getResources().getDrawable(R.drawable.inicio)), InicioContainerFragment.class, null);

        if(!datosAplicacion.getRiesgoActual().getId().equals("5")) {
            tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(null, getResources().getDrawable(R.drawable.verificar)),
                    VerificarContainerFragment.class, null);
        }

        if(!datosAplicacion.getRiesgoActual().getId().equals("3") && !datosAplicacion.getRiesgoActual().getId().equals("5") && !datosAplicacion.getRiesgoActual().getId().equals("4")) {
            tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(null, getResources().getDrawable(R.drawable.simulacro)),
                    SimulacroContainerFragment.class, null);
            tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(null, getResources().getDrawable(R.drawable.evacuar)),
                    EvacuacionContainerFragment.class, null);
        }
        if(!datosAplicacion.getRiesgoActual().getId().equals("5")) {
            tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator(null, getResources().getDrawable(R.drawable.historial)),
                    AlertaContainerFragment.class, null);
    }

        datosAplicacion = (DatosAplicacion) getApplicationContext();
        if (datosAplicacion.getCuenta() == null) {
            CuentaDataSource cuentaDataSource = new CuentaDataSource(getApplicationContext());
            cuentaDataSource.open();
            Cuenta cuenta = cuentaDataSource.getCuenta();
            datosAplicacion.setCuenta(cuenta);
            cuentaDataSource.close();
        }
        TipoAlerta tipoAlerta = datosAplicacion.getRiesgoActual().getTipoAlerta();

        if(tipoAlerta != null && tipoAlerta.getId() != null) {
            Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
            Typeface fontLight = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");

            TextView tituloAlerta = (TextView) findViewById(R.id.tituloAlerta);
            TextView leyendaAlerta = (TextView) findViewById(R.id.subtituloAlerta);
            tituloAlerta.setText(tipoAlerta.getNombre());
            tituloAlerta.setTypeface(fontBold);
            leyendaAlerta.setTypeface(fontLight);
            leyendaAlerta.setText(tipoAlerta.getLeyendaCorta());

            tituloAlerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            leyendaAlerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            tituloAlerta.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
            leyendaAlerta.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));

            btnTipoALerta = (Button) findViewById(R.id.btnTipoAlerta);
            btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColorOscuro()));

            btnTipoALerta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetalleAlertaFragment fragment = new DetalleAlertaFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(android.R.id.tabcontent, fragment);
                    transaction.commit();

                    TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getApplicationContext());
                    tipoAlertaDataSource.open();
                    TipoAlerta tipoAlerta1 = tipoAlertaDataSource.getTipoAlertaByCodigo(datosAplicacion.getRiesgoActual().getAlertaActual(), datosAplicacion.getRiesgoActual().getId());
                    tipoAlertaDataSource.close();

                    btnTipoALerta.setEnabled(false);
                    btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta1.getCodigoColor()));
                    btnTipoALerta.setText("");
                }
            });

            ImageView imagenALerta = (ImageView) findViewById(R.id.imagenAlerta);
            imagenALerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            if (tipoAlerta.getCodigoTipoAlerta().equals("BLA") && datosAplicacion.getRiesgoActual().getEstadoRiesgo().equals("V")) {
                imagenALerta.setImageResource(R.drawable.gris);
            } else if (tipoAlerta.getCodigoTipoAlerta().equals("BLA") && datosAplicacion.getRiesgoActual().getEstadoRiesgo().equals("F")) {
                imagenALerta.setImageResource(R.drawable.fenomenonino);
            } else if (tipoAlerta.getCodigoTipoAlerta().equals("BLA") && datosAplicacion.getRiesgoActual().getEstadoRiesgo().equals("T")) {
                imagenALerta.setImageResource(R.drawable.tsugris);
            }

            datosAplicacion.setPrincipalActivity(this);


//            time = new Timer();
//            ObtenerRiesgoTimer st = new ObtenerRiesgoTimer(PrincipalActivity.this);
//            time.schedule(st, 0, 60000);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab1")) {
                    InicioContainerFragment fragment = new InicioContainerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(android.R.id.tabcontent, fragment);
                    transaction.commit();
                } else if (tabId.equals("tab4")) {
                    EvacuacionContainerFragment fragment = new EvacuacionContainerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(android.R.id.tabcontent, fragment);
                    transaction.commit();
                }
            }
        });

        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if (tabHost.getCurrentTabTag().equals("tab1")) {
                tabHost.setCurrentTab(0);
                InicioContainerFragment fragment = new InicioContainerFragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(android.R.id.tabcontent, fragment);
                transaction.commit();
                //  }
            }
        });


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            //Encienda el GPS
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    PrincipalActivity.this);
            alertDialogBuilder.setTitle("INFORMACION")
                    .setMessage("Para utilizar esta aplicación se recomienda que active el GPS")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if (getIntent().hasExtra("pantalla") && getIntent().getExtras().get("pantalla").equals("EVACUAR")) {
            //Abrir pantalla de historial'
            tabHost.setCurrentTab(3);
            // No funciona esta instruccion
        }

        if (getIntent().hasExtra("pantalla") && getIntent().getExtras().get("pantalla").equals("HISTORIAL")) {
            //Abrir pantalla de historial'
            tabHost.setCurrentTab(4);
            // No funciona esta instruccion
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mostrarPantallaEmergencia();
        return true;
    }


    AlertDialog alert;

    protected void mostrarPantallaEmergencia() {

        LayoutInflater layoutInflater = LayoutInflater.from(PrincipalActivity.this);
        View promptView = layoutInflater.inflate(R.layout.activity_emergencia, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PrincipalActivity.this);
        alertDialogBuilder.setView(promptView);

        datosAplicacion = (DatosAplicacion) getApplicationContext();

        TextView txtTitulo = (TextView) promptView.findViewById(R.id.txtTitulo);
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        txtTitulo.setTypeface(fontRegular);

        ImageButton llamar911 = (ImageButton) promptView.findViewById(R.id.llamar911);
        llamar911.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PrincipalActivity.this);
                alertDialogBuilder.setTitle("LLAMADA DE EMERGENCIA")
                        .setMessage("Por favor utilice esta opción de una forma responsable, únicamente en casos de emergencia")
                        .setCancelable(false)
                        .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);

                                phoneCallIntent.setData(Uri.parse("tel:911"));
                                startActivity(phoneCallIntent);

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        ImageButton reportar = (ImageButton) promptView.findViewById(R.id.reportar);
        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchApp("com.walpana.ecu911");
            }
        });

        // create an alert dialog
        alert = alertDialogBuilder.create();
        alert.show();
        ImageButton cerrar = (ImageButton) promptView.findViewById(R.id.cerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

    }

    public void actualizarAlertaPantalla(String codigoTipoAlerta) {
        if (!codigoTipoAlerta.equals("Sin conexión")) {
            TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getApplicationContext());
            tipoAlertaDataSource.open();
            TipoAlerta tipoAlerta = tipoAlertaDataSource.getTipoAlertaByCodigo(codigoTipoAlerta, datosAplicacion.getRiesgoActual().getId());
            tipoAlertaDataSource.close();

            TextView tituloAlerta = (TextView) findViewById(R.id.tituloAlerta);
            TextView leyendaAlerta = (TextView) findViewById(R.id.subtituloAlerta);
            tituloAlerta.setText(tipoAlerta.getNombre());
            leyendaAlerta.setText(tipoAlerta.getLeyendaCorta());

            tituloAlerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            leyendaAlerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));
            tituloAlerta.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));
            leyendaAlerta.setTextColor(Color.parseColor(tipoAlerta.getCodigoColorLetra()));

            btnTipoALerta = (Button) findViewById(R.id.btnTipoAlerta);

            ImageView imagenALerta = (ImageView) findViewById(R.id.imagenAlerta);
            imagenALerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColor()));

            btnTipoALerta = (Button) findViewById(R.id.btnTipoAlerta);
            btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta.getCodigoColorOscuro()));

            if (datosAplicacion.getInicioFragment() != null) {
                datosAplicacion.getInicioFragment().actualizarColorAlerta();
            }
        } else {
            TextView leyendaAlerta = (TextView) findViewById(R.id.subtituloAlerta);
            leyendaAlerta.setText(codigoTipoAlerta);
        }
    }


    public void verificarObtenerRiesgo(String respuesta) {
        if (!respuesta.equals("ERR")) {
            Boolean statusFlag = null;
            JSONObject respuestaJSON = null;
            try {
                respuestaJSON = new JSONObject(respuesta);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (statusFlag != null && statusFlag) {
                Riesgo riesgoActual = datosAplicacion.getRiesgoActual();
                try {
                    JSONObject riesgoJ = new JSONObject(respuestaJSON.getString("resultado"));
                    if (riesgoJ.has("alertaactual") && riesgoJ.has("fechaalerta")) {
                        // if (!riesgoActual.getAlertaActual().equals(riesgoJ.getString("alertaactual"))) {
                        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(getApplicationContext());
                        riesgoDataSource.open();
                        Riesgo riesgo = riesgoDataSource.getRiesgoId(datosAplicacion.getRiesgoActual().getId());
                        riesgo.setAlertaActual(riesgoJ.getString("alertaactual"));
                        riesgo.setFechaAlerta(riesgoJ.getString("fechaalerta"));
                        riesgoDataSource.updateRiesgo(riesgo);
                        riesgoDataSource.close();

                        TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getApplicationContext());
                        tipoAlertaDataSource.open();

                        TipoAlerta tipoAlerta = tipoAlertaDataSource.getTipoAlertaByCodigo(riesgoJ.getString("alertaactual"), datosAplicacion.getRiesgoActual().getId());
                        riesgo.setTipoAlerta(tipoAlerta);
                        tipoAlerta.setLeyendaCorta(riesgoJ.getString("leyenda"));
                        tipoAlertaDataSource.updateTipoAlerta(tipoAlerta);

                        tipoAlertaDataSource.close();

                        datosAplicacion.setRiesgoActual(riesgo);


                        actualizarAlertaPantalla(riesgo.getTipoAlerta().getCodigoTipoAlerta());
                        // }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                actualizarAlertaPantalla("Sin conexión");
            }

        } else {
            actualizarAlertaPantalla("Sin conexión");
        }
    }

    LayoutInflater layoutInflater;
    View promptView = null;
    android.app.AlertDialog.Builder alertDialogBuilder;

    public void verificarreportarEmergencia(String codigoReporte1) {

        if (!codigoReporte1.equals("ERR")) {
            Boolean statusFlag = null;
            JSONObject respuestaJSON = null;
            try {
                respuestaJSON = new JSONObject(codigoReporte1);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (statusFlag != null && statusFlag) {
                alert.cancel();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PrincipalActivity.this);
                alertDialogBuilder.setTitle("INFORMACIÓM")
                        .setMessage("La emergencia fue reportada")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PrincipalActivity.this);
                alertDialogBuilder.setTitle("ERROR")
                        .setMessage("No fue posible reportar la emergencia, intente llamar")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        } else {
            //Error general
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    PrincipalActivity.this);
            alertDialogBuilder.setTitle("ERROR")
                    .setMessage("No fue posible reportar la emergencia, intente llamar")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }




    @Override
    protected void onDestroy() {
        if (alert != null) {
            alert.cancel();
        }
        datosAplicacion.setPrincipalActivity(null);
        super.onDestroy();
    }



    public void launchApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // Activity was found, launch new app
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Activity not found. Send user to market
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            startActivity(intent);
        }
    }
}
