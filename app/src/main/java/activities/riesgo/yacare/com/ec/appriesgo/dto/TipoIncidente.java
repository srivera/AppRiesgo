package activities.riesgo.yacare.com.ec.appriesgo.dto;


import java.io.Serializable;

/**
 * Created by yacare on 22/09/2015.
 */
public class TipoIncidente implements Serializable{

    private String id;
    private String nombreIncidente;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreIncidente() {
        return nombreIncidente;
    }

    public void setNombreIncidente(String nombreIncidente) {
        this.nombreIncidente = nombreIncidente;
    }
}
