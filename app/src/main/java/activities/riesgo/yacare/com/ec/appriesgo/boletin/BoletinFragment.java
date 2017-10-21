package activities.riesgo.yacare.com.ec.appriesgo.boletin;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;


import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.BoletinArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerOficiosAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.BoletinDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Boletin;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class BoletinFragment extends Fragment{

	private ListView listView;
	private BoletinArrayAdapter boletinArrayAdapter;

	private Button btnRegresar;

	private FrameLayout detalleBoletin;

	private FrameLayout listaBoletin;

	private WebView webView;
	private View rootView;
	private boolean mostrarLista = true;

	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_boletin, container, false);

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		if(getActivity().getActionBar() != null) {
			if(datosAplicacion.getRiesgoActual().getId().equals("2")) {
				getActivity().getActionBar().setTitle("INFORMES");
			}else{
				getActivity().getActionBar().setTitle("BOLETINES");
			}
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		listView = (ListView) rootView.findViewById(android.R.id.list);

		detalleBoletin = (FrameLayout) rootView.findViewById(R.id.detalleBoletin);
		listaBoletin = (FrameLayout) rootView.findViewById(R.id.listaBoletin);

		btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarBoletin);
		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mostrarLista) {
					InicioFragment fragment = new InicioFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				} else {
					mostrarLista = true;
					listaBoletin.setVisibility(View.VISIBLE);
					detalleBoletin.setVisibility(View.GONE);
					BoletinDataSource boletinDataSource = new BoletinDataSource(getActivity().getApplicationContext());
					boletinDataSource.open();

					boletinArrayAdapter = new BoletinArrayAdapter(BoletinFragment.this.getActivity(), boletinDataSource.getBoletinByRiesgo(datosAplicacion.getRiesgoActual().getId()), BoletinFragment.this);
					listView.setAdapter(boletinArrayAdapter);

					boletinDataSource.close();
				}

			}
		});

		BoletinDataSource boletinDataSource = new BoletinDataSource(getActivity().getApplicationContext());
		boletinDataSource.open();

		boletinArrayAdapter = new BoletinArrayAdapter(getActivity(), boletinDataSource.getBoletinByRiesgo(datosAplicacion.getRiesgoActual().getId()), this);
		listView.setAdapter(boletinArrayAdapter);

		boletinDataSource.close();

		//Buscar nuevos boletines
		ObtenerOficiosAsyncTask obtenerOficiosAsyncTask = new ObtenerOficiosAsyncTask(getActivity().getApplicationContext(), BoletinFragment.this);
		obtenerOficiosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		return rootView;
	}

	public void verDetalleBoletin(Boletin boletin){
		if(mostrarLista){
			mostrarLista = false;
			listaBoletin.setVisibility(View.GONE);
			detalleBoletin.setVisibility(View.VISIBLE);
			webView = (WebView) rootView.findViewById(R.id.webView);
			webView.setWebViewClient(new WebViewClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(boletin.getUrl());
		}
	}

	public void verificarBoletines(Boolean actualizar) {
		if(actualizar && listaBoletin.getVisibility() == View.VISIBLE){
			BoletinDataSource boletinDataSource = new BoletinDataSource(getActivity().getApplicationContext());
			boletinDataSource.open();

			boletinArrayAdapter = new BoletinArrayAdapter(getActivity(), boletinDataSource.getBoletinByRiesgo(datosAplicacion.getRiesgoActual().getId()), this);
			listView.setAdapter(boletinArrayAdapter);

			boletinDataSource.close();
		}
	}
}
