package net.wuxianjie.springbootweb.shared.pagination;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * 分页查询参数。
 *
 * @author 吴仙杰
 **/
@Data
public class PaginationParam {

  /**
   * 页码，必填，值 >= 1。
   */
  @Min(value = 1, message = "页码不能小于 1")
  private int pageNum;

  /**
   * 每页条数，必填，值 >= 1。
   */
  @Min(value = 1, message = "每页显示条目个数不能小于 1")
  private int pageSize;

  /**
   * MySQL、SQLite 等数据库的偏移量 OFFSET，例如：
   *
   * <ul>
   *   <li>{@code select * from table_name limit #{pageSize} offset #{offset}}</li>
   *   <li>{@code select * from table_name limit #{offset}, #{pageSize}}</li>
   * </ul>
   */
  @Setter(AccessLevel.PRIVATE)
  private int offset;

  /**
   * 由程序自动设置偏移量参数，详见 {@link PaginationOffsetAspect}。
   */
  public void setOffset() {
    setOffset((pageNum - 1) * pageSize);
  }
}
