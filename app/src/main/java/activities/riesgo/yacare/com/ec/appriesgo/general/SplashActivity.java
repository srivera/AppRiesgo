package activities.riesgo.yacare.com.ec.appriesgo.general;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.sql.SQLException;
import java.util.Timer;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ActualizarBaeDatosAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.GuardarPersonaTimer;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.CuentaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;
import activities.riesgo.yacare.com.ec.appriesgo.gcm.QuickstartPreferences;
import activities.riesgo.yacare.com.ec.appriesgo.gcm.RegistrationIntentService;


public class SplashActivity extends Activity {

	private DatosAplicacion datosAplicacion;
	
	private String error;

	private SQLiteDatabase database;

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String TAG = "SplashActivity";

	private BroadcastReceiver mRegistrationBroadcastReceiver;


	public static String SENDER_ID = "916673684930"; //produccion
//	public static String SENDER_ID = "1087591938757"; //desarrollo
	private GoogleCloudMessaging gcm;
	private NotificationHub hub;
	private String HubName = "yacaretech";
	private String HubListenConnectionString = "Endpoint=sb://yacaretech-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=Mx1oClaxIu6/RBX4nyFWjqZDp9G+VMu2hKo6iwNnjkM=";
	private static Boolean isVisible = false;

	/**
	 * Google API client.
	 */
	private GoogleApiClient mGoogleApiClient;

	/**
	 * Determines if the client is in a resolution state, and
	 * waiting for resolution intent to return.
	 */
	private boolean mIsInResolution;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		datosAplicacion = (DatosAplicacion) getApplicationContext();

		NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);
		gcm = GoogleCloudMessaging.getInstance(this);
		hub = new NotificationHub(HubName, HubListenConnectionString, this);
		registerWithNotificationHubs();

		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(context);
				boolean sentToken = sharedPreferences
						.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
				if (sentToken) {
					Log.d(TAG, "CORRECTO");
				} else {
					Log.d(TAG, "ERROR");
				}
			}
		};

		if (checkPlayServices()) {
			// Start IntentService to register this application with GCM.
			Intent intent = new Intent(SplashActivity.this, RegistrationIntentService.class);
			startService(intent);
		}

		Log.d(TAG, "INGRESAR");

		RiesgoSqlLite riesgoSqlLite = new RiesgoSqlLite(getApplicationContext(), "riesgos.db");
		try {
			riesgoSqlLite.createDataBase();
			database = riesgoSqlLite.openDataBase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		RiesgoLocalSqlLite riesgoLocalSqlLite = new RiesgoLocalSqlLite(getApplicationContext(), "riesgoslocal.db");
		try {
			riesgoLocalSqlLite.createDataBase();
			database = riesgoLocalSqlLite.openDataBase();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		datosAplicacion.setIdDispositivo(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
		//Verificar si esta creada la cuenta se va a la principal, sino a la de registro
		CuentaDataSource cuentaDataSource = new CuentaDataSource(getApplicationContext());
		cuentaDataSource.open();
		Cuenta cuenta = cuentaDataSource.getCuenta();
		datosAplicacion.setCuenta(cuenta);
		if(cuenta != null && cuenta.getId().equals("0")){
			Timer time = new Timer();
			GuardarPersonaTimer st1 = new GuardarPersonaTimer(datosAplicacion);
			time.schedule(st1, 0, 60000);
		}
		ActualizarBaeDatosAsyncTask actualizarBaeDatosAsyncTask = new ActualizarBaeDatosAsyncTask(getApplicationContext());
		actualizarBaeDatosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		if(cuenta == null ){
			Intent i = new Intent(SplashActivity.this, PoliticaPrivacidadActivity.class);
			startActivity(i);
		}else{
			Intent i = new Intent(SplashActivity.this, MenuActivity.class);
			if(getIntent().hasExtra("mensaje")){
				i.putExtra("mensaje", getIntent().getStringExtra("mensaje"));
				i.putExtra("titulo", getIntent().getStringExtra("titulo"));
			}
			startActivity(i);
		}
		cuentaDataSource.close();


		new Thread() {
			@Override
			public void run() {
				handler.sendMessageDelayed(handler.obtainMessage(), 500);
			};
		}.start();
	}



	@SuppressWarnings("unchecked")
	private void registerWithNotificationHubs() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					String regid = gcm.register(SENDER_ID);
					//	DialogNotify("Registered Successfully","RegId : " +
							datosAplicacion.setRegIdAzure(hub.register(regid).getRegistrationId());
				} catch (Exception e) {
					e.printStackTrace();
					return e;
				}
				return null;
			}
		}.execute(null, null, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onStart() {
		super.onStart();
		isVisible = true;
	}

	//Handler que se invoca en el start para determinar a que actividad se debe ir la primera vez
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

		}
	};


	@Override
	protected void onResume() {
		super.onResume();
//		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//				new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
	}


	@Override
	protected void onStop() {
		super.onStop();
		isVisible = false;
	}

	@Override
	protected void onPause() {
//		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
		super.onPause();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}
}
