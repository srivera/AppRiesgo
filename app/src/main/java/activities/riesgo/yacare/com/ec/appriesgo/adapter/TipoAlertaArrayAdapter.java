package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.alerta.DetalleAlertaFragment;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;


public class TipoAlertaArrayAdapter extends ArrayAdapter<TipoAlerta> {
		private final Context context;
		private final ArrayList<TipoAlerta> values;
		private DetalleAlertaFragment detalleAlertaFragment;

	  public TipoAlertaArrayAdapter(Context context, ArrayList<TipoAlerta> values, DetalleAlertaFragment detalleAlertaFragment) {
	    super(context, R.layout.tipo_alerta_item, values);
	    this.context = context;
	    this.values = values;
		  this.detalleAlertaFragment = detalleAlertaFragment;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.tipo_alerta_item, parent, false);
	    final TipoAlerta tipoAlerta = (TipoAlerta) values.get(position);
	    TextView textView = (TextView) rowView.findViewById(R.id.nombreLeyendaTipoAlerta);
	    textView.setText(tipoAlerta.getPregunta());
		  textView.setTag(tipoAlerta);
		  Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Black.ttf");
		  textView.setTypeface(fontBlack);
		  ImageView imagen = (ImageView) rowView.findViewById(R.id.imagenTipoAlerta);
		  Resources res = context.getResources();
		  int resID = res.getIdentifier(tipoAlerta.getIcono().replace(".png", "") , "drawable", context.getPackageName());
		  Drawable drawable = res.getDrawable(resID );
		  imagen.setImageDrawable(drawable );


		  tipoAlerta.setVerTipoAlerta((Button) rowView.findViewById(R.id.btnVerTipoALerta));
		  tipoAlerta.getVerTipoAlerta().setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  detalleAlertaFragment.setTipoAlertaMostrar(tipoAlerta);
				  detalleAlertaFragment.mostrarDetalle();
			  }
		  });


	    return rowView;
	  }
	  
	  
	  

}
