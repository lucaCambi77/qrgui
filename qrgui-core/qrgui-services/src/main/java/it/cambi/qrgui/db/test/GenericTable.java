package it.cambi.qrgui.db.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "GENERIC_TABLE")
public class GenericTable implements java.io.Serializable
{

    private int id;
    private String name;

    public GenericTable()
    {
    }

    public GenericTable(int id)
    {
        this.id = id;
    }

    public GenericTable(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public int getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 32)
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
