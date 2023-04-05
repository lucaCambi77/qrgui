package it.cambi.qrgui.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Temi15UteQue.class)
public abstract class Temi15UteQue_ {

	public static volatile SingularAttribute<Temi15UteQue, Long> que;
	public static volatile SetAttribute<Temi15UteQue, Temi16QueCatAss> temi16QueCatAsses;
	public static volatile SingularAttribute<Temi15UteQue, Date> insQue;
	public static volatile SingularAttribute<Temi15UteQue, String> json;
	public static volatile SetAttribute<Temi15UteQue, Temi18RouQue> temi18RouQues;
	public static volatile SingularAttribute<Temi15UteQue, String> nam;
	public static volatile SingularAttribute<Temi15UteQue, String> tenant;

	public static final String QUE = "que";
	public static final String TEMI16_QUE_CAT_ASSES = "temi16QueCatAsses";
	public static final String INS_QUE = "insQue";
	public static final String JSON = "json";
	public static final String TEMI18_ROU_QUES = "temi18RouQues";
	public static final String NAM = "nam";
	public static final String TENANT = "tenant";

}

