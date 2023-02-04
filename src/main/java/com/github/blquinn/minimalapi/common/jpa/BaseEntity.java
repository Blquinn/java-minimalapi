package com.github.blquinn.minimalapi.common.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.type.TextType;

// Default all String fields to "text" type, instead of varchar(255).
@TypeDefs({
  @TypeDef(name = "string", defaultForType = String.class, typeClass = TextType.class)
})
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
  @Id
  @Column(updatable = false, nullable = false, name = "id")
  @GeneratedValue(generator = "sequence_id_generator")
  @GenericGenerator(
      name = "sequence_id_generator",
      strategy = "sequence",
      parameters = @Parameter(
          name = SequenceStyleGenerator.CONFIG_PREFER_SEQUENCE_PER_ENTITY,
          value = "true"
      )
  )
  protected Long id;
}
