package activities.riesgo.yacare.com.ec.appriesgo.inicio;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;

public class PoliticaPrivacidadFragment extends Fragment {

    private Button btnAceptarPolitica;

    private Button btnRegresarPolitica;

    private CheckBox checkPolitica;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_politica_privacidad, container, false);


        Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");

        btnAceptarPolitica = (Button) rootView.findViewById(R.id.btnAceptarPolitica);

        checkPolitica = (CheckBox) rootView.findViewById(R.id.checkPolitica);
        checkPolitica.setTypeface(fontRegular);
        checkPolitica.setChecked(true);
        checkPolitica.setEnabled(false);

        btnRegresarPolitica = (Button) rootView.findViewById(R.id.btnRegresarPolitica);
        btnRegresarPolitica.setVisibility(View.VISIBLE);
        btnRegresarPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioFragment fragment = new InicioFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });

        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + "notadedescargo.html");

        btnAceptarPolitica.setTypeface(fontRegular);
        btnAceptarPolitica.setText("MODIFICAR DATOS");
        btnAceptarPolitica.setEnabled(true);
        btnAceptarPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RegistroEcuFragment fragment = new RegistroEcuFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);
            }
        });

        return rootView;
    }


}
