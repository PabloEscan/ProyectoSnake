import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Hereda de la clase base ElementosSnake
public class Snake extends ElementosSnake {

    private String direccion = "NINGUNA";

    public String getDireccion() {
        return direccion;
    }


    private boolean acabaDeCrecer = false;

    public boolean isAcabaDeCrecer() {
        return acabaDeCrecer;
    }

    public void setAcabaDeCrecer(boolean v) {
        acabaDeCrecer = v;
    }


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
        moverCola();
        this.y -= tamanoSegmento;
        direccion = "ARRIBA";
    }

    public void moverAbajo() {
        moverCola();
        this.y += tamanoSegmento;
        direccion = "ABAJO";
    }

    public void moverIzquierda() {
        moverCola();
        this.x -= tamanoSegmento;
        direccion = "IZQUIERDA";
    }

    public void moverDerecha() {
        moverCola();
        this.x += tamanoSegmento;
        direccion = "DERECHA";
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
        ElementosSnake prototipoSegmento = registro.obtenerPrototipo("segmentoBase");
        ElementosSnake nuevoSegmento = prototipoSegmento.clone();

        int nuevoX = x;
        int nuevoY = y;

        // Crecer justo detrás de la cabeza según dirección
        switch (direccion) {
            case "ARRIBA" -> nuevoY = y + tamanoSegmento;
            case "ABAJO" -> nuevoY = y - tamanoSegmento;
            case "IZQUIERDA" -> nuevoX = x + tamanoSegmento;
            case "DERECHA" -> nuevoX = x - tamanoSegmento;
        }

        // Si existe cola, seguir creciendo al final de la cola
        if (!cola.isEmpty()) {
            ElementosSnake ultimo = cola.get(cola.size() - 1);
            nuevoX = ultimo.getX();
            nuevoY = ultimo.getY();
        }

        nuevoSegmento.setX(nuevoX);
        nuevoSegmento.setY(nuevoY);

        cola.add(nuevoSegmento);
    }


}