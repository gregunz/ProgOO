package ch.epfl.imhof.painting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

public final class SwissFlag {
    public static final int SIZE = 200;

    public static void main(String[] args)
        throws IOException {
        BufferedImage image =
            new BufferedImage(SIZE, SIZE,
                              BufferedImage.TYPE_INT_ARGB);
        Graphics2D ctx = image.createGraphics();

        // Active l'anticrénelage.
        ctx.setRenderingHint(KEY_ANTIALIASING,
                             VALUE_ANTIALIAS_ON);

        // Remplit le fond en rouge.
        ctx.setColor(Color.RED);
        ctx.fillRect(0, 0, SIZE, SIZE);

        // Change le repère pour que chaque côté soit
        // de longueur unitaire…
        ctx.scale(SIZE, SIZE);
        // …et l'origine au centre.
        ctx.translate(0.5, 0.5);

        // Dessine deux rectangles tournés de 90° pour
        // obtenir la croix.
        ctx.setColor(Color.WHITE);
        for (int i = 0; i < 2; ++i) {
            Path2D rect = new Path2D.Double();
            rect.moveTo(-0.1, -0.3);
            rect.lineTo(0.1, -0.3);
            rect.lineTo(0.1, 0.3);
            rect.lineTo(-0.1, 0.3);
            rect.closePath();
            ctx.fill(rect);

            ctx.rotate(Math.PI / 2d);
        }

        // Ecrit le résultat au format PNG dans un fichier.
        ImageIO.write(image, "png", new File("images\\ch.png"));
    }
}