package gdimageparting;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GdImageParting {

    static JFrame frame;
    JLabel label;
    static String starFile;
    static public double schaal;
    static int imageHoogte;

    private void createUi(Container cnt, ImageIcon backgr) {
        SelectRectangle area = new SelectRectangle(backgr, this);
        cnt.add(area);
    }

    protected static ImageIcon createBachground(String deImageFile, int frameHoogte) throws IOException {
        File imageFile = new File(deImageFile);
        Image imageToBeDisplayed = ImageIO.read(imageFile);
        int heightImageToBeDisplayed = imageToBeDisplayed.getHeight(null);
        Image imageVooricon = imageToBeDisplayed.getScaledInstance(-1, frameHoogte, Image.SCALE_FAST);
        imageHoogte = imageVooricon.getHeight(frame);
        schaal = frameHoogte / (double) heightImageToBeDisplayed;
        return new ImageIcon(imageVooricon);
    }

    private static void createGUI(String parameterFile) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] schermen = ge.getScreenDevices();
        GraphicsDevice mijnScherm = schermen[0];
        starFile = parameterFile;
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("ImageParting");
        if (parameterFile == "leeg") {
            JFileChooser fc = new JFileChooser();
//            FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                    "JPG & GIF Images", "jpg", "gif", "png","jpeg", "tiff");
//            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                starFile = file.getCanonicalPath();
            } else {
                frame.removeAll();
                frame.pack();
                System.exit(99);

            }
        } else {
            starFile = parameterFile;
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameHoogte = mijnScherm.getDisplayMode().getHeight();
        int schermwijdte = mijnScherm.getDisplayMode().getWidth();
        frameHoogte = frameHoogte - 100;
        GdImageParting controller = new GdImageParting();
        controller.createUi(frame.getContentPane(), createBachground(starFile, frameHoogte));
        frame.setSize(imageHoogte, frameHoogte);
        frame.setLocation(20, 0);
        ToetsLuistenaar toetsl = new ToetsLuistenaar();
        frame.addKeyListener(toetsl);

        frame.pack();
        frame.setVisible(true);
    }

    static private class ToetsLuistenaar extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("toets " + e.getKeyCode());
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                frame.removeAll();
                frame.pack();

                try {
                    createGUI("leeg");
                } catch (IOException ex) {
                    Logger.getLogger(GdImageParting.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (args.length <= 0) {
                    try {
                        createGUI("leeg");
                    } catch (IOException ex) {
                        Logger.getLogger(GdImageParting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        createGUI(args[0]);
                    } catch (IOException ex) {
                        Logger.getLogger(GdImageParting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
