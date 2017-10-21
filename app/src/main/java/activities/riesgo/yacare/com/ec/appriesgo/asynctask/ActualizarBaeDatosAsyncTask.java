package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import activities.riesgo.yacare.com.ec.appriesgo.datasource.ConsejoRiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.RiesgoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;

public class ActualizarBaeDatosAsyncTask extends AsyncTask<String, Float, String> {


	private Context context;

	public ActualizarBaeDatosAsyncTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(String... arg0) {

		//Verificar si debe realizar los inserts de riesgo Tungurahua
		RiesgoDataSource riesgoDataSource = new RiesgoDataSource(context);
		riesgoDataSource.open();
		Riesgo riesgoTungurahua = riesgoDataSource.getRiesgoId("2");

		ConsejoRiesgoDataSource consejoRiesgoDataSource = new ConsejoRiesgoDataSource(context);
		consejoRiesgoDataSource.open();

		if(riesgoTungurahua == null || riesgoTungurahua.getId() == null){
			riesgoDataSource.actualizarBaseDatosTungurahua();
			consejoRiesgoDataSource.actualizarBaseDatosTungurahua();
		}


		TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(context);
		tipoAlertaDataSource.open();
		TipoAlerta tipoAlerta = tipoAlertaDataSource.getTipoAlertaByCodigo("BLA", "1");
		tipoAlertaDataSource.close();
		if(tipoAlerta == null || tipoAlerta.getId() == null) {
			riesgoDataSource.actualizarAlertaBlanca();
		}


		Riesgo riesgoNino = riesgoDataSource.getRiesgoId("3");

		if(riesgoNino == null || riesgoNino.getId() == null){
			riesgoDataSource.actualizarBaseFenomenoNino();
			consejoRiesgoDataSource.actualizarBaseFenomenoNino();
		}

		consejoRiesgoDataSource.actualizarBaseDatosTungurahuaLahar();

		consejoRiesgoDataSource.actualizarLinksNino();

//		SharedPreferences sharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(context);
//		SharedPreferences.Editor editor2 = sharedPrefs2.edit();
//		editor2.putString("prefActualizado", "0");
//		editor2.apply();
//		editor2.commit();

		//consejoRiesgoDataSource.actualizarBaseDatosCotopaxi2();

		riesgoDataSource.actualizarAlertaNaranjaTungurahua();
		Riesgo riesgoTsunami = riesgoDataSource.getRiesgoId("4");
		if(riesgoTsunami == null || riesgoTsunami.getId() == null){
			riesgoDataSource.actualizarBaseDatosTsunami();
			riesgoDataSource.actualizarBaseDatosTsunami2();
			consejoRiesgoDataSource.actualizarBaseDatosTsunamis();
			consejoRiesgoDataSource.actualizarBaseDatosTsunamis2();
			consejoRiesgoDataSource.actualizarBaseDatosCotopaxi2();
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString("prefActualizado", "1");
			editor.apply();
			editor.commit();

		}else{
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			String prefActualizado = sharedPrefs.getString("prefActualizado", "0");
			if(prefActualizado.equals("0")){
				riesgoDataSource.actualizarBaseDatosTsunami2();
				consejoRiesgoDataSource.actualizarBaseDatosCotopaxi2();
				consejoRiesgoDataSource.actualizarBaseDatosTsunamis2();

				SharedPreferences.Editor editor = sharedPrefs.edit();
				editor.putString("prefActualizado", "1");
				editor.apply();
				editor.commit();
			}
			//consejoRiesgoDataSource.actualizarBaseDatosTsunamisPuntos();

//			CapaDataSource capaDataSource = new CapaDataSource(getApplicationContext());
//			capaDataSource.open();
//			Capa capa = capaDataSource.getCapaById("RETS2");
//			capaDataSource.close();
//			if(capa == null || capa.getId() == null){
//				consejoRiesgoDataSource.insertarRutasTsunamis();
//			}
		}

		Riesgo riesgoTurismo = riesgoDataSource.getRiesgoId("5");
		if(riesgoTurismo == null || riesgoTurismo.getId() == null){
			riesgoDataSource.actualizarBaseDatosTurismo();
		}


		//	riesgoDataSource.actualizarColorTurismo();
		riesgoDataSource.close();
		consejoRiesgoDataSource.close();
		return "";
	}
	

}
