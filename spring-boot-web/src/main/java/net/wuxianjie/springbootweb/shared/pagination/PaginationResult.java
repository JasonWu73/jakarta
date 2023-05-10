package net.wuxianjie.springbootweb.shared.pagination;

import java.util.List;

/**
 * 分页查询结果。
 *
 * @param pageNumber 页码
 * @param pageSize 每页显示条目个数
 * @param total 总条目数
 * @param list 数据列表
 * @param <E> 列表项数据类型
 * @author 吴仙杰
 */
public record PaginationResult<E>(
  int pageNumber,
  int pageSize,
  long total,
  List<E> list
) {
}
