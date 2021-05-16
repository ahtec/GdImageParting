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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
//import org.apache.commons.io.comparator.NameFileComparator;

public class GdImageParting {

    static JFrame frame;
    private static String versie =  "v2";
    JLabel label;
    static String starFile;
    static public double schaal;
    static int imageHoogte, imageBreedte;

    static File currentFile;
    static File imageFile;
    static File[] filesInDirectoryVanCurrentImage;
    static int filePionterInCurrectDirectory;

    private static File[] vulFilesInDirectoryVanCurrentImage() throws IOException {
                    System.out.println("Vullen files array " + currentFile.getCanonicalPath());

        File directoryVanCurrentImage = currentFile.getParentFile();
        File[] eruit = directoryVanCurrentImage.listFiles(new MyFileExtensionFilter());
//        Arrays.sort(eruit);

        return (eruit);
    }

    protected static ImageIcon createBachground(String deImageFile, int schermHoogte, int schermBreedte) throws IOException {
        double quotientSchermWH, quotientImageWH;
        Image imageVooricon;
        imageFile = new File(deImageFile);

        Image imageToBeDisplayed = ImageIO.read(imageFile);
        int heightImageToBeDisplayed = imageToBeDisplayed.getHeight(null);
        int widthImageToBeDisplayed = imageToBeDisplayed.getWidth(null);

        System.out.println("Image breedte "+ widthImageToBeDisplayed);
        System.out.println("Image hoogte "+heightImageToBeDisplayed);

        quotientSchermWH = schermBreedte / (double) schermHoogte;
        quotientImageWH = widthImageToBeDisplayed / (double) heightImageToBeDisplayed  ;
        System.out.println("quaScherWH"+ quotientSchermWH);
        System.out.println("quaImageWH"+ quotientImageWH);
        

        quotientSchermWH = schermBreedte / (double) schermHoogte;
        quotientImageWH = widthImageToBeDisplayed / (double) heightImageToBeDisplayed;
        if (quotientSchermWH > quotientImageWH) {
            // schalen op hoogte
            System.out.println("schalen op hoogte");
            imageVooricon = imageToBeDisplayed.getScaledInstance(-1, schermHoogte, Image.SCALE_FAST);
            imageHoogte = imageVooricon.getHeight(frame);
            imageBreedte = imageVooricon.getWidth(frame);
            schaal = schermHoogte / (double) heightImageToBeDisplayed;

        } else {
            // schalen op breedte
            System.out.println("schalen op breedte");
            imageVooricon = imageToBeDisplayed.getScaledInstance(schermBreedte, -1, Image.SCALE_FAST);
            imageHoogte = imageVooricon.getHeight(frame);
            imageBreedte = imageVooricon.getWidth(frame);
            schaal = schermBreedte / (double) widthImageToBeDisplayed;
        }
        return new ImageIcon(imageVooricon);
    }

    private void createUi(Container cnt, ImageIcon backgr) {
        SelectRectangle area = new SelectRectangle(backgr, this);
        cnt.add(area);
    }

    private static void createGUI(String parameterFile) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] schermen = ge.getScreenDevices();
        GraphicsDevice mijnScherm = schermen[0];
        starFile = parameterFile;
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame(starFile);
        if (parameterFile.compareTo("leeg") == 0) {
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
        //OP dit punt weet ik de imagefile en de image directory
        currentFile = new File(starFile);
//        String vorigeDir = filesInDirectoryVanCurrentImage[0].getParentFile().getCanonicalPath();
//        if (parameterFile.compareToIgnoreCase(vorigeDir) == 0) {
            filesInDirectoryVanCurrentImage = vulFilesInDirectoryVanCurrentImage();
//        }
        filePionterInCurrectDirectory = setFilePionterInCurrectDirectory();
        frame.setTitle(starFile + " "+ versie);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameHoogte = mijnScherm.getDisplayMode().getHeight();
        int schermwijdte = mijnScherm.getDisplayMode().getWidth();
        frameHoogte = frameHoogte - 100;
        schermwijdte = schermwijdte - 100;
        System.out.println("frameHoogte "+ frameHoogte);
        System.out.println("schermwijdte "+ schermwijdte);
        GdImageParting controller = new GdImageParting();
        controller.createUi(frame.getContentPane(), createBachground(starFile, frameHoogte, schermwijdte));
//controler.SelectRectangle area = new SelectRectangle(createBachground(starFile, frameHoogte, schermwijdte), this);
        frame.setSize(imageBreedte, imageHoogte);
        frame.setLocation(20, 0);
        ToetsLuistenaar toetsl = new ToetsLuistenaar();
        frame.addKeyListener(toetsl);

        frame.pack();
        frame.setVisible(true);
    }
 private static void  createGUINext(String parameterFile) throws IOException {
           GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] schermen = ge.getScreenDevices();
        GraphicsDevice mijnScherm = schermen[0];
        starFile = parameterFile;
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setTitle(starFile);
//        frame = new JFrame(starFile);
        if (parameterFile.compareTo("leeg") == 0) {
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
                System.exit(0);

            }
        } else {
            starFile = parameterFile;
        }
        //OP dit punt weet ik de imagefile en de image directory
        currentFile = new File(starFile);
//        String vorigeDir = filesInDirectoryVanCurrentImage[0].getParentFile().getCanonicalPath();
//        if (parameterFile.compareToIgnoreCase(vorigeDir) == 0) {
//            filesInDirectoryVanCurrentImage = vulFilesInDirectoryVanCurrentImage();
//        }
//        filePionterInCurrectDirectory = setFilePionterInCurrectDirectory();
        frame.setTitle(starFile);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameHoogte = mijnScherm.getDisplayMode().getHeight();
        int schermwijdte = mijnScherm.getDisplayMode().getWidth();
        frameHoogte = frameHoogte - 100;
        schermwijdte = schermwijdte - 100;
        GdImageParting controller = new GdImageParting();
        controller.createUi(frame.getContentPane(), createBachground(starFile, frameHoogte, schermwijdte));
        frame.setSize(imageBreedte, imageHoogte);
        frame.setLocation(20, 0);
        ToetsLuistenaar toetsl = new ToetsLuistenaar();
        frame.addKeyListener(toetsl);

        frame.pack();
        frame.setVisible(true);  
 }
    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            index++;
            return fileName.substring(index);
        }
    }

    private static int setFilePionterInCurrectDirectory() throws IOException {
        int i = 0;
        for (File fileInDirectoryVanCurrentImage : filesInDirectoryVanCurrentImage) {
            if (fileInDirectoryVanCurrentImage.isFile()) {
                if (fileInDirectoryVanCurrentImage.equals(currentFile)) {
                    System.out.println("gevonden :  " + fileInDirectoryVanCurrentImage.getCanonicalPath());
                    break;
                }
            }
            i++;
        }

        return (i);
    }

//    static void showNext(File erin) throws IOException {
//        frame.removeAll();
////        frame.pack();
//        frame.setTitle(erin.getCanonicalPath());
//        controller.createUi(frame.getContentPane(), createBachground());
//        frame.setSize(imageBreedte, imageHoogte);
//        frame.setLocation(20, 0);
////        ToetsLuistenaar toetsl = new ToetsLuistenaar();
////        frame.addKeyListener(toetsl);
//        frame.pack();
//        frame.setVisible(true);
//
//    }
//
    static private class ToetsLuistenaar extends KeyAdapter {

//        static File[] filesInDirectoryVanCurrentImage;
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

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                filePionterInCurrectDirectory++;
                if (filePionterInCurrectDirectory >= filesInDirectoryVanCurrentImage.length) {
                    filePionterInCurrectDirectory = 0;
                }
                frame.removeAll();
//                frame.pack();
                try {
                    createGUINext(filesInDirectoryVanCurrentImage[filePionterInCurrectDirectory].getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(GdImageParting.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                filePionterInCurrectDirectory--;
                if (filePionterInCurrectDirectory < 0) {
                    filePionterInCurrectDirectory = filesInDirectoryVanCurrentImage.length;
                    filePionterInCurrectDirectory--;
                }
                frame.removeAll();
                frame.pack();
                try {
                    createGUINext(filesInDirectoryVanCurrentImage[filePionterInCurrectDirectory].getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(GdImageParting.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }
 
    public static void main(String[] args) {
//        init();
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

    public static class MyFileExtensionFilter implements FilenameFilter {

        private String extension;

//		public MyFileNameFilter() {
////			this.extension = extension.toLowerCase();
//		}
        @Override
        public boolean accept(File dir, String name) {
            boolean eruit;
            eruit = Boolean.FALSE;
            if ((name.toLowerCase().endsWith("jpeg")) || (name.toLowerCase().endsWith("jpg"))) {
                eruit = Boolean.TRUE;
            }
            return (eruit);
        }

    }
}
