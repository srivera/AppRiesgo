package activities.riesgo.yacare.com.ec.appriesgo.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.MenuRiesgosArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerRiesgosAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.verificacion.LocationService;


public class MenuActivity extends Activity {

    private DatosAplicacion datosAplicacion;
    private MenuRiesgosArrayAdapter menuRiesgosArrayAdapter;
    private ListView listView;
    private ArrayList<Riesgo> riesgos;
    private  ArrayList<Riesgo> riesgos2;
    private ListView listView2;
    private MenuRiesgosArrayAdapter menuRiesgosArrayAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_riesgos);

        ObtenerRiesgosAsyncTask obtenerRiesgosAsyncTask = new ObtenerRiesgosAsyncTask(MenuActivity.this);
        obtenerRiesgosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        listView = (ListView) findViewById(android.R.id.list);

        listView2 = (ListView) findViewById(R.id.list2);

        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(getApplicationContext());
        riesgoDataSource.open();
        riesgos2 = riesgoDataSource.getAllInfromativo();
        riesgoDataSource.close();

        menuRiesgosArrayAdapter2 = new MenuRiesgosArrayAdapter(getApplicationContext(), riesgos2, false);

        listView2.setAdapter(menuRiesgosArrayAdapter2);

        datosAplicacion = (DatosAplicacion) getApplicationContext();
        datosAplicacion.setMenuActivity(MenuActivity.this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("RIESGO", "RIESGO SELECCIONADO");
                datosAplicacion.setRiesgoActual(riesgos.get(position));
                Intent i = new Intent(MenuActivity.this, PrincipalActivity.class);
                startActivity(i);
            }

        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("RIESGO","RIESGO SELECCIONADO");
                datosAplicacion.setRiesgoActual(riesgos2.get(position));
                Intent i = new Intent(MenuActivity.this, PrincipalActivity.class);
                startActivity(i);
            }

        });

        if (datosAplicacion.getCuenta() == null) {
            CuentaDataSource cuentaDataSource = new CuentaDataSource(getApplicationContext());
            cuentaDataSource.open();
            Cuenta cuenta = cuentaDataSource.getCuenta();
            datosAplicacion.setCuenta(cuenta);
            cuentaDataSource.close();
        }
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        TextView tituloAlertaActiva = (TextView) findViewById(R.id.tituloAlertaActiva);
        TextView leyendaMenuAlertas = (TextView) findViewById(R.id.leyendaMenuAlertas);
        TextView realizadaPor = (TextView) findViewById(R.id.realizadaPor);
       // TextView ministerio = (TextView) findViewById(R.id.ministerio);
        tituloAlertaActiva.setTypeface(fontBold);
        leyendaMenuAlertas.setTypeface(fontRegular);
        realizadaPor.setTypeface(fontBold);
       // ministerio.setTypeface(fontRegular);

        if(getIntent().hasExtra("mensaje")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                   MenuActivity.this);
            alertDialogBuilder.setTitle(getIntent().getStringExtra("titulo"))
                    .setMessage(getIntent().getStringExtra("mensaje"))
                    .setCancelable(false)
                    .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public void verificarObtenerRiesgo(String respuesta) {
        RiesgoDataSource riesgoDataSource = new RiesgoDataSource(getApplicationContext());
        riesgoDataSource.open();
        riesgos = riesgoDataSource.getAll();
        riesgoDataSource.close();

        TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getApplicationContext());
        tipoAlertaDataSource.open();
        for (Riesgo riesgo : riesgos) {
            riesgo.setTipoAlerta(tipoAlertaDataSource.getTipoAlertaByCodigo(riesgo.getAlertaActual(), riesgo.getId()));

        }
        tipoAlertaDataSource.close();
        if(respuesta.equals("true")) {
             menuRiesgosArrayAdapter = new MenuRiesgosArrayAdapter(getApplicationContext(), riesgos, true);
        }else{
            menuRiesgosArrayAdapter = new MenuRiesgosArrayAdapter(getApplicationContext(), riesgos, false);
        }

        listView.setAdapter(menuRiesgosArrayAdapter);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("INFORMACIÓN")
                .setMessage("Desea salir de la aplicación?")
                .setNegativeButton("NO", null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        stopService(new Intent(MenuActivity.this, LocationService.class));
                        MenuActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MenuActivity.this, LocationService.class));

        super.onDestroy();
    }
}
