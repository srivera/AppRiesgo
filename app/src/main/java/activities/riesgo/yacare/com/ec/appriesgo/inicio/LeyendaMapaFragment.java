package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.evacuacion.EvacuacionContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.evacuacion.EvacuacionFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;
import activities.riesgo.yacare.com.ec.appriesgo.verificacion.MapaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.verificacion.VerificarContainerFragment;


public class LeyendaMapaFragment extends Fragment{

	private WebView webView;

	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_leyenda_mapa, container, false);

		DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();


		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("LEYENDA DEL MAPA");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}


		Button btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarLeyendaMapa);


		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getParentFragment() instanceof VerificarContainerFragment){
					MapaFragment fragment = new MapaFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}else if(getParentFragment() instanceof InicioContainerFragment){
					MapaPuntosFragment fragment = new MapaPuntosFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}else if(getParentFragment() instanceof EvacuacionContainerFragment){
					EvacuacionFragment fragment = new EvacuacionFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
				}

			}
		});

		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/" + "leyendas.html");
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
		return rootView;
	}


}
