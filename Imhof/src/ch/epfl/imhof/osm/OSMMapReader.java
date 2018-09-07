package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import ch.epfl.imhof.PointGeo;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import static ch.epfl.imhof.osm.OSMRelation.Member;
import static java.lang.Long.parseLong;
import static java.lang.Double.parseDouble;

/**
 * Permet de construire une carte OpenStreetMap à partir de données stockées dans un fichier au format OSM.
 * Cette classe n'est pas instanciable
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class OSMMapReader {

    private OSMMapReader(){}

    /**
     * Lit la carte OSM contenue dans le fichier de nom donné, en le décompressant avec gzip si nécessaire (et indiqué)
     *
     * @param fileName
     *      Le nom du fichier à lire
     * @param unGZip
     *      Si vrai, le fichier sera décompressé avec gzip
     *
     * @return
     *      Une carte OSMMap construite grâce au fichier passé en paramètre
     *
     * @throws SAXException
     *      S'il y a erreur dans le format du fichier XML contenant la carte
     * @throws java.io.IOException
     *      En cas d'autre erreur d'entrée/sortie, p.ex. si le fichier n'existe pas
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws IOException, SAXException {
        final OSMMap.Builder map = new OSMMap.Builder();

        try (InputStream i =
                     unGZip ? new GZIPInputStream( new FileInputStream(fileName)) : new FileInputStream(fileName)) {
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(new DefaultHandler() {

                private OSMEntity.Builder currentBuilder;
                private OSMNode.Builder nodeBuilder;
                private OSMWay.Builder wayBuilder;
                private OSMRelation.Builder relationBuilder;

                private boolean skipThis = false;

                @Override
                public void startElement(String uri, String lName, String qName, Attributes atts) throws SAXException {
                    if(skipThis){
                        return;
                    }
                    switch (qName){
                        case "node"     :
                            nodeBuilder = newNodeBuilder(atts.getValue("lon"), atts.getValue("lat"), atts.getValue("id"));
                            currentBuilder = nodeBuilder;
                            break;
                        case "way"      :
                            wayBuilder = new OSMWay.Builder( parseLong(atts.getValue("id")) );
                            currentBuilder = wayBuilder;
                            break;
                        case "nd"       :
                            processNd(atts.getValue("ref"));
                            break;
                        case "relation" :
                            relationBuilder = new OSMRelation.Builder( parseLong(atts.getValue("id") ) );
                            currentBuilder = relationBuilder;
                            break;
                        case "member"   :
                            processMember(atts.getValue("type"), atts.getValue("ref"), atts.getValue("role") );
                            break;
                        case "tag"      :
                            currentBuilder.setAttribute(atts.getValue("k"), atts.getValue("v"));
                            break;
                    }

                }

                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName){
                        case "node"     :
                            map.addNode(nodeBuilder.build());
                            break;
                        case "way"      :
                            if(!wayBuilder.isIncomplete() ){
                                map.addWay(wayBuilder.build() );
                            }
                            skipThis = false;
                            break;
                        case "relation" :
                            if(!relationBuilder.isIncomplete() ){
                                map.addRelation(relationBuilder.build() );
                            }
                            skipThis = false;
                            break;
                    }
                }

                private void processMember(String type, String ref, String role){
                    final Member.Type memberType = Member.Type.valueOf(type.toUpperCase() );
                    final long id = parseLong(ref);
                    OSMEntity entity;

                    switch (memberType){
                        case NODE       :
                            entity = map.nodeForId(id);
                            break;
                        case RELATION   :
                            entity = map.relationForId(id);
                            break;
                        case WAY        :
                            entity = map.wayForId(id);
                            break;
                        default         :
                            entity = null;
                    }

                    if(entity == null){
                        relationBuilder.setIncomplete();
                        skipThis = true;
                    }
                    else{
                        relationBuilder.addMember(memberType, role, entity);
                    }
                }

                private void processNd(String id){
                    OSMNode nd = map.nodeForId( parseLong(id) );

                    if(nd == null){
                        wayBuilder.setIncomplete();
                        skipThis = true;
                    }
                    else{
                        wayBuilder.addNode(nd);
                    }
                }

                private OSMNode.Builder newNodeBuilder(String lng, String lat, String id){
                    double lngRad = Math.toRadians( parseDouble(lng) ), latRad = Math.toRadians( parseDouble(lat) );
                    PointGeo p = new PointGeo(lngRad, latRad);

                    return new OSMNode.Builder(parseLong(id), p);
                }
            });
            r.parse(new InputSource(i));
        }

        return map.build();
    }

}
