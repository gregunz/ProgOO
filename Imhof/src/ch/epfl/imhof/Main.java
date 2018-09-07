package ch.epfl.imhof;

import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Projet Imhof - classe principale Main. Le programme accepte les paramètres suivants, dans l'ordre:
 * <li>1- le nom (chemin) d'un fichier OSM compressé avec gzip contenant les données de la carte à dessiner,</li>
 * <li>2- le nom (chemin) d'un fichier HGT couvrant la totalité de la zone de la carte à dessiner,
 * zone tampon incluse,</li>
 * <li>3- la longitude du point bas-gauche de la carte, en degrés,</li>
 * <li>4- la latitude du point bas-gauche de la carte, en degrés,</li>
 * <li>5- la longitude du point haut-droite de la carte, en degrés,</li>
 * <li>6- la latitude du point haut-droite de la carte, en degrés,</li>
 * <li>7- la résolution de l'image à dessiner, en points par pouce (dpi),</li>
 * <li>8- le nom (chemin) du fichier PNG à générer.</li>
 * Avec ces arguments, le programme produit une carte de la zone désirée à l'échelle 1:25000.
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Main {

    private final static Painter painter = SwissPainter.painter();
    private final static Projection projection = new CH1903Projection();
    private final static Vector3 light = new Vector3(-1, 1 , 1);

    private final static double INCH_TO_METER = 0.0254d;
    private final static double BLUR_RADIUS_IN_METER = 0.0017d;
    private final static double MAP_SCALE = 1/25000d;

    public static void main(String[] args){
        final PointGeo bottomLeft = new PointGeo( toRadians(args[2]), toRadians(args[3]) ),
                topRight = new PointGeo ( toRadians(args[4]), toRadians(args[5]) );
        final Map map =  createMap(args[0]);
        final Point bl = projection.project(bottomLeft),
                tr = projection.project(topRight);
        final int dpi = Integer.parseInt(args[6]);
        final double dpm = dpi/INCH_TO_METER,
                     radius = (float) (dpm * BLUR_RADIUS_IN_METER);
        final int height = (int) Math.round(
                dpm * (topRight.latitude() - bottomLeft.latitude()) * Earth.RADIUS * MAP_SCALE),
                width = (int) Math.round(height * (tr.x() - bl.x()) / (tr.y() - bl.y()));

        //Dessin de la toile
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi, Color.WHITE);
        //Dessin de la carte sur la toile
        painter.drawMap(map, canvas);
        BufferedImage imgMap = canvas.image(), imgRelief;

        //Dessin du relief
        ReliefShader rs = new ReliefShader(projection, args[1], light);
        try{
            imgRelief = rs.shadedRelief(bl, tr, width, height, radius);
        }
        catch (IOException e){
            System.out.println("Erreur IO lors de la lecture du fichier HGT, le programme va s'arrêter. Erreur: ");
            e.printStackTrace();
            return;
        }

        //Cartes multipliées entre elles
        final BufferedImage img = multiplyImage(imgMap, imgRelief);
        //Ecriture de la carte à l'endroit demandé
        printImage(img, new File(args[7]));
    }

    private static double toRadians (String s){
        return Math.toRadians(Double.parseDouble(s));
    }

    private static Map createMap(String osmPath){
        try {
            OSMMap m = OSMMapReader.readOSMFile(osmPath, true);
            return new OSMToGeoTransformer(projection).transform(m);
        }
        catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage multiplyImage(BufferedImage img1, BufferedImage img2){
        for(int x=0; x < img1.getWidth(); ++x){
            for(int y=0; y < img1.getHeight(); ++y) {
                Color c = Color.rgb(img1.getRGB(x,y)).multiplyWith(Color.rgb(img2.getRGB(x,y)));
                img1.setRGB(x, y, c.toAWTColor().getRGB());
            }
        }
        return img1;
    }

    private static void printImage(BufferedImage img, File f){
        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

