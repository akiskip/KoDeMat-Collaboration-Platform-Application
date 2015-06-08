/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.visu;

/**
 *
 * @author Kipouridis
 */

import com.jme3.asset.*;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.UrlLocator;
import com.jme3.asset.plugins.ZipLocator;

public class TestManyLocators {
    public static void main(String[] args){
        AssetManager am = new DesktopAssetManager();

        am.registerLocator("https://github.com/akiskip/KoDeMat-Collaboration-Platform-Application/tree/master/KoDeMat_Collaboration3D/assets/",
                           UrlLocator.class);


        am.registerLocator("http://jmonkeyengine.googlecode.com/files/wildhouse.zip",
                           HttpZipLocator.class);
        
        am.registerLocator("/", ClasspathLocator.class);
        
        // Try loading from Core-Data source package
        AssetInfo a = am.locateAsset(new AssetKey<Object>("Interface/Fonts/Default.fnt"));

        // Try loading from town scene zip file
        AssetInfo b = am.locateAsset(new ModelKey("casaamarela.jpg"));

        // Try loading from wildhouse online scene zip file
        AssetInfo c = am.locateAsset(new ModelKey("glasstile2.png"));

        // Try loading directly from HTTP
        AssetInfo d = am.locateAsset(new TextureKey("planet-2.jpg"));

        if (a == null)
            System.out.println("Failed to load from classpath");
        else
            System.out.println("Found classpath font: " + a.toString());

        if (c == null)
            System.out.println("Failed to load from wildhouse.zip on googlecode.com");
        else
            System.out.println("Found online zip image: " + c.toString());

        if (d == null)
            System.out.println("Failed to load from HTTP");
        else
            System.out.println("Found HTTP showcase image: " + d.toString());
    }
}