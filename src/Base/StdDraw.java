package Base;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.geom.*;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.LinkedList;
import java.util.TreeSet;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
/**
 * Drawing library
 */
public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    /**
     *  The color black.
     */
    public static final Color BLACK = Color.BLACK;

    /**
     *  The color blue.
     */
    public static final Color BLUE = Color.BLUE;

    /**
     *  The color cyan.
     */
    public static final Color CYAN = Color.CYAN;

    /**
     *  The color dark gray.
     */
    public static final Color DARK_GRAY = Color.DARK_GRAY;

    /**
     *  The color gray.
     */
    public static final Color GRAY = Color.GRAY;

    /**
     *  The color green.
     */
    public static final Color GREEN  = Color.GREEN;

    /**
     *  The color light gray.
     */
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;

    /**
     *  The color magenta.
     */
    public static final Color MAGENTA = Color.MAGENTA;

    /**
     *  The color orange.
     */
    public static final Color ORANGE = Color.ORANGE;

    /**
     *  The color pink.
     */
    public static final Color PINK = Color.PINK;

    /**
     *  The color red.
     */
    public static final Color RED = Color.RED;

    /**
     *  The color white.
     */
    public static final Color WHITE = Color.WHITE;

    /**
     *  The color yellow.
     */
    public static final Color YELLOW = Color.YELLOW;

    /**
     * Shade of blue
     */
    public static final Color BOOK_BLUE = new Color(9, 90, 166);

    /**
     * Shade of red
     */
    public static final Color BOOK_RED = new Color(150, 35, 31);

    // default colors
    private static final Color DEFAULT_PEN_COLOR    = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR  = WHITE;

    // current pen color
    private static Color penColor;

    // default canvas size is DEFAULT_SIZE x DEFAULT_SIZE
    private static final int DEFAULT_SIZE = 512;
    private static int width = DEFAULT_SIZE;
    private static int height = DEFAULT_SIZE;

    // default pen radius
    private static final double DEFAULT_PEN_RADIUS = 0.002;

    // current pen radius
    private static double penRadius;

    // Draw now or wait
    private static boolean defer = false;

    // boundary of drawing canvas, 0% border
    // private static final double BORDER = 0.05;
    private static final double BORDER          = 0.00;
    private static final double DEFAULT_XMIN    = 0.0;
    private static final double DEFAULT_XMAX    = 1.0;
    private static final double DEFAULT_YMIN    = 0.0;
    private static final double DEFAULT_YMAX    = 1.0;
    private static double xmin, ymin, xmax, ymax;

    // sync
    private static Object mouseLock = new Object();
    private static Object keyLock = new Object();

    // default font
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // current font
    private static Font font;

    // double buffered graphics
    private static BufferedImage offscreenImage, onscreenImage;
    private static Graphics2D offscreen, onscreen;

    // singleton for callbacks
    // avoids extra CLASS files
    private static StdDraw std = new StdDraw();

    // the frame for drawing to the screen
    private static JFrame frame;

    // mouse state
    private static boolean mousePressed = false;
    private static double mouseX = 0;
    private static double mouseY = 0;

    // queue of typed key characters
    private static LinkedList<Character> keysTyped = new LinkedList<Character>();

    // set of key codes pressed down
    private static TreeSet<Integer> keysDown = new TreeSet<Integer>();

    // time in ms, used to control the framerate
    private static long nextDraw = -1;

    // singleton pattern
    private StdDraw() { }

    // static initializer
    static {
        init();
    }

    /**
     * Sets the canvas to be 512x by 512px.
     * Erases the current drawing and resets the coordinate system, pen radius, pen color,
     * and font back to their default values.
     */
    public static void setCanvasSize() {
        setCanvasSize(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    /**
     * Sets the canvas to be width by height in px.
     * Erases current drawing and resets coordinate system, pen radius, pen color,
     * and font back to their default values
     *
     * @param canvasWidth the width in px
     * @param canvasHeight the height in px
     * @throws IllegalArgumentException unless params are +ve
     */
    public static void setCanvasSize(int canvasWidth, int canvasHeight) {
        if (canvasWidth <= 0 || canvasHeight <= 0) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        width = canvasWidth;
        height = canvasHeight;
        init();
    }

    // init
    private static void init() {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame();

        offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        offscreen = offscreenImage.createGraphics();
        onscreen = onscreenImage.createGraphics();

        setXscale();
        setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, width, height);
        setPenColor();
        setPenRadius();
        setFont();
        clear();

        // add AA
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // frame
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);

        draw.addMouseListener(std);
        draw.addMouseMotionListener(std);

        frame.setContentPane(draw);
        frame.addKeyListener(std);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Draw by kkmonlee");
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    // create menu bar
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem("Save...");
        menuItem1.addActionListener(std);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(menuItem1);
        return menuBar;
    }

    /**
     * Sets x-scale to be default (0.0 to 1.0)
     */
    public static void setXscale() {
        setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
    }

    /**
     * Sets the y-scale to be the default
     */
    public static void setYscale() {
        setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
    }

    /**
     * Sets x- and y-scale to be default
     */
    public static void setScale() {
        setXscale();
        setYscale();
    }

    public static void setXscale(double min, double max) {
        double size = max - min;
        if (size == 0.0) throw new IllegalArgumentException("min and max are the same");
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
        }
    }


    public static void setYscale(double min, double max) {
        double size = max - min;
        if (size == 0.0) throw new IllegalArgumentException("min and max are the same");
        synchronized (mouseLock) {
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    public static void setScale(double min, double max) {
        double size = max - min;
        if (size == 0.0) throw new IllegalArgumentException("min and max are the same");
        synchronized (mouseLock) {
            xmin = min - BORDER * size;
            xmax = max + BORDER * size;
            ymin = min - BORDER * size;
            ymax = max + BORDER * size;
        }
    }

    private static double scaleX(double x) { return width * (x - xmin) / (xmax - xmin); }
    private static double scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private static double factorX(double w) { return w * width  / Math.abs(xmax - xmin); }
    private static double factorY(double h) { return h * height / Math.abs(ymax - ymin); }
    private static double   userX(double x) { return xmin + x * (xmax - xmin) / width; }
    private static double   userY(double y) { return ymax - y * (ymax - ymin) / height; }

    public static void clear() {
        clear(DEFAULT_CLEAR_COLOR);
    }

    public static void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        draw();
    }

    public static double getPenRadius() {
        return penRadius;
    }

    public static void setPenRadius() {
        setPenRadius(DEFAULT_PEN_RADIUS);
    }

    public static void setPenRadius(double radius) {
        if (!(radius >= 0)) throw new IllegalArgumentException("pen radius must be nonnegative");
        penRadius = radius;
        float scaledPenRadius = (float) (radius * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        offscreen.setStroke(stroke);
    }

    public static Color getPenColor() {
        return penColor;
    }

    public static void setPenColor() {
        setPenColor(DEFAULT_PEN_COLOR);
    }

    public static void setPenColor(Color color) {
        if (color == null) throw new NullPointerException();
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setPenColor(int red, int green, int blue) {
        if (red < 0 || red >= 256) throw new IllegalArgumentException();
        if (green < 0 || green >= 256) throw new IllegalArgumentException();
        if (blue < 0 || blue >= 256) throw new IllegalArgumentException();
        setPenColor(new Color(red, green, blue));
    }

    public static Font getFont() {
        return font;
    }

    public static void setFont() {
        setFont(DEFAULT_FONT);
    }

    public static void setFont(Font font) {
        if (font == null) throw new NullPointerException();
        StdDraw.font = font;
    }

    public static void line(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        draw();
    }

    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        float scaledPenRadius = (float) (r * DEFAULT_SIZE);

        if (scaledPenRadius <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - scaledPenRadius / 2, ys - scaledPenRadius / 2, scaledPenRadius, scaledPenRadius));
        draw();
    }

    public static void circle(double x, double y, double radius) {
        if (!(radius >= 0)) throw new IllegalArgumentException();
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*radius);
        double hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledCircle(double x, double y, double radius) {
        if (!(radius >= 0)) throw new IllegalArgumentException("radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*radius);
        double hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (!(semiMajorAxis >= 0)) throw new IllegalArgumentException("ellipse semimajor axis must be nonnegative");
        if (!(semiMinorAxis >= 0)) throw new IllegalArgumentException("ellipse semiminor axis must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis);
        double hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (!(semiMajorAxis >= 0)) throw new IllegalArgumentException("ellipse semimajor axis must be nonnegative");
        if (!(semiMinorAxis >= 0)) throw new IllegalArgumentException("ellipse semiminor axis must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis);
        double hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void arc(double x, double y, double radius, double angle1, double angle2) {
        if (radius < 0) throw new IllegalArgumentException("arc radius must be nonnegative");
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*radius);
        double hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        draw();
    }

    public static void square(double x, double y, double halfLength) {
        if (!(halfLength >= 0)) throw new IllegalArgumentException("half length must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfLength);
        double hs = factorY(2*halfLength);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledSquare(double x, double y, double halfLength) {
        if (!(halfLength >= 0)) throw new IllegalArgumentException("half length must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfLength);
        double hs = factorY(2*halfLength);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (!(halfWidth  >= 0)) throw new IllegalArgumentException("half width must be nonnegative");
        if (!(halfHeight >= 0)) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        if (!(halfWidth  >= 0)) throw new IllegalArgumentException("half width must be nonnegative");
        if (!(halfHeight >= 0)) throw new IllegalArgumentException("half height must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void polygon(double[] x, double[] y) {
        if (x == null) throw new NullPointerException();
        if (y == null) throw new NullPointerException();
        int n1 = x.length;
        int n2 = y.length;
        if (n1 != n2) throw new IllegalArgumentException("arrays must be of the same length");
        int n = n1;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.draw(path);
        draw();
    }

    public static void filledPolygon(double[] x, double[] y) {
        if (x == null) throw new NullPointerException();
        if (y == null) throw new NullPointerException();
        int n1 = x.length;
        int n2 = y.length;
        if (n1 != n2) throw new IllegalArgumentException("arrays must be of the same length");
        int n = n1;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++)
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.fill(path);
        draw();
    }

    private static Image getImage(String filename) {
        if (filename == null) throw new NullPointerException();

        // to read from file
        ImageIcon icon = new ImageIcon(filename);

        // try to read from URL
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            try {
                URL url = new URL(filename);
                icon = new ImageIcon(url);
            }
            catch (Exception e) {
                /* not a url */
            }
        }

        // in case file is inside a .jar (classpath relative to StdDraw)
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource(filename);
            if (url != null)
                icon = new ImageIcon(url);
        }

        // in case file is inside a .jar (classpath relative to root of jar)
        if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
            URL url = StdDraw.class.getResource("/" + filename);
            if (url == null) throw new IllegalArgumentException("image " + filename + " not found");
            icon = new ImageIcon(url);
        }

        return icon.getImage();
    }

    public static void picture(double x, double y, String filename) {
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");

        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        draw();
    }


    public static void picture(double x, double y, String filename, double degrees) {
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0), null);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);

        draw();
    }


    public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
        Image image = getImage(filename);
        if (scaledWidth < 0) throw new IllegalArgumentException("width is negative: " + scaledWidth);
        if (scaledHeight < 0) throw new IllegalArgumentException("height is negative: " + scaledHeight);
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(scaledWidth);
        double hs = factorY(scaledHeight);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image " + filename + " is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                    (int) Math.round(ys - hs/2.0),
                    (int) Math.round(ws),
                    (int) Math.round(hs), null);
        }
        draw();
    }


    public static void text(double x, double y, String text) {
        if (text == null) throw new NullPointerException();
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws/2.0), (float) (ys + hs));
        draw();
    }

    public static void textLeft(double x, double y, String text) {
        if (text == null) throw new NullPointerException();
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) xs, (float) (ys + hs));
        draw();
    }

    public static void textRight(double x, double y, String text) {
        if (text == null) throw new NullPointerException();
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws), (float) (ys + hs));
        draw();
    }

    public static void show(int t) {
        // sleep until the next time we're allowed to draw
        long millis = System.currentTimeMillis();
        if (millis < nextDraw) {
            try {
                Thread.sleep(nextDraw - millis);
            }
            catch (InterruptedException e) {
                System.out.println("Error sleeping");
            }
            millis = nextDraw;
        }

        show();
        enableDoubleBuffering();

        // when are we allowed to draw again
        nextDraw = millis + t;
    }


    public static void pause(int t) {
        // sleep until the next time we're allowed to draw
        long millis = System.currentTimeMillis();
        if (millis < nextDraw) {
            try {
                Thread.sleep(nextDraw - millis);
            }
            catch (InterruptedException e) {
                System.out.println("Error sleeping");
            }
            millis = nextDraw;
        }

        // when are we allowed to draw again
        nextDraw = millis + t;
    }

    public static void show() {
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    // draw onscreen if defer is false
    private static void draw() {
        if (!defer) show();
    }

    public static void enableDoubleBuffering() {
        defer = true;
    }

    public static void disableDoubleBuffering() {
        defer = false;
    }

    public static void save(String filename) {
        if (filename == null) throw new NullPointerException();
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        // png files
        if (suffix.toLowerCase().equals("png")) {
            try {
                ImageIO.write(onscreenImage, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // need to change from ARGB to RGB for JPEG
        // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
        else if (suffix.toLowerCase().equals("jpg")) {
            WritableRaster raster = onscreenImage.getRaster();
            WritableRaster newRaster;
            newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[] {0, 1, 2});
            DirectColorModel cm = (DirectColorModel) onscreenImage.getColorModel();
            DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(),
                    cm.getRedMask(),
                    cm.getGreenMask(),
                    cm.getBlueMask());
            BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false,  null);
            try {
                ImageIO.write(rgbBuffer, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        else {
            System.out.println("Invalid image file type: " + suffix);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(StdDraw.frame, "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        String filename = chooser.getFile();
        if (filename != null) {
            StdDraw.save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }

    public static boolean mousePressed() {
        synchronized (mouseLock) {
            return mousePressed;
        }
    }

    public static double mouseX() {
        synchronized (mouseLock) {
            return mouseX;
        }
    }


    public static double mouseY() {
        synchronized (mouseLock) {
            return mouseY;
        }
    }


    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseEntered(MouseEvent e) { }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseExited(MouseEvent e) { }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
            mousePressed = true;
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (mouseLock) {
            mousePressed = false;
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseDragged(MouseEvent e)  {
        synchronized (mouseLock) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
        }
    }


    public static boolean hasNextKeyTyped() {
        synchronized (keyLock) {
            return !keysTyped.isEmpty();
        }
    }

    public static char nextKeyTyped() {
        synchronized (keyLock) {
            if (keysTyped.isEmpty()) {
                throw new NoSuchElementException("your program has already processed all keystrokes");
            }
            return keysTyped.removeLast();
        }
    }

    public static boolean isKeyPressed(int keycode) {
        synchronized (keyLock) {
            return keysDown.contains(keycode);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        synchronized (keyLock) {
            keysTyped.addFirst(e.getKeyChar());
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.add(e.getKeyCode());
        }
    }

    /**
     * This method cannot be called directly.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.remove(e.getKeyCode());
        }
    }

    public static void main(String[] args) {
        StdDraw.square(.2, .8, .1);
        StdDraw.filledSquare(.8, .8, .2);
        StdDraw.circle(.8, .2, .2);

        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.setPenRadius(.02);
        StdDraw.arc(.8, .2, .1, 200, 45);

        // draw a blue diamond
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        double[] x = { .1, .2, .3, .2 };
        double[] y = { .2, .3, .2, .1 };
        StdDraw.filledPolygon(x, y);

        // text
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.2, 0.5, "black text");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.8, 0.8, "white text");
    }

}
