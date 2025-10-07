package prograsof.cinemagenta;

import prograsof.cinemagenta.controladores.ControladorMenu;
import prograsof.cinemagenta.controladores.DBConector;

public class CineMagenta {

    public static void main(String[] args) {
        // Probar conexión con la base de datos al iniciar el programa
        DBConector.test();
        
        ControladorMenu controlador = new ControladorMenu();
        controlador.iniciar();
        
    }
}