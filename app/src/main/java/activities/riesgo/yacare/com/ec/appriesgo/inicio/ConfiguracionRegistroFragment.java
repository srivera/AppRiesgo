package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class ConfiguracionRegistroFragment extends BaseContainerFragment{

	private Button btnGuardarDatos;

	private Button btnRegresarConfiguracion;

	private EditText editPrimerNombre;
	private EditText editSegundoNombre;
	private EditText editPrimerApellido;
	private EditText editSegundoApellido;
	private EditText editTelefono;
	private EditText editEmail;
	private EditText editNumeroCedula;
	private EditText editCiudad;
	private EditText editSector;

	private TextView txtTituloRegistro;
	private TextView txtSubTituloRegistro;

	private Spinner spinTipoDocumento;

	private Cuenta cuenta;

	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_configuracion_registro, container, false);
		Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
		Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");

		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("CONFIGURACIÓN");
			getActivity().getActionBar().setSubtitle("DATOS PERSONALES");
		}

		btnRegresarConfiguracion = (Button) rootView.findViewById(R.id.btnRegresarConfiguracion);
		btnRegresarConfiguracion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InicioFragment fragment = new InicioFragment();
				((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
			}
		});

		txtTituloRegistro = (TextView) rootView.findViewById(R.id.txtTituloRegistro);
		txtSubTituloRegistro = (TextView) rootView.findViewById(R.id.txtSubTituloRegistro);
		txtTituloRegistro.setTypeface(fontBold);
		txtSubTituloRegistro.setTypeface(fontLight);

		spinTipoDocumento = (Spinner) rootView.findViewById(R.id.spinTipoDocumento);
		List<String> list = new ArrayList<String>();
		list.add("Cédula");
		list.add("Pasaporte");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
				(getActivity(), android.R.layout.simple_spinner_item,list);

		dataAdapter.setDropDownViewResource
				(android.R.layout.simple_spinner_dropdown_item);

		spinTipoDocumento.setAdapter(dataAdapter);

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

		btnGuardarDatos = (Button) rootView.findViewById(R.id.btnGuardarDatos);

		editPrimerNombre = (EditText) rootView.findViewById(R.id.editPrimerNombre);
		editSegundoNombre = (EditText) rootView.findViewById(R.id.editSegundoNombre);
		editPrimerApellido = (EditText) rootView.findViewById(R.id.editPrimerApellido);
		editSegundoApellido = (EditText) rootView.findViewById(R.id.editSegundoApellido);
		editNumeroCedula = (EditText) rootView.findViewById(R.id.editNumeroCedula);
		editEmail = (EditText) rootView.findViewById(R.id.editEmail);
		editCiudad = (EditText) rootView.findViewById(R.id.editCiudad);
		editSector = (EditText) rootView.findViewById(R.id.editSector);
		editTelefono = (EditText) rootView.findViewById(R.id.editTelefono);


		editPrimerNombre.setTypeface(fontRegular);
		editSegundoNombre.setTypeface(fontRegular);
		editPrimerApellido.setTypeface(fontRegular);
		editSegundoApellido.setTypeface(fontRegular);
		editNumeroCedula.setTypeface(fontRegular);
		editEmail.setTypeface(fontRegular);
		editCiudad.setTypeface(fontRegular);
		editSector.setTypeface(fontRegular);
		editTelefono.setTypeface(fontRegular);

		CuentaDataSource cuentaDataSource = new CuentaDataSource(getActivity());
		cuentaDataSource.open();
		cuenta = cuentaDataSource.getCuenta();
		cuentaDataSource.close();

		editPrimerNombre.setText(cuenta.getPrimerNombre());
		editSegundoNombre.setText(cuenta.getSegundoNombre());
		editPrimerApellido.setText(cuenta.getPrimerApellido());
		editSegundoApellido.setText(cuenta.getSegundoApellido());
		editNumeroCedula.setText(cuenta.getNumeroDocumento());
		editEmail.setText(cuenta.getEmail());
		editCiudad.setText(cuenta.getCiudad());
		editSector.setText(cuenta.getSector());
		editTelefono.setText(cuenta.getNumeroTelefono());

		if(cuenta.getTipoDocumento().equals("CED")){
			spinTipoDocumento.setSelection(0);
		}else{
			spinTipoDocumento.setSelection(1);
		}

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
				} else if (spinTipoDocumento.getSelectedItem().toString().equals("Cédula") && (!TextUtils.isDigitsOnly(editNumeroCedula.getText().toString().trim()) || !validarCedula(editNumeroCedula.getText().toString().trim()))) {
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
				if (editSector.getText().toString().isEmpty()) {
					editSector.setError("Ingrese el sector");
					grabar = false;
				}
				if (editCiudad.getText().toString().isEmpty()) {
					editCiudad.setError("Ingrese la ciudad");
					grabar = false;
				}

				if (editTelefono.getText().toString().isEmpty()) {
					editTelefono.setError("Ingrese el número de celular");
					grabar = false;
				} else if (editTelefono.getText().toString().length() != 10 || !editTelefono.getText().toString().startsWith("09")) {
					editTelefono.setError("El formato del celular es incorrecto");
					grabar = false;
				}
				if (grabar) {
					btnGuardarDatos.setEnabled(false);

					cuenta.setPrimerNombre(editPrimerNombre.getText().toString());
					cuenta.setSegundoNombre(editSegundoNombre.getText().toString());
					cuenta.setPrimerApellido(editPrimerApellido.getText().toString());
					cuenta.setSegundoApellido(editSegundoApellido.getText().toString());
					cuenta.setNumeroDocumento(editNumeroCedula.getText().toString());
					cuenta.setNumeroTelefono(editTelefono.getText().toString());
					cuenta.setEmail(editEmail.getText().toString());
					cuenta.setCiudad(editCiudad.getText().toString());
					cuenta.setSector(editSector.getText().toString());

					if(spinTipoDocumento.getSelectedItem().equals("Cédula")){
						cuenta.setTipoDocumento("CED");
					}else{
						cuenta.setTipoDocumento("PAS");
					}

//					GuardarPersonaAsyncTask guardarPersonaAsyncTask = new GuardarPersonaAsyncTask(null, ConfiguracionRegistroFragment.this);
//					guardarPersonaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					btnGuardarDatos.setEnabled(true);
				}


			}
		});

		return rootView;
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

			if (statusFlag != null  && statusFlag) {
				String idCuenta = null;
				try {
					JSONObject respuestaJSON = new JSONObject(respuesta);
					JSONObject respuestaId = new JSONObject(respuestaJSON.getString("resultado"));
					idCuenta = respuestaId.getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				CuentaDataSource datasource = new CuentaDataSource(getActivity().getApplicationContext());
				datasource.open();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				cuenta.setUltimaFecha(dateFormat.format(date));
				cuenta.setId(idCuenta);
				datasource.updateCuenta(cuenta);
				datasource.close();
				datosAplicacion.setCuenta(cuenta);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("INFORMACIÓN")
						.setMessage("Sus datos se actualizaron correctamente")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								;
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} else {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());
					alertDialogBuilder.setTitle("ERROR")
							.setMessage("Vuelva a intentar en un momento, por favor")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

									;
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
			}
		} else {
			//Error general
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
			alertDialogBuilder.setTitle("ERROR")
					.setMessage("Vuelva a intentar en un momento")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
}
