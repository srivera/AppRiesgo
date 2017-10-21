package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.ConsejoArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.ConsejoRiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ConsejoRiesgo;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class InformacionUtilFragment extends  BaseContainerFragment{

	private ListView listView;
	private ConsejoArrayAdapter consejoArrayAdapter;

	private Button btnRegresar;

	private FrameLayout detalleConsejo;

	private FrameLayout listaConsejo;

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

		rootView = inflater.inflate(R.layout.activity_informacion_util, container, false);

		listView = (ListView) rootView.findViewById(android.R.id.list);

		detalleConsejo = (FrameLayout) rootView.findViewById(R.id.detalleConsejo);
		listaConsejo = (FrameLayout) rootView.findViewById(R.id.listaConsejo);

		btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarInformacionUtil);
		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mostrarLista) {
					InicioFragment fragment = new InicioFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				} else {
					mostrarLista = true;
					webView.loadUrl("about:blank");
					listaConsejo.setVisibility(View.VISIBLE);
					detalleConsejo.setVisibility(View.GONE);
				}

			}
		});
		if(getActivity().getActionBar() != null) {
			DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
			getActivity().getActionBar().setTitle("INFORMACIÓN ÚTIL");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}


		ConsejoRiesgoDataSource consejoRiesgoDataSource = new ConsejoRiesgoDataSource(getActivity().getApplicationContext());
		consejoRiesgoDataSource.open();

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
		consejoArrayAdapter = new ConsejoArrayAdapter(getActivity(), consejoRiesgoDataSource.getConsejoByIdRiesgo( datosAplicacion.getRiesgoActual().getId() ), this);
		listView.setAdapter(consejoArrayAdapter);

		consejoRiesgoDataSource.close();

		return rootView;
	}

	public void verDetalleConsejo(ConsejoRiesgo consejoRiesgo){
		if(mostrarLista){
			mostrarLista = false;
			listaConsejo.setVisibility(View.GONE);
			detalleConsejo.setVisibility(View.VISIBLE);
			webView = (WebView) rootView.findViewById(R.id.webView);
			webView.setWebViewClient(new WebViewClient());
			webView.getSettings().setJavaScriptEnabled(true);

			try {
				if(!datosAplicacion.getRiesgoActual().getAlertaActual().equals("BLA")){
					AssetManager assetManager = getActivity().getAssets();
					List<String> mapList = Arrays.asList(assetManager.list(""));

					if(mapList.contains("bla" + consejoRiesgo.getArchivo())){
						webView.loadUrl("file:///android_asset/" + "bla" + consejoRiesgo.getArchivo());
						webView.setOnKeyListener(new View.OnKeyListener() {
							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
									webView.goBack();
									return true;
								}

								return false;
							}
						});
					}else{
						webView.loadUrl("file:///android_asset/" + consejoRiesgo.getArchivo());
						webView.setOnKeyListener(new View.OnKeyListener() {
							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
									webView.goBack();
									return true;
								}

								return false;
							}
						});
					}
				}else{
					webView.loadUrl("file:///android_asset/" + consejoRiesgo.getArchivo());
					webView.setOnKeyListener(new View.OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
								webView.goBack();
								return true;
							}

							return false;
						}
					});
				}

			} catch (IOException e) {
				e.printStackTrace();
			}



		}
	}



}
