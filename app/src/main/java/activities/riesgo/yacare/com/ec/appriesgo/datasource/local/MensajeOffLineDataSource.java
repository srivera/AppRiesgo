package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;

public class MensajeOffLineDataSource {

	public static final String TABLA_MENSAJE_OFFLINE = "mensajeOffLine";
	public static final String COLUMN_MENSAJEOL_ID = "id";
	public static final String COLUMN_MENSAJEOL = "mensaje";
	public static final String COLUMN_ADDRESSOL = "addressbluetooth";
	public static final String COLUMN_ESTADOOL = "estado";
	public static final String COLUMN_IDCONPOL = "idcontacto";
	public static final String COLUMN_LONGITUDOL = "longitud";
	public static final String COLUMN_LATITUDOL = "latitud";
	public static final String COLUMN_FECHAOL = "fecha";


	// Database fields
	private SQLiteDatabase database;
	private RiesgoLocalSqlLite dbHelper;
	private String[] allColumns = { COLUMN_MENSAJEOL_ID,
			COLUMN_MENSAJEOL, COLUMN_ADDRESSOL,  COLUMN_ESTADOOL, COLUMN_IDCONPOL,
			COLUMN_LONGITUDOL, COLUMN_LATITUDOL, COLUMN_FECHAOL};

	public MensajeOffLineDataSource(Context context) {
		dbHelper = new RiesgoLocalSqlLite(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public MensajeOffLine createMensajeOffLineNew(MensajeOffLine mensajeOffLine) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_MENSAJEOL_ID, mensajeOffLine.getId());
		values.put(COLUMN_MENSAJEOL, mensajeOffLine.getMensaje());
		values.put(COLUMN_ADDRESSOL, mensajeOffLine.getAddressBlueTooth());
		values.put(COLUMN_ESTADOOL, mensajeOffLine.getEstado());
		values.put(COLUMN_IDCONPOL, mensajeOffLine.getIdContacto());
		values.put(COLUMN_LONGITUDOL, mensajeOffLine.getLongitud());
		values.put(COLUMN_LATITUDOL, mensajeOffLine.getLatitud());
		values.put(COLUMN_FECHAOL, mensajeOffLine.getFecha());
		
		database.insert(TABLA_MENSAJE_OFFLINE, null, values);
		Cursor cursor = database.query(TABLA_MENSAJE_OFFLINE,
				allColumns, COLUMN_MENSAJEOL_ID + " = '" + mensajeOffLine.getId() + "'"  , null,
				null, null, null);
		cursor.moveToFirst();
		MensajeOffLine mensajeOffLineNew = cursorToMensajeOffLineNew(cursor);
		cursor.close();
		return mensajeOffLineNew;
	}

	public void deleteMensajeOffLine(String idMensajeOffLine) {
		System.out.println("deleteMensajeOffLineNew with id: " + idMensajeOffLine);
		database.delete(TABLA_MENSAJE_OFFLINE, COLUMN_MENSAJEOL_ID
				+ " = '" + idMensajeOffLine + "'", null);
	}

	public void deleteAllMensajeOffLine() {
		database.delete(TABLA_MENSAJE_OFFLINE, null, null);
	}
	
	public void updateMensajeOffLine(MensajeOffLine mensajeOffLine) {
		System.out.println("updateMensajeOffLine with id: " + mensajeOffLine.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_MENSAJEOL_ID, mensajeOffLine.getId());
		values.put(COLUMN_MENSAJEOL, mensajeOffLine.getMensaje());
		values.put(COLUMN_ADDRESSOL, mensajeOffLine.getAddressBlueTooth());
		values.put(COLUMN_ESTADOOL, mensajeOffLine.getEstado());
		values.put(COLUMN_IDCONPOL, mensajeOffLine.getIdContacto());
		values.put(COLUMN_LONGITUDOL, mensajeOffLine.getLongitud());
		values.put(COLUMN_LATITUDOL, mensajeOffLine.getLatitud());
		values.put(COLUMN_FECHAOL, mensajeOffLine.getFecha());
		database.update(TABLA_MENSAJE_OFFLINE, values, COLUMN_MENSAJEOL_ID + " = '" + mensajeOffLine.getId() + "'", null);
	}

	public ArrayList<MensajeOffLine> getAllMensajeOffLine() {
		ArrayList<MensajeOffLine> mensajeOffLines = new ArrayList<MensajeOffLine>();
		Cursor cursor = database.query(TABLA_MENSAJE_OFFLINE,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MensajeOffLine mensajeOffLine = cursorToMensajeOffLineNew(cursor);
			mensajeOffLines.add(mensajeOffLine);
			cursor.moveToNext();
		}
		cursor.close();
		return mensajeOffLines;
	}

	
	public ArrayList<MensajeOffLine> getMensajeOffLinePendiente() {
		ArrayList<MensajeOffLine> mensajes = new ArrayList<MensajeOffLine>();
		Cursor cursor = database.query(TABLA_MENSAJE_OFFLINE,
				allColumns,  COLUMN_ESTADOOL + " = '" + "PEN" + "' or " + COLUMN_ESTADOOL + " = '" + "PET" + "'", null, null, null, COLUMN_FECHAOL);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MensajeOffLine mensajeOffLine = cursorToMensajeOffLineNew(cursor);
			mensajes.add(mensajeOffLine);
			cursor.moveToNext();
		}
		cursor.close();
		return mensajes;
	}


	public ArrayList<MensajeOffLine> getAllMensajes() {
		ArrayList<MensajeOffLine> mensajes = new ArrayList<MensajeOffLine>();

		Cursor cursor = database.query(TABLA_MENSAJE_OFFLINE,
				allColumns,  COLUMN_ESTADOOL + " = '" + "PEN" + "' or " + COLUMN_ESTADOOL + " = '" + "PRO" + "'", null, null, null,  COLUMN_FECHAOL + " DESC",  0 + "," + 50);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MensajeOffLine mensajeOffLine = cursorToMensajeOffLineNew(cursor);
			mensajes.add(mensajeOffLine);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return mensajes;
	}


	private MensajeOffLine cursorToMensajeOffLineNew(Cursor cursor) {
		MensajeOffLine mensajeOffLine = new MensajeOffLine();
		mensajeOffLine.setId(cursor.getString(0));
		mensajeOffLine.setMensaje(cursor.getString(1));
		mensajeOffLine.setAddressBlueTooth(cursor.getString(2));
		mensajeOffLine.setEstado(cursor.getString(3));
		mensajeOffLine.setIdContacto(cursor.getString(4));
		mensajeOffLine.setLongitud(cursor.getString(5));
		mensajeOffLine.setLatitud(cursor.getString(6));
		mensajeOffLine.setFecha(cursor.getString(7));
		return mensajeOffLine;
	}
}
