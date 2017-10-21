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
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class GuiaVisitaFragment extends Fragment {


    private WebView webView;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_guia_visita, container, false);

        DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();


        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(getArguments().getString("titulo"));
            getActivity().getActionBar().setSubtitle(null);
        }


        Button btnRegresar = (Button) rootView.findViewById(R.id.btnRegresarGuiaVisita);


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioFragment fragment = new InicioFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);

            }
        });

        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        Integer codigo = getArguments().getInt("codigo");
        if(codigo.equals(8)){
            webView.loadUrl("file:///android_asset/" + "parque.html");
        }else  if(codigo.equals(9)) {
            webView.loadUrl("file:///android_asset/" + "tur911.html");
        }else  if(codigo.equals(10)) {
            webView.loadUrl("file:///android_asset/" + "turupc.html");
        }else  if(codigo.equals(11)) {
            webView.loadUrl("file:///android_asset/" + "turboton.html");
        }else  if(codigo.equals(12)) {
            webView.loadUrl("file:///android_asset/" + "tursitio.html");
        }else  if(codigo.equals(13)) {
            webView.loadUrl("file:///android_asset/" + "turtrans.html");
        }else  if(codigo.equals(14)) {
            webView.loadUrl("file:///android_asset/" + "tursalud.html");
        }else  if(codigo.equals(15)) {
            webView.loadUrl("file:///android_asset/" + "turinformacion.html");
        }else  if(codigo.equals(16)) {
            webView.loadUrl("file:///android_asset/" + "turconsulado.html");
        }else  if(codigo.equals(17)) {
            webView.loadUrl("file:///android_asset/" + "turfiscalia.html");
        }else  if(codigo.equals(18)) {
            webView.loadUrl("file:///android_asset/" + "turcajero.html");
        }else  if(codigo.equals(19)) {
            webView.loadUrl("file:///android_asset/" + "turapp.html");
        }

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


