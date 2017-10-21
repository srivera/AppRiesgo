package activities.riesgo.yacare.com.ec.appriesgo.verificacion;


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


public class MapaNinoFragment extends Fragment{

	private WebView webView;

	private View rootView;

	private Button btnInundacion;

	private Button btnMovimiento;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_mapa_nino, container, false);

		DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		btnInundacion = (Button) rootView.findViewById(R.id.btnInundacion);
		btnInundacion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.setWebViewClient(new WebViewClient());
				webView.getSettings().setBuiltInZoomControls(true);
				webView.getSettings().setDisplayZoomControls(true);
				webView.getSettings().setSupportZoom(true);
				webView.loadUrl("file:///android_asset/" + "mapafn.html");
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
		});

		btnMovimiento = (Button) rootView.findViewById(R.id.btnMovimientos);
		btnMovimiento.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.setWebViewClient(new WebViewClient());
				webView.getSettings().setBuiltInZoomControls(true);
				webView.getSettings().setDisplayZoomControls(true);
				webView.getSettings().setSupportZoom(true);
				webView.loadUrl("file:///android_asset/" + "mapafn2.html");
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
		});


		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("MAPAS");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}
		webView = (WebView) rootView.findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.loadUrl("file:///android_asset/" + "mapafn.html");
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
