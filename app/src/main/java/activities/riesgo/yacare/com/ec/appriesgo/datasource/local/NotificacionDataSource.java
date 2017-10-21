package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;

public class NotificacionDataSource {

	public static final String TABLA_NOTIFICACION = "notificacion";
	public static final String COLUMN_NOTIFICACION_ID = "idnotificacion";
	public static final String COLUMN_NOTIFICACION_MENSAJE = "mensaje";
	public static final String COLUMN_NOTIFICACION_FECHA = "fecha";
	public static final String COLUMN_NOTIFICACION_IDRIESGO = "idRiesgo";
	public static final String COLUMN_NOTIFICACION_TITULO= "titulo";


	  private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_NOTIFICACION_ID,
			  COLUMN_NOTIFICACION_MENSAJE,
			  COLUMN_NOTIFICACION_FECHA,
			  COLUMN_NOTIFICACION_IDRIESGO,
			  COLUMN_NOTIFICACION_TITULO};

	  public NotificacionDataSource(Context context) {
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	public Notificacion createNotificacion(Notificacion notificacion) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NOTIFICACION_ID, notificacion.getId());
		values.put(COLUMN_NOTIFICACION_MENSAJE, notificacion.getMensaje());
		values.put(COLUMN_NOTIFICACION_FECHA, notificacion.getFecha());
		values.put(COLUMN_NOTIFICACION_IDRIESGO, notificacion.getIdRiesgo());
		values.put(COLUMN_NOTIFICACION_TITULO, notificacion.getTitulo());

		database.insert(TABLA_NOTIFICACION, null, values);
		Cursor cursor = database.query(TABLA_NOTIFICACION,
				allColumns, COLUMN_NOTIFICACION_ID + " = '" + notificacion.getId() + "'"  , null,
				null, null, null);
		cursor.moveToFirst();
		Notificacion newNotificacion = cursorToNotificacion(cursor);
		cursor.close();
		return newNotificacion;
	}

	public void updateNotificacion(Notificacion notificacion) {
		System.out.println("updateNotificacion with id: " + notificacion.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_NOTIFICACION_ID, notificacion.getId());
		values.put(COLUMN_NOTIFICACION_MENSAJE, notificacion.getMensaje());
		values.put(COLUMN_NOTIFICACION_FECHA, notificacion.getFecha());
		values.put(COLUMN_NOTIFICACION_IDRIESGO, notificacion.getIdRiesgo());
		values.put(COLUMN_NOTIFICACION_TITULO, notificacion.getTitulo());
		database.update(TABLA_NOTIFICACION, values, COLUMN_NOTIFICACION_ID + " = '" + notificacion.getId() + "'", null);
	}

	public Notificacion getNotificacionId(String idNotificacion) {
		Notificacion notificacion = null;
		Cursor cursor = database.query(TABLA_NOTIFICACION,
				allColumns,COLUMN_NOTIFICACION_ID + " = '" + idNotificacion + "'", null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			notificacion = cursorToNotificacion(cursor);
		}
		cursor.close();
		return notificacion;
	}

	public ArrayList<Notificacion> getNotificacionByRiesgo(String idRiesgo) {
	    ArrayList<Notificacion> notificaciones = new ArrayList<Notificacion>();

	    Cursor cursor = database.query(TABLA_NOTIFICACION,
	        allColumns, COLUMN_NOTIFICACION_IDRIESGO + " = '" + idRiesgo + "'", null, null, null, COLUMN_NOTIFICACION_FECHA + " DESC",  0 + "," + 50);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Notificacion notificacion = cursorToNotificacion(cursor);
			notificaciones.add(notificacion);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return notificaciones;
	  }

	public ArrayList<Notificacion> getAllNotificacion() {
		ArrayList<Notificacion> notificaciones = new ArrayList<Notificacion>();

		Cursor cursor = database.query(TABLA_NOTIFICACION,
				allColumns,null, null, null, null, COLUMN_NOTIFICACION_FECHA + " DESC",  0 + "," + 50);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Notificacion notificacion = cursorToNotificacion(cursor);
			notificaciones.add(notificacion);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return notificaciones;
	}

	  private Notificacion cursorToNotificacion(Cursor cursor) {
		  Notificacion notificacion = new Notificacion();
		  notificacion.setId(cursor.getString(0));
		  notificacion.setMensaje(cursor.getString(1));
		  notificacion.setFecha(cursor.getString(2));
		  notificacion.setIdRiesgo(cursor.getString(3));
		  notificacion.setTitulo(cursor.getString(4));
	    return notificacion;
	  }
}
