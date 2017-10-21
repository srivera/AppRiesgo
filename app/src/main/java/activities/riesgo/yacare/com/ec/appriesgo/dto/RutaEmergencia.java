package activities.riesgo.yacare.com.ec.appriesgo.dto;


import java.util.ArrayList;

/**
 * Created by yacare on 22/09/2015.
 */
public class RutaEmergencia {
    private String id;
    private String idCapa;
    private String latitudOrigen;
    private String longitudOrigen;
    private String latitudDestino;
    private String longitudDestino;
    private String distancia;
    private Double distanciaTramos;
    private String orden;

    private Capa capa;

    ArrayList<DetalleRutaEmergencia> detalleRuta;

    ArrayList<DetallePuntosEmergencia> detallePuntos;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCapa() {
        return idCapa;
    }

    public void setIdCapa(String idCapa) {
        this.idCapa = idCapa;
    }

    public String getLatitudOrigen() {
        return latitudOrigen;
    }

    public void setLatitudOrigen(String latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }

    public String getLongitudOrigen() {
        return longitudOrigen;
    }

    public void setLongitudOrigen(String longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }

    public String getLatitudDestino() {
        return latitudDestino;
    }

    public void setLatitudDestino(String latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public String getLongitudDestino() {
        return longitudDestino;
    }

    public void setLongitudDestino(String longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public Capa getCapa() {
        return capa;
    }

    public void setCapa(Capa capa) {
        this.capa = capa;
    }

    public ArrayList<DetalleRutaEmergencia> getDetalleRuta() {
        return detalleRuta;
    }

    public void setDetalleRuta(ArrayList<DetalleRutaEmergencia> detalleRuta) {
        this.detalleRuta = detalleRuta;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public ArrayList<DetallePuntosEmergencia> getDetallePuntos() {
        return detallePuntos;
    }

    public void setDetallePuntos(ArrayList<DetallePuntosEmergencia> detallePuntos) {
        this.detallePuntos = detallePuntos;
    }

    public Double getDistanciaTramos() {
        return distanciaTramos;
    }

    public void setDistanciaTramos(Double distanciaTramos) {
        this.distanciaTramos = distanciaTramos;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
}
