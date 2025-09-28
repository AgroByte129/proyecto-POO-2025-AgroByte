import java.util.Date;

public class PlanCosecha {
    private int id;
    private String nombre;
    private Date inicio;
    private Date finEstimado;
    private Date finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;

    PlanCosecha(int id, String nom, Date ini, Date finEst, double meta, double precio, Cuartel cuartel){
        this.id = id;
        this.nombre = nom;
        this.inicio = ini;
        this.finEstimado = finEst;
        this.finReal = finEst;
        this.metaKilos = metaKilos;
        this.precioBaseKilo = precioBaseKilo;
        this.estado = EstadoPlan.PLANIFICADO;
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

    }
    public boolean addCuadrilla(int idCuad, String nomCuadrilla, Supervisor supervisor){

    }
    public boolean addCosechadorToCuadrilla(int idCuad, Date fIni, Date fFin, double meta, Cosechador cos){

    }
    public Cuadrilla getCuadrilla(int idCuadrilla){

    }
}
