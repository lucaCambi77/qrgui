package it.cambi.qrgui.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Temi14UteCat.class)
public abstract class Temi14UteCat_ {

	public static volatile SingularAttribute<Temi14UteCat, Integer> par;
	public static volatile SetAttribute<Temi14UteCat, Temi16QueCatAss> temi16QueCatAsses;
	public static volatile SingularAttribute<Temi14UteCat, Date> insPar;
	public static volatile SingularAttribute<Temi14UteCat, String> des;
	public static volatile SingularAttribute<Temi14UteCat, Integer> cat;
	public static volatile SingularAttribute<Temi14UteCat, Temi20AnaTipCat> temi20AnaTipCat;
	public static volatile SingularAttribute<Temi14UteCat, Date> insCat;

	public static final String PAR = "par";
	public static final String TEMI16_QUE_CAT_ASSES = "temi16QueCatAsses";
	public static final String INS_PAR = "insPar";
	public static final String DES = "des";
	public static final String CAT = "cat";
	public static final String TEMI20_ANA_TIP_CAT = "temi20AnaTipCat";
	public static final String INS_CAT = "insCat";

}

