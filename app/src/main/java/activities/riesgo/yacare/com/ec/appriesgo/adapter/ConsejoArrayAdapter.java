package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
import activities.riesgo.yacare.com.ec.appriesgo.dto.ConsejoRiesgo;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.InformacionUtilFragment;


public class ConsejoArrayAdapter extends ArrayAdapter<ConsejoRiesgo> {
		private final Context context;
		private final ArrayList<ConsejoRiesgo> values;
		private InformacionUtilFragment informacionUtilFragment;

	  public ConsejoArrayAdapter(Context context, ArrayList<ConsejoRiesgo> values, InformacionUtilFragment informacionUtilFragment) {
	    super(context, R.layout.inicio_item, values);
	    this.context = context;
	    this.values = values;
		  this.informacionUtilFragment = informacionUtilFragment;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.consejo_item, parent, false);
	    final ConsejoRiesgo consejoRiesgo = (ConsejoRiesgo) values.get(position);
		  TextView textView = (TextView) rowView.findViewById(R.id.nombreConsejo);
		  textView.setText(consejoRiesgo.getNombre());
		  textView.setTag(consejoRiesgo);
		  Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
		  textView.setTypeface(fontBlack);
		  ImageView imagen = (ImageView) rowView.findViewById(R.id.imagenConsejo);
		  Resources res = context.getResources();
		  int resID = res.getIdentifier(consejoRiesgo.getIcono().replace(".png", "") , "drawable", context.getPackageName());
		  Drawable drawable = res.getDrawable(resID );
		  imagen.setImageDrawable(drawable );

		  consejoRiesgo.setVerDetalleConsejo((Button) rowView.findViewById(R.id.btnVerConsejo));
		  consejoRiesgo.getVerDetalleConsejo().setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  informacionUtilFragment.verDetalleConsejo(consejoRiesgo);
			  }
		  });
	    return rowView;
	  }
	  
	  
	  

}
