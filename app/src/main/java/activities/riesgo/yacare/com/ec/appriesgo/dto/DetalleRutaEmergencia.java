package activities.riesgo.yacare.com.ec.appriesgo.dto;



/**
 * Created by yacare on 22/09/2015.
 */
public class DetalleRutaEmergencia {
    private String id;
    private String idRuta;
    private String instruccion;
    private String distancia;


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

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }
}
