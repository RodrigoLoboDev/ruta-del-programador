package ar.codersacademy.ruta.modelo;

/** Alumno registrado en la netbook (CU01). */
public class Alumno {
    private final int id;
    private String nombre;
    private String apellido;
    private final int idFranja;

    public Alumno(int id, String nombre, String apellido, int idFranja) {
        this.id = id;
        setNombre(nombre);
        setApellido(apellido);
        this.idFranja = idFranja;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getIdFranja() { return idFranja; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del alumno no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido del alumno no puede estar vacío");
        }
        this.apellido = apellido.trim();
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
