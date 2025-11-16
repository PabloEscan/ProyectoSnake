import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

// Clase que representa la serpiente, hereda ElementosSnake
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

    private int tamanoSegmento = 20;

    private List<ElementosSnake> cola;

    private PrototypeRegistry registro;

    public Snake(int id, int x, int y, String tipo, PrototypeRegistry registro) {
        // Inicializa posición en coordenadas de celda
        super(id, x, y, tipo);
        cola = new ArrayList<>();
        this.registro = registro;
    }

    @Override
    public void dibujar(Graphics g) {
        // Dibuja cabeza multiplicando coordenadas por tamaño de celda
        g.setColor(Color.GREEN);
        g.fillRect(x * tamanoSegmento, y * tamanoSegmento, tamanoSegmento, tamanoSegmento);

        // Dibuja cada segmento de la cola
        if (cola != null) {
            for (ElementosSnake segmento : cola) {
                segmento.dibujar(g);
            }
        }
    }

    // Mueve la serpiente hacia arriba en coordenadas de celda
    public void moverArriba() {
        moverCola();
        this.y -= 1;
        direccion = "ARRIBA";
    }

    // Mueve la serpiente hacia abajo en coordenadas de celda
    public void moverAbajo() {
        moverCola();
        this.y += 1;
        direccion = "ABAJO";
    }

    // Mueve la serpiente hacia la izquierda en coordenadas de celda
    public void moverIzquierda() {
        moverCola();
        this.x -= 1;
        direccion = "IZQUIERDA";
    }

    // Mueve la serpiente hacia la derecha en coordenadas de celda
    public void moverDerecha() {
        moverCola();
        this.x += 1;
        direccion = "DERECHA";
    }

    // Actualiza las posiciones de los segmentos de la cola para que sigan la cabeza
    private void moverCola() {
        if (cola != null && cola.size() > 0) {
            // Cada segmento toma la posición del segmento anterior
            for (int i = cola.size() - 1; i > 0; i--) {
                ElementosSnake segmentoActual = cola.get(i);
                ElementosSnake segmentoDelantero = cola.get(i - 1);
                segmentoActual.setX(segmentoDelantero.getX());
                segmentoActual.setY(segmentoDelantero.getY());
            }

            // El primer segmento sigue la posición actual de la cabeza
            ElementosSnake primerSegmento = cola.get(0);
            primerSegmento.setX(this.x);
            primerSegmento.setY(this.y);
        }
    }

    // Agrega un nuevo segmento a la cola, tomando la posición del último segmento o ajustándola según dirección
    public void crecer() {
        ElementosSnake prototipoSegmento = registro.obtenerPrototipo("segmentoBase");
        ElementosSnake nuevoSegmento = prototipoSegmento.clone();

        int nuevoX = x;
        int nuevoY = y;

        // Ajusta posición inicial del nuevo segmento según dirección de la serpiente
        switch (direccion) {
            case "ARRIBA" -> nuevoY = y + 1;
            case "ABAJO" -> nuevoY = y - 1;
            case "IZQUIERDA" -> nuevoX = x + 1;
            case "DERECHA" -> nuevoX = x - 1;
        }

        // Si ya hay segmentos, el nuevo segmento toma la posición del último
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
