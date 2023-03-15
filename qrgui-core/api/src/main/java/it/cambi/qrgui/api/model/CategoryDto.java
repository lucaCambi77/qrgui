package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CategoryDto implements java.io.Serializable {
    private TipCatDto temi20TipCatDao;
    private Integer par;
    private Date insPar;
    private String des;
    private Set<QueCatAssDto> queCatAsses = new HashSet<>(0);
    private int cat;
    private Date insCat;

    public CategoryDto() {
    }

    public CategoryDto(Integer cat) {
        this.cat = cat;
    }

    public CategoryDto(int cat, Date insCat, TipCatDto TipCatDao, String des) {
        this.cat = cat;
        this.insCat = insCat;
        this.temi20TipCatDao = TipCatDao;
        this.des = des;
    }

    public CategoryDto(
            int cat,
            Date insCat,
            TipCatDto TipCatDao,
            Integer par,
            String des,
            Set<QueCatAssDto> queCatAsses) {
        this.cat = cat;
        this.insCat = insCat;
        this.temi20TipCatDao = TipCatDao;
        this.par = par;
        this.des = des;
        this.queCatAsses = queCatAsses;
    }

    public int getCat() {
        return this.cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public Date getInsCat() {
        return this.insCat;
    }

    public void setInsCat(Date insCat) {
        this.insCat = insCat;
    }

    public TipCatDto getTemi20AnaTipCat() {
        return this.temi20TipCatDao;
    }

    public void setTemi20AnaTipCat(TipCatDto TipCatDao) {
        this.temi20TipCatDao = TipCatDao;
    }

    public Integer getPar() {
        return this.par;
    }

    public void setPar(Integer par) {
        this.par = par;
    }

    public Date getInsPar() {
        return this.insPar;
    }

    public void setInsPar(Date insPar) {
        this.insPar = insPar;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Set<QueCatAssDto> getTemi16QueCatAsses() {
        return this.queCatAsses;
    }

    public void setTemi16QueCatAsses(Set<QueCatAssDto> queCatAsses) {
        this.queCatAsses = queCatAsses;
    }
}
