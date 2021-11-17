package domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistenteID {
    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return id;
    }
}
