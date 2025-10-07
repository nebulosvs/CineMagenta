package prograsof.cinemagenta.modelos;

// Modelo abstracto
public class Pelicula {
    
    // Atributos
    private int id;
    private String titulo;
    private String director;
    private int anio;
    private int duracion; // en minutos
    private String genero;

    // Constructor
    public Pelicula(int id, String titulo, String director, int anio, int duracion, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.duracion = duracion;
        this.genero = genero;
    }

    // Constructor vacío (necesario para el DAO)
    public Pelicula() {
    }
    
    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public int getAnio() {
        return anio;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getGenero() {
        return genero;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Título: %s, Director: %s, Año: %d, Duración: %d min, Género: %s",
            id, titulo, director, anio, duracion, genero);
    }
}
