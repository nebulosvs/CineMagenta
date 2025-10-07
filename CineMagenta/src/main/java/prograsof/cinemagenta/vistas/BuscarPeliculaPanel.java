package prograsof.cinemagenta.vistas;

import prograsof.cinemagenta.modelos.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BuscarPeliculaPanel extends JPanel {
    // DAO
    private final Cartelera cartelera = new Cartelera();
    
    // Controles de filtro
    private JComboBox<String> cbGenero;
    private JCheckBox chkDesde, chkHasta;
    private JSpinner spDesde, spHasta;
    
    //private JTextField busquedaField = new JTextField(20);
    //private JButton buscarButton = new JButton("Buscar");
    //private JTable tablaResultados;
    //private DefaultTableModel tablaModelo;

    // Tabla
    private JTable tabla;
    private DefaultTableModel modelo;
    
    public BuscarPeliculaPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        add(crearBarraFiltros(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        
        // Cargar géneros una vez arranque el UI
        SwingUtilities.invokeLater(this::cargarGenerosEnCombo);
        
        //JPanel panelSuperior = new JPanel();
        //panelSuperior.add(new JLabel("Título:"));
        //panelSuperior.add(busquedaField);
        //panelSuperior.add(buscarButton);
        //add(panelSuperior, BorderLayout.NORTH);

        //String[] columnas = {"ID", "Título", "Director", "Año", "Duración", "Género"};
        //tablaModelo = new DefaultTableModel(columnas, 0);
        //tablaResultados = new JTable(tablaModelo);
        //JScrollPane scrollPane = new JScrollPane(tablaResultados);
        //add(scrollPane, BorderLayout.CENTER);

        //buscarButton.addActionListener(e -> buscarPeliculas());
    }

     /* ---------- UI ---------- */

    private JComponent crearBarraFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));

        // Género
        p.add(new JLabel("Género:"));
        cbGenero = new JComboBox<>();
        cbGenero.addItem("(Todos)");       // placeholder; luego se llena con BD
        cbGenero.setPreferredSize(new Dimension(160, 26));
        p.add(cbGenero);

        // Años (Desde/Hasta) con check para habilitar
        chkDesde = new JCheckBox("Desde");
        spDesde  = new JSpinner(new SpinnerNumberModel(2000, 1900, 2100, 1));
        spDesde.setPreferredSize(new Dimension(90, 26));
        spDesde.setEnabled(false);
        chkDesde.addActionListener(e -> spDesde.setEnabled(chkDesde.isSelected()));
        p.add(chkDesde);
        p.add(spDesde);

        chkHasta = new JCheckBox("Hasta");
        spHasta  = new JSpinner(new SpinnerNumberModel(2025, 1900, 2100, 1));
        spHasta.setPreferredSize(new Dimension(90, 26));
        spHasta.setEnabled(false);
        chkHasta.addActionListener(e -> spHasta.setEnabled(chkHasta.isSelected()));
        p.add(chkHasta);
        p.add(spHasta);

        // Botones
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> aplicarBusqueda());
        p.add(btnBuscar);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> resetFiltros());
        p.add(btnReset);

        return p;
    }
    private JComponent crearTabla() {
        modelo = new DefaultTableModel(
                new Object[]{"ID", "Título", "Director", "Año", "Duración", "Género"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        return new JScrollPane(tabla);
    }

    /* ---------- Lógica ---------- */

    private void cargarGenerosEnCombo() {
        try {
            cbGenero.removeAllItems();
            cbGenero.addItem("(Todos)");
            for (String g : cartelera.listarGeneros()) {
                if (g != null && !g.isBlank()) cbGenero.addItem(g);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No fue posible cargar géneros:\n" + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarBusqueda() {
        modelo.setRowCount(0);

        String generoSel = (String) cbGenero.getSelectedItem();
        String genero = (generoSel == null || "(Todos)".equalsIgnoreCase(generoSel)) ? null : generoSel;

        Integer desde = chkDesde.isSelected() ? (Integer) spDesde.getValue() : null;
        Integer hasta = chkHasta.isSelected() ? (Integer) spHasta.getValue() : null;

        if (desde != null && hasta != null && desde > hasta) {
            JOptionPane.showMessageDialog(this,
                    "El año 'Desde' no puede ser mayor que 'Hasta'.",
                    "Rango inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Pelicula> lista = cartelera.filtrarSP(
                    genero,
                    desde,
                    hasta
            );
            for (Pelicula p : lista) {
                modelo.addRow(new Object[]{
                        p.getId(), p.getTitulo(), p.getDirector(),
                        p.getAnio(), p.getDuracion(), p.getGenero()
                });
            }
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFiltros() {
        cbGenero.setSelectedIndex(0);
        chkDesde.setSelected(false);
        chkHasta.setSelected(false);
        spDesde.setEnabled(false);
        spHasta.setEnabled(false);
        modelo.setRowCount(0); // limpia la tabla; si prefieres, recarga toda la cartelera aquí
    }

    /* ---------- (opcional) método para exponer el modelo/tabla desde el menú ---------- */

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModelo() { return modelo; }

    /**
     * private void buscarPeliculas() {
        tablaModelo.setRowCount(0);
        String tituloBusqueda = busquedaField.getText();
        
        List<Pelicula> peliculas = cartelera.buscarPeliculasPorTitulo(tituloBusqueda);
        
        for (Pelicula p : peliculas) {
            Object[] fila = {
                p.getId(),
                p.getTitulo(),
                p.getDirector(),
                p.getAnio(),
                p.getDuracion(),
                p.getGenero()
            };
            tablaModelo.addRow(fila);
        }
    }
     */
    

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
