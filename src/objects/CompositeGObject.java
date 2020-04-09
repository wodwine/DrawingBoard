package objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeGObject extends GObject {

    private List<GObject> gObjects;

    public CompositeGObject() {
        super(0, 0, 0, 0);
        gObjects = new ArrayList<GObject>();
    }

    public void add(GObject gObject) {
        this.gObjects.add(gObject);
    }

    public void remove(GObject gObject) {
        this.gObjects.remove(gObject);
    }

    @Override
    public void move(int dX, int dY) {
        this.x += dX;
        this.y += dY;
        for (GObject g : this.gObjects) {
            g.move(dX, dY);
        }
    }

    public void recalculateRegion() {
        List<Integer> xList = new ArrayList<Integer>();
        List<Integer> yList = new ArrayList<Integer>();
        for (GObject g : this.gObjects) {
            xList.add(g.x);
            yList.add(g.y);
        }
        int xMin = Collections.min(xList);
        int xMax = Collections.max(xList);
        int yMin = Collections.min(yList);
        int yMax = Collections.max(yList);
        this.x = xMin;
        this.y = yMin;
        for (GObject g : this.gObjects) {
            if(xMax==g.x){
                this.width=g.width+xMax-xMin;
            }
            if(yMax==g.y){
                this.height=g.height+yMax-yMin;
            }
        }

    }

    @Override
    public void paintObject(Graphics g) {
        for (GObject gO : this.gObjects) {
            gO.paintObject(g);
        }
    }

    @Override
    public void paintLabel(Graphics g) {
        g.drawString("Group", x, y+this.height+10);
    }

}
