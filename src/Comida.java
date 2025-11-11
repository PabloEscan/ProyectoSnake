package prototipo;

import java.awt.Color;
import java.awt.Graphics;

// Hereda de la clase base 'ElementosSnake'
public class Comida extends ElementosSnake {

    // Constructor para un objeto Comida
    // Llama al constructor de la clase ElementosSnake
    public Comida(int id, int x, int y) {
        super(id, x, y, "comida"); 
    }

    // Implementación  del método abstracto
  
    @Override
    public void dibujar(Graphics g) {
        
        //círculo amarillo
        g.setColor(Color.YELLOW);
        // Usa los 'x' y 'y' heredados de ElementosSnake
        g.fillOval(x, y, 20, 20); 

        // id en el centro
        g.setColor(Color.BLACK);
        // Usa el id heredado de ElementosSnake
        g.drawString(String.valueOf(id), x + 7, y + 15); 
    }
    
}