package Searching.AStarSearchAlgorithm.Example;

import Searching.AStarSearchAlgorithm.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PathTest extends JFrame {
    private GameMap map = new GameMap();
    private AStarPathFinder finder;
    private Path path;

    private Image[] tiles = new Image[6];
    private Image buffer;

    private int selectedx = -1;
    private int selectedy = -1;

    private int lastFindX = -1;
    private int lastFindY = -1;

    public PathTest() {
        super("A* Path Finding");

        try {
            tiles[GameMap.TREES] = ImageIO.read(getResource("res/trees.png"));
            tiles[GameMap.GRASS] = ImageIO.read(getResource("res/grass.png"));
            tiles[GameMap.WATER] = ImageIO.read(getResource("res/water.png"));
            tiles[GameMap.TANK] = ImageIO.read(getResource("res/tank.png"));
            tiles[GameMap.PLANE] = ImageIO.read(getResource("res/plane.png"));
            tiles[GameMap.BOAT] = ImageIO.read(getResource("res/boat.png"));
        } catch (IOException e) {
            System.err.println("Failed to load resources: " + e.getMessage());
            System.exit(0);
        }

        finder = new AStarPathFinder(map, 500, true);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e.getX(), e.getY());
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
    }

    private InputStream getResource(String ref) throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ref);
        if (in != null) {
            return in;
        }

        return new FileInputStream(ref);
    }

    private void handleMouseMoved(int x, int y) {
        x -= 50;
        y -= 50;
        x /= 16;
        y /= 16;

        if ((x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles())) {
            return;
        }

        if (selectedx != -1) {
            if ((lastFindX != x) || (lastFindY != y)) {
                lastFindX = x;
                lastFindY = y;
                path = finder.findPath(new UnitMover(map.getUnit(selectedx, selectedy)), selectedx, selectedy, x, y);
                repaint(0);
            }
        }
    }

    private void handleMousePressed(int x, int y) {
        x -= 50;
        y -= 50;
        x /= 16;
        y /= 16;

        if ((x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles())) {
            return;
        }

        if (map.getUnit(x, y) != 0) {
            selectedx = x;
            selectedy = y;
            lastFindX = - 1;
        } else {
            if (selectedx != -1) {
                map.clearVisited();
                path = finder.findPath(new UnitMover(map.getUnit(selectedx, selectedy)),
                        selectedx, selectedy, x, y);

                if (path != null) {
                    path = null;
                    int unit = map.getUnit(selectedx, selectedy);
                    map.setUnit(selectedx, selectedy, 0);
                    map.setUnit(x,y,unit);
                    selectedx = x;
                    selectedy = y;
                    lastFindX = - 1;
                }
            }
        }

        repaint(0);
    }

    public void paint(Graphics graphics) {
        if (buffer == null) {
            buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics g = buffer.getGraphics();

        g.clearRect(0,0,600,600);
        g.translate(50, 50);

        // cycle through the tiles in the map drawing the appropriate

        // image for the terrain and units where appropriate

        for (int x=0;x<map.getWidthInTiles();x++) {
            for (int y=0;y<map.getHeightInTiles();y++) {
                g.drawImage(tiles[map.getTerrain(x, y)],x*16,y*16,null);
                if (map.getUnit(x, y) != 0) {
                    g.drawImage(tiles[map.getUnit(x, y)],x*16,y*16,null);
                } else {
                    if (path != null) {
                        if (path.contains(x, y)) {
                            g.setColor(Color.blue);
                            g.fillRect((x*16)+4, (y*16)+4,7,7);
                        }
                    }
                }
            }
        }

        // if a unit is selected then draw a box around it

        if (selectedx != -1) {
            g.setColor(Color.black);
            g.drawRect(selectedx*16, selectedy*16, 15, 15);
            g.drawRect((selectedx*16)-2, (selectedy*16)-2, 19, 19);
            g.setColor(Color.white);
            g.drawRect((selectedx*16)-1, (selectedy*16)-1, 17, 17);
        }

        // finally draw the buffer to the real graphics context in one

        // atomic action

        graphics.drawImage(buffer, 0, 0, null);

    }

    public static void main(String[] args) {
        PathTest test = new PathTest();
    }
}
