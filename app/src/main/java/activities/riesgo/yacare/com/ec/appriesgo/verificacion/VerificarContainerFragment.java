package activities.riesgo.yacare.com.ec.appriesgo.verificacion;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;


public class VerificarContainerFragment extends BaseContainerFragment{

	private boolean mIsViewInited;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("test", "tab 1 oncreateview");
		return inflater.inflate(R.layout.container_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 1 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}
	}

	private void initView() {
		Log.e("test", "tab 1 init view");
		DatosAplicacion datosAplicacion = (DatosAplicacion) getActivity().getApplicationContext();
		if(datosAplicacion.getRiesgoActual().getId().equals("3")) {
			replaceFragment(new MapaNinoFragment(), false);

		}else if(datosAplicacion.getRiesgoActual().getId().equals("4")) {
			replaceFragment(new MapaTsunamiFragment(), false);
		}else{
			replaceFragment(new MapaFragment(), false);
		}
	}

}
