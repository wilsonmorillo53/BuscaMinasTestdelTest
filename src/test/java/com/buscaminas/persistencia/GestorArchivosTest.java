package test.java.com.buscaminas.persistencia;

import main.java.com.buscaminas.modelo.Tablero;
import main.java.com.buscaminas.persistencia.GestorArchivos;

import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GestorArchivosTest {

    private static final String ARCHIVO_PRUEBA = "partida.ser";

    private GestorArchivos gestor;
    private Tablero tablero;

    @BeforeEach
    void inicializar() {
        gestor = new GestorArchivos();
        tablero = new Tablero();
    }

    @AfterEach
    void eliminarArchivoPrueba() {
        File archivo = new File(ARCHIVO_PRUEBA);

        if (archivo.exists()) {
            assertTrue(archivo.delete());
        }
    }

    @Test
    @DisplayName("Debe guardar y cargar correctamente una partida")
    void debeGuardarYCargarPartida() {

        boolean guardado = gestor.guardarPartida(tablero);

        assertTrue(guardado);

        Tablero tableroCargado = gestor.cargarPartida();

        assertNotNull(tableroCargado);
        assertNotSame(tablero, tableroCargado);
        assertEquals(
                tablero.getTamano(),
                tableroCargado.getTamano()
        );
    }

    @Test
    @DisplayName("Debe retornar null cuando el archivo no existe")
    void debeRetornarNullSiArchivoNoExiste() {

        File archivo = new File(ARCHIVO_PRUEBA);

        if (archivo.exists()) {
            archivo.delete();
        }

        Tablero tableroCargado = gestor.cargarPartida();

        assertNull(tableroCargado);
    }

    @Test
    @DisplayName("Debe sobrescribir una partida existente")
    void debeSobrescribirPartidaExistente() {

        assertTrue(gestor.guardarPartida(tablero));

        Tablero nuevoTablero = new Tablero();

        assertTrue(gestor.guardarPartida(nuevoTablero));

        Tablero tableroCargado = gestor.cargarPartida();

        assertNotNull(tableroCargado);
        assertEquals(
                nuevoTablero.getTamano(),
                tableroCargado.getTamano()
        );
    }
}