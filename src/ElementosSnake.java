import java.awt.Graphics;

// Clase base abstracta para todos los elementos del juego
// Implementa Cloneable para usar el Patrón Prototype
public abstract class ElementosSnake implements Cloneable {

    // Atributos comunes para todos los elementos (id, posición, tipo)
    protected int id;
    protected int x;
    protected int y;
    protected String tipo;

    // Constructor para inicializar los atributos comunes
    public ElementosSnake(int id, int x, int y, String tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }
    
    // Método abstracto para el dibujo
    public abstract void dibujar(Graphics g);
    
    //Getters y Setters 
    //ID
    public int getId(){ 
        return id; 
    }
    public void setId(int id){
        this.id = id; 
    }

    //X
    public int getX(){ 
        return x; 
    }  
    public void setX(int x){ 
        this.x = x; 
    }

    //Y
    public int getY(){ 
        return y; 
    }
    public void setY(int y){ 
        this.y = y; 
    }
    
    //TIPO
    public String getTipo(){ 
        return tipo; 
    }
    public void setTipo(String tipo){ 
        this.tipo = tipo; 
    }

    // --- Método del Patrón Prototype ---

    // El patrón Prototype indica que clone() solo debe crear una copia exacta del objeto
    // La lógica de asignar nuevos IDs o mover el clon 
    
    @Override
    public ElementosSnake clone() {
        try {
            // Llama a super.clone() para hacer la copia exacta
            return (ElementosSnake) super.clone();
            
        } catch (CloneNotSupportedException e) {
            System.out.println("Error: la clonación no es posible");
            e.printStackTrace();
            return null;
        }
    }
}