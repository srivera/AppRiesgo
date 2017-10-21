package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.InicioArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar.Opciones;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class InicioFragment extends BaseContainerFragment{

	private ListView listView;
	private ArrayList<Opciones> opciones;
	private InicioArrayAdapter inicioArrayAdapter;
	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_inicio, container, false);
		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
		datosAplicacion.setInicioFragment(this);

		listView = (ListView) rootView.findViewById(android.R.id.list);

//		if(getActivity().getActionBar() != null) {
//			String nombreRiesgo = datosAplicacion.getRiesgoActual().getNombreRiesgo();
//			if(nombreRiesgo.split(" ").length > 1) {
//				getActivity().getActionBar().setTitle(nombreRiesgo.substring(0, nombreRiesgo.indexOf(" ")));
//				getActivity().getActionBar().setSubtitle(nombreRiesgo.substring(nombreRiesgo.indexOf(" "), nombreRiesgo.length()));
//			}else{
//				getActivity().getActionBar().setTitle(nombreRiesgo);
//				getActivity().getActionBar().setSubtitle("");
//			}
//		}

		if (getActivity().getActionBar() != null) {
			String nombreRiesgo = datosAplicacion.getRiesgoActual().getNombreRiesgo();
			if(datosAplicacion.getRiesgoActual().getId().equals("4")) {
				//Tsunami
				getActivity().getActionBar().setTitle(nombreRiesgo);
				getActivity().getActionBar().setSubtitle(null);
				getActivity().getActionBar().setIcon(R.drawable.tsublanco);
			}else if(datosAplicacion.getRiesgoActual().getId().equals("5")) {
				getActivity().getActionBar().setTitle("ECUADOR MÁS SEGURO");
				getActivity().getActionBar().setSubtitle(null);
				getActivity().getActionBar().setIcon(R.drawable.maleta);
			}else{
				getActivity().getActionBar().setTitle(nombreRiesgo.substring(0, nombreRiesgo.indexOf(" ")));
				getActivity().getActionBar().setSubtitle(nombreRiesgo.substring(nombreRiesgo.indexOf(" "), nombreRiesgo.length()));
				if (datosAplicacion.getRiesgoActual().getId().equals("3")) {
					getActivity().getActionBar().setIcon(R.drawable.fenomenonino);
				} else{
					getActivity().getActionBar().setIcon(R.drawable.volcan);
				}
			}
		}

		if(!datosAplicacion.getRiesgoActual().getId().equals("5")) {
			opciones = new ArrayList<Opciones>();
			Opciones opcion = new Opciones();
			opcion.setId("1");
			opcion.setNombre("Información Útil");
			opcion.setLeyenda("Consejos, tips, ayuda y más");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("2");
			opcion.setNombre("Mochila de Emergencia");
			opcion.setLeyenda("Los artículos que debe contener");
			opciones.add(opcion);

			if (!datosAplicacion.getRiesgoActual().getId().equals("3")) {
				opcion = new Opciones();
				opcion.setId("3");
				opcion.setNombre("Zonas Seguras");
				opcion.setLeyenda("Sitios seguros y albergues");
				opciones.add(opcion);
			}

			if (datosAplicacion.getRiesgoActual().getId().equals("1")) {
				opcion = new Opciones();
				opcion.setId("8");
				opcion.setNombre("Parque Nacional Cotopaxi");
				opcion.setLeyenda("Guía de Visita");
				opciones.add(opcion);
			}


			opcion = new Opciones();
			opcion.setId("4");
			if (datosAplicacion.getRiesgoActual().getId().equals("2")) {
				opcion.setNombre("Informes");
			} else {
				opcion.setNombre("Boletines");
			}
			opcion.setLeyenda("Manténgase informado");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("5");
			opcion.setNombre("Información Oficial");
			opcion.setLeyenda("Consulte las fuentes oficiales");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("6");
			opcion.setNombre("Configuración");
			opcion.setLeyenda("Datos personales");
			opciones.add(opcion);


			opcion = new Opciones();
			opcion.setId("7");
			opcion.setNombre("Acerca de");
			opcion.setLeyenda("Conozca más sobre la app");
			opciones.add(opcion);
		}else{
			opciones = new ArrayList<Opciones>();
			Opciones opcion = new Opciones();
			opcion.setId("9");
			opcion.setNombre("ECU 911");
			opcion.setLeyenda("Servicio Integrado de Seguridad");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("10");
			opcion.setNombre("UPC");
			opcion.setLeyenda("Unidad de Policía Comunitaria");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("11");
			opcion.setNombre("Botón Turismo Seguro");
			opcion.setLeyenda("");
			opciones.add(opcion);


			opcion = new Opciones();
			opcion.setId("12");
			opcion.setNombre("Sitios Públicos Seguros");
			opcion.setLeyenda("");
			opciones.add(opcion);


			opcion = new Opciones();
			opcion.setId("13");
			opcion.setNombre("Transporte Seguro");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("14");
			opcion.setNombre("Salud");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("15");
			opcion.setNombre("Información y Facilitación");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("16");
			opcion.setNombre("Consulado Virtual");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("17");
			opcion.setNombre("Fiscalía General");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("18");
			opcion.setNombre("Cajeros Automáticos");
			opcion.setLeyenda("");
			opciones.add(opcion);

			opcion = new Opciones();
			opcion.setId("19");
			opcion.setNombre("¡Descargue las aplicaciones!");
			opcion.setLeyenda("");
			opciones.add(opcion);
		}


		inicioArrayAdapter = new InicioArrayAdapter(getActivity().getApplicationContext(), opciones, InicioFragment.this);
		listView.setAdapter(inicioArrayAdapter);
		listView.setClickable(false);

		return rootView;
	}

	public void actualizarColorAlerta(){
		if(getActivity() != null && getActivity().getApplicationContext() != null) {
			inicioArrayAdapter = new InicioArrayAdapter(getActivity().getApplicationContext(), opciones, InicioFragment.this);
			listView.setAdapter(inicioArrayAdapter);
			listView.setClickable(false);
		}
	}

}
