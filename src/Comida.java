import java.awt.Graphics;

public class Comida extends ElementosSnake {

    public Comida(int id, int x, int y) {
        super(id, x, y, "Comida");
    }

    @Override
    public void dibujar(Graphics g) {
        //Aqui se deberia dibujar la comida en las coodenadas dadas la primera
    }
}
