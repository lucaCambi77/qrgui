package it.cambi.qrgui.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Temi20AnaTipCat.class)
public abstract class Temi20AnaTipCat_ {

	public static volatile SingularAttribute<Temi20AnaTipCat, String> des;
	public static volatile SingularAttribute<Temi20AnaTipCat, String> tipCat;
	public static volatile SetAttribute<Temi20AnaTipCat, Temi14UteCat> temi14UteCats;

	public static final String DES = "des";
	public static final String TIP_CAT = "tipCat";
	public static final String TEMI14_UTE_CATS = "temi14UteCats";

}

