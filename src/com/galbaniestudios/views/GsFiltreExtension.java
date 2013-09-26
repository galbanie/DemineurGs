/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.galbaniestudios.views;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author galbanie
 */
public class GsFiltreExtension extends FileFilter {
    
    String extension;
    String description;

    public GsFiltreExtension(String extension, String description) {
        if (extension.indexOf('.') == -1) extension = '.'+ extension;
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if(f.getName().endsWith(extension))
            return true;
        else if(f.isDirectory())
            return true;
        else return false;
    }

    @Override
    public String getDescription() {
        return this.description + "(*" + extension + ")";
    }
    
}
