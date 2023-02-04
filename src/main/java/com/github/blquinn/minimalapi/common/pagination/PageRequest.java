package com.github.blquinn.minimalapi.common.pagination;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageRequest {
  @Min(1)
  @Size(min = 1)
  int page = 1;
  @Size(min = 5, max = 100)
  int pageSize = 30;
}
