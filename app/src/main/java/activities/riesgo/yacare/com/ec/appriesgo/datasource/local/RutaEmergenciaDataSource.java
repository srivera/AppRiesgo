package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.CapaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetallePuntosEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.DetalleRutaEmergencia;
import activities.riesgo.yacare.com.ec.appriesgo.dto.RutaEmergencia;

public class RutaEmergenciaDataSource {

    public static final String TABLA_RUTA_EMERGENCIA = "rutaemergencia";
    public static final String COLUMN_RUTA_ID = "idruta";
    public static final String COLUMN_RUTA_CAPA = "idcapa";
    public static final String COLUMN_RUTA_LATITUD_ORIGEN = "latitudorigen";
    public static final String COLUMN_RUTA_LONGITUD_ORIGEN = "longitudorigen";
    public static final String COLUMN_RUTA_LATITUD_DESTINO = "latituddestino";
    public static final String COLUMN_RUTA_LONGITUD_DESTINO = "longituddestino";
    public static final String COLUMN_RUTA_DISTANCIA = "distancia";


    public static final String TABLA_DETRUTA_EMERGENCIA = "detallerutaemergencia";
    public static final String COLUMN_DETRUTA_ID = "iddetalle";
    public static final String COLUMN_DETRUTA_RUTA = "idruta";
    public static final String COLUMN_DETRUTA_INSTRUCCION = "instruccion";
    public static final String COLUMN_DETRUTA_DISTANCIA = "distancia";

    public static final String TABLA_DETPUNTO_EMERGENCIA = "detallepuntosemergencia";
    public static final String COLUMN_DETPUNTO_ID = "iddetalle";
    public static final String COLUMN_DETPUNTO_RUTA = "idruta";
    public static final String COLUMN_DETPUNTO_LATITUD = "latitud";
    public static final String COLUMN_DETPUNTO_LONGITUD= "longitud";

    private Context context;

    private SQLiteDatabase database;
    private RiesgoLocalSqlLite dbHelper;
    private String[] allColumns = {COLUMN_RUTA_ID,
            COLUMN_RUTA_CAPA,
            COLUMN_RUTA_LATITUD_ORIGEN,
            COLUMN_RUTA_LONGITUD_ORIGEN,
            COLUMN_RUTA_LATITUD_DESTINO,
            COLUMN_RUTA_LONGITUD_DESTINO,
            COLUMN_RUTA_DISTANCIA};

    private String[] allColumnsDetalle = {COLUMN_DETRUTA_ID,
            COLUMN_DETRUTA_RUTA,
            COLUMN_DETRUTA_INSTRUCCION,
            COLUMN_DETRUTA_DISTANCIA};

    private String[] allColumnsDetallePuntos = {COLUMN_DETPUNTO_ID,
            COLUMN_DETPUNTO_RUTA,
            COLUMN_DETPUNTO_LATITUD,
            COLUMN_DETPUNTO_LONGITUD};

    public RutaEmergenciaDataSource(Context context) {
        dbHelper = new RiesgoLocalSqlLite(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createRutaEmergencia(RutaEmergencia rutaEmergencia) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RUTA_ID, rutaEmergencia.getId());
        values.put(COLUMN_RUTA_CAPA, rutaEmergencia.getIdCapa());
        values.put(COLUMN_RUTA_LATITUD_ORIGEN, rutaEmergencia.getLatitudOrigen());
        values.put(COLUMN_RUTA_LONGITUD_ORIGEN, rutaEmergencia.getLongitudOrigen());
        values.put(COLUMN_RUTA_LATITUD_DESTINO, rutaEmergencia.getLatitudDestino());
        values.put(COLUMN_RUTA_LONGITUD_DESTINO, rutaEmergencia.getLongitudDestino());
        values.put(COLUMN_RUTA_DISTANCIA, rutaEmergencia.getDistancia());

        database.insert(TABLA_RUTA_EMERGENCIA, null, values);

        for(DetalleRutaEmergencia detalleRutaEmergencia : rutaEmergencia.getDetalleRuta()){
            createRutaDetalleEmergencia(detalleRutaEmergencia);
        }

        for(DetallePuntosEmergencia detallePuntosEmergencia : rutaEmergencia.getDetallePuntos()){
            createRutaDetallePuntos(detallePuntosEmergencia);
        }
  }

    private void createRutaDetalleEmergencia(DetalleRutaEmergencia detalleRutaEmergencia) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETRUTA_ID, detalleRutaEmergencia.getId());
        values.put(COLUMN_DETRUTA_RUTA, detalleRutaEmergencia.getIdRuta());
        values.put(COLUMN_DETRUTA_INSTRUCCION, detalleRutaEmergencia.getInstruccion());
        values.put(COLUMN_DETRUTA_DISTANCIA, detalleRutaEmergencia.getDistancia());
        database.insert(TABLA_DETRUTA_EMERGENCIA, null, values);
    }

    private void createRutaDetallePuntos(DetallePuntosEmergencia detallePuntosEmergencia) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DETPUNTO_ID, detallePuntosEmergencia.getId());
        values.put(COLUMN_DETPUNTO_RUTA, detallePuntosEmergencia.getIdRuta());
        values.put(COLUMN_DETPUNTO_LATITUD, detallePuntosEmergencia.getLatitud());
        values.put(COLUMN_DETPUNTO_LONGITUD, detallePuntosEmergencia.getLongitud());
        database.insert(TABLA_DETPUNTO_EMERGENCIA, null, values);
    }


    public void deleteAllRutaEmergencia() {
        deleteAllDetalleEmergencia();
        deleteAllDetallePuntos();
        database.delete(TABLA_RUTA_EMERGENCIA, null, null);
    }

    private void deleteAllDetalleEmergencia() {
        database.delete(TABLA_DETRUTA_EMERGENCIA, null, null);
    }

    private void deleteAllDetallePuntos() {
        database.delete(TABLA_DETPUNTO_EMERGENCIA, null, null);
    }


    public ArrayList<RutaEmergencia> getAllRutaEmergencia() {
        ArrayList<RutaEmergencia> emergencias = new ArrayList<RutaEmergencia>();

        Cursor cursor = database.query(TABLA_RUTA_EMERGENCIA,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RutaEmergencia elementoEmergencia = cursorToRutaEmergencia(cursor);
            elementoEmergencia.setDetallePuntos(new ArrayList<DetallePuntosEmergencia>());
            elementoEmergencia.getDetallePuntos().addAll(getIdRutaDetallePuntos(elementoEmergencia.getId()));
            elementoEmergencia.setDetalleRuta(new ArrayList<DetalleRutaEmergencia>());
            elementoEmergencia.getDetalleRuta().addAll(getIdRutaDetalleEmergencia(elementoEmergencia.getId()));
            emergencias.add(elementoEmergencia);

            CapaDataSource capaDataSource = new CapaDataSource(context);
            capaDataSource.open();
            elementoEmergencia.setCapa(capaDataSource.getCapaById(elementoEmergencia.getIdCapa()));
            capaDataSource.close();

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return emergencias;
    }

    private ArrayList<DetalleRutaEmergencia> getIdRutaDetalleEmergencia(String idRuta) {
        ArrayList<DetalleRutaEmergencia> emergencias = new ArrayList<DetalleRutaEmergencia>();

        Cursor cursor = database.query(TABLA_DETRUTA_EMERGENCIA,
                allColumnsDetalle, COLUMN_DETRUTA_RUTA + " = '" + idRuta + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DetalleRutaEmergencia detalleRutaEmergencia = cursorToDetalleRutaEmergencia(cursor);
            emergencias.add(detalleRutaEmergencia);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return emergencias;
    }

    private ArrayList<DetallePuntosEmergencia> getIdRutaDetallePuntos(String idRuta) {
        ArrayList<DetallePuntosEmergencia> emergencias = new ArrayList<DetallePuntosEmergencia>();

        Cursor cursor = database.query(TABLA_DETPUNTO_EMERGENCIA,
                allColumnsDetallePuntos,  COLUMN_DETPUNTO_RUTA + " = '" + idRuta + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DetallePuntosEmergencia elementoEmergencia = cursorToDetallePuntosEmergencia(cursor);
            emergencias.add(elementoEmergencia);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return emergencias;
    }

    private RutaEmergencia cursorToRutaEmergencia(Cursor cursor) {
        RutaEmergencia emergencia = new RutaEmergencia();
        emergencia.setId(cursor.getString(0));
        emergencia.setIdCapa(cursor.getString(1));
        emergencia.setLatitudOrigen(cursor.getString(2));
        emergencia.setLongitudOrigen(cursor.getString(3));
        emergencia.setLatitudDestino(cursor.getString(4));
        emergencia.setLongitudDestino(cursor.getString(5));
        emergencia.setDistancia(cursor.getString(6));
        return emergencia;
    }

    private DetalleRutaEmergencia cursorToDetalleRutaEmergencia(Cursor cursor) {
        DetalleRutaEmergencia detalleRutaEmergencia = new DetalleRutaEmergencia();
        detalleRutaEmergencia.setId(cursor.getString(0));
        detalleRutaEmergencia.setIdRuta(cursor.getString(1));
        detalleRutaEmergencia.setInstruccion(cursor.getString(2));
        detalleRutaEmergencia.setDistancia(cursor.getString(3));
        return detalleRutaEmergencia;
    }

    private DetallePuntosEmergencia cursorToDetallePuntosEmergencia(Cursor cursor) {
        DetallePuntosEmergencia emergencia = new DetallePuntosEmergencia();
        emergencia.setId(cursor.getString(0));
        emergencia.setIdRuta(cursor.getString(1));
        emergencia.setLatitud(cursor.getString(2));
        emergencia.setLongitud(cursor.getString(3));
        return emergencia;
    }
}
