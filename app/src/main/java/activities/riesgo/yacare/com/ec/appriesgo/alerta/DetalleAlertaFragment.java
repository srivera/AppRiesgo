package activities.riesgo.yacare.com.ec.appriesgo.alerta;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.TipoAlertaArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class DetalleAlertaFragment extends BaseContainerFragment {


	private FrameLayout detalleListaTipoAlerta;

	private FrameLayout detalleTipoAlerta;

	private LinearLayout detalleWeb;

	private ListView listView;
	private TipoAlertaArrayAdapter tipoAlertaArrayAdapter;

	private TipoAlerta tipoAlertaMostrar;

	private DatosAplicacion datosAplicacion;

	private WebView webView;

	private ImageButton btnVerLibro;

	View rootView;

	private static final String HTML_MIME_TYPE = "text/html";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_detalle_alerta, container, false);

		if(getActivity().getActionBar() != null) {
			DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
			if(!datosAplicacion.getRiesgoActual().getId().equals("5")) {
				getActivity().getActionBar().setTitle("INFORMACIÓN UTIL");
				getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
			}
		}

		Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");
		Typeface fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");


		detalleListaTipoAlerta = (FrameLayout) rootView.findViewById(R.id.detalleListaTipoAlerta);
		detalleTipoAlerta = (FrameLayout) rootView.findViewById(R.id.detalleTipoAlerta);
		detalleWeb = (LinearLayout) rootView.findViewById(R.id.detalleWeb);

		listView = (ListView) rootView.findViewById(android.R.id.list);

		btnVerLibro = (ImageButton) rootView.findViewById(R.id.btnVerLibro);
		btnVerLibro.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse("http://archivo.seguridad.gob.ec:8080/owncloud/index.php/s/FHNEZ8DAnlI8JcF"), HTML_MIME_TYPE);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(i);
			}
		});

		datosAplicacion = (DatosAplicacion)getActivity().getApplicationContext();

		tipoAlertaMostrar = datosAplicacion.getRiesgoActual().getTipoAlerta();

		datosAplicacion.setDetalleAlertaFragment(DetalleAlertaFragment.this);

		//Alerta actual
//		TextView tituloAlerta = (TextView) rootView.findViewById(R.id.tituloAlerta);
//		TextView leyendaAlerta = (TextView) rootView.findViewById(R.id.subtituloAlerta);
//		tituloAlerta.setText(datosAplicacion.getRiesgoActual().getTipoAlerta().getNombre());
//		tituloAlerta.setTypeface(fontBold);
//		leyendaAlerta.setTypeface(fontLight);
//		leyendaAlerta.setText(datosAplicacion.getRiesgoActual().getTipoAlerta().getLeyendaCorta());
//
//		tituloAlerta.setBackgroundColor(Color.parseColor(datosAplicacion.getRiesgoActual().getTipoAlerta().getCodigoColor()));
//		leyendaAlerta.setBackgroundColor(Color.parseColor(datosAplicacion.getRiesgoActual().getTipoAlerta().getCodigoColor()));
//		tituloAlerta.setTextColor(Color.parseColor(datosAplicacion.getRiesgoActual().getTipoAlerta().getCodigoColorLetra()));
//		leyendaAlerta.setTextColor(Color.parseColor(datosAplicacion.getRiesgoActual().getTipoAlerta().getCodigoColorLetra()));
//
//		ImageView imagenAlerta = (ImageView) rootView.findViewById(R.id.imagenAlerta);
//		imagenAlerta.setBackgroundColor(Color.parseColor(datosAplicacion.getRiesgoActual().getTipoAlerta().getCodigoColor()));


		mostrarDetalle();


		Button regresarDetalleAlerta  = (Button) rootView.findViewById(R.id.btnRegresarDetalleAlertal);
		regresarDetalleAlerta.setTypeface(fontBold);
		regresarDetalleAlerta.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//getActivity().getSupportFragmentManager().beginTransaction().remove(DetalleAlertaFragment.this).commit();
				InicioFragment fragment = new InicioFragment();
				FragmentManager manager = getActivity().getSupportFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.remove(DetalleAlertaFragment.this);
				transaction.commit();

				TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getActivity().getApplicationContext());
				tipoAlertaDataSource.open();
				TipoAlerta tipoAlerta1 = tipoAlertaDataSource.getTipoAlertaByCodigo(datosAplicacion.getRiesgoActual().getAlertaActual(), datosAplicacion.getRiesgoActual().getId());
				tipoAlertaDataSource.close();

				datosAplicacion.getPrincipalActivity().btnTipoALerta.setEnabled(true);
				datosAplicacion.getPrincipalActivity().btnTipoALerta.setBackgroundColor(Color.parseColor(tipoAlerta1.getCodigoColorOscuro()));
				datosAplicacion.getPrincipalActivity().btnTipoALerta.setText(">");
			}
		});

		Button compartir  = (Button) rootView.findViewById(R.id.btnComparteAlerta);
		compartir.setTypeface(fontBold);
		compartir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("image/jpeg");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, tipoAlertaMostrar.getNombre());
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tipoAlertaMostrar.getNombre() + " " + tipoAlertaMostrar.getLeyendaLarga());

				Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName()
						+ "/drawable/" + tipoAlertaMostrar.getIcono().replace(".png", ""));

				sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
				startActivity(Intent.createChooser(sharingIntent, "Compartir vía"));
			}
		});

		Button verTodas = (Button) rootView.findViewById(R.id.btnComparteVerTodasAlerta);
		verTodas.setTypeface(fontLight);
		verTodas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(getActivity().getApplicationContext());
				tipoAlertaDataSource.open();

				tipoAlertaArrayAdapter = new TipoAlertaArrayAdapter(getActivity().getApplicationContext(), tipoAlertaDataSource.getByRiesgo(datosAplicacion.getRiesgoActual().getId()), DetalleAlertaFragment.this);
				listView.setAdapter(tipoAlertaArrayAdapter);

				tipoAlertaDataSource.close();
				detalleListaTipoAlerta.setVisibility(View.VISIBLE);
				detalleTipoAlerta.setVisibility(View.GONE);
			}
		});



		return rootView;
	}

	public void mostrarDetalle() {

		if(!datosAplicacion.getRiesgoActual().getId().equals("5")) {
			Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
			Typeface fontBlack = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Black.ttf");


			ImageView imagenDetalleAlerta = (ImageView) rootView.findViewById(R.id.imagendetallealerta);
			Resources res = getResources();
			int resID = res.getIdentifier(tipoAlertaMostrar.getIcono().replace(".png", ""), "drawable", getActivity().getPackageName());
			Drawable drawable = res.getDrawable(resID);
			imagenDetalleAlerta.setImageDrawable(drawable);

			TextView pregunta = (TextView) rootView.findViewById(R.id.txtpreguntaalerta);
			pregunta.setText(tipoAlertaMostrar.getPregunta());
			pregunta.setTypeface(fontBlack);

			TextView leyendaLarga = (TextView) rootView.findViewById(R.id.txtleyendalargaalerta);
			leyendaLarga.setText(tipoAlertaMostrar.getLeyendaLarga());
			pregunta.setTypeface(fontRegular);

			//		WebView view = (WebView) rootView.findViewById(R.id.textContent);
			//		String text;
			//		text = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><html><body><p align=\"justify\">";
			//		text+=tipoAlertaMostrar.getLeyendaLarga();
			//		text+= "</p></body></html>";
			//		view.loadData(text, "text/html", "UTF-8");

			detalleListaTipoAlerta.setVisibility(View.GONE);
			detalleTipoAlerta.setVisibility(View.VISIBLE);
			detalleWeb.setVisibility(View.GONE);
		}else{
			detalleListaTipoAlerta.setVisibility(View.GONE);
			detalleTipoAlerta.setVisibility(View.GONE);
			detalleWeb.setVisibility(View.VISIBLE);
			webView = (WebView) rootView.findViewById(R.id.webView);

			WebSettings settings = webView.getSettings();

			settings.setDomStorageEnabled(true);
//
//			settings.setJavaScriptEnabled(true);
//			settings.setBuiltInZoomControls(true);
//			settings.setLoadWithOverviewMode(true);
//			settings.setUseWideViewPort(true);
//			settings.setDatabaseEnabled(true);
			webView.setWebViewClient(new WebViewClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl("file:///android_asset/" + "turasis.html");



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

	}

	public TipoAlerta getTipoAlertaMostrar() {
		return tipoAlertaMostrar;
	}

	public void setTipoAlertaMostrar(TipoAlerta tipoAlertaMostrar) {
		this.tipoAlertaMostrar = tipoAlertaMostrar;
	}
}
