package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import activities.riesgo.yacare.com.ec.appriesgo.alerta.ListaAlertasFragment;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.NotificacionDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class ObtenerNoificacionesAsyncTask extends AsyncTask<String, Float, String>  {

	private  ListaAlertasFragment listaAlertasFragment;
	private  Context context;
	private Integer maximo;
	private SharedPreferences sharedPrefs;

	public ObtenerNoificacionesAsyncTask(Context context, ListaAlertasFragment listaAlertasFragment) {
		super();
		this.listaAlertasFragment = listaAlertasFragment;
		this.context = context;
	}


	@Override
	protected String doInBackground(String... arg0) {
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		
		HttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httppost = new HttpPost(Constantes.URL_OBTENER_NOTIFICACION);
		httppost.setHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8");

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(listaAlertasFragment.getActivity().getApplicationContext());
		maximo = sharedPrefs.getInt(Constantes.PREF_ULTIMA_NOTIFICACION, 1);


		String respStr = "";
		try {
			String certificadoSeguridad = VariablesUtil.generarMD5(maximo + Constantes.CERTIFICADO_SEGURIDAD);

			String json =  "{\"obtenerNotificacion\":{\"idNotificacion\":\""+ maximo
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
		if(!respuesta.equals("ERR")){
			Boolean statusFlag = null;
			JSONObject respuestaJSON = null;
			try {
				respuestaJSON = new JSONObject(respuesta);
				statusFlag = respuestaJSON.getBoolean("statusFlag");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if(statusFlag != null  && statusFlag){
				String idCuenta = null;

				JSONObject cuentaJSON = null;
				try {
					cuentaJSON = new JSONObject( respuestaJSON.getString("resultado"));
					JSONArray notificaciones = new JSONArray(cuentaJSON.getString("notificaciones"));

					if(notificaciones.length() > 0) {
						NotificacionDataSource notificacionDataSource = new NotificacionDataSource(context);
						notificacionDataSource.open();
						for (int i = 0, size = notificaciones.length(); i < size; i++) {
							try {
								JSONObject notInArray = notificaciones.getJSONObject(i);

								Notificacion notificacion = new Notificacion();
								notificacion.setId(notInArray.getString("id"));
								if(Integer.valueOf(notificacion.getId()) > maximo) {
									maximo = Integer.valueOf(notificacion.getId());
									SharedPreferences.Editor editor = sharedPrefs.edit();
									editor.putInt(Constantes.PREF_ULTIMA_NOTIFICACION, Integer.valueOf(notificacion.getId()));
									editor.apply();
									editor.commit();
								}
								notificacion.setMensaje(notInArray.getString("mensaje"));
								notificacion.setIdRiesgo("1");
								notificacion.setFecha(notInArray.getString("fechaformateada"));
								notificacion.setTitulo(notInArray.getString("titulo"));
								notificacionDataSource.createNotificacion(notificacion);


							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						notificacionDataSource.close();
						if(notificaciones.length() > 0 && listaAlertasFragment != null) {
							listaAlertasFragment.verificarNotificaciones(true);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
