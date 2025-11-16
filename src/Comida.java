import java.awt.Color;
import java.awt.Graphics;

// Hereda de la clase base 'ElementosSnake'
public class Comida extends ElementosSnake {

    // Constructor para un objeto Comida
    public Comida(int id, int x, int y) {
        // x, y representan coordenadas de celda (no pixeles)
        super(id, x, y, "comida");
    }

    @Override
    public void dibujar(Graphics g) {
        // Multiplicamos las coordenadas de celda por 20 para obtener posición en píxeles
        int pixelX = x * 20;
        int pixelY = y * 20;

        // Dibuja un círculo amarillo (manzana) en la posición correspondiente
        g.setColor(Color.YELLOW);
        g.fillOval(pixelX, pixelY, 20, 20);

        // Dibuja el id en el centro de la manzana
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(id), pixelX + 7, pixelY + 15);
    }
}
