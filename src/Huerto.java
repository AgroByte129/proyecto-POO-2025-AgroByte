import java.util.ArrayList;

public class Huerto {
    private String nombre;
    private float superficie;
    private String ubicacion;
    private Propietario propietario;
    private ArrayList<Cuartel> cuarteles = new ArrayList<>();

    public Huerto(String nombre, float superficie, String ubicacion, Propietario propietario) {
        this.nombre = nombre;
        this.superficie = superficie;
        this.ubicacion = ubicacion;
        this.propietario = propietario;

        if (this.propietario != null) {
            this.propietario.addHuerto(this);
        }
    }
    public String getNombre() {return nombre;}
    public float getSuperficie() {return superficie;}
    public void setSuperficie(float superficie) {this.superficie = superficie;}
    public String getUbicacion() {return ubicacion;}
    public void setUbicacion(String ubicacion) {this.ubicacion = ubicacion;}
    public Propietario getPropietario() {return propietario;}
    public void setPropietario(Propietario propietario) {this.propietario = propietario;}

    public boolean addCuartel(int id, float sup, Cultivo cult) {
        for (Cuartel c : cuarteles) {
            if (c.getId() == id) {
                return false;
            }
        }

        Cuartel cuartel = new Cuartel(id, sup, cult, this);

        if (cult != null) {
            cult.addCuartel(cuartel);
        }

        return cuarteles.add(cuartel);
    }

    public Cuartel getCuartel(int id) {
        for (Cuartel c : cuarteles) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public Cuartel[] getCuarteles() {
        return cuarteles.toArray(new Cuartel[0]);
    }
}
