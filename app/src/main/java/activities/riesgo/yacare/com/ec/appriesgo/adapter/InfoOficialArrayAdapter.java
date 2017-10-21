package activities.riesgo.yacare.com.ec.appriesgo.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Link;

public class InfoOficialArrayAdapter extends ArrayAdapter<Link> {
    private final Context context;
    private final ArrayList<Link> values;
    private ImageView imagen;

    public InfoOficialArrayAdapter(Context context, ArrayList<Link> values) {
        super(context, R.layout.informacion_oficial_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.informacion_oficial_item, parent, false);
        final Link link = (Link) values.get(position);
        TextView textView = (TextView) rowView.findViewById(R.id.nombreInfoOficial);
        textView.setText(link.getNombre());
        textView.setTag(link);
        Typeface fontBlack = Typeface.createFromAsset(context.getAssets(), "Lato-Black.ttf");
        textView.setTypeface(fontBlack);

        Typeface fontRegular= Typeface.createFromAsset(context.getAssets(), "Lato-Regular.ttf");
        TextView textView1 = (TextView) rowView.findViewById(R.id.urlInfoOficial);
        textView1.setText(link.getUrl());
        textView1.setTag(link);
        textView1.setTypeface(fontRegular);


        imagen = (ImageView) rowView.findViewById(R.id.imagenInfoOficial);
        Resources res = context.getResources();
        int resID = res.getIdentifier(link.getIcono().replace(".png", ""), "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imagen.setImageDrawable(drawable);

        return rowView;
    }


}
