package pl.wojtektrzos.filmkrecimy.entity;

import javax.persistence.*;

@Entity
@Table(name="prerequisits")
public class Prerequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String discriminator; // typ pola po którym będziemy szukac
    private String fieldValue; // wartość pola

    public Prerequisite() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
