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

import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;

public class GuardarUbicacionAsyncTask  extends AsyncTask<String, Float, String>  {

	private  String idCuenta;
	private  Location location;
	private  PrincipalActivity principalActivity;

	public GuardarUbicacionAsyncTask(String idCuenta, Location location, PrincipalActivity principalActivity) {
		super();
		this.idCuenta = idCuenta;
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
		HttpPost httppost = new HttpPost(Constantes.URL_GUARDAR_UBICACION);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded");

		String respStr = "";
		try {
			String json =  "{\"guardarUbicacion\":{\"idcuenta\":\""+ idCuenta
					+ "\",\"latitud\":\""+ location.getLatitude()
					+ "\",\"longitud\":\""+ location.getLongitude()
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
			//principalActivity.verificarGuardarUbicacion(respuesta);
		}
	}
}
