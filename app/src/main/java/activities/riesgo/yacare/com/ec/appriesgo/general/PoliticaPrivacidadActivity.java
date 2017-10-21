package activities.riesgo.yacare.com.ec.appriesgo.general;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import activities.riesgo.yacare.com.ec.appriesgo.R;

public class PoliticaPrivacidadActivity extends Activity {

    private Button btnAceptarPolitica;



    private CheckBox checkPolitica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);

        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");

        btnAceptarPolitica = (Button) findViewById(R.id.btnAceptarPolitica);

       checkPolitica = (CheckBox) findViewById(R.id.checkPolitica);
        checkPolitica.setTypeface(fontRegular);
        checkPolitica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAceptarPolitica.setEnabled(true);
                } else {
                    btnAceptarPolitica.setEnabled(false);
                }

            }
        });

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + "notadedescargo.html");

        btnAceptarPolitica.setTypeface(fontRegular);

        btnAceptarPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(PoliticaPrivacidadActivity.this, RegistroEcuActivity.class);
                startActivity(i);
            }
        });
    }


}
