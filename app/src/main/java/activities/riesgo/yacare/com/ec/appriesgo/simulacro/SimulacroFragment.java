package activities.riesgo.yacare.com.ec.appriesgo.simulacro;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ElementoEmergenciaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.evacuacion.EvacuacionFragment;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.recomendaciones.RecomendacionFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

import static activities.riesgo.yacare.com.ec.appriesgo.util.Constantes.PREF_HORA_INICIO_SIMULACRO;


public class SimulacroFragment extends BaseContainerFragment{


	private DatosAplicacion datosAplicacion;

	private TextView tituloSimulacro;
	private TextView leyendaSimulacro;
	private TextView pasoUno;
	private TextView pasoUno2;
	private ImageButton btnAyudaPasoUno;
	private TextView pasoDos;
	private TextView pasoDos2;
	private ImageButton btnAyudaPasoDos;

	private Button iniciarSimulacro;
	private TextView leyendaTiempo;
	private TextView tiempoTranscurrido;
	private TextView leyendaDistancia;
	private TextView distanciaAprox;
	private TextView metros;
	private TextView horas;

	private CheckBox checkBoxDos;
	private CheckBox checkBoxUno;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_simulacro, container, false);

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		if(getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("SIMULACRO");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
		Typeface fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
		Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

		tituloSimulacro = (TextView) rootView.findViewById(R.id.tituloSimulacro);
		leyendaSimulacro = (TextView) rootView.findViewById(R.id.leyendaSimulacro);

		checkBoxDos = (CheckBox) rootView.findViewById(R.id.checkBoxDos);
		checkBoxUno = (CheckBox) rootView.findViewById(R.id.checkBoxUno);

		checkBoxUno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(!verificarMochila()){
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								getActivity());
						alertDialogBuilder.setTitle("INFORMACIÓN")
								.setMessage("Su mochila no está completa. Por favor verifique los elementos que hacen falta.")
								.setCancelable(false)
								.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});

						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
						checkBoxUno.setChecked(false);
					}
				}
			}
		});

		checkBoxDos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
				if (!sharedPrefs.getBoolean(Constantes.PREF_SIMULACRO_INICIADO, false)) {
					if (isChecked) {
						iniciarSimulacro.setEnabled(true);
						iniciarSimulacro.setBackgroundResource(R.drawable.button_compartir);
						iniciarSimulacro.setText("Iniciar el simulacro");
					} else if (!sharedPrefs.getBoolean(Constantes.PREF_SIMULACRO_INICIADO, false)) {
						iniciarSimulacro.setEnabled(false);
						iniciarSimulacro.setBackgroundResource(R.drawable.button_gris);
					}
				}
			}
		});


		btnAyudaPasoUno = (ImageButton) rootView.findViewById(R.id.btnAyudaPasoUno);
		pasoUno = (TextView) rootView.findViewById(R.id.pasoUnoLeyenda);
		pasoUno2 = (TextView) rootView.findViewById(R.id.pasoUnoLeyenda2);
		btnAyudaPasoUno.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RecomendacionFragment fragment = new RecomendacionFragment();
				((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
			}
		});

		btnAyudaPasoDos = (ImageButton) rootView.findViewById(R.id.btnAyudaPasoDos);
		pasoDos = (TextView) rootView.findViewById(R.id.pasoDosLeyenda);
		pasoDos2 = (TextView) rootView.findViewById(R.id.pasoDosLeyenda2);

		btnAyudaPasoDos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EvacuacionFragment fragment = new EvacuacionFragment();
				((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
			}
		});

//		if(datosAplicacion.getRiesgoActual().getId().equals("2")){
//			btnAyudaPasoDos.setEnabled(false);
//		}

		tituloSimulacro.setTypeface(fontRegular);
		leyendaSimulacro.setTypeface(fontRegular);

		//btnAyudaPasoUno.setTypeface(fontAwesome);
		pasoUno.setTypeface(fontRegular);
		pasoUno2.setTypeface(fontRegular);

		//btnAyudaPasoDos.setTypeface(fontAwesome);
		pasoDos.setTypeface(fontRegular);
		pasoDos2.setTypeface(fontRegular);

		iniciarSimulacro  = (Button) rootView.findViewById(R.id.iniciarSimulacro);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		if (sharedPrefs.getBoolean(Constantes.PREF_SIMULACRO_INICIADO, false)) {
			iniciarSimulacro.setText("Finalizar el simulacro");
			iniciarSimulacro.setBackgroundResource(R.drawable.button_rojo);
			iniciarSimulacro.setEnabled(true);
		}else{

		}


		iniciarSimulacro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Location location = VariablesUtil.getInstance().retornarUbicacion1(getActivity().getApplicationContext(), null);
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

				if (!sharedPrefs.getBoolean(Constantes.PREF_SIMULACRO_INICIADO, false)) {

					tiempoTranscurrido.setText("00:00");
					distanciaAprox.setText("0");
					SharedPreferences.Editor editor = sharedPrefs.edit();
					editor.putLong(PREF_HORA_INICIO_SIMULACRO, (new Date()).getTime());
					editor.putString(Constantes.PREF_LATITUD_INICIAL, String.valueOf(location.getLatitude()));
					editor.putString(Constantes.PREF_LONGITUD_INICIAL, String.valueOf(location.getLongitude()));
					editor.putBoolean(Constantes.PREF_SIMULACRO_INICIADO, true);
					editor.apply();
					editor.commit();
					iniciarSimulacro.setText("Finalizar el simulacro");
					iniciarSimulacro.setBackgroundResource(R.drawable.button_rojo);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());
					alertDialogBuilder.setTitle("Simulacro iniciado")
							.setMessage("Los datos iniciales están registrados, vuelva a esta opción cuando el simulacro haya finalizado")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
//									if(datosAplicacion.getRiesgoActual().getId().equals("1")) {
										EvacuacionFragment fragment = new EvacuacionFragment();
										((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
//									}
								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				} else {
					SharedPreferences.Editor editor = sharedPrefs.edit();
					editor.putLong(Constantes.PREF_HORA_FINAL_SIMULACRO, (new Date()).getTime());
					editor.putString(Constantes.PREF_LATITUD_FINAL, String.valueOf(location.getLatitude()));
					editor.putString(Constantes.PREF_LONGITUD_FINAL, String.valueOf(location.getLongitude()));
					editor.putBoolean(Constantes.PREF_SIMULACRO_INICIADO, false);
					editor.apply();
					editor.commit();
					iniciarSimulacro.setText("Iniciar el simulacro");
//					iniciarSimulacro.setBackgroundColor(Color.parseColor("#33A948"));
					iniciarSimulacro.setBackgroundResource(R.drawable.button_compartir);

					long diff = sharedPrefs.getLong(Constantes.PREF_HORA_FINAL_SIMULACRO,0) - sharedPrefs.getLong(PREF_HORA_INICIO_SIMULACRO,0);

					long diffSeconds = diff / 1000 % 60;
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);

					String minutos = String.valueOf(diffMinutes);
					String hor = String.valueOf(diffHours);
					if(minutos.length() == 1){
						minutos = "0" + minutos;
					}
					if(hor.length() == 1){
						hor = "0" + hor;
					}


					tiempoTranscurrido.setText( hor+ ":" + minutos);

					Double distancia = VariablesUtil.calcularDistancia(Double.valueOf(sharedPrefs.getString(Constantes.PREF_LATITUD_INICIAL, "0.00")),
							Double.valueOf(sharedPrefs.getString(Constantes.PREF_LONGITUD_INICIAL,"0.00")),
							Double.valueOf(sharedPrefs.getString(Constantes.PREF_LATITUD_FINAL,"0.00")),
							Double.valueOf(sharedPrefs.getString(Constantes.PREF_LONGITUD_FINAL,"0.00")));

					if(distancia.equals(0)) {
						distanciaAprox.setText("0.0");
					}else {
						distancia = distancia * 1000;
						distanciaAprox.setText(String.valueOf(distancia.intValue()));
					}
				}
			}
		});



		leyendaTiempo = (TextView) rootView.findViewById(R.id.leyendaTiempo);
		tiempoTranscurrido = (TextView) rootView.findViewById(R.id.tiempoTranscurrido);
		leyendaDistancia = (TextView) rootView.findViewById(R.id.leyendaDistancia);
		distanciaAprox = (TextView) rootView.findViewById(R.id.distanciaAprox);
		metros = (TextView) rootView.findViewById(R.id.distancia);
		horas = (TextView) rootView.findViewById(R.id.horas);
		horas.setTypeface(fontRegular);
		leyendaTiempo.setTypeface(fontRegular);
		leyendaDistancia.setTypeface(fontRegular);

		tiempoTranscurrido.setTypeface(fontLight);
		distanciaAprox.setTypeface(fontLight);
		metros.setTypeface(fontLight);
		distanciaAprox.setTypeface(fontLight);

		verificarMochila();


		if(datosAplicacion.getDetalleAlertaFragment() != null) {
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

		return rootView;
	}

	private boolean verificarMochila() {
		//Verificacion de la mochila
		ElementoEmergenciaDataSource elementoEmergenciaDataSource = new ElementoEmergenciaDataSource(getActivity().getApplicationContext());
		elementoEmergenciaDataSource.open();

		if(elementoEmergenciaDataSource.getElementoByIdRiesgoandCheck(datosAplicacion.getRiesgoActual().getId()).size() == 0){
			//Mochila completa
			checkBoxDos.setEnabled(true);
			//iniciarSimulacro.setEnabled(true);
			checkBoxUno.setChecked(true);
			elementoEmergenciaDataSource.close();
			return true;
		}else{
			//Mochila incompleta
			checkBoxDos.setEnabled(false);
			iniciarSimulacro.setEnabled(false);
			elementoEmergenciaDataSource.close();
			return false;
		}

	}
}
