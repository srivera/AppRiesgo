package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;


public class RutaArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public RutaArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.message, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message, parent, false);
        final String ruta = values.get(position);
        String[] textos = ruta.split(";");
        Typeface fontRegular = Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");

        //Gira a la <b>izquierda</b>.<div style="font-size:0.9em">El destino está a la derecha.</div>;44 m

        TextView textView = (TextView) rowView.findViewById(R.id.textoRuta);
        Spanned html = Html.fromHtml(textos[0].replace("\"<div style=\"font-size:0.9em\">", "").replace("</div>", ""));
        if(html.toString().contains("\n")){
            textView.setText(html.toString().replace("\n", " "));
        }else{
            textView.setText(html);
        }


        textView.setTypeface(fontRegular);
        textView = (TextView) rowView.findViewById(R.id.textoDistancia);
        textView.setText(Html.fromHtml(textos[1]));
        textView.setTypeface(fontRegular);

        return rowView;
    }


}
