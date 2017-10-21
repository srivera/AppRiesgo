package activities.riesgo.yacare.com.ec.appriesgo.general;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import activities.riesgo.yacare.com.ec.appriesgo.R;


public class ConfirmarRegistroActivity extends Activity {

	private Button btnContinuar;


	private TextView txtTituloConfirmar;
	private TextView txtSubTituloConfirmar;
	private TextView txtSubTituloConfirmar2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirma_registro);

		Typeface fontRegular = Typeface.createFromAsset(getAssets(), "Lato-Regular.ttf");
		Typeface fontLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");
		Typeface fontBold = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");

		btnContinuar = (Button) findViewById(R.id.btnContinuar);

		txtTituloConfirmar  = (TextView) findViewById(R.id.txtTituloConfirmar);
		txtSubTituloConfirmar  = (TextView) findViewById(R.id.txtSubTituloConfirmar);
		txtSubTituloConfirmar2  = (TextView) findViewById(R.id.txtSubTituloConfirmar2);

		txtSubTituloConfirmar2.setTypeface(fontRegular);
		txtTituloConfirmar.setTypeface(fontBold);
		txtSubTituloConfirmar.setTypeface(fontRegular);
		btnContinuar.setTypeface(fontRegular);



		btnContinuar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ConfirmarRegistroActivity.this, MenuActivity.class);
				startActivity(i);


			}
		});


	}


}
