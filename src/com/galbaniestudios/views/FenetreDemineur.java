/*
 * Setruk Marc-Roger
 * Démineur Gs
 * 
 */
package com.galbaniestudios.views;

import com.galbaniestudios.modeles.Grille;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author galbanie
 */
public class FenetreDemineur extends JFrame implements Observer, ActionListener, MouseListener{

    // boutton
    private GsButton[][] btns;
    private GsButton btnStatus;
    private GsDialog config;
    // modèle
    private Grille grilleModele;
    // label
    private JLabel lblNbMine;
    private JLabel lblMine;
    private JLabel lblNbCase;
    private JLabel lblCase;
    private JLabel lblNbFlagRestant;
    private JLabel lblFlagRestant;
    private JLabel lblFlag;
    private JLabel lblStatus;
    //private JLabel lblpourcentage;
    //variable native
    private String[] args;
    private int lignes, colonnes;
    private boolean pause = false;
    
    
    // attribut menu bar et menu
    private JMenuBar menu;
    private JMenu fichier;
    private JMenu partie;
    private JMenu aide;
    private JMenu triche;
    // attribut item menu fichier
    private JMenuItem sauvegarder;
    private JMenuItem charger;
    private JMenuItem propriete;
    private JMenuItem quitter;
    // attribut item menu partie
    private JMenuItem nouveau;
    private JMenuItem jouer;
    private JMenuItem MIpause;
    private JMenuItem recommencer;
    private JMenuItem decouvrirTous;
    private JMenuItem recouvrirTous;
    // attribut item menu ?
    private JMenuItem apropos;
    // Autres composantes
    private JFileChooser fileChooser;
    private JPanel panePrincipal;
    private JOptionPane optionPane;

    /*public FenetreDemineur() throws HeadlessException {
        super();
        
        /*config = new GsDialog(this, "Propriété du démineur", true);
        /*lignes = demanderLigne();
        colonnes = demanderColonne();
        grilleModele = new Grille(lignes,colonnes,demanderDifficulte());
        nbDrapeauRestant = grilleModele.getNombreMines();
        grilleModele.addObserver(this);
        
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("GS File", "gs", "dem");
        fileChooser.setFileFilter(filter);
        
        construireMenu();
        construireLbl();
        construireBtn();
        construireContentPane();
        construire();
        
        
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }*/
    
    /**
     *
     * @param grille
     * @throws HeadlessException
     */
    public FenetreDemineur(Grille grille) throws HeadlessException {
        config = new GsDialog(this, "Propriété du démineur", true);
        /*lignes = demanderLigne();
        colonnes = demanderColonne();*/
        
        if (grille != null) {
            config.reconfigure(grille.getNombreLignes(), grille.getNombreColonnes());
            grilleModele = grille;
        } else {
            grilleModele = new Grille(lignes,colonnes,demanderDifficulte());
        }
        
        grilleModele.addObserver(this);
        
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new GsFiltreExtension("dem", "Demineur Gs File"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        construireMenu();
        construireLbl();
        construireBtn();
        construireContentPane();
        construire();
        
        if (grille != null) actualiser();
        actualiseStatutIcon();
        
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
        
    }
    
    private void construire() {
        setTitle("Gs Démineur");
        try{
            setIconImage(ImageIO.read(getClass().getResource("/Ressources/Bomb-Cool-icon.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menu);
        setContentPane(panePrincipal);
    }
    
     private void construireContentPane() {
         panePrincipal = new JPanel(new BorderLayout());
         JPanel paneNord = new JPanel(new FlowLayout(FlowLayout.CENTER));
         JPanel paneNordLblLeft = new JPanel(new FlowLayout(FlowLayout.CENTER));
         paneNordLblLeft.setPreferredSize(new Dimension(250, 30));
         JPanel paneNordLblRight = new JPanel(new FlowLayout(FlowLayout.CENTER));
         //paneNordLblRight.setPreferredSize(new Dimension(250, 30));
         JPanel paneCentre = new JPanel(new GridLayout(lignes,colonnes));
         JPanel paneSud = new JPanel(new FlowLayout(FlowLayout.LEFT));
         
         //Label left
         paneNordLblLeft.add(lblMine);
         paneNordLblLeft.add(lblNbMine);
         paneNordLblLeft.add(lblNbCase);
         paneNordLblLeft.add(lblCase);
         paneNord.add(paneNordLblLeft);
         
         //Boutton Status
         paneNord.add(btnStatus);
         
         //Label right
         paneNordLblRight.add(lblFlagRestant);
         paneNordLblRight.add(lblNbFlagRestant);
         paneNordLblRight.add(lblFlag);
         paneNord.add(paneNordLblRight);
         
         //Label status
         paneSud.add(lblStatus);
  
         //Boutton cases
          for (int i = 0; i < lignes; i++){
             for (int y = 0; y < colonnes; y++){
                 paneCentre.add(btns[i][y]);
             }
         }
          
         panePrincipal.add(paneNord,BorderLayout.NORTH);
         panePrincipal.add(paneCentre,BorderLayout.CENTER);
         panePrincipal.add(paneSud,BorderLayout.SOUTH);
         
     }
     
     private void construireBtn(){
         btnStatus = new GsButton();
         btnStatus.setEnabled(false);
         btnStatus.setBorderPainted(false);
         btnStatus.setPreferredSize(new Dimension(64, 64));
         btnStatus.setMaximumSize(btnStatus.getPreferredSize());
         btnStatus.addActionListener(this);
         
         btns = new GsButton[lignes][colonnes];
         for (int i = 0; i < lignes; i++){
             for (int y = 0; y < colonnes; y++){
                 btns[i][y] = new GsButton();
                 btns[i][y].setPreferredSize(new Dimension(39,39));
                 btns[i][y].addActionListener(this);
                 btns[i][y].addMouseListener(this);
                 grilleModele.getCase(i, y).addObserver(btns[i][y]);
             }
         }
     }
     
     private void construireMenu(){
         menu = new JMenuBar();
         fichier = new JMenu("Fichier");
         partie = new JMenu("Partie");
         aide = new JMenu("?");
         
         sauvegarder = new JMenuItem("Sauvegarder");
         //sauvegarder.setEnabled(false);
         charger = new JMenuItem("Charger");
         //charger.setEnabled(false);
         propriete = new JMenuItem("Propriétés");
         quitter = new JMenuItem("Quitter");
         
         jouer = new JMenuItem("Continuer");
         jouer.setEnabled(false);
         MIpause = new JMenuItem("Pause");
         nouveau = new JMenuItem("Nouvelle partie");
         recommencer = new JMenuItem("Recommencer la partie");
         decouvrirTous = new JMenuItem("Découvrir tous");
         recouvrirTous = new JMenuItem("Recouvrir tous");
         recouvrirTous.setEnabled(false);
         
         apropos = new JMenuItem("A propos");
         
         fichier.add(sauvegarder);
         fichier.add(charger);
         fichier.add(propriete);
         fichier.add(quitter);
         
         triche = new JMenu("Mode triche");
         triche.add(decouvrirTous);
         triche.add(recouvrirTous);
         
         partie.add(jouer);
         partie.add(MIpause);
         partie.add(nouveau);
         partie.add(recommencer);
         partie.add(triche);
         
         aide.add(apropos);
         
         sauvegarder.addActionListener(this);
         charger.addActionListener(this);
         propriete.addActionListener(this);
         quitter.addActionListener(this);
         
         jouer.addActionListener(this);
         MIpause.addActionListener(this);
         nouveau.addActionListener(this);
         recommencer.addActionListener(this);
         decouvrirTous.addActionListener(this);
         recouvrirTous.addActionListener(this);
         
         apropos.addActionListener(this);
         
         menu.add(fichier);
         menu.add(partie);
         menu.add(aide);
     }
     
     private void construireLbl(){
        lblNbMine = new JLabel(String.valueOf(grilleModele.getNombreMines()));
        lblNbMine.setForeground(Color.red);
        lblNbMine.setFont(new Font("Bell Mt",Font.BOLD,20));
        lblMine = new JLabel("Mines : ");
        lblNbCase = new JLabel(" / "+String.valueOf(lignes*colonnes));
        lblNbCase.setFont(new Font("Bell Mt",Font.BOLD,20));
        lblCase= new JLabel("Cases");
        lblNbFlagRestant = new JLabel(String.valueOf(grilleModele.getNbrFlagRestant()));
        lblNbFlagRestant.setFont(new Font("Bell Mt",Font.BOLD,20));
        lblFlagRestant = new JLabel("Drapeau Restant : ");
        lblFlag = new JLabel(" drapeaux");
        lblStatus = new JLabel("En marche");
        lblStatus.setForeground(Color.GREEN);
        lblStatus.setFont(new Font("Bell Mt",Font.ITALIC,14));
     }
     
    
     
     private void actualiser(){
         //this.update(grilleModele, args);
         for (int i = 0; i < lignes; i++){
             for (int y = 0; y < colonnes; y++){
                 btns[i][y].update(grilleModele.getCase(i,y), args);
             }
         }
     }
     
     private void actualiseStatutIcon(){
         if (grilleModele.getDifficulte() == 1 )btnStatus.setImg("/Ressources/Smiley-icon.png");
         else if (grilleModele.getDifficulte() == 2) btnStatus.setImg("/Ressources/Smiley-grumpy-icon.png");
         else if (grilleModele.getDifficulte() == 3) btnStatus.setImg("/Ressources/Smiley-teards-icon.png");
     }
     
     // demanderDifficule
     private int demanderDifficulte(){
         String[] strDifficute = {"Facile", "Moyen", "Difficile"};
         JOptionPane opt = new JOptionPane();
         
         return opt.showOptionDialog(null,"Veuillez indiquer la difficultée !", "Gs Demineur : Initialisation.", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, strDifficute, strDifficute[1]) + 1;
     }
     
     /*private int demanderLigne(){
         JOptionPane opt = new JOptionPane();
         int value;
         String str = opt.showInputDialog(this, "Veillez indiquer le nombre de ligne !", "Gs Demineur : Initialisation.", JOptionPane.QUESTION_MESSAGE);
         try{
             value = Integer.parseInt(str);
         }catch(NumberFormatException n){
             JOptionPane err = new JOptionPane();
             err.showMessageDialog(this, "Erreur dans la saisie du nombre de ligne...\n La ligne sera initialisé avec la valeur 10.", "Gs Demineur : Erreur!!!", JOptionPane.ERROR_MESSAGE);
             value = 10;
         }
         return value;
     }
     
     private int demanderColonne(){
         JOptionPane opt = new JOptionPane();
         int value;
         String str = opt.showInputDialog(this, "Veillez indiquer le nombre de colonne !", "Gs Demineur : Initialisation.", JOptionPane.QUESTION_MESSAGE);
         try{
             value = Integer.parseInt(str);
         }catch(NumberFormatException n){
             JOptionPane err = new JOptionPane();
             err.showMessageDialog(this, "Erreur dans la saisie du nombre de colonne...\n La colonne sera initialisé avec la valeur 10.", "Gs Demineur : Erreur!!!", JOptionPane.ERROR_MESSAGE);
             value = 10;
         }
         return value;
     }*/
      
     private int dialogRejouer(String message){
         String[] str = {"Rejouer", "Quitter", "Fermer"};
         optionPane = new JOptionPane();

         return optionPane.showOptionDialog(this,message, "Gs Demineur", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, str, str[0]);
     }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Grille){
            String message = null;
            if( ((Grille)o).isGagne() ){
                btnStatus.setImg("/Ressources/victory-icon.png");
                btnStatus.repaint();
                sauvegarder.setEnabled(false);
                MIpause.setEnabled(false);
                triche.setEnabled(false);
                lblStatus.setText("Vous avez Gagné");
                lblStatus.setForeground(Color.GREEN);
                grilleModele.devoilerTous();
                message = "Bravo! Vous avez gagnez...!";
                
            }
            if( ((Grille)o).isPerdu() ){
                btnStatus.setImg("/Ressources/cry-icon.png");
                btnStatus.repaint();
                sauvegarder.setEnabled(false);
                MIpause.setEnabled(false);
                triche.setEnabled(false);
                grilleModele.devoilerTous();
                lblStatus.setText("Vous avez Perdu");
                lblStatus.setForeground(Color.RED);
                message = "Dommage! Vous avez perdu...!";
            }
            
            lblNbFlagRestant.setText(String.valueOf(grilleModele.getNbrFlagRestant()));
            
            if (((Grille)o).isGagne() || ((Grille)o).isPerdu()){
            int choix = dialogRejouer(message);
            if (choix == 0) {
                this.dispose();
                FenetreDemineur.main(args,null);
            }
            else if (choix == 1) System.exit(0);
            else optionPane.setVisible(false);
            }
            
        }
    }

    /**
     *
     * @return lignes(int)
     */
    public int getLignes() {
        return lignes;
    }

    /**
     *
     * @param lignes
     */
    public void setLignes(int lignes) {
        this.lignes = lignes;
    }

    /**
     *
     * @return
     */
    public int getColonnes() {
        return colonnes;
    }

    /**
     *
     * @param colonnes
     */
    public void setColonnes(int colonnes) {
        this.colonnes = colonnes;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == sauvegarder){
            //fileChooser.setSelectedFile(new File(".dem"));
            fileChooser.setAccessory(null);
            int returnVal = fileChooser.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try{
                    FileOutputStream fichier = new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath());
                    ObjectOutputStream oos = new ObjectOutputStream(fichier);
                    oos.writeObject(grilleModele);
                    oos.flush();
                    oos.close();
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        else if (source == charger){
            fileChooser.setAccessory(new GsPanelFileChooser(fileChooser));
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                try{
                    FileInputStream fichier = new FileInputStream(fileChooser.getSelectedFile().getAbsolutePath());
                    ObjectInputStream ois = new ObjectInputStream(fichier);
                    Grille grille = (Grille)ois.readObject();
                    this.dispose();
                    FenetreDemineur.main(args,grille);
                }catch(IOException | ClassNotFoundException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        else if (source == propriete){
            config.setVisible(true);
        }
        else if (source == quitter) System.exit(0);
        else if (source == nouveau){
            this.dispose();
            FenetreDemineur.main(args,null);
        }
        else if (source == recommencer){
            grilleModele.revoilerTous();
            grilleModele.recommencer();
            actualiseStatutIcon();
            sauvegarder.setEnabled(true);
            jouer.setEnabled(false);
            MIpause.setEnabled(true);
            triche.setEnabled(true);
            lblStatus.setText("En marche");
            lblStatus.setForeground(Color.GREEN);
        }
        else if (source == jouer){
            pause = false;
            jouer.setEnabled(false);
            MIpause.setEnabled(true);
            lblStatus.setText("En marche");
            lblStatus.setForeground(Color.GREEN);
        }
        else if (source == MIpause){
            pause = true;
            jouer.setEnabled(true);
            MIpause.setEnabled(false);
            lblStatus.setText("En pause");
            lblStatus.setForeground(Color.RED);
        }
        else if (source == decouvrirTous) {
            grilleModele.devoilerTous();
            sauvegarder.setEnabled(false);
            jouer.setEnabled(false);
            MIpause.setEnabled(false);
            recouvrirTous.setEnabled(true);
            decouvrirTous.setEnabled(false);
            pause = true;
            lblStatus.setText("En pause");
            lblStatus.setForeground(Color.RED);
        }
        else if (source == recouvrirTous) {
            grilleModele.revoilerTous();
            sauvegarder.setEnabled(true);
            jouer.setEnabled(true);
            MIpause.setEnabled(true);
            recouvrirTous.setEnabled(false);
            decouvrirTous.setEnabled(true);
            pause = false;
            lblStatus.setText("En marche");
            lblStatus.setForeground(Color.GREEN);
        }
        else if (source == apropos){
            String messageApropos = "Ce jeu est un projet de session.\n"
                    + "\tAuteur : Setruk Marc Roger\n"
                    +"\tCours : Java\n"
                    +"\tProfesseur : A. Toudef";
            JOptionPane opt = new JOptionPane();
            opt.showMessageDialog(this, messageApropos, "Gs Apropos", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (!pause){
            for (int i = 0; i < lignes; i++)
                for (int y = 0; y < colonnes; y++)
                    if (source == btns[i][y]){
                        grilleModele.decouvrirCase(i, y);
                    }
        }
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args){
        //new FenetreDemineur();
        FenetreDemineur.main(args, null);
    }
    
    
    /**
     *
     * @param args
     * @param grille
     */
    public static void main(String[] args, Grille grille){
        if (grille != null) new FenetreDemineur(grille);
        else new FenetreDemineur(null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isMetaDown() && !pause){
            for (int i = 0; i < lignes; i++)
                for (int y = 0; y < colonnes; y++)
                    if ((GsButton)e.getSource() == btns[i][y]){
                        if (!grilleModele.getCase(i,y).isFlag()) {
                            grilleModele.PlacerFlagCase(i, y);
                            if (grilleModele.getNbrFlagRestant() < 2) lblFlag.setText("drapeau");
                        }
                        else if (grilleModele.getCase(i,y).isFlag()) {
                            grilleModele.EnleverFlagCase(i, y);
                            if (grilleModele.getNbrFlagRestant() > 1) lblFlag.setText("drapeaux");
                        }
                    }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}