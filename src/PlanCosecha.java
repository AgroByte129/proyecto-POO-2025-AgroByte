import java.util.Date; //cambiar a LocalDate
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
        this.finReal = null;
        this.metaKilos = meta;
        this.precioBaseKilo = precio;
        this.estado = EstadoPlan.PLANIFICADO;
        this.cuartel = cuartel;
        this.cuadrillas = new ArrayList<>();

        if (this.cuartel != null) {
            this.cuartel.addPlanCosecha(this);
        }
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Date getInicio() {
        return inicio;
    }
    public Date getFinEstimado() {
        return finEstimado;
    }
    public Date getFinReal() {
        return finReal;
    }
    public double getMetaKilos() {
        return metaKilos;
    }
    public void setMetaKilos(double metaKilos) {
        this.metaKilos = metaKilos;
    }
    public double getPrecioBaseKilo() {
        return precioBaseKilo;
    }
    public void setPrecioBaseKilo(double precioBaseKilo) {
        this.precioBaseKilo = precioBaseKilo;
    }
    public EstadoPlan getEstado() {
        return estado;
    }
    public void setEstado(EstadoPlan estado) {
        this.estado = estado;
    }
    public Cuartel getCuartel() {
        return cuartel;
    }
    private Cuadrilla findCuadrillaById(int idCuad) {
        for (Cuadrilla c : cuadrillas) {
            if (c.getId() == idCuad) {
                return c;
            }
        }
        return null;
    }
    public boolean addCuadrilla(int idCuad, String nomCuadrilla, Supervisor supervisor){
        if (findCuadrillaById(idCuad) != null) {
            return false;
        }
        Cuadrilla nueva = new Cuadrilla(idCuad, nomCuadrilla, supervisor, this);
        cuadrillas.add(nueva);
        return true;
    }
    public ArrayList<Cuadrilla> getCuadrillas() {
        return cuadrillas;
    }
    public boolean addCosechadorToCuadrilla(int idCuad, Date fIni, Date fFin, double meta, Cosechador cos){
        Cuadrilla c = findCuadrillaById(idCuad);
        if (c == null) {
            return false;
        }
        return c.addCosechador(fIni, fFin, meta, cos);
    }
}
