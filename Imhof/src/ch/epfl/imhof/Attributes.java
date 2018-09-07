package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Représente un ensemble d'attributs et la valeur qui leur est associée.
 * Les attributs et leur valeur sont des chaînes de caractères
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Attributes {

    private final Map<String, String> attributes;

    /**
     * Construit un ensemble d'attributs avec les paires clef/valeur présentes dans la table associative donnée
     *
     * @param attributes
     *      Une table associative contenant des attributs (clefs) et la valeur qui leur est associée
     */
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Indique si l'ensemble d'attributs est vide
     *
     * @return
     *      <p>true si l'ensemble d'attributs est vide</p>
     *      <p>false autrement</p>
     */
    public boolean isEmpty(){
        return attributes.isEmpty();
    }

    /**
     * Indique si l'ensemble d'attributs contient la clef donnée en paramètre
     *
     * @return
     *      <p>true si la clef donnée en paramètre se trouve dans l'ensemble d'attributs</p>
     *      <p>false autrement</p>
     */
    public boolean contains(String key){
        return attributes.containsKey(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée
     *
     * @param key
     *      La clef (attribut) desirée
     *
     * @return
     *      <p>La valeur associée à la clef donnée, si cette dernière existe</p>
     *      <p>null si la clef n'existe pas</p>
     */
    public String get(String key){
        return attributes.get(key);
    }

    /**
     * Retourne la valeur textuelle associée à la clef donnée ou la valeur par défaut si aucune valeur ne lui est
     * associée
     * 
     * @param key
     * 		La clef (attribut) desirée
     * @param defaultValue
     * 		La valeur par défaut
     *
     * @return
     * 		<p>La valeur associée à la clef donnée, si cette dernière existe.</p>
     * 		<p>La valeur par défaut si elle n'existait pas</p>
     */
    public String get(String key, String defaultValue){
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Retourne l'entier associé à la clef donnée ou la valeur par défaut si la clé n'existe pas ou que la valeur
     * associée ne représente pas un entier valide
     * 
     * @param key
     * 		La clef (attribut) désirée
     * @param defaultValue
     * 		La valeur par défaut
     *
     * @return
     * 		<p>La valeur entière associée à la clef donnée, si cette dernière existe et que la valeur associée
     * 		représente un entier valide</p>
     * 		<p>La valeur par défaut autrement</p>
     */
    public int get(String key, int defaultValue){
        int val;

        try{
            val = Integer.parseInt( this.get(key) );
        }
        catch(NumberFormatException e){
            val = defaultValue;
        }

        return val;
    }

    /**
     * Retourne la version filtrée des attributs ne contenant que ceux dont le nom figure dans l'ensemble passé
     * 
     * @param keysToKeep
     * 		Ensemble de clefs (attributs) que l'on veut garder
     * @return
     * 		Les attributs dont on a extrait que ceux demandés
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        final Attributes.Builder ab = new Attributes.Builder();
        String val;

        for(String key : keysToKeep){
            val = this.get(key);
            if(val != null){
                ab.put(key, val);
            }
        }

        return ab.build();
    }


    /**
     * Permet de construire une liste associative d'attributs en plusieurs étapes
     */
    public static final class Builder{

        private final Map<String, String> attributes = new HashMap<>();

        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs en cours de construction.
         * Si un attribut de même nom est déjà présent dans l'ensemble, sa valeur est remplacée par celle donnée.
         *
         * @param key
         *      La clef (nom de l'attribut)
         * @param value
         *      La valeur associée à la clef (attribut)
         */
        public void put(String key, String value){
            attributes.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les associations clef/valeur ajoutées jusqu'à présent
         *
         * @return
         *      Un ensemble immuable d'attributs de type Attributes
         */
        public Attributes build(){
            return new Attributes(attributes);
        }

    }
}