package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import activities.riesgo.yacare.com.ec.appriesgo.R;


public class MenuArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final ArrayList<String> values;

	  public MenuArrayAdapter(Context context,  ArrayList<String> values) {
	    super(context, R.layout.menu_item, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.menu_item, parent, false);
	    final String opcion = values.get(position);

		  Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");


		  String[] texto = opcion.split(";");
		  TextView textView = (TextView) rowView.findViewById(R.id.nombreOpcion);
		  textView.setText(texto[0]);
		  textView.setTypeface(fontRegular);
		  textView.setTag(texto[1]);

	    return rowView;
	  }
	  
	  
	  

}
