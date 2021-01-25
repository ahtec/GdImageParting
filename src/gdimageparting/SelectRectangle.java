/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gdimageparting;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

class SelectRectangle extends JLabel {

    Rectangle curRect = null;
    Rectangle rectDraw = null;
    Rectangle prevRectDrawn = new Rectangle();
    GdImageParting drag;
    int drukX, drukY;
    int idx = 1;

    public SelectRectangle(ImageIcon icon, GdImageParting ctlr) {

        //In order to dispay the image
        super(icon);
        this.drag = ctlr;
        setOpaque(true);
        setMinimumSize(new Dimension(4, 4));
        MListener mylistn = new MListener();
        addMouseListener(mylistn);
        addMouseMotionListener(mylistn);

    }

    private class MListener extends MouseInputAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            int x = event.getX();
            drukX = x;
            int y = event.getY();
            drukY = y;
            System.out.println("SelectRectangle.MListener.mousePressed()" + x + " " + y);
            curRect = new Rectangle(x, y, 0, 0);
            updateDrawableRect(getWidth(), getHeight());
            repaint();

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            updateSize(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int losX = e.getX();
            int losY = e.getY();

            updateSize(e);
//            System.out.println("SelectRectangle.MListener.mouseReleased()" + e.getX() + " " + e.getY());
            Object[] options = {"Yes, please", "toch maar niet"};
            int n = JOptionPane.showOptionDialog(GdImageParting.frame,
                    "Deze selected area gebruiken?",
                    "Nieuw van selectie",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, //do not use a custom Icon
                    options, //the titles of buttons
                    options[1]); //default button title
//            System.out.println("SelectRectangle.MListener.mouseReleased() " + n);
            if (n == 0) {

                try {
                    // make  deel image
                    File inputFile = new File(GdImageParting.starFile);
                    String erinExtension = getExtension(inputFile.getName());
                    final BufferedImage source = ImageIO.read(inputFile);
                    System.out.println("SelectRectangle.MListener.mouseReleased()" + drukX + " " + drukY + " " + losX + " " + losY);
                    int maxX = source.getWidth();
                    int maxY = source.getHeight();

                    int linksX = Math.min(losX, drukX);
                    int bovenY = Math.min(losY, drukY);

                    int rechtsX = Math.max(losX, drukX);
                    rechtsX = Math.min(rechtsX, maxX);
                    int onderY = Math.max(losY, drukY);
                    onderY = Math.min(onderY, maxY);
                    
                    int widthX = rechtsX - linksX;
                    int widthY = onderY - bovenY;

                    ImageIO.write(source.getSubimage(linksX, bovenY, widthX, widthY), erinExtension, new File(inputFile.getCanonicalPath() + idx++ + "." + erinExtension));

                } catch (IOException ex) {
                    Logger.getLogger(SelectRectangle.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        void updateSize(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            curRect.setSize(x - curRect.x, y - curRect.y);
            updateDrawableRect(getWidth(), getHeight());
            Rectangle totalRepaint = rectDraw.union(prevRectDrawn);
//            System.out.println("SelectRectangle.MListener.updateSize()" + x + " "+ y);
            repaint(totalRepaint.x, totalRepaint.y, totalRepaint.width, totalRepaint.height);

        }
    }

    @Override
    protected void paintComponent(Graphics grapgic) {
        // setup the backgroud
        super.paintComponent(grapgic);
        if (curRect != null) {
            grapgic.setXORMode(Color.white);
            grapgic.drawRect(rectDraw.x, rectDraw.y, rectDraw.width - 1, rectDraw.height - 1);
            drag.updateLabel(rectDraw);
        }
    }

    private void updateDrawableRect(int compW, int compH) {
        int x = curRect.x;
        int y = curRect.y;
        int w = curRect.width;
        int h = curRect.height;
        if (w < 0) {
            w = 0 - w;
            x = x - w + 1;
            if (x < 0) {
                w += x;
                x = 0;
            }
        }
        if (h < 0) {
            h = 0 - h;
            y = y - h + 1;
            if (y < 0) {
                h += y;
                y = 0;
            }
        }
        if ((x + w) > compW) {
            w = compW - x;
        }
        if ((y + h) > compH) {
            h = compH - y;
        }
        if (rectDraw != null) {
            prevRectDrawn.setBounds(rectDraw.x, rectDraw.y, rectDraw.width, rectDraw.height);
            rectDraw.setBounds(x, y, w, h);
        } else {
            rectDraw = new Rectangle(x, y, w, h);
        }
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

}
