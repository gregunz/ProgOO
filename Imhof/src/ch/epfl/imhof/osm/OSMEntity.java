package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

import static java.util.Objects.requireNonNull;

/**
 * Classe mère abstraite de toutes les classes représentant les entités OSM
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */

public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * Construit une entité OSM dotée de l'identifiant unique et des attributs donnés
     * 
     * @param id
     * 		L'identifiant unique
     * @param attributes
     * 		Les attributs
     */
    public OSMEntity(long id, Attributes attributes){
        this.id = id;
        this.attributes = requireNonNull(attributes);
    }

    /**
     * Retourne l'identifiant unique de l'entité
     *
     * @return
     * 		L'identifiant unique
     */
    public long id(){
        return id;
    }

    /**
     * Retourne les attributs de l'entité
     *
     * @return
     * 		Les attributs
     */
    public Attributes attributes(){
        return attributes;
    }

    /**
     * Retourne vrai si l'entité possède la clé passée en argument dans ses attributs
     *
     * @param key
     * 		La clé dont on veut vérifier la présence
     * @return
     * 		<p>true si la clé est contenue dans l'ensemble d'attributs de l'entité</p>
     *      <p>false autrement</p>
     */
    public boolean hasAttribute(String key){
        return attributes.contains(key);
    }

    /**
     * Retourne la valeur associée à la clé passée en argument ou null si elle n'existe pas
     *
     * @param key
     * 		La clé
     * @return
     * 		La valeur associé à la clé
     */
    public String attributeValue(String key){
        return attributes.get(key);
    }

    /**
	 *	Bâtisseur permettant la création d'une entité OSM en plusieurs étapes
     */
    public static abstract class Builder{

        protected final long id;
        protected final Attributes.Builder attributesBuilder;
        private boolean incomplete;

        /**
         * Construit un bâtisseur pour une entité OSM identifiée par l'entier donné
         *
         * @param id
         * 		L'identifiant
         */
        public Builder(long id){
            this.id = id;
            attributesBuilder = new Attributes.Builder();
            incomplete = false;
        }

        /**
         * Ajoute l'association (clé,valeur) donnée à l'ensemble d'attributs de l'entité en construction.
         * Si une valeur était déjà liée à la clé, elle est remplacée par celle donnée
         *
         * @param key
         * 		clé
         * @param value
         * 		valeur
         */
        public void setAttribute(String key, String value){
            attributesBuilder.put(key, value);
        }

        /**
         * Retourne vrai si l'entité en cours de construction est incomplète, c-à-d si la méthode setIncomplete a été
         * appelée au moins une fois sur ce bâtisseur depuis sa création.
         *
         * @return
         * 		true si construction est incomplète, false sinon
         */
        public boolean isIncomplete() {
            return incomplete;
        }

        /**
         * Déclare que l'entité en cours de construction est incomplète
         */
        public void setIncomplete() {
            this.incomplete = true;
        }
    }
}
