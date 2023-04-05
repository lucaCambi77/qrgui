package it.cambi.qrgui.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Temi17UteRou.class)
public abstract class Temi17UteRou_ {

	public static volatile SingularAttribute<Temi17UteRou, String> des;
	public static volatile SingularAttribute<Temi17UteRou, Long> rou;
	public static volatile SetAttribute<Temi17UteRou, Temi18RouQue> temi18RouQues;
	public static volatile SingularAttribute<Temi17UteRou, Date> insRou;

	public static final String DES = "des";
	public static final String ROU = "rou";
	public static final String TEMI18_ROU_QUES = "temi18RouQues";
	public static final String INS_ROU = "insRou";

}

