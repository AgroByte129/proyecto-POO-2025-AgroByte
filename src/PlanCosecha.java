import java.time.LocalDate;
import java.util.ArrayList;

public class PlanCosecha {
    private int id;
    private String nombre;
    private LocalDate inicio;
    private LocalDate finEstimado;
    private LocalDate finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;
    private Cuartel cuartel;
    private ArrayList<Cuadrilla> cuadrillas;

    public PlanCosecha(int id, String nom, LocalDate ini, LocalDate finEst,
                       double meta, double precio, Cuartel cuartel){
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

    public LocalDate getInicio(){ return inicio; }
    public void setInicio(LocalDate inicio){ this.inicio = inicio; }

    public LocalDate getFinEstimado(){ return finEstimado; }
    public void setFinEstimado(LocalDate finEstimado){ this.finEstimado = finEstimado; }

    public LocalDate getFinReal(){ return finReal; }
    public void setFinReal(LocalDate finReal){ this.finReal = finReal; }

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

    public boolean addCosechadorToCuadrilla(int idCuad, LocalDate fIni,
                                            LocalDate fFin, double meta, Cosechador cosechador){
        for (Cuadrilla c : cuadrillas){
            if (c.getId() == idCuad){
                return c.addCosechador(fIni, fFin, meta, cosechador);
            }
        }
        return false;
    }

    public Cuadrilla[] getCuadrillas(){
        return cuadrillas.toArray(new Cuadrilla[0]);
    }
}
