package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CosechadorAsignado {
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
        double totalKilos = 0;
        for (Pesaje p : pesajes) {
            totalKilos += p.getCantidadKg();
        }
        if (metaKilos <= 0) return 0;
        return (totalKilos / metaKilos) * 100.0;
    }

    public int getNroPesajesImpagos() {
        int count = 0;
        for (Pesaje p : pesajes) {
            if (!p.isPagado()) count++;
        }
        return count;
    }

    public double getMontoPesajesImpagos() {
        double total = 0;
        for (Pesaje p : pesajes) {
            if (!p.isPagado()) total += p.getMonto();
        }
        return total;
    }

    public int getNroPesajesPagados() {
        int count = 0;
        for (Pesaje p : pesajes) {
            if (p.isPagado()) count++;
        }
        return count;
    }

    public double getMontoPesajesPagados() {
        double total = 0;
        for (Pesaje p : pesajes) {
            if (p.isPagado()) total += p.getMonto();
        }
        return total;
    }

    public void addPesaje(Pesaje p) {
        if (p != null && !pesajes.contains(p)) {
            pesajes.add(p);
        }
    }

    public Pesaje[] getPesajes() {
        return pesajes.toArray(new Pesaje[0]);
    }


}


