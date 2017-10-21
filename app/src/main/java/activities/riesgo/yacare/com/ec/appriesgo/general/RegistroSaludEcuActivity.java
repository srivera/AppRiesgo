package activities.riesgo.yacare.com.ec.appriesgo.general;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;


public class RegistroSaludEcuActivity extends Activity {

    private Button btnGuardarDatos;


    private TextView txtTituloRegistro;
    private TextView txtSubTituloRegistro;

    private Spinner spinDiscapacidad;
    private Spinner spinTipoSangre;
    private EditText editAlergia;

    private Cuenta cuenta;

    private DatosAplicacion datosAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ecu_salud);

        datosAplicacion = (DatosAplicacion) getApplicationContext();

        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        Typeface fontLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");

        btnGuardarDatos = (Button) findViewById(R.id.btnGuardarDatos);

        txtTituloRegistro = (TextView) findViewById(R.id.txtTituloRegistro);
        txtSubTituloRegistro = (TextView) findViewById(R.id.txtSubTituloRegistro);
        txtTituloRegistro.setTypeface(fontBold);
        txtSubTituloRegistro.setTypeface(fontRegular);

        editAlergia = (EditText) findViewById(R.id.editAlergia);

        spinDiscapacidad = (Spinner) findViewById(R.id.spinDiscapacidad);
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("Ninguna");
        list.add("Motora");
        list.add("Auditiva");
        list.add("Visual");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinDiscapacidad.setAdapter(dataAdapter);


        spinTipoSangre = (Spinner) findViewById(R.id.spinTipoSangre);
        list = new ArrayList<String>();
        list.add("");
        list.add("O+");
        list.add("O-");
        list.add("A+");
        list.add("A-");
        list.add("B+");
        list.add("B-");
        list.add("AB+");
        list.add("AB-");

        dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinTipoSangre.setAdapter(dataAdapter);


        if(datosAplicacion.getCuenta() != null && datosAplicacion.getCuenta().getDiscapacidad() != null){
            cuenta = datosAplicacion.getCuenta();
            if(cuenta.getDiscapacidad().equals("Ninguna")) {
                spinDiscapacidad.setSelection(1);
            }else  if(cuenta.getDiscapacidad().equals("Motora")){
                spinDiscapacidad.setSelection(2);
            }else  if(cuenta.getDiscapacidad().equals("Auditiva")){
                spinDiscapacidad.setSelection(3);
            }else  if(cuenta.getDiscapacidad().equals("Visual")){
                spinDiscapacidad.setSelection(4);
            }

            if(cuenta.getTiposangre().equals("O+")) {
                spinTipoSangre.setSelection(1);
            }else  if(cuenta.getTiposangre().equals("O-")){
                spinTipoSangre.setSelection(2);
            }else  if(cuenta.getTiposangre().equals("A+")){
                spinTipoSangre.setSelection(3);
            }else  if(cuenta.getTiposangre().equals("A-")){
                spinTipoSangre.setSelection(4);
            }else  if(cuenta.getTiposangre().equals("B+")){
                spinTipoSangre.setSelection(5);
            }else  if(cuenta.getTiposangre().equals("B-")){
                spinTipoSangre.setSelection(6);
            }else  if(cuenta.getTiposangre().equals("AB+")){
                spinTipoSangre.setSelection(7);
            }else  if(cuenta.getTiposangre().equals("AB-")){
                spinTipoSangre.setSelection(8);
            }

            editAlergia.setText(cuenta.getAlergiaMedicamento());
        }



        btnGuardarDatos.setTypeface(fontRegular);


        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Validar campos
                btnGuardarDatos.setEnabled(false);
                boolean grabar = true;

                 if (spinDiscapacidad.getSelectedItem().toString().equals("")) {
                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                             RegistroSaludEcuActivity.this);
                     alertDialogBuilder.setTitle("ERROR")
                             .setMessage("Seleccione un valor para discapacidad")
                             .setCancelable(false)
                             .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     ;
                                 }
                             });

                     AlertDialog alertDialog = alertDialogBuilder.create();
                     alertDialog.show();
                    grabar = false;
                }

                if (spinTipoSangre.getSelectedItem().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            RegistroSaludEcuActivity.this);
                    alertDialogBuilder.setTitle("ERROR")
                            .setMessage("Seleccione un valor para el tipo de sangre")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ;
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    grabar = false;
                }
                if (editAlergia.getText().toString().isEmpty()) {
                    editAlergia.setError("Si no tiene alergias escriba NINGUNA");
                    grabar = false;
                }

                if (grabar) {
                    btnGuardarDatos.setEnabled(false);
                    cuenta = datosAplicacion.getCuenta();
                    cuenta.setDiscapacidad(spinDiscapacidad.getSelectedItem().toString());
                    cuenta.setTiposangre(spinTipoSangre.getSelectedItem().toString());
                    cuenta.setAlergiaMedicamento(editAlergia.getText().toString());

                    CuentaDataSource datasource = new CuentaDataSource(getApplicationContext());
                    datasource.open();
                    datasource.updateCuenta(cuenta);
                    datasource.close();
                    datosAplicacion.setCuenta(cuenta);

                    Intent i = new Intent(RegistroSaludEcuActivity.this, RegistroContactoEcuActivity.class);
                    startActivity(i);
                }else{
                    btnGuardarDatos.setEnabled(true);
                }


            }
        });

    }



    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
