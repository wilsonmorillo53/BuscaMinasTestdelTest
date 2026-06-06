package main.java.com.buscaminas.modelo;

import java.io.Serializable;

/**
 * Representa una casilla del tablero de Buscaminas.
 */
public class Casilla implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean tieneMina;
    private boolean descubierta;
    private boolean marcada;
    private int minasAlrededor;

    /**
     * Crea una casilla vacía.
     */
    public Casilla() {
        this.tieneMina = false;
        this.descubierta = false;
        this.marcada = false;
        this.minasAlrededor = 0;
    }

    // Getters

    public boolean tieneMina() {
        return tieneMina;
    }

    public boolean isDescubierta() {
        return descubierta;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public int getMinasAlrededor() {
        return minasAlrededor;
    }

    // Setters

    public void setTieneMina(boolean tieneMina) {
        this.tieneMina = tieneMina;
    }

    public void setMinasAlrededor(int minasAlrededor) {
        this.minasAlrededor = minasAlrededor;
    }

    /**
     * Descubre la casilla.
     */
    public void descubrir() {
        this.descubierta = true;
    }

    /**
     * Alterna el estado de marcado de la casilla.
     * Solo puede marcarse si aún no ha sido descubierta.
     */
    public void alternarMarcado() {
        if (!descubierta) {
            marcada = !marcada;
        }
    }

    /**
     * Indica si la casilla está vacía
     * (sin mina y sin minas alrededor).
     */
    public boolean estaVacia() {
        return !tieneMina && minasAlrededor == 0;
    }
}