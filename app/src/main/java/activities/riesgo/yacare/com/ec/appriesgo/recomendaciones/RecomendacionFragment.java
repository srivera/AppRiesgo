package activities.riesgo.yacare.com.ec.appriesgo.recomendaciones;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.ElementoArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ElementoEmergenciaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ElementoEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.simulacro.SimulacroFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class RecomendacionFragment extends Fragment{

	private ListView listView;
	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_recomendacion, container, false);
		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		if(getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("MOCHILA EMERGENCIA");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Light.ttf");
		Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

		listView = (ListView) rootView.findViewById(android.R.id.list);

		TextView titulo = (TextView) rootView.findViewById(R.id.titulomochila);
//		TextView subtitulo1 = (TextView) rootView.findViewById(R.id.subtitulomochila1);
//		TextView subtitulo2 = (TextView) rootView.findViewById(R.id.subtitulomochila2);
		titulo.setTypeface(fontRegular);
//		subtitulo1.setTypeface(fontRegular);
//		subtitulo2.setTypeface(fontRegular);

		Button btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarRecomendacion);
		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getParentFragment() instanceof SimulacroContainerFragment) {
					SimulacroFragment fragment = new SimulacroFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				} else {
					InicioFragment fragment = new InicioFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}
			}
		});

		ElementoEmergenciaDataSource elementoEmergenciaDataSource = new ElementoEmergenciaDataSource(getActivity().getApplicationContext());
		elementoEmergenciaDataSource.open();


		ArrayList<ElementoEmergencia> elementos = elementoEmergenciaDataSource.getElementoByIdRiesgo(datosAplicacion.getRiesgoActual().getId());

		ElementoArrayAdapter elementoArrayAdapter = new ElementoArrayAdapter(getActivity(), elementos, this);
		listView.setAdapter(elementoArrayAdapter);

//		Integer numeroFilas = elementos.size() / 3;
//		if ((elementos.size() % 3) > 0){
//			numeroFilas++;
//		}
//
//		int indiceElemento = 0;
//
//		for(int i = 0; i < numeroFilas; i++){
//			TableRow tr = new TableRow(getActivity());
//			tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
//			for(int j = 0; j < 3; j++) {
//
//				if(indiceElemento < elementos.size() ) {
//					LinearLayout layout = new LinearLayout(getActivity());
//					layout.setOrientation(LinearLayout.VERTICAL);
//					LinearLayout.LayoutParams childParam1 = new TableRow.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
//					childParam1.setMargins(3, 3, 3, 3);
//					childParam1.weight = 0.33f;
//					layout.setLayoutParams(childParam1);
//					layout.setPadding(10, 25, 10, 25);
//					layout.setBackgroundColor(Color.parseColor("#F4F4F2"));
//
//					ImageView imagen = new ImageView(getActivity());
//					Resources res = getActivity().getResources();
//					int resID = res.getIdentifier(elementos.get(indiceElemento).getIcono().replace(".png", ""), "drawable", getActivity().getPackageName());
//					Drawable drawable = res.getDrawable(resID);
//					imagen.setImageDrawable(drawable);
//					imagen.setColorFilter(Color.parseColor("#36A0CE"));
//
//					TextView leyenda = new TextView(getActivity());
//					leyenda.setText(elementos.get(indiceElemento).getNombre());
//					leyenda.setTypeface(fontLight);
//					leyenda.setTextSize(14);
//
//					indiceElemento++;
//					layout.addView(imagen);
//					layout.addView(leyenda);
//					leyenda.setGravity(Gravity.CENTER_HORIZONTAL);
//					tr.addView(layout);
//				}
//			}
//			tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//		}

		elementoEmergenciaDataSource.close();

		return rootView;
	}

	public void verificarMochila(){
		ElementoEmergenciaDataSource elementoEmergenciaDataSource = new ElementoEmergenciaDataSource(getActivity().getApplicationContext());
		elementoEmergenciaDataSource.open();

		if(elementoEmergenciaDataSource.getElementoByIdRiesgoandCheck(datosAplicacion.getRiesgoActual().getId()).size() == 0){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
			alertDialogBuilder.setTitle("INFORMACIÓN")
					.setMessage("Su mochila está completa. Puede utilizar la opción Simulacro.")
					.setCancelable(false)
					.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
		elementoEmergenciaDataSource.close();
	}

}
