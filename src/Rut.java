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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rut rut = (Rut) o;
        return numero.equals(rut.numero);
    }
}

