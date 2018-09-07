package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

/**
 * Un MNT stocké dans un fichier au format HGT
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {

    private static final double DELTA = 10e-8;
    private static final double ZONE_COVERED = Math.toRadians(1);

    private final PointGeo southWest;
    private final double resolution;
    private final int pointsOnALine;
    private final FileInputStream stream;
    private ShortBuffer buffer;

    /**
     * Prend un unique argument, de type File, qui désigne le fichier HGT contenant le modèle numérique du terrain.
     *
     * @param hgtFile
     *      Fichier binaire contenant les altitudes d'un certain nombre de points séparés les uns des autres par une
     *      distance angulaire constante et identique en longitude et en latitude.
     *
     * @throws IllegalArgumentException
     *      <li>Si le fichier n'est pas au format HGT</li>
     *      <li>Si la taille du fichier divisée par 2 n'est pas une racine carrée entière</li>
     *      <li>Si la latitude n'est pas au format N ou S</li>
     *      <li>Si la longitude n'est pas au format E ou W</li>
     */
    public HGTDigitalElevationModel(File hgtFile) throws IOException {
        final String name = hgtFile.getName();
        if(!name.substring( name.length()-4 ).equals(".hgt") ){
            throw new IllegalArgumentException("Format de fichier incorrect, fichier .hgt requis. Extension reçue: " +
                    name.substring( name.length()-4 ));
        }

        final String
                latSign = name.substring(0,1),
                lat = name.substring(1,3),
                lngSign = name.substring(3,4),
                lng = name.substring(4,7);
        final long nbPoints = hgtFile.length()/2;
        final double pointsOnALine = Math.sqrt(nbPoints);
        this.pointsOnALine = (int)pointsOnALine;

        if( !(Math.abs(pointsOnALine - this.pointsOnALine) <= DELTA) ){
            throw new IllegalArgumentException("Taille du fichier incorrect: un fichier .hgt" +
                    " représente toujours une zone carrée.");
        }

        southWest = buildSouthWest(lng, lngSign, lat, latSign);
        resolution = ZONE_COVERED / (pointsOnALine - 1.);
        try(FileInputStream stream = new FileInputStream(hgtFile)){
            this.stream = stream;
            this.buffer = stream
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, nbPoints * 2)
                        .asShortBuffer();
        }
    }

    private PointGeo buildSouthWest(String lng, String lngSign, String lat, String latSign){
        final double latN, lngN;
        final int latSignN, lngSignN;

        switch (latSign){
            case "N" :
                latSignN = 1;
                break;
            case "S" :
                latSignN = -1;
                break;
            default :
                throw new IllegalArgumentException("La première lettre du nom du fichier doit être N ou S");
        }
        try {
            latN = Integer.parseInt(lat);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Valeur incorrecte pour la latitude dans le nom du fichier");
        }

        switch (lngSign){
            case "E" :
                lngSignN = 1;
                break;
            case "W" :
                lngSignN = -1;
                break;
            default :
                throw new IllegalArgumentException("La quatrième lettre du nom du fichier doit être E ou W");
        }
        try {
            lngN = Integer.parseInt(lng);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Valeur incorrecte pour la longitude dans le nom du fichier");
        }

        return new PointGeo(Math.toRadians(lngN * lngSignN), Math.toRadians(latN * latSignN));
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public Vector3 normalAt(PointGeo p){
        final double diffLng = p.longitude() - southWest.longitude(), diffLat = p.latitude() - southWest.latitude();

        if( !( (0. <= diffLng && diffLng <= ZONE_COVERED) && (0. <= diffLat && diffLat <= ZONE_COVERED) ) ){
            throw new IllegalArgumentException("Le point fourni est en dehors des limites du fichier HGT.\n" +
                    "Différence de longitude: " + Math.toDegrees(diffLng) + "°, différence de latitude: "
                    + Math.toDegrees(diffLat) + "°");
        }

        final int i = (int)Math.floor(diffLng / resolution), j = (int)Math.floor(diffLat / resolution);
        final short
                zij = getFromBuffer(i,j), zi1j1 = getFromBuffer(i+1, j+1),
                zij1 = getFromBuffer(i, j+1), zi1j = getFromBuffer(i+1, j);
        final double
                s = Earth.RADIUS * resolution,
                za = zi1j - zij, zb = zij1 - zij, zc = zij1 - zi1j1, zd = zi1j - zi1j1; //i1 étant i+1, etc...
        final Vector3
                a = new Vector3(s, 0, za), b = new Vector3(0, s, zb),
                c = new Vector3(-s, 0, zc), d = new Vector3(0, -s, zd),
                n1 = a.crossProduct(b), n2 = c.crossProduct(d);

        return n1.add(n2).multiplyWithScalar(0.5);
	}

    /**
     * Permet d'obtenir une valeur à partir du buffer en le considérant comme un tableau bidimensionnel
     */
    private short getFromBuffer(int i, int j){
        return buffer.get( i + pointsOnALine*(pointsOnALine-j-1) );
    }

    /**
     * Ferme le MNT et libère les ressources utilisées pour sa lecture
     *
     * @throws IOException
     *      En cas d'erreur IO lors de la fermeture du fichier système
     */
    @Override
    public void close() throws IOException {
        stream.close();
        buffer = null;
    }
}
