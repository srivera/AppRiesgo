package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ConsejoRiesgo;
import activities.riesgo.yacare.com.ec.appriesgo.dto.ElementoEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;

public class ElementoEmergenciaDataSource {

	public static final String TABLA_EMERGENCIA = "elementoemergencia";
	public static final String COLUMN_EMER_ID = "idelementoemergencia";
	public static final String COLUMN_EMER_RIESGO = "idriesgo";
	public static final String COLUMN_EMER_ORDEN = "ordenelementoemergencia";
	public static final String COLUMN_EMER_NOMBRE = "nombreelementoemergencia";
	public static final String COLUMN_EMER_CHECK = "checklist";
	public static final String COLUMN_EMER_ICONO = "icono";


	  private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_EMER_ID,
			  COLUMN_EMER_RIESGO,
			  COLUMN_EMER_ORDEN,
			  COLUMN_EMER_NOMBRE,
			  COLUMN_EMER_CHECK,
			  COLUMN_EMER_ICONO};

	  public ElementoEmergenciaDataSource(Context context) {
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }


	  public ArrayList<ElementoEmergencia> getAll() {
	    ArrayList<ElementoEmergencia> emergencias = new ArrayList<ElementoEmergencia>();

	    Cursor cursor = database.query(TABLA_EMERGENCIA,
	        allColumns, null, null, null, null, COLUMN_EMER_ORDEN);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      ElementoEmergencia elementoEmergencia = cursorToEmergencia(cursor);
			emergencias.add(elementoEmergencia);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return emergencias;
	  }

	public void updateElementoEmergencia(ElementoEmergencia elementoEmergencia) {
		System.out.println("updateMensajeOffLine with id: " + elementoEmergencia.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_EMER_ID, elementoEmergencia.getId());
		values.put(COLUMN_EMER_ORDEN, elementoEmergencia.getOrden());
		values.put(COLUMN_EMER_NOMBRE, elementoEmergencia.getNombre());
		values.put(COLUMN_EMER_CHECK, elementoEmergencia.getCheck());
		values.put(COLUMN_EMER_ICONO, elementoEmergencia.getIcono());
		database.update(TABLA_EMERGENCIA, values, COLUMN_EMER_ID + " = '" + elementoEmergencia.getId() + "'", null);
	}

	public ArrayList<ElementoEmergencia> getElementoByIdRiesgo(String idRiesgo) {
		ArrayList<ElementoEmergencia> elementoEmergencias = new ArrayList<ElementoEmergencia>();

		Cursor cursor = database.query(TABLA_EMERGENCIA,
				allColumns, COLUMN_EMER_RIESGO + " = '" + idRiesgo + "'", null, null, null,COLUMN_EMER_ORDEN, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ElementoEmergencia elementoEmergencia = cursorToEmergencia(cursor);
			elementoEmergencias.add(elementoEmergencia);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return elementoEmergencias;
	}

	public ArrayList<ElementoEmergencia> getElementoByIdRiesgoandCheck(String idRiesgo) {
		ArrayList<ElementoEmergencia> elementoEmergencias = new ArrayList<ElementoEmergencia>();

		Cursor cursor = database.query(TABLA_EMERGENCIA,
				allColumns, COLUMN_EMER_RIESGO + " = '" + idRiesgo + "' and " + COLUMN_EMER_CHECK + " = '0'" , null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ElementoEmergencia elementoEmergencia = cursorToEmergencia(cursor);
			elementoEmergencias.add(elementoEmergencia);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return elementoEmergencias;
	}

	  private ElementoEmergencia cursorToEmergencia(Cursor cursor) {
	    ElementoEmergencia emergencia = new ElementoEmergencia();
		  emergencia.setId(cursor.getString(0));
		  emergencia.setIdRiesgo(cursor.getString(1));
		  emergencia.setOrden(cursor.getString(2));
		  emergencia.setNombre(cursor.getString(3));
		  emergencia.setCheck(cursor.getString(4));
		  emergencia.setIcono(cursor.getString(5));
	    return emergencia;
	  }
}
