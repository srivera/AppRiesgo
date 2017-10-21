package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Contacto;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Notificacion;

public class ContactoDataSource {

	public static final String TABLA_CONTACTO = "contacto";
	public static final String COLUMN_CONTACTO_ID = "idcontacto";
	public static final String COLUMN_CONTACTO_NOMBRE= "nombre";
	public static final String COLUMN_CONTACTO_TIPO= "tipo";

	  private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_CONTACTO_ID,
			  COLUMN_CONTACTO_NOMBRE,
			  COLUMN_CONTACTO_TIPO};

	  public ContactoDataSource(Context context) {
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	public Contacto createContacto(Contacto contacto) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTACTO_ID, contacto.getId());
		values.put(COLUMN_CONTACTO_NOMBRE, contacto.getNombre());
		values.put(COLUMN_CONTACTO_TIPO, contacto.getTipo());

		database.insert(TABLA_CONTACTO, null, values);
		Cursor cursor = database.query(TABLA_CONTACTO,
				allColumns, COLUMN_CONTACTO_ID + " = '" + contacto.getId() + "'"  , null,
				null, null, null);
		cursor.moveToFirst();
		Contacto newContacto = cursorToContacto(cursor);
		cursor.close();
		return newContacto;
	}

	public void deleteContacto(String idContacto) {
		System.out.println("deleteContacto with id: " + idContacto);
		database.delete(TABLA_CONTACTO, COLUMN_CONTACTO_ID
				+ " = '" + idContacto + "'", null);
	}

	public void deleteAllContacto() {
		System.out.println("deleteAllContacto  ");
		database.delete(TABLA_CONTACTO, null, null);
	}

	public void deleteContactoByTipo(String tipo) {
		System.out.println("deleteAllContacto  ");
		database.delete(TABLA_CONTACTO,  COLUMN_CONTACTO_TIPO
				+ " = '" + tipo + "'", null);
	}

	public ArrayList<Contacto> getAllContactoByTipo(String tipo) {
		ArrayList<Contacto> contactos = new ArrayList<Contacto>();

		Cursor cursor = database.query(TABLA_CONTACTO,
				allColumns, COLUMN_CONTACTO_TIPO + " = '" + tipo + "'", null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contacto contacto = cursorToContacto(cursor);
			contactos.add(contacto);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return contactos;
	}

	public ArrayList<Contacto> getAllContacto() {
		ArrayList<Contacto> contactos = new ArrayList<Contacto>();

		Cursor cursor = database.query(TABLA_CONTACTO,
				allColumns, null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contacto contacto = cursorToContacto(cursor);
			contactos.add(contacto);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return contactos;
	}
	public Contacto getContactoId(String idContacto) {
		Contacto contacto = new Contacto();
		Cursor cursor = database.query(TABLA_CONTACTO,
				allColumns,COLUMN_CONTACTO_ID + " = '" + idContacto + "'", null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			contacto = cursorToContacto(cursor);
		}
		cursor.close();
		return contacto;
	}
	public void updateContacto(Contacto contacto) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTACTO_ID, contacto.getId());
		values.put(COLUMN_CONTACTO_NOMBRE, contacto.getNombre());
		values.put(COLUMN_CONTACTO_TIPO, contacto.getTipo());
		database.update(TABLA_CONTACTO, values, COLUMN_CONTACTO_ID + " = '" + contacto.getId() + "'", null);
	}


	  private Contacto cursorToContacto(Cursor cursor) {
		  Contacto contacto = new Contacto();
		  contacto.setId(cursor.getString(0));
		  contacto.setNombre(cursor.getString(1));
		  contacto.setTipo(cursor.getString(2));
		  return contacto;
	  }
}
