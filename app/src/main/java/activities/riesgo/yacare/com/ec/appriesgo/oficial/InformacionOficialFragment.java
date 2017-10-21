package activities.riesgo.yacare.com.ec.appriesgo.oficial;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.InfoOficialArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.LinkDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Link;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InicioFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class InformacionOficialFragment extends Fragment{

	private ImageButton btnFacebook;
	private ImageButton btnTwitter;
	private ImageButton btnYouTube;


	private FrameLayout detalleInfoOficial;

	private FrameLayout listaInfoOficial;

	private boolean mostrarLista = true;

	private WebView webView;

	private boolean twitterCargado = false;
	private View rootView;

	private ListView listView;

	private ArrayList<Link> links;

	private Link linkFacebook;
	private Link linkYouTube;
	private Link linkTwitter;

	private DatosAplicacion datosAplicacion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_informacion_oficial, container, false);

		datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();

		listView = (ListView) rootView.findViewById(android.R.id.list);

		if(getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("INFORMACIÓN OFICIAL");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}

		Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");

		detalleInfoOficial = (FrameLayout) rootView.findViewById(R.id.detalleInfoOficial);
		listaInfoOficial = (FrameLayout) rootView.findViewById(R.id.listaInfoOficial);
		Button btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarInformacionOficial);


		TextView txtRedesSociales = (TextView) rootView.findViewById(R.id.txtRedesSociales);
		txtRedesSociales.setTypeface(fontBold);

		btnRegresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mostrarLista) {
					InicioFragment fragment = new InicioFragment();
					((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
					if (webView != null) {
						webView.loadUrl("about:blank");
					}
				} else {
					mostrarLista = true;
					listaInfoOficial.setVisibility(View.VISIBLE);
					detalleInfoOficial.setVisibility(View.GONE);
					webView.loadUrl("about:blank");
				}
				if (webView != null) {
					webView.goBack();
				}

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				if (links.get(position).getUrl().equals("www.igepn.edu.ec/")) {
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					i.setDataAndType(Uri.parse(links.get(position).getUrl()), "text/html");
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					datosAplicacion.getPrincipalActivity().startActivity(i);
//				}else {
					mostrarLista = false;
					listaInfoOficial.setVisibility(View.GONE);
					detalleInfoOficial.setVisibility(View.VISIBLE);
					webView = (WebView) rootView.findViewById(R.id.webView);
					webView.setWebViewClient(new WebViewClient());

					webView.getSettings().setJavaScriptEnabled(true);

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
					webView.loadUrl(links.get(position).getUrl());
//				}
			}
		});

		LinkDataSource linkDataSource = new LinkDataSource(getActivity().getApplicationContext());
		linkDataSource.open();

		links = linkDataSource.getLinkByIdRiesgoandTipo(datosAplicacion.getRiesgoActual().getId(), "URL");

		InfoOficialArrayAdapter infoOficialArrayAdapter = new InfoOficialArrayAdapter(getActivity(), links);
		listView.setAdapter(infoOficialArrayAdapter);

		linkFacebook = linkDataSource.getLinkByIdRiesgoandRedSocial(datosAplicacion.getRiesgoActual().getId(), "FAC");

		linkYouTube = linkDataSource.getLinkByIdRiesgoandRedSocial(datosAplicacion.getRiesgoActual().getId(), "YOU");

		linkTwitter = linkDataSource.getLinkByIdRiesgoandRedSocial(datosAplicacion.getRiesgoActual().getId(), "TWI");

		linkDataSource.close();

		//listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, links.size() * 200));


		btnFacebook = (ImageButton) rootView.findViewById(R.id.btnFacebook);
		btnTwitter = (ImageButton) rootView.findViewById(R.id.btnTwitter);
		btnYouTube = (ImageButton) rootView.findViewById(R.id.btnYouTube);

		btnFacebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mostrarLista = false;
				listaInfoOficial.setVisibility(View.GONE);
				detalleInfoOficial.setVisibility(View.VISIBLE);
				webView = (WebView) rootView.findViewById(R.id.webView);
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						//use the param "view", and call getContentHeight in scrollTo
						view.loadUrl("javascript:document.getElementById(\"header\").setAttribute(\"style\",\"display:none;\");");
					}
				});

				webView.getSettings().setJavaScriptEnabled(true);
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
				webView.loadUrl(linkFacebook.getUrl());
			}
		});

		btnTwitter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				twitterCargado = false;
				mostrarLista = false;
				listaInfoOficial.setVisibility(View.GONE);
				detalleInfoOficial.setVisibility(View.VISIBLE);
				webView = (WebView) rootView.findViewById(R.id.webView);
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						view.loadUrl("javascript:document.getElementsByTagName(\"header\")[0].setAttribute(\"style\",\"display:none;\");");
						if (!twitterCargado) {

							twitterCargado = true;
							view.reload();

						} else {
							webView.getSettings().setJavaScriptEnabled(false);
							view.scrollBy(300, 300);
						}

					}
				});
				webView.getSettings().setJavaScriptEnabled(true);
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
				webView.loadUrl(linkTwitter.getUrl());
			}
		});


		btnYouTube.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mostrarLista = false;
				listaInfoOficial.setVisibility(View.GONE);
				detalleInfoOficial.setVisibility(View.VISIBLE);
				webView = (WebView) rootView.findViewById(R.id.webView);
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

				webView.setWebViewClient(new WebViewClient());
				webView.getSettings().setJavaScriptEnabled(true);
				webView.loadUrl(linkYouTube.getUrl());
			}
		});
		return rootView;
	}



}


