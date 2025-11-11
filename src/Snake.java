import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Hereda de la clase base ElementosSnake
public class Snake extends ElementosSnake {

    // Tamaño de cada cuadrado de la serpiente
    private int tamanoSegmento = 20; 

    private List<ElementosSnake> cola;
    
    // Referencia al registro para poder clonar segmentos
    private PrototypeRegistry registro;

    // Constructor para la serpiente
    // El 'id' y 'tipo' se usan para el prototipo "segmentoBase"
    public Snake(int id, int x, int y, String tipo, PrototypeRegistry registro) {

        // Llama al constructor de ElementosSnake(id, x, y, tipo)
        super(id, x, y, tipo); 
        
        // La serpiente real necesita inicializar su lista
        this.cola = new ArrayList<>();
        this.registro = registro;
    }

    // Implementación del método de dibujo
    @Override
    public void dibujar(Graphics g) {
        // Dibuja la cabeza 
        g.setColor(Color.GREEN);
        g.fillRect(x, y, tamanoSegmento, tamanoSegmento); // Usa 'x' y 'y' heredados

        // Dibuja la cola 
        if (cola != null) {
            for (ElementosSnake segmento : cola) {
                segmento.dibujar(g);
            }
        }
    }
    
    //Métodos de movimiento
    
    // Mueve la serpiente hacia arriba
    public void moverArriba() {
        moverCola(); // Mueve la cola primero
        this.y -= tamanoSegmento; // Mueve la cabeza
    }

    // Mueve la serpiente hacia abajo
    public void moverAbajo() {
        moverCola();
        this.y += tamanoSegmento;
    }

    // Mueve la serpiente hacia la izquierda
    public void moverIzquierda() {
        moverCola();
        this.x -= tamanoSegmento;
    }

    // mueve la serpiente hacia la derecha
    public void moverDerecha() {
        moverCola();
        this.x += tamanoSegmento;
    }
    
    // Método privado que hace que la cola siga a la cabeza
    private void moverCola() {
        if (cola != null && cola.size() > 0) {s
            // mueve cada segmento a la posición del que tiene adelante
            for (int i = cola.size() - 1; i > 0; i--) {
                ElementosSnake segmentoActual = cola.get(i);
                ElementosSnake segmentoDelantero = cola.get(i - 1);
                //usa los setters heredados para mover el segmento
                segmentoActual.setX(segmentoDelantero.getX());
                segmentoActual.setY(segmentoDelantero.getY());
            }
            
            //mueve el primer segmento a la posición actual de la cabeza
            ElementosSnake primerSegmento = cola.get(0);
            primerSegmento.setX(this.x);
            primerSegmento.setY(this.y);
        }
    }

    // Lógica de crecimiento 
    
    // Implementa el método para agregar segmentos clonados cuando come
    public void crecer() {
        
        // Obtiene del registro el prototipo segmentoBase
        ElementosSnake prototipoSegmento = registro.obtenerPrototipo("segmentoBase");
        
        // Se clona 
        ElementosSnake nuevoSegmento = prototipoSegmento.clone();
        
        // Lo posiciona y se ajustará en el siguiente movimiento
        nuevoSegmento.setX(-100); 
        nuevoSegmento.setY(-100);
        
        //Se agrega al final de la serpiente
        cola.add(nuevoSegmento);
    }
}