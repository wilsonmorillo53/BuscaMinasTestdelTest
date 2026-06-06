package main.java.com.buscaminas.controlador;

import java.util.Scanner;
import main.java.com.buscaminas.excepciones.*;
import main.java.com.buscaminas.modelo.Tablero;
import main.java.com.buscaminas.persistencia.GestorArchivos;
import main.java.com.buscaminas.vista.VistaConsola;

public class JuegoControlador {
    private Tablero tablero;
    private VistaConsola vista;
    private Scanner scanner;
    private GestorArchivos gestor;

    public JuegoControlador() {
        this.vista = new VistaConsola();
        this.scanner = new Scanner(System.in);
        this.gestor = new GestorArchivos();
        this.tablero = new Tablero();
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMensaje("=== MENÚ DEL VIDEOJUEGO ===");
            vista.mostrarMensaje("1. Comenzar Partida");
            vista.mostrarMensaje("2. Cargar Partidas");
            vista.mostrarMensaje("3. Creadores del Juego");
            vista.mostrarMensaje("4. Salir");
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    jugar(new Tablero());
                    break;
                case "2":
                    cargarPartidaMenu();
                    break;
                case "3":
                    mostrarCreadores();
                    break;
                case "4":
                    salir = true;
                    vista.mostrarMensaje("¡Hasta luego!");
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
        }
    }

    private void jugar(Tablero t) {
        this.tablero = t;
        vista.mostrarMensaje("=== BUSCAMINAS ===");
        boolean regresar = false;
        while (!regresar && !tablero.isJuegoTerminado()) {
            vista.mostrarTablero(tablero, false);
            vista.mostrarAyuda();
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            switch (input.toUpperCase()) {
                case "S":
                    regresar = true;
                    vista.mostrarMensaje("Regresando al menú principal.");
                    break;
                case "G":
                    guardarPartidaMenu();
                    break;
                case "C":
                    cargarPartidaMenuDuranteJuego();
                    break;
                default:
                    try {
                        procesarComando(input);
                    } catch (CoordenadaInvalidaException | CasillaYaDescubiertaException e) {
                        vista.mostrarMensaje("Error: " + e.getMessage());
                    }
            }
        }
        if (tablero.isJuegoTerminado()) {
            vista.mostrarTablero(tablero, true);
            vista.mostrarMensaje(tablero.isVictoria() ? "¡VICTORIA!" : "¡BOOM! Has perdido.");
            vista.mostrarMensaje("Presione Enter para regresar al menú principal...");
            scanner.nextLine();
        }
    }

    private void guardarPartidaMenu() {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        vista.mostrarMensaje("=== ESTADO DE GUARDADO ===");
        for (int i = 1; i <= 3; i++) {
            main.java.com.buscaminas.modelo.PartidaGuardada pg = gestor.cargarDatosPartida(i);
            if (pg != null) {
                vista.mostrarMensaje("  Slot " + i + ": " + pg.getFechaGuardado().format(formatter));
            } else {
                vista.mostrarMensaje("  Slot " + i + ": (Vacío)");
            }
        }
        int slotUsado = gestor.guardarPartidaAuto(tablero);
        if (slotUsado > 0) {
            vista.mostrarMensaje("Partida guardada en Slot " + slotUsado + ".");
        } else {
            vista.mostrarMensaje("Error al guardar la partida.");
        }
    }

    private void cargarPartidaMenu() {
        Tablero t = obtenerPartidaDeMenu();
        if (t != null) {
            jugar(t);
        }
    }

    private void cargarPartidaMenuDuranteJuego() {
        Tablero t = obtenerPartidaDeMenu();
        if (t != null) {
            this.tablero = t;
            vista.mostrarMensaje("Partida cargada.");
        }
    }

    private Tablero obtenerPartidaDeMenu() {
        boolean regresar = false;
        while (!regresar) {
            vista.mostrarMensaje("=== SLOTS DE CARGA ===");
            for (int i = 1; i <= 3; i++) {
                main.java.com.buscaminas.modelo.PartidaGuardada pg = gestor.cargarDatosPartida(i);
                if (pg != null) {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    String fechaStr = pg.getFechaGuardado().format(formatter);
                    vista.mostrarMensaje(i + ". Slot " + i + " (Guardada el " + fechaStr + ")");
                } else {
                    vista.mostrarMensaje(i + ". Slot " + i + " (Vacío)");
                }
            }
            vista.mostrarMensaje("R. Regresar");
            System.out.print("> ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("R")) {
                return null;
            } else if (input.equals("1") || input.equals("2") || input.equals("3")) {
                int slot = Integer.parseInt(input);
                if (!gestor.existePartida(slot)) {
                    vista.mostrarMensaje("El Slot " + slot + " está vacío.");
                } else {
                    Tablero t = gestor.cargarPartida(slot);
                    if (t != null) {
                        return t;
                    } else {
                        vista.mostrarMensaje("Error al cargar la partida.");
                    }
                }
            } else {
                vista.mostrarMensaje("Opción inválida.");
            }
        }
        return null;
    }

    private void mostrarCreadores() {
        boolean regresar = false;
        while (!regresar) {
            vista.mostrarMensaje("=== CREADORES ===");
            vista.mostrarMensaje("JAZMIN MILENA HERNANDEZ CHERE");
            vista.mostrarMensaje("JOSSELIN ANDREA GUAJALA IZQUIERDO");
            vista.mostrarMensaje("MORILLO ESTRADA WILSON HAROLD");
            vista.mostrarMensaje("CRISTOPHER GABRIEL PILOZO VELASQUEZ");
            vista.mostrarMensaje("");
            vista.mostrarMensaje("R. Regresar");
            System.out.print("> ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("R")) {
                regresar = true;
            } else {
                vista.mostrarMensaje("Opción inválida.");
            }
        }
    }

    public void procesarComando(String input) throws CoordenadaInvalidaException, CasillaYaDescubiertaException {
        boolean esMarcar = input.toUpperCase().startsWith("M");
        String coord = esMarcar ? input.substring(1) : input;
        if (coord.length() < 2) throw new CoordenadaInvalidaException("Formato inválido");
        char letra = Character.toUpperCase(coord.charAt(0));
        int num;
        try { num = Integer.parseInt(coord.substring(1)); }
        catch (NumberFormatException e) { throw new CoordenadaInvalidaException("Número inválido"); }
        int fila = num - 1;
        int columna = letra - 'A';
        if (fila < 0 || fila >= tablero.getTamano() || columna < 0 || columna >= tablero.getTamano())
            throw new CoordenadaInvalidaException("Coordenada fuera del tablero");
        if (esMarcar) tablero.marcarCasilla(fila, columna);
        else tablero.descubrirCasilla(fila, columna);
    }

    public Tablero getTablero() {
        return tablero;
    }
}