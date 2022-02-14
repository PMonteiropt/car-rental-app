package com.car.rental.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDefs({@FilterDef(name = "filterDeleted", parameters = @ParamDef(name = "deletedparam", type = "boolean"))})

@Filters({@Filter(name = "filterDeleted", condition = "deleted = :deletedparam")})

public abstract class AbstractEntity implements Serializable {
  protected static final String RAWTYPES = "rawtypes";
  private static final long serialVersionUID = 2801266813634680104L;
  



  @Column(name = "deleted")
  private boolean deleted = false;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "insert_date")
  private Date insertDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

 
  public boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public Date getInsertDate() {
    return insertDate;
  }

  public void setInsertDate(Date insertDate) {
    this.insertDate = insertDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }


  @PreUpdate
  private void generateUpdateDate() {
    this.updateDate = new Date();
  }

  @PrePersist
  private void generateInsertDate() {
    this.insertDate = new Date();
  }



}
