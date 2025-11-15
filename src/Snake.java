package prototipo; 
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Hereda de la clase base ElementosSnake
public class Snake extends ElementosSnake {

    // Tamaño de cada cuadrado de la serpiente
    private int tamanoSegmento = 20; 

    private List<ElementosSnake> cola;
    
    // registro para poder clonar segmentos
    private PrototypeRegistry registro;

    // Constructor para la serpiente
    public Snake(int id, int x, int y, String tipo, PrototypeRegistry registro) {
        // Llama al constructor de ElementosSnake
        super(id, x, y, tipo); 
        
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
    
    //  Métodos de movimiento 

    // Mueve la serpiente hacia arriba
    public void moverArriba() {
        moverCola(); //mueve la cola primero
        this.y -= tamanoSegmento; //mueve la cabeza
    }

    //mueve la serpiente hacia abajo
    public void moverAbajo() {
        moverCola();
        this.y += tamanoSegmento;
    }

    //Mueve la serpiente hacia la izquierda
    public void moverIzquierda() {
        moverCola();
        this.x -= tamanoSegmento;
    }

    // Mueve la serpiente hacia la derecha
    public void moverDerecha() {
        moverCola();
        this.x += tamanoSegmento;
    }
    
    // Método privado que hace que la cola siga a la cabeza
    private void moverCola() {
        if (cola != null && cola.size() > 0) {
            for (int i = cola.size() - 1; i > 0; i--) {
                ElementosSnake segmentoActual = cola.get(i);
                ElementosSnake segmentoDelantero = cola.get(i - 1);
                segmentoActual.setX(segmentoDelantero.getX());
                segmentoActual.setY(segmentoDelantero.getY());
            }
            
            ElementosSnake primerSegmento = cola.get(0);
            primerSegmento.setX(this.x);
            primerSegmento.setY(this.y);
        }
    }

    // logica de crecimiento
    
    public void crecer() {
        
        // 1) Obtiene del registro el prototipo segmentoBase
        ElementosSnake prototipoSegmento = registro.obtenerPrototipo("segmentoBase");
        
        // 2) Se clona
        ElementosSnake nuevoSegmento = prototipoSegmento.clone();
        
        int nuevoX;
        int nuevoY;

        if (cola.isEmpty()) {
            // Si no hay cola, el  segmento aparece donde está la cabeza
            nuevoX = this.x;
            nuevoY = this.y;
        } else {
            // Si hay cola, aparece donde está el último segmento de la cola
            ElementosSnake ultimoSegmento = cola.get(cola.size() - 1);
            nuevoX = ultimoSegmento.getX();
            nuevoY = ultimoSegmento.getY();
        }
        
        //se posiciona el nuevo segmento en ese lugar
        nuevoSegmento.setX(nuevoX); 
        nuevoSegmento.setY(nuevoY);
        
        // 3) se agrega al final de la serpiente
        cola.add(nuevoSegmento);
    }
}