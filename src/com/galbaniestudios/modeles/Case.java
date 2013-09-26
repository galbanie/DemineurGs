/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.modeles;

import java.io.Serializable;
import java.util.Observable;


/**
 *
 * @author Setruk Marc-Roger
 */
public class Case extends Observable implements Serializable{
    private int nombre;
    private boolean mine;
    private boolean decouvert;
    private boolean dejaVisite;
    private boolean flag;
    
    
    public Case(){
        decouvert = false;
        dejaVisite = false;
        flag = false;
        
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int _nombre) {
        nombre = _nombre;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean _mine) {
        mine = _mine;
    }

    public boolean isDecouvert() {
        return decouvert;
    }

    public void setDecouvert(boolean _decouvert) {
        decouvert = _decouvert;
        setChanged();
        notifyObservers(this.decouvert);
    }

    public boolean isDejaVisite() {
        return dejaVisite;
    }

    public void setDejaVisite(boolean _dejaVisite) {
        dejaVisite = _dejaVisite;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
        setChanged();
        notifyObservers(this.flag);
    }
    
    public String triche(){
        return ((mine)?"[*]" :(nombre == 0)?"[ ]":"["+nombre+"]" );
    }
    
    @Override
    public String toString(){
        return (decouvert)?((mine)?"[*]" :(nombre == 0)?"[ ]":"["+nombre+"]" ):"[#]";
    }
    
    
}
