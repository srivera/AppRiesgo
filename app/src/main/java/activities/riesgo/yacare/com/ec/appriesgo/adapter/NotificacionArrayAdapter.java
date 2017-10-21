package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;


public class NotificacionArrayAdapter extends ArrayAdapter<Notificacion> {
		private final Context context;
		private final ArrayList<Notificacion> values;

	  public NotificacionArrayAdapter(Context context, ArrayList<Notificacion> values) {
	    super(context, R.layout.notificacion_item, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.notificacion_item, parent, false);
	    final Notificacion notificacion = (Notificacion) values.get(position);
		  TextView dia = (TextView) rowView.findViewById(R.id.txtDiaNotificacion);
		  TextView mes = (TextView) rowView.findViewById(R.id.txtMesNotificacion);
		  TextView anio = (TextView) rowView.findViewById(R.id.txtAnioNotificacion);
		  TextView hora = (TextView) rowView.findViewById(R.id.txtHoraNotificacion);
		  TextView leyenda = (TextView) rowView.findViewById(R.id.txtLeyendaNotificacion);
		  TextView titulo = (TextView) rowView.findViewById(R.id.txtTituloNotificacion);

//		  TextView nombreRiesgo = (TextView) rowView.findViewById(R.id.txtNombreRiesgo);
//		  nombreRiesgo.setText("   " + notificacion.getNombreRiesgo());

		  String[] fechaTotal = notificacion.getFecha().split(" ");
		  String[] fecha = fechaTotal[0].split("/");
		  dia.setText(fecha[2]);
		  anio.setText(fecha[0]);
		  mes.setText(getMonth(Integer.valueOf(fecha[1])));
		  hora.setText(fechaTotal[1]);

		  leyenda.setText(notificacion.getMensaje());
		  titulo.setText(notificacion.getTitulo());
		  Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
		  Typeface fontBold = Typeface.createFromAsset(context.getAssets(), "Lato-Bold.ttf");
		  dia.setTypeface(fontBold);
		  anio.setTypeface(fontBold);
		  mes.setTypeface(fontBold);
		  hora.setTypeface(fontRegular);
		  titulo.setTypeface(fontRegular);
		  leyenda.setTypeface(fontRegular);

//		  nombreRiesgo.setTypeface(fontRegular);
		  return rowView;
	  }

	public String getMonth(int month) {
		switch(month){
			case 1:
				return "ENE";

			case 2:
				return "FEB";

			case 3:
				return "MAR";

			case 4:
				return "ABR";

			case 5:
				return "MAY";

			case 6:
				return "JUN";

			case 7:
				return "JUN";

			case 8:
				return "AGO";

			case 9:
				return "SEP";

			case 10:
				return "OCT";

			case 11:
				return "NOV";

			case 12:
				return "DIC";
		}

		return "";
	}

}
