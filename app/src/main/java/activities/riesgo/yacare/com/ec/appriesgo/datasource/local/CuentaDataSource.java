package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Cuenta;

public class CuentaDataSource {

	public static final String TABLA_CUENTA = "cuenta";
	public static final String COLUMN_CUENTA_ID = "idcuenta";
	public static final String COLUMN_CUENTA_NUMDOC = "numerodocumento";
	public static final String COLUMN_CUENTA_EMAIL = "email";
	public static final String COLUMN_CUENTA_PNOMBRE = "primernombre";
	public static final String COLUMN_CUENTA_SNOMBRE = "segundonombre";
	public static final String COLUMN_CUENTA_PAPELLIDO = "primerapellido";
	public static final String COLUMN_CUENTA_SAPELLIDO = "segundoapellido";
	public static final String COLUMN_CUENTA_FECHA_CREACION = "fechacreacion";
	public static final String COLUMN_CUENTA_ULTIMA_FECHA = "ultimafecha";
	public static final String COLUMN_CUENTA_NUMERO_TELEFONO = "numerotelefono";
	public static final String COLUMN_CUENTA_ALERGIA_MEDICAMENTO = "alergiamedicamento";
	public static final String COLUMN_CUENTA_CONTACTO = "contactoemergencia";
	public static final String COLUMN_CUENTA_TEL_CONTACTO = "telefonoemergencia";
	public static final String COLUMN_CUENTA_CIUDAD = "ciudad";
	public static final String COLUMN_CUENTA_SECTOR = "sector";
	public static final String COLUMN_CUENTA_TIPO_DOCUMENTO = "tipodocumento";
	public static final String COLUMN_CUENTA_CODIGO_PAIS = "codigopais";
	public static final String COLUMN_CUENTA_DISCAPACIDAD = "discapacidad";
	public static final String COLUMN_CUENTA_TIPO_SANGRE = "tiposangre";
	public static final String COLUMN_CUENTA_NOMBRE_CONTACTO = "nombrecontacto";
	public static final String COLUMN_CUENTA_APELLIDO_CONTACTO = "apellidocontacto";
	public static final String COLUMN_CUENTA_PAIS_CONTACTO = "codigopaiscontacto";
	public static final String COLUMN_CUENTA_RELACION_CONTACTO = "relacioncontacto";


	private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_CUENTA_ID,
			  COLUMN_CUENTA_NUMDOC,
			  COLUMN_CUENTA_EMAIL,
			  COLUMN_CUENTA_PNOMBRE,
			  COLUMN_CUENTA_SNOMBRE,
			  COLUMN_CUENTA_PAPELLIDO,
			  COLUMN_CUENTA_SAPELLIDO,
			  COLUMN_CUENTA_FECHA_CREACION,
			  COLUMN_CUENTA_ULTIMA_FECHA,
			  COLUMN_CUENTA_NUMERO_TELEFONO,
			  COLUMN_CUENTA_ALERGIA_MEDICAMENTO,
			  COLUMN_CUENTA_CONTACTO,
			  COLUMN_CUENTA_TEL_CONTACTO,
			  COLUMN_CUENTA_CIUDAD,
			  COLUMN_CUENTA_SECTOR,
			  COLUMN_CUENTA_TIPO_DOCUMENTO,
			  COLUMN_CUENTA_CODIGO_PAIS,
			  COLUMN_CUENTA_DISCAPACIDAD,
			  COLUMN_CUENTA_TIPO_SANGRE,
			  COLUMN_CUENTA_NOMBRE_CONTACTO,
			  COLUMN_CUENTA_APELLIDO_CONTACTO,
			  COLUMN_CUENTA_PAIS_CONTACTO,
			  COLUMN_CUENTA_RELACION_CONTACTO};


	private Cuenta cursorToCuenta(Cursor cursor) {
		Cuenta cuenta = new Cuenta();
		cuenta.setId(cursor.getString(0));
		cuenta.setNumeroDocumento(cursor.getString(1));
		cuenta.setEmail(cursor.getString(2));
		cuenta.setPrimerNombre(cursor.getString(3));
		cuenta.setSegundoNombre(cursor.getString(4));
		cuenta.setPrimerApellido(cursor.getString(5));
		cuenta.setSegundoApellido(cursor.getString(6));
		cuenta.setFechaCreacion(cursor.getString(7));
		cuenta.setUltimaFecha(cursor.getString(8));
		cuenta.setNumeroTelefono(cursor.getString(9));
		cuenta.setAlergiaMedicamento(cursor.getString(10));
		cuenta.setContactoEmergencia(cursor.getString(11));
		cuenta.setTelefonoEmergencia(cursor.getString(12));
		cuenta.setCiudad(cursor.getString(13));
		cuenta.setSector(cursor.getString(14));
		cuenta.setTipoDocumento(cursor.getString(15));
		cuenta.setCodigopais(cursor.getString(16));
		cuenta.setDiscapacidad(cursor.getString(17));
		cuenta.setTiposangre(cursor.getString(18));
		cuenta.setNombrecontacto(cursor.getString(19));
		cuenta.setApellidocontacto(cursor.getString(20));
		cuenta.setCodigopaiscontacto(cursor.getString(21));
		cuenta.setRelacioncontacto(cursor.getString(22));
		return cuenta;
	}

	  public CuentaDataSource(Context context) {
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	public Cuenta createCuenta(Cuenta cuenta) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CUENTA_ID, cuenta.getId());
		values.put(COLUMN_CUENTA_NUMDOC, cuenta.getNumeroDocumento());
		values.put(COLUMN_CUENTA_EMAIL, cuenta.getEmail());
		values.put(COLUMN_CUENTA_PNOMBRE, cuenta.getPrimerNombre());
		values.put(COLUMN_CUENTA_SNOMBRE, cuenta.getSegundoNombre());
		values.put(COLUMN_CUENTA_PAPELLIDO, cuenta.getPrimerApellido());
		values.put(COLUMN_CUENTA_SAPELLIDO, cuenta.getSegundoApellido());
		values.put(COLUMN_CUENTA_FECHA_CREACION, cuenta.getFechaCreacion());
		values.put(COLUMN_CUENTA_ULTIMA_FECHA, cuenta.getUltimaFecha());
		values.put(COLUMN_CUENTA_NUMERO_TELEFONO, cuenta.getNumeroTelefono());
		values.put(COLUMN_CUENTA_ALERGIA_MEDICAMENTO, cuenta.getAlergiaMedicamento());
		values.put(COLUMN_CUENTA_CONTACTO, cuenta.getContactoEmergencia());
		values.put(COLUMN_CUENTA_TEL_CONTACTO, cuenta.getTelefonoEmergencia());
		values.put(COLUMN_CUENTA_CIUDAD, cuenta.getCiudad());
		values.put(COLUMN_CUENTA_SECTOR, cuenta.getSector());
		values.put(COLUMN_CUENTA_TIPO_DOCUMENTO, cuenta.getTipoDocumento());
		values.put(COLUMN_CUENTA_CODIGO_PAIS, cuenta.getCodigopais());
		values.put(COLUMN_CUENTA_DISCAPACIDAD, cuenta.getDiscapacidad());
		values.put(COLUMN_CUENTA_TIPO_SANGRE, cuenta.getTiposangre());
		values.put(COLUMN_CUENTA_NOMBRE_CONTACTO, cuenta.getNombrecontacto());
		values.put(COLUMN_CUENTA_APELLIDO_CONTACTO, cuenta.getApellidocontacto());
		values.put(COLUMN_CUENTA_PAIS_CONTACTO, cuenta.getCodigopaiscontacto());
		values.put(COLUMN_CUENTA_RELACION_CONTACTO, cuenta.getRelacioncontacto());

		database.insert(TABLA_CUENTA, null, values);
		Cursor cursor = database.query(TABLA_CUENTA,
				allColumns, COLUMN_CUENTA_ID + " = '" + cuenta.getId() + "'"  , null,
				null, null, null);
		cursor.moveToFirst();
		Cuenta newCuenta = cursorToCuenta(cursor);
		cursor.close();
		return newCuenta;
	}

	public void updateCuenta(Cuenta cuenta) {
		System.out.println("updateCuenta with id: " + cuenta.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_CUENTA_ID, cuenta.getId());
		values.put(COLUMN_CUENTA_NUMDOC, cuenta.getNumeroDocumento());
		values.put(COLUMN_CUENTA_EMAIL, cuenta.getEmail());
		values.put(COLUMN_CUENTA_PNOMBRE, cuenta.getPrimerNombre());
		values.put(COLUMN_CUENTA_SNOMBRE, cuenta.getSegundoNombre());
		values.put(COLUMN_CUENTA_PAPELLIDO, cuenta.getPrimerApellido());
		values.put(COLUMN_CUENTA_SAPELLIDO, cuenta.getSegundoApellido());
		values.put(COLUMN_CUENTA_FECHA_CREACION, cuenta.getFechaCreacion());
		values.put(COLUMN_CUENTA_ULTIMA_FECHA, cuenta.getUltimaFecha());
		values.put(COLUMN_CUENTA_NUMERO_TELEFONO, cuenta.getNumeroTelefono());
		values.put(COLUMN_CUENTA_ALERGIA_MEDICAMENTO, cuenta.getAlergiaMedicamento());
		values.put(COLUMN_CUENTA_CONTACTO, cuenta.getContactoEmergencia());
		values.put(COLUMN_CUENTA_TEL_CONTACTO, cuenta.getTelefonoEmergencia());
		values.put(COLUMN_CUENTA_CIUDAD, cuenta.getCiudad());
		values.put(COLUMN_CUENTA_SECTOR, cuenta.getSector());
		values.put(COLUMN_CUENTA_CODIGO_PAIS, cuenta.getCodigopais());
		values.put(COLUMN_CUENTA_DISCAPACIDAD, cuenta.getDiscapacidad());
		values.put(COLUMN_CUENTA_TIPO_SANGRE, cuenta.getTiposangre());
		values.put(COLUMN_CUENTA_NOMBRE_CONTACTO, cuenta.getNombrecontacto());
		values.put(COLUMN_CUENTA_APELLIDO_CONTACTO, cuenta.getApellidocontacto());
		values.put(COLUMN_CUENTA_PAIS_CONTACTO, cuenta.getCodigopaiscontacto());
		values.put(COLUMN_CUENTA_RELACION_CONTACTO, cuenta.getRelacioncontacto());
		database.update(TABLA_CUENTA, values, null, null);
	}


	public Cuenta getCuenta() {
		Cuenta cuenta = new Cuenta();

		Cursor cursor = database.query(TABLA_CUENTA,
				allColumns, null, null, null, null,  null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			cuenta = cursorToCuenta(cursor);
			cursor.close();
			return cuenta;
		}else{
			cursor.close();
			return null;
		}
	}



}
