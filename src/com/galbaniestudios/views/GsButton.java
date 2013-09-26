/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.views;

import com.galbaniestudios.modeles.Case;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 *
 * @author galbanie
 */
public class GsButton extends JButton implements Observer{
    private Image img;

    public GsButton() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img == null) return;
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    public void setImg(String str) {
        if ("".equals(str)) img = null;
        try{
            img = ImageIO.read(getClass().getResource(str));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Case){
            Case c = (Case)o;
            if (c.isDecouvert()){
                this.setEnabled(false);
                if (c.isMine()) {
                    setImg("/Ressources/mineperdu.jpg");
                }
                else {
                    setImg("/Ressources/bloc_"+String.valueOf(c.getNombre())+".bmp");
                }
            }
            else {
                if(c.isFlag()) setImg("/Ressources/drapeau.png");
                else{
                    this.setEnabled(true);
                    this.setImg("");
                    this.repaint();
                }
                
            }
        }
    }
    
}
