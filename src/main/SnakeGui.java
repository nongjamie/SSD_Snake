package main;

import lib.*;
import main.SnakeGame.Memento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class SnakeGui extends JFrame implements Observer {

    private SnakeGame game;
    private Renderer renderer;
    private Controller controller;
    private JButton saveButton;
    private JButton loadButton;

    private Memento memento = null;

    private void loadGame( Memento m ) {
    		System.out.println( "load button was pressed." );
    		this.game.load( m );
    }

    private void saveGame() {
    		loadButton.setEnabled( true );
    		System.out.println( "save button was pressed." );
    		memento = game.save();
    }

    public SnakeGui(SnakeGame snakeGame) {
        game = snakeGame;
        game.addObserver(this);

        controller = new Controller();

        renderer = new Renderer();
        renderer.addKeyListener(controller);

        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
        add(new JPanel() {
            {
                saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveGame();
                        renderer.requestFocus();
                    }
                });
                add(saveButton);
                loadButton = new JButton("Load");
                loadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadGame( memento );
                        renderer.requestFocus();
                    }
                });
                loadButton.setEnabled(false);
                add(loadButton);
            }
        }, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        renderer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        renderer.repaint();
    }

    class Controller extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                game.handleUpKey();
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                game.handleDownKey();
            } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                game.handleLeftKey();
            } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                game.handleRightKey();
            }
        }
    }

    class Renderer extends JPanel {

        private int blockWidth = 20;
        private int mapSize;

        public Renderer() {
            mapSize = game.getMapSize() * blockWidth;
            setPreferredSize(new Dimension(mapSize, mapSize));
            setDoubleBuffered(true);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintGrids(g);
            paintBlocks(g);
        }

        private void paintGrids(Graphics g) {
            g.setColor(Color.gray);
            for (int i = 0; i < mapSize; i++) {
                g.drawLine(i * blockWidth, 0, i * blockWidth, getHeight());
                g.drawLine(0, i * blockWidth, getWidth(), i * blockWidth);
            }
        }

        private void paintBlocks(Graphics g) {
            g.setColor(Color.red);
            for(Block b : game.getBlocks()) {
                g.fillRect(b.getX() * blockWidth, b.getY() * blockWidth, blockWidth, blockWidth);
            }
        }
    }
   
}
