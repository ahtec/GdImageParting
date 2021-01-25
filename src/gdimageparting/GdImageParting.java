package gdimageparting;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GdImageParting {


    static JFrame frame;
    JLabel label;
    static String starFile ;
    static public double schaal;

    private void createUi(Container cnt, ImageIcon backgr) {
        cnt.setLayout(new BoxLayout(cnt, BoxLayout.PAGE_AXIS));
        SelectRectangle area = new SelectRectangle(backgr, this);
        cnt.add(area);
        label = new JLabel("");
        label.setLabelFor(area);
        cnt.add(label);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void updateLabel(Rectangle rect) {
        int w = rect.width;
        int h = rect.height;
        if (w == 0) {
            w = 1;
        }
        if (h == 0) {
            h = 1;
        }
        label.setText("Rectangle extends from (" + rect.x + ", " + rect.y
                + ") to (" + (rect.x + w - 1) + ", "
                + (rect.y + h - 1) + ").");
    }

    protected static ImageIcon createBachground(String path) {
        return new ImageIcon(path);
    }

    private static void createGUI( ) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] schermen = ge.getScreenDevices();
        GraphicsDevice mijnScherm = schermen[0];

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("ImageParting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameHoogte = mijnScherm.getDisplayMode().getHeight();
        frameHoogte = frameHoogte - 100;

        GdImageParting controller = new GdImageParting();
        controller.createUi(frame.getContentPane(), createBachground(starFile));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                starFile = args[0];
                createGUI();
            }
        });
    }
}


