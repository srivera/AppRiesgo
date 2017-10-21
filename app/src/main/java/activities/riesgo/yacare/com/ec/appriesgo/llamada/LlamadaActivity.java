package activities.riesgo.yacare.com.ec.appriesgo.llamada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.MenuArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;

public class LlamadaActivity extends Activity {

	private ListView listView;
	private ArrayList<String> opciones = new ArrayList<String>();
	private MenuArrayAdapter menuArrayAdapter;

	private DatosAplicacion datosAplicacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		datosAplicacion = (DatosAplicacion)getApplicationContext();

		listView = (ListView) findViewById(android.R.id.list);

		ImageButton cerrar = (ImageButton) findViewById(R.id.cerrar);
		cerrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		opciones.add("Enviar tu ubicación;2");
		opciones.add("Llamar 911;3");

		menuArrayAdapter = new MenuArrayAdapter(this, opciones);
		listView.setAdapter(menuArrayAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == 0) {
					try {
						Intent i = new Intent("INICIO");
						i.putExtra("msg", "INICIO");
						i.setAction("INICIO");
						getApplicationContext().sendBroadcast(i);

					} catch (Exception e) {
						System.out.print(e);
					}
//					datosAplicacion.getPrincipalActivity().cargarInicio();
					finish();
//				} else if (position == 1) {
//					RecomendacionFragment fragment = new RecomendacionFragment();
//					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
//				} else if (position == 2) {
//					PuntosSegurosFragment fragment = new PuntosSegurosFragment();
//					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
//				} else if (position == 3) {
//					BoletinFragment fragment = new BoletinFragment();
//					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
//				} else if (position == 4) {
//					InformacionOficialFragment fragment = new InformacionOficialFragment();
//					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	}

}
