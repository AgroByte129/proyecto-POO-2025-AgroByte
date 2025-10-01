import java.util.Date;
import java.util.ArrayList;

public class PlanCosecha {
    private int id;
    private String nombre;
    private Date inicio;
    private Date finEstimado;
    private Date finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;
    private Cuartel cuartel;
    private ArrayList<Cuadrilla> cuadrillas;

    public PlanCosecha(int id, String nom, Date ini, Date finEst, double meta, double precio, Cuartel cuartel){
        this.id = id;
        this.nombre = nom;
        this.inicio = ini;
        this.finEstimado = finEst;
        this.metaKilos = meta;
        this.precioBaseKilo = precio;
        this.cuartel = cuartel;
        this.estado = EstadoPlan.PLANIFICADO;
        this.cuadrillas = new ArrayList<>();
        if (cuartel != null){ cuartel.addPlanCosecha(this); }
    }

    public int getId(){ return id; }
    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public Date getInicio(){ return inicio; }
    public void setInicio(Date inicio){ this.inicio = inicio; }
    public Date getFinEstimado(){ return finEstimado; }
    public void setFinEstimado(Date finEstimado){ this.finEstimado = finEstimado; }
    public Date getFinReal(){ return finReal; }
    public void setFinReal(Date finReal){ this.finReal = finReal; }
    public double getMetaKilos(){ return metaKilos; }
    public void setMetaKilos(double metaKilos){ this.metaKilos = metaKilos; }
    public double getPrecioBaseKilo(){ return precioBaseKilo; }
    public void setPrecioBaseKilo(double precioBaseKilo){ this.precioBaseKilo = precioBaseKilo; }
    public EstadoPlan getEstado(){ return estado; }
    public void setEstado(EstadoPlan estado){ this.estado = estado; }
    public Cuartel getCuartel(){ return cuartel; }

    public boolean addCuadrilla(int idCuad, String nomCuad, Supervisor supervisor){
        for (Cuadrilla c : cuadrillas){
            if (c.getId() == idCuad){ return false; }
        }
        Cuadrilla nueva = new Cuadrilla(idCuad, nomCuad, supervisor, this);
        cuadrillas.add(nueva);
        return true;
    }

    public boolean addCosechadorToCuadrilla(int idCuad, Date fIni, Date fFin, double meta, Cosechador cosechador){
        for (Cuadrilla c : cuadrillas){
            if (c.getId() == idCuad){ return c.addCosechador(fIni, fFin, meta, cosechador); }
        }
        return false;
    }

    public Cuadrilla[] getCuadrillas(){
        return cuadrillas.toArray(new Cuadrilla[0]);
    }
}
