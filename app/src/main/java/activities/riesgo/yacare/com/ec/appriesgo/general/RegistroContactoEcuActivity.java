package activities.riesgo.yacare.com.ec.appriesgo.general;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;


public class RegistroContactoEcuActivity extends Activity {

    private Button btnGuardarDatos;


    private TextView txtTituloRegistro;
    private TextView txtSubTituloRegistro;

    private EditText editNombreContacto;
    private EditText editApellidoContacto;
    private EditText editCodigoPaisContacto;
    private EditText editTelefonoContacto;
    private EditText editRelacionContacto;

    private Cuenta cuenta;

    private DatosAplicacion datosAplicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ecu_contacto);

        datosAplicacion = (DatosAplicacion) getApplicationContext();


        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
        Typeface fontLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");

        btnGuardarDatos = (Button) findViewById(R.id.btnGuardarDatos);

        txtTituloRegistro = (TextView) findViewById(R.id.txtTituloRegistro);
        txtSubTituloRegistro = (TextView) findViewById(R.id.txtSubTituloRegistro);
        txtTituloRegistro.setTypeface(fontBold);
        txtSubTituloRegistro.setTypeface(fontRegular);

        editNombreContacto = (EditText) findViewById(R.id.editNombreContacto);
        editApellidoContacto = (EditText) findViewById(R.id.editApellidoContacto);
        editCodigoPaisContacto = (EditText) findViewById(R.id.editCodigoPaisContacto);
        editTelefonoContacto = (EditText) findViewById(R.id.editTelefonoContacto);
        editRelacionContacto = (EditText) findViewById(R.id.editRelacionContacto);

        editNombreContacto.setTypeface(fontRegular);
        editApellidoContacto.setTypeface(fontRegular);
        editCodigoPaisContacto.setTypeface(fontRegular);
        editTelefonoContacto.setTypeface(fontRegular);
        editRelacionContacto.setTypeface(fontRegular);


        btnGuardarDatos.setTypeface(fontRegular);


        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Validar campos
                btnGuardarDatos.setEnabled(false);
                boolean grabar = true;

                if (editNombreContacto.getText().toString().isEmpty()) {
                    editNombreContacto.setError("Ingrese el nombre de su contacto");
                    grabar = false;
                }
                if (editApellidoContacto.getText().toString().isEmpty()) {
                    editApellidoContacto.setError("Ingrese el apellido de su contacto");
                    grabar = false;
                }

                if (editCodigoPaisContacto.getText().toString().isEmpty()) {
                    editCodigoPaisContacto.setError("Ingrese el código de país de su contacto");
                    grabar = false;
                }
                if (editTelefonoContacto.getText().toString().isEmpty()) {
                    editTelefonoContacto.setError("Ingrese el celular de su contacto");
                    grabar = false;
                }
                if (editRelacionContacto.getText().toString().isEmpty()) {
                    editRelacionContacto.setError("Ingrese la relación con su contacto");
                    grabar = false;
                }
                if (grabar) {
                    btnGuardarDatos.setEnabled(false);
                    cuenta = datosAplicacion.getCuenta();
                    cuenta.setNombrecontacto(editNombreContacto.getText().toString());
                    cuenta.setApellidocontacto(editApellidoContacto.getText().toString());
                    cuenta.setCodigopaiscontacto(editCodigoPaisContacto.getText().toString());
                    cuenta.setRelacioncontacto(editRelacionContacto.getText().toString());
                    cuenta.setTelefonoEmergencia(editTelefonoContacto.getText().toString());

                    CuentaDataSource datasource = new CuentaDataSource(getApplicationContext());
                    datasource.open();
                    datasource.updateCuenta(cuenta);
                    datasource.close();
                    datosAplicacion.setCuenta(cuenta);

                    Intent i = new Intent(RegistroContactoEcuActivity.this, ConfirmarRegistroActivity.class);
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
