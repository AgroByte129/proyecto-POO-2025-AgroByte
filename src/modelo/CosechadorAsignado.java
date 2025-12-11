package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CosechadorAsignado implements Serializable {
    private LocalDate desde;
    private LocalDate hasta;
    private double metaKilos;

    private Cuadrilla cuadrilla;
    private Cosechador cosechador;
    private final ArrayList<Pesaje> pesajes = new ArrayList<>();

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

    public double getCumplimientoMeta() {
        return metaKilos <= 0 ? 0 :
                (pesajes.stream()
                        .mapToDouble(Pesaje::getCantidadKg)
                        .sum() / metaKilos) * 100.0;
    }

    public int getNroPesajesImpagos() {
        return Math.toIntExact(pesajes.stream()
                .filter(p -> !p.isPagado())
                .count());
    }

    public double getMontoPesajesImpagos() {
        return pesajes.stream()
                .filter(p -> !p.isPagado())
                .mapToDouble(Pesaje::getMonto)
                .sum();
    }

    public int getNroPesajesPagados() {
        return Math.toIntExact(pesajes.stream()
                .filter(Pesaje::isPagado)
                .count());
    }

    public double getMontoPesajesPagados() {
        return pesajes.stream()
                .filter(Pesaje::isPagado)
                .mapToDouble(Pesaje::getMonto)
                .sum();
    }

    public void addPesaje(Pesaje p) {
        if (p != null && !pesajes.contains(p)) {
            pesajes.add(p);
        }
    }

    public Pesaje[] getPesajes() {
        return pesajes.toArray(Pesaje[]::new);
    }


}


