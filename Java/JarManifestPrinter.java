package fr.kraiss.scratch.gist;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Pierrick Rassat
 * @see https://github.com/kraiss 
 */
public class JarManifestPrinter {

    public static void printJarManifestAttributes() throws IOException {
        // Get jarfile url
        String jarUrl = JarManifestPrinter.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        // Get the manifest from the jarfile
        File file = new File(jarUrl);
        JarFile jar = new JarFile(file);
        Manifest manifest = jar.getManifest();
        Attributes attributes = manifest.getMainAttributes();

        // Print manifest 'standard' attributes
        System.out.println("Manifest Attributes:");
        System.out.println("Vendor     : " + attributes.getValue("Implementation-Vendor"));
        System.out.println("Title      : " + attributes.getValue("Implementation-Title"));
        System.out.println("Version    : " + attributes.getValue("Implementation-Version"));
        System.out.println("Build date : " + attributes.getValue("Built-Date"));
        System.out.println("Build by   : " + attributes.getValue("Built-By"));
    }
}
