import java.util.ArrayList;

public class Cuartel {
    private int id;
    private float superficie;
    private EstadoFenologico estado;

    private Cultivo cultivo;
    private Huerto huerto;
    private ArrayList<PlanCosecha> planes;

    public Cuartel(int id, float sup, Cultivo cult, Huerto huerto) {
        this.id = id;
        this.superficie = sup;
        this.cultivo = cultivo;
        this.huerto = huerto;
        this.estado = EstadoFenologico.REPOSO_INVERNAL;
        this.planes = new ArrayList<>();
    }

    public int getId() {return id;}
    public float getSuperficie() {return superficie;}
    public void setSuperficie(float superficie) {this.superficie = superficie;}
    public float getRendimientoEsperado(){
        return cultivo.getRendimiento();
    }
    public EstadoFenologico getEstado() {return estado;}
    public Cultivo getCultivo() {return cultivo;}
    public Huerto getHuerto() {return huerto;}
    public PlanCosecha[] getPlanes() {return planes.toArray(new PlanCosecha[0]);}
}

