package activities.riesgo.yacare.com.ec.appriesgo.asynctask;

import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.Constantes;

public class NewEmergencyAsyncTask extends AsyncTask<String, Float, String> {


	private PrincipalActivity principalActivity;

	private final String NAMESPACE = "urn:WebServiceControllerwsdl";
	private final String METHOD = "newEmergency";
	private String codigoIncidente;
	private Location ubicacion;


	public NewEmergencyAsyncTask(PrincipalActivity principalActivity , String codigoIncidente, Location ubicacion) {
		super();
		this.principalActivity = principalActivity;
		this.codigoIncidente = codigoIncidente;
		this.ubicacion = ubicacion;
	}


	@Override
	protected String doInBackground(String... arg0) {

		DatosAplicacion datosAplicacion  = ((DatosAplicacion) principalActivity.getApplicationContext());
		Cuenta cuenta = datosAplicacion.getCuenta();
		String imei = Settings.Secure.getString(principalActivity.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

		Boolean esEcuatoriano = true;
		if(cuenta.getTipoDocumento().equals("PAS")){
			esEcuatoriano = false;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		cuenta.setFechaCreacion(dateFormat.format(date));
		principalActivity.fechaIncidente =  String.valueOf(dateFormat.format(date));

		String respStr = "";
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD);

			//String argumento  ="{\"id_fuente\":\"01-00-2A-90-02-00-CC-70-03-00-FD-EB-04-00-4B-44-05-00-39-31-07-00-AF-25-08-00-F2-ED-09-00-23-DD\",\"nombre\":\"JUAN\",\"apellido\":\"LEON\",\"codigo\":\"593\",\"telefono\":\"999999999\",\"latitud\":-0.206453,\"longitud\":-78.428366,\"direccion_evento\":\"PRUEBAS MICS\",\"id_incidente\":20604,\"descripcion\":\"Send Windows Phone\",\"cedula\":\"0704824846\",\"ecuatoriano\":true,\"discapacidad\":\"PHYSICAL\",\"sangre\":\"O+\",\"allergies\":\"NINGUNA\",\"emergencia_nombre\":\"DARWIN\",\"emergencia_apellido\":\"QUINCHE\",\"emergencia_codigo\":\"593\",\"emergencia_telefono\":\"999999999\",\"emergencia_relacion\":\"TIO\",\"timestamp\":\"2015-10-13 08:51:43\"}";

			String argumento  ="{\"id_fuente\":\"" + imei + "\"," +
					"\"nombre\":\"" + cuenta.getPrimerNombre() + "\"," +
					"\"apellido\":\"" + cuenta.getPrimerApellido()+ "\"," +
					"\"codigo\":\"" + cuenta.getCodigopais() + "\"," +
					"\"telefono\":\"" + cuenta.getNumeroTelefono() + "\"," +
					"\"latitud\":" + String.valueOf(ubicacion.getLatitude()) + "," +
					"\"longitud\":" + String.valueOf(ubicacion.getLongitude()) + "," +
					"\"direccion_evento\":\"MICS\"," +
					"\"id_incidente\":" + codigoIncidente + "," +
					"\"descripcion\":\"MICS\"," +
					"\"cedula\":\"" + cuenta.getNumeroDocumento() + "\"," +
					"\"ecuatoriano\":"+ String.valueOf(esEcuatoriano) +"," +
					"\"discapacidad\":\"" + cuenta.getDiscapacidad() +"\"," +
					"\"sangre\":\"" + cuenta.getTiposangre() + "\"," +
					"\"allergies\":\"" + cuenta.getAlergiaMedicamento() + "\"," +
					"\"emergencia_nombre\":\"" + cuenta.getNombrecontacto() + "\"," +
					"\"emergencia_apellido\":\"" + cuenta.getApellidocontacto() + "\"," +
					"\"emergencia_codigo\":\"" + cuenta.getCodigopaiscontacto() + "\"," +
					"\"emergencia_telefono\":\"" + cuenta.getTelefonoEmergencia() + "\"," +
					"\"emergencia_relacion\":\"" + cuenta.getRelacioncontacto() + "\"," +
					"\"timestamp\":\"" + principalActivity.fechaIncidente + "\"}";

			request.addProperty("data", argumento);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			AuthTransportSE androidHttpTransport = new AuthTransportSE(Constantes.URL_NEW_EMERGENCY,"ecuservices","Ecu911S3rv1c3s");

			try {
				androidHttpTransport.call("ecuserver", envelope);
				respStr = (String) envelope.getResponse();
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
		//principalActivity.verificarreportarEmergencia(respuesta, true);

	}
}
