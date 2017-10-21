package activities.riesgo.yacare.com.ec.appriesgo.dto;



/**
 * Created by yacare on 22/09/2015.
 */
public class DetallePuntosEmergencia {
    private String id;
    private String idRuta;
    private String longitud;
    private String latitud;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }
}
