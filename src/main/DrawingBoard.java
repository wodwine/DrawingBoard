package main;

import objects.CompositeGObject;
import objects.GObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends JPanel {

    private MouseAdapter mouseAdapter;
    private List<GObject> gObjects;
    private GObject target;

    private int gridSize = 10;

    public DrawingBoard() {
        gObjects = new ArrayList<GObject>();
        mouseAdapter = new MAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        setPreferredSize(new Dimension(800, 600));
    }

    public void addGObject(GObject gObject) {
        this.gObjects.add(gObject);
        repaint();
    }

    public void groupAll() {
        CompositeGObject compositeGObject = new CompositeGObject();
        for (GObject g : this.gObjects) {
            compositeGObject.add(g);
        }
        deleteSelected();
        clear();
        compositeGObject.recalculateRegion();
        addGObject(compositeGObject);

    }

    public void deleteSelected() {
        this.gObjects.remove(target);
        target = null;
        repaint();
    }

    public void clear() {
        this.gObjects.clear();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintBackground(g);
        paintGrids(g);
        paintObjects(g);
    }

    private void paintBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintGrids(Graphics g) {
        g.setColor(Color.lightGray);
        int gridCountX = getWidth() / gridSize;
        int gridCountY = getHeight() / gridSize;
        for (int i = 0; i < gridCountX; i++) {
            g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
        }
        for (int i = 0; i < gridCountY; i++) {
            g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
        }
    }

    private void paintObjects(Graphics g) {
        for (GObject go : gObjects) {
            go.paint(g);
        }
    }

    class MAdapter extends MouseAdapter {
        int curX;
        int curY;

        private void deselectAll() {
            target = null;
            for (GObject g : gObjects) {
                g.deselected();
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            deselectAll();
            for (GObject g : gObjects) {
                if (g.pointerHit(e.getX(), e.getY())) {
                    curX = e.getX();
                    curY = e.getY();
                    g.selected();
                    target = g;
                    repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (target.pointerHit(e.getX(), e.getY())) {
                target.move(e.getX() - curX, e.getY() - curY);
                curX = e.getX();
                curY = e.getY();
                repaint();
            }
        }
    }

}