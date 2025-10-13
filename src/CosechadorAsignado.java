import java.time.LocalDate;

public class CosechadorAsignado {
    private LocalDate desde;
    private LocalDate hasta;
    private double metaKilos;

    private Cuadrilla cuadrilla;   
    private Cosechador cosechador; 

    public CosechadorAsignado(LocalDate fIni, LocalDate fFin, double meta,
                              Cuadrilla cuad, Cosechador cos) {
        this.desde = fIni;
        this.hasta = fFin;
        this.metaKilos = meta;
        this.cuadrilla = cuad;
        this.cosechador = cos;
        //establece la relación con cosechador y cuadrilla
        cos.addCuadrilla(this);
    }

    public LocalDate getDesde() {return desde;}
    public void setDesde(LocalDate desde) {this.desde = desde;}
    public LocalDate getHasta() {return hasta;}
    public void setHasta(LocalDate hasta) {this.hasta = hasta;}
    public double getMetaKilos() {return metaKilos;}
    public void setMetaKilos(double metaKilos) {this.metaKilos = metaKilos;}
    public Cuadrilla getCuadrilla() {return cuadrilla;}
    public Cosechador getCosechador() {return cosechador;}
}


