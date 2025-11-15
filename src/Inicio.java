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
        panelControles.setBackground(new Color(11, 61, 11)); // verde oscuro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnIniciarObjetos = new JButton("Iniciar objetos");
        btnClonarComida = new JButton("Clonar comida");
        btnActualizar = new JButton("Actualizar");
        btnIniciar = new JButton("Iniciar (Movimiento)");

        // Estilos botones
        JButton[] botones = {btnIniciarObjetos, btnClonarComida, btnActualizar, btnIniciar};
        for (JButton b : botones) {
            b.setBackground(new Color(30, 130, 76)); // verde brillante
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        JLabel lblId = new JLabel("ID:");
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");

        // Estilo texto labels
        lblId.setForeground(Color.WHITE);
        lblX.setForeground(Color.WHITE);
        lblY.setForeground(Color.WHITE);

        JLabel lblActualizar = new JLabel("Actualizar posición:");
        lblActualizar.setForeground(Color.WHITE);

        txtId = new JTextField(5);
        txtX = new JTextField(5);
        txtY = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelControles.add(btnIniciarObjetos, gbc);
        gbc.gridy++;
        panelControles.add(btnClonarComida, gbc);
        gbc.gridy++;
        panelControles.add(lblActualizar, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panelControles.add(lblId, gbc);
        gbc.gridx = 1;
        panelControles.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelControles.add(lblX, gbc);
        gbc.gridx = 1;
        panelControles.add(txtX, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelControles.add(lblY, gbc);
        gbc.gridx = 1;
        panelControles.add(txtY, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panelControles.add(btnActualizar, gbc);
        gbc.gridy++;
        panelControles.add(btnIniciar, gbc);

        add(panelControles, BorderLayout.EAST);

        // Eventos
        btnIniciarObjetos.addActionListener(e -> iniciarObjetos());
        btnClonarComida.addActionListener(e -> clonarComida());
        btnActualizar.addActionListener(e -> actualizarComida());
        btnIniciar.addActionListener(e -> toggleMovimiento());
    }

    // Métodos
    private void iniciarObjetos() {
        comidasClonadas.clear();
        contadorIds.set(1);

        // Crear la comida prototipo
        comidaBase = new Comida(0, 0, 0);
        registro.registrarPrototipo("comidaBase", comidaBase);

        // Crear la serpiente (solo la cabeza)
        int startX = (panelJuego.getWidth() / 2 / 20) * 20;
        int startY = (panelJuego.getHeight() / 2 / 20) * 20;
        snake = new Snake(0, startX, startY, "serpiente", registro);

        // Registrar un segmento base
        ElementosSnake segmentoBase = new ElementosSnake(0, 0, 0, "segmento") {
            @Override
            public void dibujar(Graphics g) {
                g.setColor(new Color(0, 100, 0)); // verde oscuro serpiente
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

        int posX = comidaBase.getX();
        int posY = comidaBase.getY() + (comidasClonadas.size() + 1) * 20;

        if (posY + 20 > panelJuego.getHeight()) {
            JOptionPane.showMessageDialog(this, "No se puede clonar: la comida aparecería fuera del panel.");
            return;
        }

        clon.setX(posX);
        clon.setY(posY);

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

    // Panel de juego
    private class GamePanel extends JPanel implements KeyListener {

        private boolean movimientoPermitido(int x, int y) {
            return x >= 0 && y >= 0 && x + 20 <= getWidth() && y + 20 <= getHeight();
        }

        public GamePanel() {
            setBackground(Color.WHITE);
            setFocusable(true);
            addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Cuadrícula ajedrez
            Color c1 = new Color(210, 166, 121); // café claro
            Color c2 = new Color(255, 138, 117); // tomate suave

            for (int y = 0; y < getHeight(); y += 20) {
                for (int x = 0; x < getWidth(); x += 20) {
                    g.setColor(((x + y) / 20) % 2 == 0 ? c1 : c2);
                    g.fillRect(x, y, 20, 20);
                }
            }

            // comida base
            if (comidaBase != null) comidaBase.dibujar(g);

            // comidas clonadas
            for (Comida c : comidasClonadas.values()) c.dibujar(g);

            // serpiente
            if (snake != null) snake.dibujar(g);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!movimientoActivo || snake == null) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> { if (movimientoPermitido(snake.getX(), snake.getY() - 20)) snake.moverArriba(); }
                case KeyEvent.VK_DOWN -> { if (movimientoPermitido(snake.getX(), snake.getY() + 20)) snake.moverAbajo(); }
                case KeyEvent.VK_LEFT -> { if (movimientoPermitido(snake.getX() - 20, snake.getY())) snake.moverIzquierda(); }
                case KeyEvent.VK_RIGHT ->{ if (movimientoPermitido(snake.getX() + 20, snake.getY())) snake.moverDerecha(); }
            }

            checkColisiones();
            repaint();
        }

        private void checkColisiones() {
            if (snake == null) return;

            Rectangle cabeza = new Rectangle(snake.getX(), snake.getY(), 20, 20);
            Integer idColision = null;

            for (Map.Entry<Integer, Comida> entry : comidasClonadas.entrySet()) {
                Comida c = entry.getValue();
                Rectangle r = new Rectangle(c.getX(), c.getY(), 20, 20);
                if (cabeza.intersects(r)) { idColision = entry.getKey(); break; }
            }

            if (idColision != null) {
                comidasClonadas.remove(idColision);
                snake.crecer();
                panelJuego.repaint();
                return;
            }

            if (snake.isAcabaDeCrecer()) {
                snake.setAcabaDeCrecer(false);
                return;
            }

            try {
                var colaField = snake.getClass().getDeclaredField("cola");
                colaField.setAccessible(true);
                java.util.List<ElementosSnake> cola = (java.util.List<ElementosSnake>) colaField.get(snake);

                for (ElementosSnake segmento : cola) {
                    if (cabeza.intersects(new Rectangle(segmento.getX(), segmento.getY(), 20, 20))) {
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

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicio().setVisible(true));
    }
}
