package main.java.com.buscaminas.vista;

import main.java.com.buscaminas.modelo.Tablero;
import main.java.com.buscaminas.modelo.Casilla;

public class VistaConsola {
    public void mostrarTablero(Tablero tablero, boolean mostrarMinas) {
        int n = tablero.getTamano();
        int cellWidth = 3;

        // Column headers
        System.out.print("   ");
        for (int i = 0; i < n; i++) {
            System.out.print(String.format(" %c ", (char)('A' + i)));
        }
        System.out.println();

        // Top border
        System.out.print("   ");
        for (int i = 0; i < n; i++) System.out.print("───");
        System.out.println();

        for (int i = 0; i < n; i++) {
            // Row number
            System.out.printf("%2d ", i + 1);
            // Cells with vertical separators
            for (int j = 0; j < n; j++) {
                Casilla c = tablero.getCasilla(i, j);
                String contenido;
                if (mostrarMinas && c.tieneMina()) {
                    contenido = "✸"; // mina visible al final del juego
                } else if (!c.isDescubierta()) {
                    if (c.isMarcada()) contenido = "⚑"; // marcada
                    else contenido = "■"; // oculta
                } else {
                    if (c.tieneMina()) contenido = "✹"; // mina explotada
                    else {
                        int num = c.getMinasAlrededor();
                        contenido = (num == 0) ? " " : Integer.toString(num);
                    }
                }
                // centrado en campo de ancho fijo
                System.out.print(String.format(" %1s", padCenter(contenido, cellWidth-1)));
            }
            System.out.println();
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarAyuda() {
        System.out.println("Comandos:");
        System.out.println(" - <letra><numero>  : descubrir (ej: A5)");
        System.out.println(" - M<letra><numero> : marcar/desmarcar (ej: MA5)");
        System.out.println(" - G : guardar partida, C : cargar, S : salir");
    }

    private String padCenter(String s, int width) {
        if (s == null) s = "";
        if (s.length() >= width) return s;
        int total = width - s.length();
        int left = total / 2;
        int right = total - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(s);
        for (int i = 0; i < right; i++) sb.append(' ');
        return sb.toString();
    }
}