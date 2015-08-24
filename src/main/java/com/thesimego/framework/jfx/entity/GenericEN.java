package com.thesimego.framework.jfx.entity;

import com.j256.ormlite.dao.Dao;
import java.io.Serializable;
import javafx.scene.Node;

/**
 *
 * @author Daniel
 * @param <T>
 */
public abstract class GenericEN<T> implements Serializable {

    abstract public Long getId();
    abstract public Dao<T, Long> getDao();
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GenericEN)) {
            return false;
        }
        GenericEN other = (GenericEN) object;
        return (this.getId() != null || other.getId() == null) && (this.getId() == null || this.getId().equals(other.getId()));
    }
    
    @Override
    public String toString() {
        return this.getClass().getSuperclass().getName() + "[ id=" + getId() + " ]";
    }
    
    public Node getTableRowHoverNode() {
        return null;
    }
    

}
