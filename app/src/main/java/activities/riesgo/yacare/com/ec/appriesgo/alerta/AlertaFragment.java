package activities.riesgo.yacare.com.ec.appriesgo.alerta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;


public class AlertaFragment extends Fragment{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if(getActivity().getActionBar() != null) {
			DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
			getActivity().getActionBar().setTitle("INFORMACIÓN ÚTIL");
			getActivity().getActionBar().setSubtitle(datosAplicacion.getRiesgoActual().getNombreRiesgo());
		}
		return inflater.inflate(R.layout.activity_alerta, container, false);
	}
}
