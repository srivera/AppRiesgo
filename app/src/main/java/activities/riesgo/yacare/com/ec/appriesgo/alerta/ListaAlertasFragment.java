package activities.riesgo.yacare.com.ec.appriesgo.alerta;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.NotificacionArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerNoificacionesAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.NotificacionDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;


public class ListaAlertasFragment extends Fragment{

	private ListView listView;
	private NotificacionArrayAdapter notificacionArrayAdapter;
	private DatosAplicacion datosAplicacion;

	private ArrayList<Notificacion> notificaciones;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_lista_alertas, container, false);

		listView = (ListView) rootView.findViewById(android.R.id.list);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Notificacion notificacion= (Notificacion) parent.getAdapter().getItem(position);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle(notificacion.getTitulo())
						.setMessage(notificacion.getMensaje())
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			}
		});

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		if(getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("ALERTAS");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		NotificacionDataSource notificacionDataSource = new NotificacionDataSource(getActivity().getApplicationContext());
		notificacionDataSource.open();
		notificaciones =  notificacionDataSource.getAllNotificacion();
		cargarRiesgo();
		notificacionArrayAdapter = new NotificacionArrayAdapter(getActivity(), notificaciones);
		listView.setAdapter(notificacionArrayAdapter);

		notificacionDataSource.close();



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

		//Buscar nuevos boletines
		ObtenerNoificacionesAsyncTask obtenerNoificacionesAsyncTask = new ObtenerNoificacionesAsyncTask(getActivity().getApplicationContext(), ListaAlertasFragment.this);
		obtenerNoificacionesAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


		return rootView;
	}

	public void verificarNotificaciones(Boolean actualizar) {
		if(actualizar && listView.getVisibility() == View.VISIBLE){

			NotificacionDataSource notificacionDataSource = new NotificacionDataSource(getActivity().getApplicationContext());
			notificacionDataSource.open();

			notificaciones =  notificacionDataSource.getAllNotificacion();
			cargarRiesgo();

			notificacionArrayAdapter = new NotificacionArrayAdapter(getActivity(), notificaciones);
			listView.setAdapter(notificacionArrayAdapter);

			notificacionDataSource.close();
		}
	}

	private void cargarRiesgo() {
		RiesgoDataSource riesgoDataSource = new RiesgoDataSource(getActivity().getApplicationContext());
		riesgoDataSource.open();
		ArrayList<Riesgo> riesgos = riesgoDataSource.getAll();
		for(Notificacion notificacion: notificaciones){
			for(Riesgo riesgo : riesgos){
				if(notificacion.getIdRiesgo().equals(riesgo.getId())){
					notificacion.setNombreRiesgo(riesgo.getNombreRiesgo());
					break;
				}
			}
		}
	}
}
