package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;

import java.util.function.Predicate;

/**
 * Classe permettant d'obtenir des filtres sous forme de predicats ayant pour objtectif de tester
 * des entités attribuées quelconques. Cette classe n'est pas instanciable.
 *
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 */
public final class Filters {

    private Filters(){}

    /**
     * Prend un nom d'attribut en argument et retourne un prédicat qui n'est vrai que si la valeur attribuée
     * à laquelle on l'applique possède un attribut portant ce nom, indépendemment de sa valeur.
     *
     * @param attributeName
     *      Le nom de l'attribut dont on désire tester l'existence dans l'entité attribuée
     * @return
     *      Un prédicat à appliquer à une entité attribuée, retournant vrai si l'entité possède l'attribut spécifié
     *      par l'argument attributeName
     */
    public static Predicate<Attributed<?>> tagged(String attributeName){
        return attributed -> attributed.hasAttribute(attributeName);
    }

    /**
     * Prend un nom d'attribut en argument et retourne un prédicat qui n'est vrai que si la valeur attribuée
     * à laquelle on l'applique possède un attribut portant ce nom, et si de plus la valeur associée à cet attribut
     * fait partie de celles données.
     *
     * @param attributeName
     *      Le nom de l'attribut dont on désire tester l'existence dans l'entité attribuée
     * @param possibleValues
     *      Les valeurs possibles pour l'attribut spécifiée par attributeName
     * @return
     *      Un prédicat à appliquer à une entité attribuée, retournant vrai si l'entité possède l'attribut spécifié
     *      par l'argument attributeName et a pour valeur une des valeurs spécifiées par l'argument possibleValues
     */
    public static Predicate<Attributed<?>> tagged(String attributeName, String... possibleValues){
        return attributed ->
            {
                final String attValue = attributed.attributeValue(attributeName);

                if(attValue != null){
                    for(String possibleValue : possibleValues) {
                        if (attValue.equals(possibleValue)) {
                            return true;
                        }
                    }
                }
                return false;
            };
    }

    /**
     * Prend un numéro (entier) de couche en argument et retourne un prédicat qui n'est vrai que lorsqu'on l'applique
     * à une entité attribuée appartenant cette couche.
     * La valeur de l'attribut layer est un entier compris entre –5 et 5, qui est 0 par défaut 
     * — c-à-d que toute entité ne possédant pas cet attribut (ou le possédant mais avec une valeur non entière)
     * se comporte comme si elle le possédait et sa valeur était 0
     *
     * @param layer
     *      Le numéro de la couche désirée
     * @return
     *      Un prédicat à appliquer à une entité attribuée retournant vrai si cette dernière est dans la couche
     *      spécifiée par l'argument layer
     */
    public static Predicate<Attributed<?>> onLayer(int layer){
        return attributed -> attributed.attributeValue("layer", 0) == layer;
    }
}
