package activities.riesgo.yacare.com.ec.appriesgo.mapas;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import activities.riesgo.yacare.com.ec.appriesgo.R;


public class TextoActivity extends FragmentActivity {

	private ListView listView;
	private ArrayAdapter<String> rutas;
	private Button regresar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_texto);
		
		listView = (ListView) findViewById(android.R.id.list);
		
		rutas = new ArrayAdapter<String>(this,  R.layout.message);
		
//		for(String texto : DirectionsJSONParser.direcciones){
//			rutas.add(Html.fromHtml(texto).toString());
//		}
//		listView.setAdapter(rutas);

		regresar= (Button) findViewById(R.id.btnRegresarRutaTexto);
		regresar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	
		
		}		
	}
	
