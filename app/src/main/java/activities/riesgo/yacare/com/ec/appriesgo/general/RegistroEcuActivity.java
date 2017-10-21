package activities.riesgo.yacare.com.ec.appriesgo.general;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.regex.Pattern;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.GuardarPersonaAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.GuardarPersonaTimer;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;


public class RegistroEcuActivity extends Activity {

    private Button btnGuardarDatos;


    private TextView txtTituloRegistro;
    private TextView txtSubTituloRegistro;

    private EditText editPrimerNombre;
    private EditText editPrimerApellido;
    private EditText editTelefono;
    private EditText editEmail;
    private EditText editNumeroCedula;
    private EditText editCodigoPais;
    private CheckBox checkEcuatoriano;

    private Cuenta cuenta;

    private DatosAplicacion datosAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ecu);

        datosAplicacion = (DatosAplicacion) getApplicationContext();


        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");

        btnGuardarDatos = (Button) findViewById(R.id.btnGuardarDatos);

        txtTituloRegistro = (TextView) findViewById(R.id.txtTituloRegistro);
        txtSubTituloRegistro = (TextView) findViewById(R.id.txtSubTituloRegistro);
        txtTituloRegistro.setTypeface(fontBold);
        txtSubTituloRegistro.setTypeface(fontRegular);

        editPrimerNombre = (EditText) findViewById(R.id.editPrimerNombre);
        editCodigoPais = (EditText) findViewById(R.id.editCodigoPais);
        editPrimerApellido = (EditText) findViewById(R.id.editPrimerApellido);
        editNumeroCedula = (EditText) findViewById(R.id.editNumeroCedula);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editTelefono = (EditText) findViewById(R.id.editTelefono);

        editPrimerNombre.setTypeface(fontRegular);
        editPrimerApellido.setTypeface(fontRegular);
        editNumeroCedula.setTypeface(fontRegular);
        editEmail.setTypeface(fontRegular);
        editTelefono.setTypeface(fontRegular);

        checkEcuatoriano = (CheckBox) findViewById(R.id.checkEcuatoriano);

        if(datosAplicacion.getCuenta() != null){
            cuenta = datosAplicacion.getCuenta();
            editPrimerNombre.setText(cuenta.getPrimerNombre());
            editCodigoPais.setText(cuenta.getCodigopais());
            editPrimerApellido.setText(cuenta.getPrimerApellido());
            editNumeroCedula.setText(cuenta.getNumeroDocumento());
            editEmail.setText(cuenta.getEmail());
            editTelefono.setText(cuenta.getNumeroTelefono());
            if(cuenta.getTipoDocumento().equals("CED")){
                checkEcuatoriano.setChecked(true);
            }

        }


        btnGuardarDatos.setTypeface(fontRegular);


        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Validar campos
                btnGuardarDatos.setEnabled(false);
                boolean grabar = true;

                Pattern pattern = Pattern.compile("\\d{10}");


                if (editNumeroCedula.getText().toString().isEmpty()) {
                    editNumeroCedula.setError("Ingrese su # de identificación");
                    grabar = false;
                } else if (checkEcuatoriano.isChecked() && (!TextUtils.isDigitsOnly(editNumeroCedula.getText().toString().trim()) || !validarCedula(editNumeroCedula.getText().toString().trim()))) {
                    editNumeroCedula.setError("El número de cédula es incorrecto");
                    grabar = false;
                }
                if (editPrimerNombre.getText().toString().isEmpty()) {
                    editPrimerNombre.setError("Ingrese su nombre");
                    grabar = false;
                }
                if (editPrimerApellido.getText().toString().isEmpty()) {
                    editPrimerApellido.setError("Ingrese su apellido");
                    grabar = false;
                }
                if (editEmail.getText().toString() != null && !editEmail.getText().toString().isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()) {
                    editEmail.setError("El formato del correo es incorrecto");
                    grabar = false;
                }

                if (editEmail.getText().toString().isEmpty()) {
                    editEmail.setError("Ingrese su correo");
                    grabar = false;
                }
                if (editCodigoPais.getText().toString().isEmpty()) {
                    editCodigoPais.setError("Ingrese el código de país");
                    grabar = false;
                }

                if (editTelefono.getText().toString().isEmpty()) {
                    editTelefono.setError("Ingrese el número de celular");
                    grabar = false;
                } else if (editTelefono.getText().toString().length() < 8 ) {
                    editTelefono.setError("El formato del celular es incorrecto");
                    grabar = false;
                }
                if (grabar) {
                    btnGuardarDatos.setEnabled(false);
                    if(cuenta == null) {
                        cuenta = new Cuenta();
                    }
                    cuenta.setPrimerNombre(editPrimerNombre.getText().toString());
                    cuenta.setPrimerApellido(editPrimerApellido.getText().toString());
                    cuenta.setNumeroDocumento(editNumeroCedula.getText().toString());
                    cuenta.setNumeroTelefono(editTelefono.getText().toString());
                    cuenta.setEmail(editEmail.getText().toString());
                    cuenta.setCodigopais(editCodigoPais.getText().toString());

                    if (checkEcuatoriano.isChecked()) {
                        cuenta.setTipoDocumento("CED");
                    } else {
                        cuenta.setTipoDocumento("PAS");
                    }

                    GuardarPersonaAsyncTask guardarPersonaAsyncTask = new GuardarPersonaAsyncTask(RegistroEcuActivity.this, null);
                    guardarPersonaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    btnGuardarDatos.setEnabled(true);
                }
            }
        });

    }


    public static boolean validarCedula(String cedula) {
        int suma = 0;
        if (cedula.length() != 10) {
            System.out.println("Ingrese su cedula de 10 digitos");
            return false;
        } else {
            int a[] = new int[cedula.length() / 2];
            int b[] = new int[(cedula.length() / 2)];
            int c = 0;
            int d = 1;
            for (int i = 0; i < cedula.length() / 2; i++) {
                a[i] = Integer.parseInt(String.valueOf(cedula.charAt(c)));
                c = c + 2;
                if (i < (cedula.length() / 2) - 1) {
                    b[i] = Integer.parseInt(String.valueOf(cedula.charAt(d)));
                    d = d + 2;
                }
            }

            for (int i = 0; i < a.length; i++) {
                a[i] = a[i] * 2;
                if (a[i] > 9) {
                    a[i] = a[i] - 9;
                }
                suma = suma + a[i] + b[i];
            }
            int aux = suma / 10;
            int dec = (aux + 1) * 10;
            if ((dec - suma) == Integer.parseInt(String.valueOf(cedula.charAt(cedula.length() - 1))))
                return true;
            else if (suma % 10 == 0 && cedula.charAt(cedula.length() - 1) == '0') {
                return true;
            } else {
                return false;
            }
        }
    }

    public void verificarGuardarPersona(String respuesta) {
        if (!respuesta.equals("ERR")) {
            Boolean statusFlag = null;
            try {
                JSONObject respuestaJSON = new JSONObject(respuesta);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (statusFlag != null && statusFlag) {
                String idCuenta = null;
                try {
                    JSONObject respuestaJSON = new JSONObject(respuesta);
                    JSONObject respuestaId = new JSONObject(respuestaJSON.getString("resultado"));
                    idCuenta = respuestaId.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CuentaDataSource datasource = new CuentaDataSource(getApplicationContext());
                datasource.open();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                cuenta.setFechaCreacion(dateFormat.format(date));
                cuenta.setId(idCuenta);
                if(datosAplicacion.getCuenta() != null){
                    datasource.updateCuenta(cuenta);
                }else {
                    cuenta = datasource.createCuenta(cuenta);
                }
                datasource.close();
                datosAplicacion.setCuenta(cuenta);

                Intent i = new Intent(RegistroEcuActivity.this, ConfirmarRegistroActivity.class);
                startActivity(i);
            } else {
                CuentaDataSource datasource = new CuentaDataSource(getApplicationContext());
                datasource.open();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                cuenta.setFechaCreacion(dateFormat.format(date));
                if(cuenta.getId() == null) {
                    cuenta.setId("0");
                }
                if(datosAplicacion.getCuenta() != null){
                    datasource.updateCuenta(cuenta);
                }else {
                    cuenta = datasource.createCuenta(cuenta);
                }
                datasource.close();
                datosAplicacion.setCuenta(cuenta);

                Timer time = new Timer();
                GuardarPersonaTimer st = new GuardarPersonaTimer(datosAplicacion);
                time.schedule(st, 0, 60000);

                Intent i = new Intent(RegistroEcuActivity.this, ConfirmarRegistroActivity.class);
                startActivity(i);
            }
        } else {
            //Error general
            CuentaDataSource datasource = new CuentaDataSource(getApplicationContext());
            datasource.open();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            cuenta.setFechaCreacion(dateFormat.format(date));
            if(cuenta.getId() == null) {
                cuenta.setId("0");
            }
            if(datosAplicacion.getCuenta() != null){
                datasource.updateCuenta(cuenta);
            }else {
                cuenta = datasource.createCuenta(cuenta);
            }
            datasource.close();
            datosAplicacion.setCuenta(cuenta);

            Timer time = new Timer();
            GuardarPersonaTimer st = new GuardarPersonaTimer(datosAplicacion);
            time.schedule(st, 0, 60000);

            Intent i = new Intent(RegistroEcuActivity.this, ConfirmarRegistroActivity.class);
            startActivity(i);

        }
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
