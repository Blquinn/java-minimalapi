package com.github.blquinn.minimalapi.common.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.TextType;

// Default all String fields to "text" type, instead of varchar(255).
@TypeDefs({
  @TypeDef(name = "string", defaultForType = String.class, typeClass = TextType.class)
})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
  @Id
  @Column(updatable = false, nullable = false, name = "id")
  @GeneratedValue
  protected Long id;
}
