import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import mapper.Mapper;

public class ImageComponent extends JComponent {

    BufferedImage img;
    
    public ImageComponent(String path) {
    	try {
    		File f = new File(Mapper.ABSOLUTE_PATH_APPLICATION + path);
    		System.out.println(f.getAbsolutePath());
    		img = ImageIO.read(f);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        g.drawImage(img, 0, 0, null);
        super.paintComponent(g);
    }

    public Dimension getPreferredSize() {
       if (img == null) {
          return new Dimension(100,100);
       } else {
          return new Dimension(img.getWidth(), img.getHeight());
       }
    }
 }
