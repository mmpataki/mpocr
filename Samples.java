/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author staff
 */
public class Samples extends JFrame {

    public static void main(String[] args) {
        new Samples().setVisible(true);
    }
    
    Samples() {
        setSize(100, 100);
        mCanvas can = new mCanvas("C:\\Users\\staff\\Desktop\\test.bmp");
        add(can).setSize(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
