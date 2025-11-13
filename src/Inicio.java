import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Inicio extends JFrame {

    private GamePanel panelJuego;
    private JButton btnIniciarObjetos;
    private JButton btnClonarComida;
    private JButton btnActualizar;
    private JButton btnIniciar;
    private JTextField txtId, txtX, txtY;

    private PrototypeRegistry registro;
    private Snake snake;
    private Comida comidaBase;
    private Map<Integer, Comida> comidasClonadas;
    private AtomicInteger contadorIds;

    private boolean movimientoActivo = false;

    public Inicio() {
        setTitle("Juego Snake - Patrón Prototype");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        registro = new PrototypeRegistry();
        comidasClonadas = new LinkedHashMap<>();
        contadorIds = new AtomicInteger(1);

        // Panel central del juego
        panelJuego = new GamePanel();
        add(panelJuego, BorderLayout.CENTER);

        // Panel lateral de controles
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnIniciarObjetos = new JButton("Iniciar objetos");
        btnClonarComida = new JButton("Clonar comida");
        btnActualizar = new JButton("Actualizar");
        btnIniciar = new JButton("Iniciar (Movimiento)");

        JLabel lblId = new JLabel("ID:");
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");
        txtId = new JTextField(5);
        txtX = new JTextField(5);
        txtY = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelControles.add(btnIniciarObjetos, gbc);
        gbc.gridy++; panelControles.add(btnClonarComida, gbc);
        gbc.gridy++; panelControles.add(new JLabel("Actualizar posición:"), gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; panelControles.add(lblId, gbc); gbc.gridx = 1; panelControles.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy++; panelControles.add(lblX, gbc); gbc.gridx = 1; panelControles.add(txtX, gbc);
        gbc.gridx = 0; gbc.gridy++; panelControles.add(lblY, gbc); gbc.gridx = 1; panelControles.add(txtY, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; panelControles.add(btnActualizar, gbc);
        gbc.gridy++; panelControles.add(btnIniciar, gbc);

        add(panelControles, BorderLayout.EAST);

        //Eventos
        btnIniciarObjetos.addActionListener(e -> iniciarObjetos());
        btnClonarComida.addActionListener(e -> clonarComida());
        btnActualizar.addActionListener(e -> actualizarComida());
        btnIniciar.addActionListener(e -> toggleMovimiento());
    }

    //Métodos

    private void iniciarObjetos() {
        comidasClonadas.clear();
        contadorIds.set(1);

        // Crear la comida prototipo
        comidaBase = new Comida(0, 5, 5);
        registro.registrarPrototipo("comidaBase", comidaBase);

        // Crear la serpiente (solo la cabeza)
        snake = new Snake(0, panelJuego.getWidth() / 2, panelJuego.getHeight() / 2, "serpiente", registro);

        // Registrar un segmento base por separado (NO clonando la serpiente)
        // Registrar un segmento base que se dibuje como parte de la serpiente
        ElementosSnake segmentoBase = new ElementosSnake(0, 0, 0, "segmento") {
            @Override
            public void dibujar(Graphics g) {
                g.setColor(new Color(0, 128, 0)); // verde oscuro
                g.fillRect(x, y, 20, 20);
            }
        };
        registro.registrarPrototipo("segmentoBase", segmentoBase);

        segmentoBase.setTipo("segmento");
        registro.registrarPrototipo("segmentoBase", segmentoBase);

        JOptionPane.showMessageDialog(this, "Objetos base iniciados correctamente.");
        panelJuego.repaint();
    }


    private void clonarComida() {
        ElementosSnake prototipo = registro.obtenerPrototipo("comidaBase");
        if (prototipo == null) {
            JOptionPane.showMessageDialog(this, "Debes presionar 'Iniciar objetos' primero.");
            return;
        }

        Comida clon = (Comida) prototipo.clone();
        int nuevoId = contadorIds.getAndIncrement();
        clon.setId(nuevoId);
        clon.setX(50);
        clon.setY(100 + comidasClonadas.size() * 30);

        comidasClonadas.put(nuevoId, clon);
        panelJuego.repaint();
    }

    private void actualizarComida() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            int x = Integer.parseInt(txtX.getText().trim());
            int y = Integer.parseInt(txtY.getText().trim());

            Comida c = comidasClonadas.get(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No existe comida con ID " + id);
                return;
            }
            c.setX(x);
            c.setY(y);
            panelJuego.repaint();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
        }
    }

    private void toggleMovimiento() {
        movimientoActivo = !movimientoActivo;
        btnIniciar.setText(movimientoActivo ? "Detener" : "Iniciar (Movimiento)");
        if (movimientoActivo) panelJuego.requestFocusInWindow();
    }

   //Panel de juego

    private class GamePanel extends JPanel implements KeyListener {

        private boolean movimientoPermitido(int x, int y) {
            return x >= 0 &&
                y >= 0 &&
                x + 20 <= getWidth() &&
                y + 20 <= getHeight();
        }


        public GamePanel() {
            setBackground(Color.WHITE);
            setFocusable(true);
            addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibujar la comida base
            if (comidaBase != null) {
                comidaBase.dibujar(g);
            }

            // Dibujar las comidas clonadas
            for (Comida c : comidasClonadas.values()) {
                c.dibujar(g);
            }

            // Dibujar la serpiente
            if (snake != null) {
                snake.dibujar(g);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!movimientoActivo || snake == null) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    if (movimientoPermitido(snake.getX(), snake.getY() - 20))
                        snake.moverArriba();
                }
                case KeyEvent.VK_DOWN -> {
                    if (movimientoPermitido(snake.getX(), snake.getY() + 20))
                        snake.moverAbajo();
                }
                case KeyEvent.VK_LEFT -> {
                    if (movimientoPermitido(snake.getX() - 20, snake.getY()))
                        snake.moverIzquierda();
                }
                case KeyEvent.VK_RIGHT -> {
                    if (movimientoPermitido(snake.getX() + 20, snake.getY()))
                        snake.moverDerecha();
                }

            }

            // Verificar colisiones
            checkColisiones();

            repaint();
        }

    private void checkColisiones() {
        if (snake == null) return;

        Rectangle cabeza = new Rectangle(snake.getX(), snake.getY(), 20, 20);
        Integer idColision = null;

        //Verificar colisión con comida ===
        for (Map.Entry<Integer, Comida> entry : comidasClonadas.entrySet()) {
            Comida c = entry.getValue();
            Rectangle rComida = new Rectangle(c.getX(), c.getY(), 20, 20);

            if (cabeza.intersects(rComida)) {
                idColision = entry.getKey();
                break;
            }
        }

        if (idColision != null) {
            comidasClonadas.remove(idColision);
            snake.crecer();
            panelJuego.repaint();
        }

        //Verificar colisión con su propio cuerpo
        // Recorremos todos los segmentos de la cola de la serpiente
        try {
            java.lang.reflect.Field colaField = snake.getClass().getDeclaredField("cola");
            colaField.setAccessible(true);
            java.util.List<ElementosSnake> cola = (java.util.List<ElementosSnake>) colaField.get(snake);

            for (ElementosSnake segmento : cola) {
                Rectangle rSegmento = new Rectangle(segmento.getX(), segmento.getY(), 20, 20);
                if (cabeza.intersects(rSegmento)) {
                    movimientoActivo = false;
                    btnIniciar.setText("Iniciar (Movimiento)");
                    JOptionPane.showMessageDialog(null, "¡Game Over! La serpiente se mordió a sí misma.");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    //Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicio().setVisible(true));
    }
}
 