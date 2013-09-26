/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.views;

import com.galbaniestudios.modeles.Grille;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author galbanie
 */
public class GsPanelFileChooser extends JPanel implements PropertyChangeListener{
    
    private JFileChooser chooser;
    private JLabel nom, date, cases,mine, difficulte, drapeau;
    private GsButton icone;
   
    public GsPanelFileChooser(JFileChooser fileChooser){
        super(new GridLayout(0, 1));
        
        icone = new GsButton();
        icone.setPreferredSize(new Dimension(32, 32));
        icone.setMaximumSize(icone.getPreferredSize());
        icone.setEnabled(false);
        icone.setBorderPainted(false);
        JPanel icon = new JPanel(new FlowLayout());
        icon.add(icone);
        add(icon);
        add(nom = new JLabel("Nom du fichier"));
        add(date = new JLabel("Dernière mod. du fichier"));
        add(cases = new JLabel("Nombres de cases"));
        add(mine = new JLabel("Nombre de mines"));
        add(difficulte = new JLabel("Difficulté"));
        add(drapeau = new JLabel("Drapeau restant"));
        
        this.chooser = fileChooser;
        this.chooser.addPropertyChangeListener(this);
        setBorder(new TitledBorder("Aperçu")); 
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
		
	if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)){
            File file = (File) evt.getNewValue();
            if(file == null){
		clear();
		return;	
            }
            
            FileSystemView vueSysteme = FileSystemView.getFileSystemView(); 
            Locale locale = Locale.getDefault();
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
            
            nom.setText("Nom du fichier : "+vueSysteme.getSystemDisplayName(file));
            String dateFile = dateFormat.format(new Date(file.lastModified()));
            date.setText("Dernière mod : "+dateFile);
            try{
                FileInputStream fichier = new FileInputStream(file.getAbsolutePath());
                ObjectInputStream ois = new ObjectInputStream(fichier);
                Grille grille = (Grille)ois.readObject();

                cases.setText("Nombre de cases : "+String.valueOf(grille.getNombreLignes()*grille.getNombreColonnes())+" cases");
                mine.setText("Nombre de mines : "+String.valueOf(grille.getNombreMines())+" mines");

                String strDifficulte = "";

                if (grille.getDifficulte() == 1) {
                    strDifficulte = "Facile";
                    icone.setImg("/Ressources/Smiley-icon.png");
                    icone.repaint();
                }
                else if (grille.getDifficulte() == 2) {
                    strDifficulte = "Moyen";
                    icone.setImg("/Ressources/Smiley-grumpy-icon.png");
                    icone.repaint();
                }
                else if (grille.getDifficulte() == 3) {
                    strDifficulte = "Difficile";
                    icone.setImg("/Ressources/Smiley-teards-icon.png");
                    icone.repaint();
                }

                difficulte.setText("Difficulté : "+strDifficulte);
                drapeau.setText("Drapeau restant : "+String.valueOf(grille.getNombreMines()-grille.compterFlag()));
            }catch(IOException ioe){
                ioe.printStackTrace();
            }catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            
        }
        else clear();
    }
        
    private void clear(){
        icone.setImg("");
	nom.setText("Nom du fichier");	
	date.setText("Dernière mod. du fichier");
	cases.setText("Nombres de cases");
        mine.setText("Nombre de mines");
	difficulte.setText("Difficulté");
        drapeau.setText("Drapeau restant");
    }
    
}
