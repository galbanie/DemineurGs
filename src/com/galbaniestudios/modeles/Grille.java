/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.modeles;

import java.io.Serializable;
import java.util.Observable;



/**
 *
 * @author galbanie
 */
public class Grille extends Observable implements Serializable{
    public static final int FACILE = 1;
    public static final int MOYEN = 2;
    public static final int DIFFICILE = 3;
    
    private int lignes ;
    private int colonnes;
    private int nbrMines;
    private int nbrFlagRestant;
    private int difficulte;
    
    private Case[][] cases;
    
    private boolean perdu;
    private boolean gagne;
    
    public  Grille(){
        this(10,10,1);
    }
    
    public  Grille(int _lignes, int _colonnes,int _difficulte){
        lignes = _lignes;
        colonnes = _colonnes;
        
        //Sécurisation de la difficulté (La difficulté est comprise obligatoirement entre 1 et 3 ouvert)
        if (_difficulte > 3) difficulte = 3;
        else if (_difficulte < 1) difficulte = 1;
        else difficulte = _difficulte;
        
        //algorithme pour le nombre de mines par la difficulté
        nbrMines = (int)((Math.sqrt(lignes * colonnes) + ((lignes * colonnes)/10)) * difficulte);
        
        initialiserFlag();
        
        cases = new Case[lignes][colonnes];
        
        perdu = false;
        gagne = false;
        
        initialisation();
        
        genererMine();
        
        genererNombre();
        
    }
    
    private void initialisation(){
        for (int i = 0; i < lignes; i++)
            for (int y = 0; y < colonnes; y++) {
                cases[i][y] = new Case();
                cases[i][y].setDecouvert(false);
                cases[i][y].setMine(false);
            }
    }
    
    private void initialiserFlag(){
        nbrFlagRestant = nbrMines;
        setChanged();
        notifyObservers(this.nbrFlagRestant);
    }
    
    private void genererMine(){
        int temp = nbrMines;
        while(temp > 0){
            int x = (int)(Math.floor(Math.random() * lignes));
            int y= (int)(Math.floor(Math.random() * colonnes));
            if (!cases[x][y].isMine()){
                cases[x][y].setMine(true);
                temp--;
            }
        }
    }
    
    private void genererNombre(){
        for(int i = 0; i < lignes;i++){
            for(int y = 0;y < colonnes;y++){
                if (cases[i][y].isMine()){
                    cases[i][y].setNombre(0);
                    continue;
                }
                int temp = 0;
                boolean gauche = (i - 1) >= 0;
                boolean droite = (i + 1) < lignes;
                boolean haut = (y - 1) >= 0;
                boolean bas = (y + 1) < colonnes;
                
                if (haut){
                    if (gauche) if (cases[i - 1][y - 1].isMine()) temp++;//la case de haut à gauche
                    if (cases[i][y - 1].isMine()) temp++; //la case de  haut au millieu
                    if (droite) if (cases[i + 1][y - 1].isMine()) temp++;//la case de haut à droite
                }
                if (gauche)
                    if (cases[i - 1][y].isMine()) temp++;
                if (droite)
                    if (cases[i + 1][y].isMine()) temp++;
                if (bas){
                    if (gauche) if (cases[i - 1][y + 1].isMine()) temp++;//la case du bas à gauche
                    if (cases[i][y + 1].isMine()) temp++;//la case du bas au millieu
                    if (droite) if (cases[i + 1][y + 1].isMine()) temp++;//la case du bas à droite
                }
                
                cases[i][y].setNombre(temp);
            }
        } 
    }

    public boolean isPerdu() {
        return perdu;
    }

    public boolean isGagne() {
        return gagne;
    }

    private void setPerdu(boolean perdu) {
        this.perdu = perdu;
        setChanged();
        notifyObservers(this.perdu);
    }

    private void setGagne(boolean gagne) {
        this.gagne = gagne;
        setChanged();
        notifyObservers(this.gagne);
    }
    
    public void recommencer(){
        setPerdu(false);
        setGagne(false);
        initialiserFlag();
    }
    
    public Case getCase(int ligne, int colonne){
        if ((ligne >= 0)&&(ligne < lignes)&&(colonne >= 0)&&(colonne < colonnes)){
            return cases[ligne][colonne];
        }
        else
            return null;
    }

    public int getNombreLignes() {
        return lignes;
    }

    public int getNombreColonnes() {
        return colonnes;
    }

    public int getNombreMines() {
        return nbrMines;
    }

    public int getNbrFlagRestant() {
        return nbrFlagRestant;
    }

    public int getDifficulte() {
        return difficulte;
    }
    
    public void PlacerFlagCase(int ligne, int colonne){
        if (!cases[ligne][colonne].isDecouvert() && !cases[ligne][colonne].isFlag() && nbrFlagRestant > 0){
            cases[ligne][colonne].setFlag(true);
            nbrFlagRestant--;
            setChanged();
            notifyObservers(this.nbrFlagRestant);
        }    
    }
    public void EnleverFlagCase(int ligne, int colonne){
        if (cases[ligne][colonne].isFlag() && nbrFlagRestant < nbrMines){
            cases[ligne][colonne].setFlag(false);
            nbrFlagRestant++;
            setChanged();
            notifyObservers(this.nbrFlagRestant);
        }
    }
    
    public void decouvrirCase(int ligne, int colonne){
        if ((ligne >= 0)&&(ligne < lignes)&&(colonne >= 0)&&(colonne < colonnes)){
            cases[ligne][colonne].setDecouvert(true);
            if (cases[ligne][colonne].isMine()){
                setPerdu(true);
            }
            else{
                if (cases[ligne][colonne].isFlag())EnleverFlagCase(ligne,colonne);
                verifierGagnant();
                //verifierGagnantFlag();
                if(!gagne) decouvrirAlentoure(ligne,colonne);
            }
        }
    }
    
     private void decouvrirAlentoure(int ligne, int colonne){
        if (!cases[ligne][colonne].isDejaVisite() && !this.verifierAlentoure(ligne,colonne)){
            cases[ligne][colonne].setDejaVisite(true);
            boolean gauche = (ligne - 1) >= 0;
            boolean droite = (ligne + 1) < lignes;
            boolean haut = (colonne - 1) >= 0;
            boolean bas = (colonne + 1) < colonnes;        
            if (haut){
                if (gauche) if (!cases[ligne - 1][colonne - 1].isMine()){
                    cases[ligne - 1][colonne - 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne - 1, colonne -1);
                    if (cases[ligne - 1][colonne - 1].isFlag())EnleverFlagCase(ligne - 1,colonne - 1);
                }//la case de haut à gauche
                if (!cases[ligne][colonne - 1].isMine()){
                    cases[ligne][colonne - 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne, colonne -1);
                    if (cases[ligne][colonne - 1].isFlag())EnleverFlagCase(ligne,colonne - 1);
                } //la case de  haut au millieu
                if (droite) if (!cases[ligne + 1][colonne - 1].isMine()){
                    cases[ligne + 1][colonne - 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne + 1, colonne -1);
                    if (cases[ligne + 1][colonne - 1].isFlag())EnleverFlagCase(ligne + 1,colonne - 1);
                }//la case de haut à droite
            }
            if (gauche)
                if (!cases[ligne - 1][colonne].isMine()){
                    cases[ligne - 1][colonne].setDecouvert(true);
                    this.decouvrirAlentoure(ligne - 1, colonne);
                    if (cases[ligne - 1][colonne].isFlag())EnleverFlagCase(ligne - 1,colonne);
                }
            if (droite)
                if (!cases[ligne + 1][colonne].isMine()){
                    cases[ligne + 1][colonne].setDecouvert(true);
                    this.decouvrirAlentoure(ligne + 1, colonne);
                    if (cases[ligne + 1][colonne].isFlag())EnleverFlagCase(ligne + 1,colonne);
                }
            if (bas){
                if (gauche) if (!cases[ligne - 1][colonne + 1].isMine()){
                    cases[ligne - 1][colonne + 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne - 1, colonne + 1);
                    if (cases[ligne - 1][colonne + 1].isFlag())EnleverFlagCase(ligne - 1,colonne + 1);
                }//la case du bas à gauche
                if (!cases[ligne][colonne + 1].isMine()){
                    cases[ligne][colonne + 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne, colonne + 1);
                    if (cases[ligne][colonne + 1].isFlag())EnleverFlagCase(ligne,colonne + 1);
                }//la case du bas au millieu
                if (droite) if (!cases[ligne + 1][colonne + 1].isMine()){
                    cases[ligne + 1][colonne + 1].setDecouvert(true);
                    this.decouvrirAlentoure(ligne + 1, colonne + 1);
                    if (cases[ligne + 1][colonne + 1].isFlag())EnleverFlagCase(ligne + 1,colonne + 1);
                }//la case du bas à droite
            }
        }
    }
    
    private boolean verifierAlentoure(int ligne, int colonne){
        boolean gauche = (ligne - 1) >= 0;
        boolean droite = (ligne + 1) < lignes;
        boolean haut = (colonne - 1) >= 0;
        boolean bas = (colonne + 1) < colonnes;
        boolean temp = false;         
        if (haut){
            if (gauche) if (cases[ligne - 1][colonne - 1].isMine()) temp = true;//la case de haut à gauche
            if (cases[ligne][colonne - 1].isMine()) temp = true; //la case de  haut au millieu
            if (droite) if (cases[ligne + 1][colonne - 1].isMine()) temp = true;//la case de haut à droite
        }
        if (gauche)
            if (cases[ligne - 1][colonne].isMine()) temp = true;
        if (droite)
            if (cases[ligne + 1][colonne].isMine()) temp = true;
        if (bas){
            if (gauche) if (cases[ligne - 1][colonne + 1].isMine()) temp = true;//la case du bas à gauche
            if (cases[ligne][colonne + 1].isMine()) temp = true;//la case du bas au millieu
            if (droite) if (cases[ligne + 1][colonne + 1].isMine()) temp = true;//la case du bas à droite
        }
        return temp;
    }
    
    private void verifierGagnant(){
        for(int i = 0;i < lignes;i++){
            for(int y = 0;y < colonnes;y++){
                if (!cases[i][y].isDecouvert()){
                    if (cases[i][y].isMine()) continue;
                    else return;
                }
            }
        }
        setGagne(true);
    }
    
//    private void verifierGagnantFlag(){
//        for(int i = 0;i < lignes;i++){
//            for(int y = 0;y < colonnes;y++){
//                if (!cases[i][y].isDecouvert()){
//                    if (cases[i][y].isMine() && cases[i][y].isFlag()) continue;
//                    else return;
//                }
//            }
//        }
//        setGagne(true);
//    }
    
    public void devoilerTous(){
        for (int i = 0; i < lignes; i++)
            for (int y = 0; y < colonnes; y++) {
                cases[i][y].setDecouvert(true);
                cases[i][y].setFlag(false);
            }
    } 
    
    public void revoilerTous(){
        for (int i = 0; i < lignes; i++)
            for (int y = 0; y < colonnes; y++) {
                cases[i][y].setDecouvert(false);
                cases[i][y].setDejaVisite(false);
            }
    }
    
    public int compterFlag(){
        int nombreFlag = 0;
        for (int i = 0; i < lignes; i++)
            for (int y = 0; y < colonnes; y++) {
                if (cases[i][y].isFlag()) nombreFlag++;
            }
        return nombreFlag;
    }
    
    public String solution(){
        StringBuilder temp = new StringBuilder();
        for (int i = -1;i < lignes;i++){
            for(int y = -1;y < colonnes;y++){
                if ((i == -1)&& (y < colonnes)) temp.append("  ").append(y+2);
                else {
                    if (y == -1) temp.append(i+1);
                    else if (y < colonnes) temp.append(cases[i][y].triche());
                }
            }
            temp.append("\n");
        }
        return temp.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder temp = new StringBuilder();
        for (int i = -1;i < lignes;i++){
            for(int y = -1;y < colonnes;y++){
                if ((i == -1)&& (y < colonnes)) temp.append("  ").append(y+2);
                else {
                    if (y == -1) temp.append(i+1);
                    else if (y < colonnes) temp.append(cases[i][y].toString());
                }
            }
            temp.append("\n");
        }
        return temp.toString();
    } 
    
    
    
}
