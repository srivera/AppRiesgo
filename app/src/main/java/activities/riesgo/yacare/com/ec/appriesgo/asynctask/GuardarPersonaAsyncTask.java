package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.general.RegistroEcuActivity;
import activities.riesgo.yacare.com.ec.appriesgo.inicio.RegistroEcuFragment;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class GuardarPersonaAsyncTask extends AsyncTask<String, Float, String> {

	
	private RegistroEcuActivity registroActivity;

	private RegistroEcuFragment configuracionRegistroFragment;
	

	public GuardarPersonaAsyncTask(RegistroEcuActivity registroActivity, RegistroEcuFragment configuracionRegistroFragment) {
		super();
		this.registroActivity = registroActivity;
		this.configuracionRegistroFragment = configuracionRegistroFragment;
	}

	@Override
	protected String doInBackground(String... arg0) {
		DatosAplicacion datosAplicacion;
		
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		
		int timeoutSocket = 30000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_GUARDAR_PERSONA);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		Cuenta cuenta;
		if(registroActivity != null){
			cuenta = registroActivity.getCuenta();
			datosAplicacion  = ((DatosAplicacion) registroActivity.getApplicationContext());
		}else{
			cuenta = configuracionRegistroFragment.getCuenta();
			datosAplicacion  = ((DatosAplicacion) configuracionRegistroFragment.getActivity().getApplicationContext());
		}

		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(datosAplicacion.getIdDispositivo() + Constantes.CERTIFICADO_SEGURIDAD);
			String json =  "{\"guardarPersona\":{\"cedulaidentidad\":\""+ cuenta.getNumeroDocumento()
					+ "\",\"primerNombre\":\""+ cuenta.getPrimerNombre()
					+ "\",\"segundoNombre\":\""+ cuenta.getSegundoNombre()
					+ "\",\"primerApellido\":\""+ cuenta.getPrimerApellido()
					+ "\",\"segundoApellido\":\""+ cuenta.getSegundoApellido()
					+ "\",\"ciudad\":\""+ cuenta.getCiudad()
					+ "\",\"sector\":\""+ cuenta.getSector()
					+ "\",\"tipoDocumento\":\""+ cuenta.getTipoDocumento()
					+ "\",\"email\":\""+ cuenta.getEmail()
					+ "\",\"cedulaidentidadPadre\":\""+ ""
					+ "\",\"idDispositivo\":\""+ datosAplicacion.getIdDispositivo()
					+ "\",\"idPushMessage\":\""+ datosAplicacion.getRegId()
					+ "\",\"idPushMessageAzure\":\""+ datosAplicacion.getRegIdAzure()
					+ "\",\"tipoDispositivo\":\""+ "ANDROID"
					+ "\",\"certificadoseguridad\":\""+ certificadoSeguridad
					+ "\"}}";

			StringEntity se = new StringEntity(json);
			httppost.setEntity(se);
			HttpResponse resp = httpclient.execute(httppost);
			respStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "ERR";
		} catch (IOException e) {
			e.printStackTrace();
			return "ERR";
		}
		return respStr;
	}
	
	protected void onPostExecute(String respuesta) {
		if(registroActivity != null){
			registroActivity.verificarGuardarPersona(respuesta);
		}else{
			configuracionRegistroFragment.verificarGuardarPersona(respuesta);
		}
	}

	
}
