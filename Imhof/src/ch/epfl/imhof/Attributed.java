package ch.epfl.imhof;

/**
 * Représente une entité de type générique dotée d'attributs
 *
 * @param <T>
 *     Le type de l'entité attribuée
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Attributed <T> {

    private final T value;
    private final Attributes attributes;

    /**
     * Construit une entité de type T dotée d'attributs (Attributes)
     * 
     * @param value
     * 		L'entité/valeur de type T 
     * @param attributes
     * 		Les attributs attachés à notre entité/valeur
     */
    public Attributed(T value, Attributes attributes){
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * @return
     * 		L'entitée à laquelle sont attachées les attributs
     */
    public T value(){
        return value;
    }

    /**
     * @return
     * 		Les attributs attachés à l'entité
     */
    public Attributes attributes(){
        return attributes;
    }

    /**
     * Retourne vrai si les attributs de l'entité incluent la clef passée en argument
     * 
     * @param attributeName
     * 		Nom de l'attribut (clef) que l'on désire vérifier la présence
     *
     * @return
     * 		<p>vrai si l'attribut (clef) est inclu dans l'ensemble d'attributs</p>
     * 		<p>faux sinon</p>
     */
    public boolean hasAttribute(String attributeName){
        return attributes.contains(attributeName);
    }

    /**
     * Retourne la valeur associée à la clef donnée dans l'ensemble d'attributs de l'entité
     *
     * @param attributeName
     *      La clef (attribut) desirée
     *
     * @return
     *      <p>La valeur associée à la clef donnée, si cette dernière existe</p>
     *      <p>null si la clef n'existe pas</p>
     */
    public String attributeValue(String attributeName){
        return attributes.get(attributeName);
    }

    /**
     * Retourne la valeur textuelle associée à la clef donnée dans l'ensemble d'attributs de l'entité
     * ou la valeur par défaut si aucune valeur ne lui est associée
     *
     * @param attributeName
     * 		La clef (attribut) desirée
     * @param defaultValue
     * 		La valeur par défaut
     *
     * @return
     * 		<p>La valeur associée à la clef donnée, si cette dernière existe.</p>
     * 		<p>La valeur par défaut si elle n'existait pas</p>
     */
    public String attributeValue(String attributeName, String defaultValue){
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Retourne l'entier associé à la clef donnée dans l'ensemble d'attributs de l'entité
     * ou la valeur par défaut si la clé n'existe pas ou que la valeur associée ne représente pas un entier valide
     *
     * @param attributeName
     * 		La clef (attribut) désirée
     * @param defaultValue
     * 		La valeur par défaut
     *
     * @return
     * 		<p>La valeur entière associée à la clef donnée, si cette dernière existe et que la valeur associée
     * 		représente un entier valide</p>
     * 		<p>La valeur par défaut autrement</p>
     */
    public int attributeValue(String attributeName, int defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
}