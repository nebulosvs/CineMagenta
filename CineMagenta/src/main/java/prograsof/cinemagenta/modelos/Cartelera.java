package prograsof.cinemagenta.modelos;

import prograsof.cinemagenta.controladores.DBConector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

// Esta es nuestra PeliculaDAO, que se renombró como Cartelera
// DAO: Data Object Access
public class Cartelera {

    
    private Pelicula mapRow(ResultSet rs) throws SQLException {
        Pelicula p = new Pelicula();
        p.setId(rs.getInt("id"));
        p.setTitulo(rs.getString("titulo"));
        p.setDirector(rs.getString("director"));
        p.setAnio(rs.getInt("anio"));
        p.setDuracion(rs.getInt("duracion"));
        p.setGenero(rs.getString("genero"));
        return p;
    }
    
    public void crearPelicula(Pelicula pelicula) {
        String sql = "INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setInt(4, pelicula.getDuracion());
            pstmt.setString(5, pelicula.getGenero());
            pstmt.executeUpdate();
            System.out.println("✅ Película creada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al crear la película: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
    }

    public List<Pelicula> obtenerPeliculas() {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pelicula pelicula = new Pelicula(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getInt("anio"),
                    rs.getInt("duracion"),
                    rs.getString("genero")
                );
                peliculas.add(pelicula);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener las películas: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
        return peliculas;
    }

    public void actualizarPelicula(Pelicula pelicula) {
        String sql = "UPDATE Cartelera SET titulo = ?, director = ?, anio = ?, duracion = ?, genero = ? WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getTitulo());
            pstmt.setString(2, pelicula.getDirector());
            pstmt.setInt(3, pelicula.getAnio());
            pstmt.setInt(4, pelicula.getDuracion());
            pstmt.setString(5, pelicula.getGenero());
            pstmt.setInt(6, pelicula.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Película actualizada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar la película: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
    }

    public void eliminarPelicula(int id) {
        String sql = "DELETE FROM Cartelera WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Película eliminada exitosamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar la película: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
    }
    
    public List<Pelicula> buscarPeliculasPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera WHERE titulo LIKE ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%"); // El '%' permite buscar coincidencias parciales
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pelicula pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                    );
                    peliculas.add(pelicula);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar películas: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
        return peliculas;
    }
    
    public Pelicula obtenerPeliculaPorId(int id) {
        Pelicula pelicula = null;
        String sql = "SELECT id, titulo, director, anio, duracion, genero FROM Cartelera WHERE id = ?";
        try (Connection conn = DBConector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si la película existe, crea el objeto Pelicula
                    pelicula = new Pelicula(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("director"),
                        rs.getInt("anio"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar la película por ID: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
        return pelicula;
    }
    
    
    // Operaciones con SP

    // Lista TODAS las películas (SP).
    public List<Pelicula> listarSP() throws SQLException {
        List<Pelicula> out = new ArrayList<>();
        final String call = "{ CALL sp_listar_peliculas() }";

        try (Connection cn = DBConector.getConnection();
             CallableStatement cs = cn.prepareCall(call);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) out.add(mapRow(rs));
        } catch (IOException e) {
            System.err.println("[DAO] Error leyendo configuración: " + e.getMessage());
        }
        return out;
    }

    /**
     * Filtra por género (LIKE) y/o por rango de años (desde/hasta) usando SP.
     * Cualquier parámetro puede ser null para “ignorar” ese filtro.
     */
    public List<Pelicula> filtrarSP(String generoLike, Integer anioDesde, Integer anioHasta) throws SQLException, IOException {
        List<Pelicula> out = new ArrayList<>();
        final String call = "{ CALL sp_filtrar_peliculas(?, ?, ?) }";

        try (Connection cn = DBConector.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            // 1) género LIKE (nullable)
            if (generoLike == null || generoLike.isBlank()) cs.setNull(1, Types.VARCHAR);
            else cs.setString(1, generoLike.trim());

            // 2) año desde (nullable)
            if (anioDesde == null) cs.setNull(2, Types.INTEGER);
            else cs.setInt(2, anioDesde);

            // 3) año hasta (nullable)
            if (anioHasta == null) cs.setNull(3, Types.INTEGER);
            else cs.setInt(3, anioHasta);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    /** Devuelve la lista de géneros distintos para poblar el combo de filtros. */
    public List<String> listarGeneros() throws SQLException, IOException {
        List<String> out = new ArrayList<>();
        final String sql = """
            SELECT DISTINCT genero
            FROM Cartelera
            WHERE genero IS NOT NULL AND genero <> ''
            ORDER BY genero
            """;
        try (Connection cn = DBConector.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(rs.getString(1));
        }
        return out;
    }
}