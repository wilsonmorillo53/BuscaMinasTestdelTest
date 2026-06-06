package main.java.com.buscaminas.modelo;

import java.io.Serializable;
import java.util.Random;
import main.java.com.buscaminas.excepciones.CasillaYaDescubiertaException;

public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int TAMANO = 10;
    private static final int TOTAL_MINAS = 10;

    private Casilla[][] casillas;
    private boolean juegoTerminado;
    private boolean victoria;

    public Tablero() {
        casillas = new Casilla[TAMANO][TAMANO];

        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                casillas[i][j] = new Casilla();
            }
        }

        juegoTerminado = false;
        victoria = false;

        colocarMinas();
        calcularNumeros();
    }

    private void colocarMinas() {
        Random rand = new Random();
        int minasColocadas = 0;

        while (minasColocadas < TOTAL_MINAS) {
            int fila = rand.nextInt(TAMANO);
            int columna = rand.nextInt(TAMANO);

            if (!casillas[fila][columna].tieneMina()) {
                casillas[fila][columna].setTieneMina(true);
                minasColocadas++;
            }
        }
    }

    private void calcularNumeros() {
        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {
                if (!casillas[i][j].tieneMina()) {
                    int count = contarMinasAlrededor(i, j);
                    casillas[i][j].setMinasAlrededor(count);
                }
            }
        }
    }

    private int contarMinasAlrededor(int fila, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                int nuevaFila = fila + i;
                int nuevaCol = col + j;

                if (nuevaFila >= 0 && nuevaFila < TAMANO
                        && nuevaCol >= 0 && nuevaCol < TAMANO) {

                    if (casillas[nuevaFila][nuevaCol].tieneMina()) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public void descubrirCasilla(int fila, int columna)
            throws CasillaYaDescubiertaException {

        if (juegoTerminado) {
            return;
        }

        Casilla casilla = casillas[fila][columna];

        if (casilla.isDescubierta()) {
            throw new CasillaYaDescubiertaException(
                    "La casilla ya está descubierta.");
        }

        if (casilla.isMarcada()) {
            return;
        }

        if (casilla.tieneMina()) {
            juegoTerminado = true;
            victoria = false;
            return;
        }

        revelarCasilla(fila, columna);

        if (verificarVictoria()) {
            juegoTerminado = true;
            victoria = true;
        }
    }

    private void revelarCasilla(int fila, int columna) {

        if (fila < 0 || fila >= TAMANO
                || columna < 0 || columna >= TAMANO) {
            return;
        }

        Casilla casilla = casillas[fila][columna];

        if (casilla.isDescubierta() || casilla.isMarcada()) {
            return;
        }

        if (casilla.tieneMina()) {
            return;
        }

        // CORREGIDO
        casilla.descubrir();

        if (casilla.getMinasAlrededor() == 0) {

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    if (i == 0 && j == 0) {
                        continue;
                    }

                    revelarCasilla(fila + i, columna + j);
                }
            }
        }
    }

    private boolean verificarVictoria() {

        for (int i = 0; i < TAMANO; i++) {
            for (int j = 0; j < TAMANO; j++) {

                Casilla c = casillas[i][j];

                if (!c.tieneMina() && !c.isDescubierta()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void marcarCasilla(int fila, int columna) {

        if (!juegoTerminado
                && !casillas[fila][columna].isDescubierta()) {

            casillas[fila][columna].alternarMarcado();
        }
    }

    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public int getTamano() {
        return TAMANO;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean isVictoria() {
        return victoria;
    }
}