package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Classe héritant de OSMEntity représentant une relation OSM
 * 
 * @author Grégoire Clément (238122)
 * @author Ali Hosseiny (237452)
 *
 */
public final class OSMRelation extends OSMEntity {

	private final List<Member> members;

	/**
	 * Construit une relation avec son identifant unique, ses membres et ses attributs donnés
     *
	 * @param id
	 * 		identifiant unique de la relation
	 * @param members
	 * 		membres de la relation
	 * @param attributes
	 * 		attributs de la relation
	 */
	public OSMRelation(long id, List<Member> members, Attributes attributes){
		super(id, attributes);
		this.members = Collections.unmodifiableList(new ArrayList<>(members));
	}

	/**
	 * Retourne la liste des membres de la relation
     *
	 * @return
	 * 		membres de la relation
	 */
	public List<Member> members(){
		return members;
	}

	/**
	 * Classe représentant un membre d'une relation OSM
	 */
	public static final class Member{

		private final Type type;
		private final String role;
		private final OSMEntity member;

		/**
		 * Construit un membre ayant le type, le rôle et la valeur donnée
         *
		 * @param type
         *      Le type du membre
		 * @param role
         *      Le rôle du membre dans la relation
		 * @param member
         *      L'entité OSM membre
		 */
		public Member(Type type, String role, OSMEntity member){
			this.type = type;
			this.role = role;
			this.member = member;
		}

		/**
		 * Retourne le type du membre
         *
		 * @return
		 * 		type du membre
		 */
		public Type type(){
			return type;
		}

		/**
		 * Retourne le rôle du membre
         *
		 * @return
		 * 		rôle du membre
		 */
		public String role(){
			return role;
		}
		
		/**
		 * Retourne le membre lui-même
         *
		 * @return
		 * 		le membre (lui-même)
		 */
		public OSMEntity member(){
			return member;
		}

		/**
		 * Enumération des trois type de membre d'une relation (NODE, WAY, RELATION)
		 */
		public enum Type{
			NODE, WAY, RELATION
		}
	}
	
	/**
	 *	Bâtisseur permettant la création d'une relation OSM en plusieurs étapes
	 */
	public static final class Builder extends OSMEntity.Builder{
		
		private final List<Member> member;
		
		/**
		 * Construit un bâtisseur pour une relations ayant l'identifiant donné
         *
		 * @param id
		 * 		L'identifiant
		 */
		public Builder(long id){
			super(id);
			member = new ArrayList<>();
		}
		
		/**
		 * Ajoute un nouveau membre de type et de rôle donnés à la relation
         *
		 * @param type
		 * 		type du membre
		 * @param role
		 * 		rôle du membre
		 * @param newMember
		 * 		nouveau membre
		 */
		public void addMember(Member.Type type, String role, OSMEntity newMember){
			member.add(new Member(type, role, newMember));
		}
		
		/**
		 * Construit et retourne la relation ayant l'identifiant passé en argument ainsi que les membres et attributs ajoutés
         *
		 * @return
		 * 		la relation créée
         *
         * @throws java.lang.IllegalStateException
         *      Si la construction est incomplète
		 */
		public OSMRelation build(){
			if(isIncomplete()){
				throw new IllegalStateException();
			}
			return new OSMRelation(id, member, attributesBuilder.build());
		}
	}

}
