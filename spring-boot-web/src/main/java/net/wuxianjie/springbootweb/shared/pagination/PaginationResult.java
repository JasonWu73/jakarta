package net.wuxianjie.springbootweb.shared.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果。
 *
 * @author 吴仙杰
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResult<E> {

  /**
   * 页码。
   */
  private int pageNum;
  /**
   * 每页条数。
   */
  private int pageSize;
  /**
   * 总条数。
   */
  private long total;
  /**
   * 数据列表。
   */
  private List<E> list;
}
