import java.awt.Graphics;

// Clase base abstracta para todos los elementos del juego
// Implementa Cloneable para el patrón Prototype
public abstract class ElementosSnake implements Cloneable {

    // Identificador único del elemento
    protected int id;
    // Coordenadas de celda (no pixeles)
    protected int x;
    protected int y;
    // Tipo de elemento (ejemplo: comida, segmento, serpiente)
    protected String tipo;

    // Constructor inicializa atributos comunes
    public ElementosSnake(int id, int x, int y, String tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    // Método abstracto para dibujar el elemento
    public abstract void dibujar(Graphics g);

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Implementación de clonación (patrón Prototype)
    @Override
    public ElementosSnake clone() {
        try {
            return (ElementosSnake) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Error: la clonación no es posible");
            e.printStackTrace();
            return null;
        }
    }
}
