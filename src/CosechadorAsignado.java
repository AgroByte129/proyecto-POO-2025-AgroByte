import java.util.Date;
import java.time.LocalDate;

public class CosechadorAsignado {
    private Date desde;
    private Date hasta;
    private double metaKilos;

    private Cuadrilla cuadrilla;   
    private Cosechador cosechador; 

    public CosechadorAsignado(Date fIni, Date fFin, double meta,
                              Cuadrilla cuad, Cosechador cos) {
        this.desde = fIni;
        this.hasta = fFin;
        this.metaKilos = meta;
        this.cuadrilla = cuad;
        this.cosechador = cos;
    }

    public LocalDate getDesde() {
        return convertir(desde);
    }

    public void setDesde(LocalDate desde) {
        this.desde = convertir(desde);
    }

    public LocalDate getHasta() {
        return convertir(hasta);
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = convertir(hasta);
    }

    public double getMetaKilos() {
        return metaKilos;
    }

    public void setMetaKilos(double metaKilos) {
        this.metaKilos = metaKilos;
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }

    public Cosechador getCosechador() {
        return cosechador;
    }

    private LocalDate convertir(Date fecha) {
        if (fecha == null) return null;
        return new java.sql.Date(fecha.getTime()).toLocalDate();
    }

    private Date convertir(LocalDate fecha) {
        if (fecha == null) return null;
        return java.sql.Date.valueOf(fecha);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true
        if(o != null || getClass() != o.getClass())) return false;
        CosechadorAsignado cosAs = (CosechadorAsignado) o;

    }
}


