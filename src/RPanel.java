
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;

class RPanel extends JPanel {
//*********SETTINGS****************************
public float mWind = 2.05f;
public float mGravity = 9.8f;
public double mRainChance = 0.99; // from 0 to 1

public int mRepaintTimeMS = 16;
public float mRainWidth=5;
public double mDdropInitialVelocity = 20;
public double mDropDiam = 2;
Random ran = new Random();
public Color mColor=new Color(0, 0, 255);
//*********************************************

public ArrayList<Rain> rainV;
public ArrayList<Drop> dropV;
public UpdateThread mUpdateThread;

public RPanel() {
    rainV = new ArrayList<>();
    dropV = new ArrayList<>();

    mUpdateThread=new UpdateThread();
    mUpdateThread.start();
}

public void stop() {
    mUpdateThread.stopped=true;
}

public int getHeight() {
    return this.getSize().height;
}

public int getWidth() {
    return this.getSize().width;
}
private Color colorRandomizer() {
        int r,g,b;
        r = ran.nextInt((255 - 0) + 1) + 0;
        g = ran.nextInt((255 - 0) + 1) + 0;
        b = ran.nextInt((255 - 0) + 1) + 0;
        return new Color(r,g,b);
    }
public class UpdateThread extends Thread {
    public volatile boolean stopped=false;
    @Override
    public void run() {
        while (!stopped) {
            RPanel.this.repaint();
            try {
                Thread.sleep(ran.nextInt((50 - 0) + 1) + 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


    protected void draw(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        
        Color color1 = colorRandomizer();
        Color color2 = Color.BLACK;
       
        
         GradientPaint blackToGray = new GradientPaint(0, 50, Color.BLACK,
            1000, 1000, Color.LIGHT_GRAY, true);
        g2d.setPaint(blackToGray);
        g2d.fillRect(0, 0, w, h);
        
    }
    
@Override
public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new BasicStroke(mRainWidth));
    g2.setColor(colorRandomizer());

    //DRAW DROPS
    Iterator<Drop> iterator2 = dropV.iterator();
    while (iterator2.hasNext()) {
        Drop drop = iterator2.next();
        drop.update();
        drop.draw(g2);
        if (drop.y >= getHeight()) {
            iterator2.remove();
        }
    }

    //DRAW RAIN
    Iterator<Rain> iterator = rainV.iterator();
    while (iterator.hasNext()) {
        Rain rain = iterator.next();
        rain.update();
        rain.draw(g2);

        if (rain.y >= getHeight()) {
            //create new drops (2-8)
            long dropCount = 1 + Math.round(Math.random() * 10);
            for (int i = 0; i < dropCount; i++) {
                dropV.add(new Drop(rain.x, getHeight()));
            }
            iterator.remove();

        }
    }

    //CREATE NEW RAIN
    if (Math.random() < mRainChance) {
        rainV.add(new Rain());
    } 
}

//*****************************************
class Rain {
    float x;
    float y;
    float prevX;
    float prevY;

    public Rain() {
        Random r = new Random();
        x = r.nextInt(getWidth());
        y = 0;
    }

    public void update() {
        prevX = x;
        prevY = y;

        x += mWind;
        y += mGravity;
    }

    public void draw(Graphics2D g2) {
        Line2D line = new Line2D.Double(x, y, prevX, prevY);
        g2.draw(line);
    }
}

//*****************************************
class Drop {

    double x0;  
    double y0;  
    double v0; //initial velocity
    double t;  //time
    double angle;
    double x;
    double y;

    public Drop(double x0, double y0) {
        super();
        this.x0 = x0;
        this.y0 = y0;

        v0 = mDdropInitialVelocity;
        angle = Math.toRadians(Math.round(Math.random() * 180)); //from 0 - 180 degrees
    }

    private void update() {
        // double g=10;
        t += mRepaintTimeMS / 200f;
        x = x0 + v0 * t * Math.cos(angle);
        y = y0 - (v0 * t * Math.sin(angle) - mGravity * t * t / 5);
    }

    public void draw(Graphics2D g2) {
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, mDropDiam, mDropDiam);
        g2.fill(circle);
    }
}
}