package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.location.Location;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;

public class SetCanceladoAsyncTask extends AsyncTask<String, Float, String> {


	private PrincipalActivity principalActivity;

	private final String NAMESPACE = "urn:WebServiceControllerwsdl";
	private final String METHOD = "setCancelado";
	private String idIncidente;
	private Location ubicacion;


	public SetCanceladoAsyncTask(PrincipalActivity principalActivity, String idIncidente, Location ubicacion) {
		super();
		this.principalActivity = principalActivity;
		this.idIncidente = idIncidente;
		this.ubicacion = ubicacion;
	}


	@Override
	protected String doInBackground(String... arg0) {


		String respStr = "";
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD);

			String argumento  ="{\"id\":" + idIncidente + "," +
					"\"latitud\":" + String.valueOf(ubicacion.getLatitude()) + "," +
					"\"longitud\":" + String.valueOf(ubicacion.getLongitude())  +
					"}";

			request.addProperty("data", argumento);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			AuthTransportSE androidHttpTransport = new AuthTransportSE(Constantes.URL_NEW_EMERGENCY,"ecuservices","Ecu911S3rv1c3s");

			try {
				androidHttpTransport.call("ecuserver", envelope);
				respStr = String.valueOf((Integer) envelope.getResponse());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			respStr = "ERR";
		}
		return respStr;
	}
	
	protected void onPostExecute(String respuesta) {
//		if(respuesta.equals("ERR")) {
//			chatBlueToothActivity.verificarEnviarMensaje(respuesta);
//		}
	}
}
