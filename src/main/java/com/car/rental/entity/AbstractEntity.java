package com.car.rental.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MappedSuperclass
@FilterDefs({@FilterDef(name = "filterDeleted", parameters = @ParamDef(name = "deletedparam", type = "boolean"))})

@Filters({@Filter(name = "filterDeleted", condition = "deleted = :deletedparam")})

public abstract class AbstractEntity implements Serializable {
  protected static final String RAWTYPES = "rawtypes";
  private static final long serialVersionUID = 2801266813634680104L;
  private static final Logger logger = LoggerFactory.getLogger(AbstractEntity.class);
  private static final String ERROR_NO_ANOTACION_ID =
      "Hemos invocado al metodo AbstractEntity.{0}, pero la clase {1} no tiene ningun atributo anotado con @Id.";


  @Column(name = "version")
  @Version
  private Integer version;

  @Column(name = "deleted")
  private boolean deleted = false;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "insert_date")
  private Date insertDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_date")
  private Date updateDate;

  @Transient
  private Integer hashCodeValue = null;

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

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

  // Campo que nos almacenara el campo mapeado con la clave primaria de la tabla
  private static transient HashMap<String, Field> hmIdAttribute = new HashMap<>();

  /**
   * Metodo que devuelve el valor del objeto anotado con @Id
   * 
   * @return
   */
  public Object getId() {

    // http://opensource.atlassian.com/projects/hibernate/browse/HHH-3718
    if (this instanceof HibernateProxy) {
      LazyInitializer lazyInitializer = ((HibernateProxy) this).getHibernateLazyInitializer();
      if (lazyInitializer.isUninitialized()) {
        return (Integer) lazyInitializer.getIdentifier();
      }
    }

    Object idValue = null;

    Class<?> clasePk = getClassId();

    // Si no hay ClassId , entonces es que solo hay una PK
    if (clasePk == null) {

      // Ahora tenemos que recuperar el atributo con la @Id
      try {

        Field campoId = obtenerCampoId();

        // Si hemos encontrado un atributo marcado con la anotacion @Id,
        // entonces obtenemos su valor.
        if (campoId != null) {
          idValue = FieldUtils.readField(campoId, this, true);
        } else {
          logger.warn(MessageFormat.format(ERROR_NO_ANOTACION_ID, new Object[] {"getId", this.getClass()}));
        }
      } catch (IllegalAccessException e) {
        // Dejamos constancia del error en el log.
        logger.error("Error getting the id using getId de AbstractEntity.", e);

        // Ahora pintamos el error por la consola para ver el error si
        // estamos depurando.
        logger.error(e.getMessage(), e);
      }

    } else {
      // Tenemos que crear una pk del tipo que queremos
      try {

        idValue = clasePk.newInstance();
        PropertyUtils.copyProperties(idValue, this);

      } catch (Exception e) {
        logger.error("Error al obtener el id compuesto mediante el metodo generico setId de AbstractEntity.", e);
        logger.error(e.getMessage(), e);
      }

      // Ahora copiamos las propiedades del padre a la PK
    }

    return idValue;
  }

  @PreUpdate
  private void generateUpdateDate() {
    this.updateDate = new Date();
  }

  @PrePersist
  private void generateInsertDate() {
    this.insertDate = new Date();
  }

  private Class<?> getClassId() {
    Class<? extends AbstractEntity> clase = this.getClass();
    Class<?> value = null;

    if (clase.isAnnotationPresent(IdClass.class)) {
      IdClass annotation = clase.getAnnotation(IdClass.class);
      value = annotation.value();
    }

    return value;
  }

  /**
   * Metodo que devolvera el campo que esta anotado con @Id.
   * 
   * @return
   */
  @SuppressWarnings(RAWTYPES)
  public Field obtenerCampoId() {

    // Si ya hemos analizado la clase para saber el atributo que esta
    // anotado con @Id, no la volvemos a analizar
    Class<? extends AbstractEntity> estaClase = this.getClass();

    if (hmIdAttribute.get(estaClase.getName()) == null) {

      int i = 0;

      Field[] declaredFields = estaClase.getDeclaredFields();
      while (i < declaredFields.length) {
        Field field = declaredFields[i];

        if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
          hmIdAttribute.put(estaClase.getName(), field);
          break; // solo queremos el primero (y unico)
        }

        i++;
      }

      // En caso de no haber encontrado en la misma clase , intento buscar
      // hacia arriba
      if (hmIdAttribute.get(estaClase.getName()) == null) {
        Class padre = estaClase.getSuperclass();
        while (padre != null) {
          i = 0;
          Field[] parentFields = padre.getDeclaredFields();
          while (i < parentFields.length) {
            Field field = parentFields[i];

            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
              hmIdAttribute.put(estaClase.getName(), field);
              break; // solo queremos el primero (y unico)
            }
            i++;
          }

          if (hmIdAttribute.get(estaClase.getName()) != null) {
            break;
          }
          padre = padre.getSuperclass();
        }

      }

    }

    // Para obtener el id debemos de recorrer todos los atributos de la
    // clase y quedarnos con aquel que tenga la anotacion @Id de JPA
    return hmIdAttribute.get(estaClase.getName());
  }


  @SuppressWarnings(RAWTYPES)
  public void setId(Object id) {
    Class clasePk = getClassId();

    if (clasePk == null) {

      try {
        Field campoId = obtenerCampoId();

        // Si hemos encontrado un atributo marcado con la anotacion @Id,
        // entonces
        // obtenemos su valor.
        if (campoId != null) {
          if (id == null && campoId.getType().isPrimitive()) {
            FieldUtils.writeField(campoId, this, 0, true);
          } else {
            FieldUtils.writeField(campoId, this, id, true);
          }
        } else {
          logger.warn(MessageFormat.format(ERROR_NO_ANOTACION_ID, new Object[] {"setId", this.getClass()}));
        }
      } catch (IllegalAccessException e) {
        // Dejamos constancia del error en el log.
        logger.error("Error al establecer el id mediante el metodo generico setId de AbstractEntity.", e);

        // Ahora pintamos el error por la consola para ver el error si
        // estamos
        // depurando.
        logger.error(e.getMessage(), e);
      }
    } else {

      // La clave es compuesta, copiamos las propiedades
      try {
        PropertyUtils.copyProperties(id, this);
      } catch (Exception e) {
        logger.error("Error al establecer el id compuesto mediante el metodo generico setId de AbstractEntity.", e);
        logger.error(e.getMessage(), e);
      }

    }

  }

  /**
   * Metodo que devuelve la primer subclase que extiende de AbstractEntity
   * 
   * @return
   */
  @SuppressWarnings(RAWTYPES)
  public Class getFirstSubClass() {

    Class<? extends AbstractEntity> estaClase = this.getClass();

    Class subClass = estaClase;

    // Recorremos hacia arriba hasta encontrar la clase que extiende de AbstractEntity
    while (subClass != null) {

      try {
        if (subClass.getSuperclass() == Class.forName("com.car.rental.entity.AbstractEntity")) {
          return subClass;
        }
      } catch (ClassNotFoundException e) {
        logger.error(e.getMessage(), e);
      }

      subClass = subClass.getSuperclass();
    }

    return subClass;

  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof AbstractEntity && this.getId() == null && ((AbstractEntity) obj).getId() == null) {
      return obj == this;
    } else {
      return obj instanceof AbstractEntity
          && (this.getClass().isAssignableFrom(obj.getClass())
              || (obj.getClass().isAssignableFrom(this.getClass()) && this.obtenerCampoId().equals(((AbstractEntity) obj).obtenerCampoId())))
          && this.getId() != null && ((AbstractEntity) obj).getId() != null && this.getId().equals(((AbstractEntity) obj).getId());
    }

  }

  @Override
  public int hashCode() {
    // Si todavia no hemos calculado el hashCode, lo calculamos.
    // Esta implementacion nos permitira que, una vez calcula el hashCode, no lo volvamos a calcular hasta que creemos una nueva instancia del objeto.
    if (hashCodeValue == null) {
      Object id = this.getId();
      Object objToCalculateHashCode = id;
      if (id == null) {
        objToCalculateHashCode = UUID.randomUUID();
      }

      hashCodeValue = new HashCodeBuilder().append(objToCalculateHashCode).toHashCode();
    }

    return hashCodeValue;
  }

}
