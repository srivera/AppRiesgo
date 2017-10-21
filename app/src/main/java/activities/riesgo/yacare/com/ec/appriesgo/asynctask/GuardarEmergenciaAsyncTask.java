package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.location.Location;
import android.os.AsyncTask;

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

import java.io.IOException;

import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;

public class GuardarEmergenciaAsyncTask extends AsyncTask<String, Float, String>  {

	private  Cuenta cuenta;
	private  Location location;
	private  PrincipalActivity principalActivity;

	public GuardarEmergenciaAsyncTask(Cuenta cuenta, Location location, PrincipalActivity principalActivity) {
		super();
		this.cuenta = cuenta;
		this.location = location;
		this.principalActivity = principalActivity;
	}


	@Override
	protected String doInBackground(String... arg0) {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_GUARDAR_EMERGENCIA);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(cuenta.getId() + location.getLatitude() +  location.getLongitude() + Constantes.CERTIFICADO_SEGURIDAD);
			String json =  "{\"guardarEmergencia\":{\"idcuenta\":\""+ cuenta.getId()
					+ "\",\"latitud\":\""+ location.getLatitude()
					+ "\",\"longitud\":\""+ location.getLongitude()
					+ "\",\"discapacidad\":\""+ cuenta.getDiscapacidad()
					+ "\",\"tiposangre\":\""+ cuenta.getTiposangre()
					+ "\",\"alergias\":\""+ cuenta.getAlergiaMedicamento()
					+ "\",\"apellidocontacto\":\""+ cuenta.getApellidocontacto()
					+ "\",\"nombrecontacto\":\""+ cuenta.getNombrecontacto()
					+ "\",\"codigopaiscontacto\":\""+ cuenta.getCodigopaiscontacto()
					+ "\",\"celularcontacto\":\""+ cuenta.getTelefonoEmergencia()
					+ "\",\"relacioncontacto\":\""+ cuenta.getRelacioncontacto()
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
		if(principalActivity!= null) {
			principalActivity.verificarreportarEmergencia(respuesta);
		}
	}
}
