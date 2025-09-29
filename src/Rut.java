public class Rut {
    private String numero;

    public Rut(String numero) {
        this.numero = numero;

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;

    }

    public String toString() {
        return numero;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rut otro = (Rut) obj;
        return numero.equals(otro.numero);

    }
    @Override
    public int hashCode() {
        return numero.hashCode();
    }
}

