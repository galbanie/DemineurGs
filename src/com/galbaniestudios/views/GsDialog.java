/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *
 * @author galbanie
 */
public class GsDialog extends JDialog implements ActionListener{
    //private JLabel  lblLigne, lblColonne;
    private JSlider sLigne, sColonne;
    private JButton appliquer,annuler;
    private File config = new File("config.gs");
            
    public GsDialog(JFrame parent, String titre, boolean modale){
        super(parent,titre,modale);
        this.setSize(550, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        this.initComponent();
        
        this.configuration();
        
        if (this.getParent() instanceof FenetreDemineur){
            FenetreDemineur p = ((FenetreDemineur)this.getParent());
            p.setLignes((int)sLigne.getValue());
            p.setColonnes((int)sColonne.getValue());
        }
    }
    
    private void initComponent(){
        /*//Titre
        lblTitre = new JLabel("Propriété du démineur.");
        JPanel panTitre = new JPanel();
        panTitre.setBackground(Color.white);
        panTitre.setLayout(new BorderLayout());
        panTitre.add(lblTitre);*/
        
        //La ligne
        JPanel panLigne = new JPanel();
        panLigne.setBackground(Color.white);
        panLigne.setPreferredSize(new Dimension(500, 150));
        sLigne = new JSlider(5,15,10);
        sLigne.setBackground(Color.white);
        sLigne.setPreferredSize(new Dimension(450, 90));
        sLigne.setMajorTickSpacing(5);
        sLigne.setMinorTickSpacing(1);
        sLigne.setPaintTicks(true);
        sLigne.setPaintLabels(true);
        panLigne.setBorder(BorderFactory.createTitledBorder("Nombre de Ligne."));
        //lblLigne = new JLabel("Sélectionner la ligne :");
        //panLigne.add(lblLigne);
        panLigne.add(sLigne);
        
        //La Colonne
        JPanel panColonne = new JPanel();
        panColonne.setBackground(Color.white);
        panColonne.setPreferredSize(new Dimension(500, 150));
        sColonne = new JSlider(5,20,10);
        sColonne.setBackground(Color.white);
        sColonne.setMajorTickSpacing(5);
        sColonne.setMinorTickSpacing(1);
        sColonne.setPaintTicks(true);
        sColonne.setPaintLabels(true);
        sColonne.setPreferredSize(new Dimension(450, 90));
        panColonne.setBorder(BorderFactory.createTitledBorder("Nombre de Colonne."));
        //lblColonne = new JLabel("Sélectionner la colonne :");
        //panColonne.add(lblColonne);
        panColonne.add(sColonne);
        
        JPanel content = new JPanel();
        content.setBackground(Color.white);
        content.add(panLigne);
        content.add(panColonne);
        
        JPanel control = new JPanel();
        appliquer = new JButton("Appliquer");
     
        appliquer.addActionListener(this);
  
        annuler = new JButton("Annuler");
        annuler.addActionListener(this);
 
    control.add(appliquer);
    control.add(annuler);
 
    //this.getContentPane().add(panTitre, BorderLayout.NORTH);
    this.getContentPane().add(content, BorderLayout.CENTER);
    this.getContentPane().add(control, BorderLayout.SOUTH);
    }
    
    public void reconfigure(int ligne, int colonne){
        sLigne.setValue(ligne);
        sColonne.setValue(colonne);
        ecrireConfiguration();
        if (this.getParent() instanceof FenetreDemineur){
            FenetreDemineur p = ((FenetreDemineur)this.getParent());
            p.setLignes((int)sLigne.getValue());
            p.setColonnes((int)sColonne.getValue());
        }
        
    }
    
    private void configuration(){
        if (config.exists()){
            lireConfiguration();
        }
        else {
            ecrireConfiguration();
            lireConfiguration();
        }
    }
    
    private void ecrireConfiguration(){
       FileOutputStream fos = null;
       try{
          fos = new FileOutputStream(config);
          fos.write(sLigne.getValue());
          fos.write(sColonne.getValue());
       } catch(FileNotFoundException e){
            e.printStackTrace();
       } catch (IOException e) {
            e.printStackTrace();
       } finally {
           try {
               fos.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
    
    private void lireConfiguration(){
        FileInputStream fis = null;
        try{
          fis = new FileInputStream(config);
          sLigne.setValue(fis.read());
          sColonne.setValue(fis.read());
        }catch(FileNotFoundException e){
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == appliquer){
            ecrireConfiguration();
            if (this.getParent() instanceof FenetreDemineur) {
                FenetreDemineur p = ((FenetreDemineur)this.getParent());
                p.setLignes((int)sLigne.getValue());
                p.setColonnes((int)sColonne.getValue());
                p.dispose();
                String[] args = null;
                p.main(args,null);
            }
            this.setVisible(false);
            
        }
        if (e.getSource() == annuler){
            this.setVisible(false);
        }
    }
}