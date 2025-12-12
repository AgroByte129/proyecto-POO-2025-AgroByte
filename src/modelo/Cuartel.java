package modelo;

import utilidades.EstadoFenologico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cuartel implements Serializable {
    private int id;
    private float superficie;
    private EstadoFenologico estado;

    private Cultivo cultivo;
    private Huerto huerto;
    private List<PlanCosecha> planes;

    public Cuartel(int id, float sup, Cultivo cult, Huerto huerto) {
        this.id = id;
        this.superficie = sup;
        this.cultivo = cult;
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
    public boolean setEstado(EstadoFenologico estado) {
        if (estado.compareTo(this.estado) > 0) {
            this.estado = estado;
            return true;
        }
        if (this.estado == EstadoFenologico.POSTCOSECHA &&
                estado == EstadoFenologico.REPOSO_INVERNAL) {
            this.estado = estado;
            return true;
        }
        return false;
    }

    public Cultivo getCultivo() {return cultivo;}
    public Huerto getHuerto() {return huerto;}
    public void addPlanCosecha(PlanCosecha planCosecha) {planes.add(planCosecha);} //No realiza verificación, solo agrega
    public PlanCosecha[] getPlanesCosecha() {return planes.toArray(PlanCosecha[]::new);}
}

