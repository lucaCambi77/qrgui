package it.cambi.qrgui.db.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 */
@Entity
@Table(name = "GENERIC_TABLE", schema = "TEST")
public class GenericTable implements Serializable
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

    @Id
    public int getId() {
        return id;
    }
}
