package activities.riesgo.yacare.com.ec.appriesgo.inicio;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class AcercaDeFragment extends BaseContainerFragment{



	private Button btnRegresarAcercade;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_acercade, container, false);

		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle("ACERCA DE");
			getActivity().getActionBar().setSubtitle("SOBRE ESTA APP");
		}

		btnRegresarAcercade = (Button) rootView.findViewById(R.id.btnRegresarAcercade);
		btnRegresarAcercade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InicioFragment fragment1 = new InicioFragment();
				((BaseContainerFragment) getParentFragment()).replaceFragment(fragment1, true);
			}
		});

//		Typeface fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
//		Typeface fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");

//		TextView txtTituloAcercade = (TextView) rootView.findViewById(R.id.txtTituloAcercade);
//		TextView txtLeyendaAcercaDe = (TextView) rootView.findViewById(R.id.txtLeyendaAcercaDe);
//		TextView txtLeyendaAcercaDe2 = (TextView) rootView.findViewById(R.id.txtLeyendaAcercaDe2);
//
//		txtTituloAcercade.setTypeface(fontBold);
//		txtLeyendaAcercaDe.setTypeface(fontRegular);
//		txtLeyendaAcercaDe2.setTypeface(fontRegular);

		return rootView;
	}

}
