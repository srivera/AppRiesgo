package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.general.ConfirmarRegistroActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class RegistroContactoEcuFragment extends BaseContainerFragment {

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

    private Button btnRegresarRegistroEcu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_registro_ecu_contacto, container, false);


        btnRegresarRegistroEcu = (Button) rootView.findViewById(R.id.btnRegresarRegistroEcu);
        btnRegresarRegistroEcu.setVisibility(View.VISIBLE);
        btnRegresarRegistroEcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroSaludEcuFragment fragment = new RegistroSaludEcuFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });


        datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();



        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
        Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Light.ttf");
        Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");

        btnGuardarDatos = (Button) rootView.findViewById(R.id.btnGuardarDatos);

        txtTituloRegistro = (TextView) rootView.findViewById(R.id.txtTituloRegistro);
        txtSubTituloRegistro = (TextView) rootView.findViewById(R.id.txtSubTituloRegistro);
        txtTituloRegistro.setTypeface(fontBold);
        txtSubTituloRegistro.setTypeface(fontRegular);

        editNombreContacto = (EditText) rootView.findViewById(R.id.editNombreContacto);
        editApellidoContacto = (EditText) rootView.findViewById(R.id.editApellidoContacto);
        editCodigoPaisContacto = (EditText) rootView.findViewById(R.id.editCodigoPaisContacto);
        editTelefonoContacto = (EditText) rootView.findViewById(R.id.editTelefonoContacto);
        editRelacionContacto = (EditText) rootView.findViewById(R.id.editRelacionContacto);

        editNombreContacto.setTypeface(fontRegular);
        editApellidoContacto.setTypeface(fontRegular);
        editCodigoPaisContacto.setTypeface(fontRegular);
        editTelefonoContacto.setTypeface(fontRegular);
        editRelacionContacto.setTypeface(fontRegular);

        if(datosAplicacion.getCuenta() != null){
            editNombreContacto.setText(datosAplicacion.getCuenta().getNombrecontacto());
            editApellidoContacto.setText(datosAplicacion.getCuenta().getApellidocontacto());
            editCodigoPaisContacto.setText(datosAplicacion.getCuenta().getCodigopaiscontacto());
            editTelefonoContacto.setText(datosAplicacion.getCuenta().getTelefonoEmergencia());
            editRelacionContacto.setText(datosAplicacion.getCuenta().getRelacioncontacto());
        }

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

                    CuentaDataSource datasource = new CuentaDataSource(getActivity().getApplicationContext());
                    datasource.open();
                    datasource.updateCuenta(cuenta);
                    datasource.close();
                    datosAplicacion.setCuenta(cuenta);

                    InicioFragment fragment = new InicioFragment();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
                }else{
                    btnGuardarDatos.setEnabled(true);
                }
            }
        });

        return  rootView;
    }




    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
