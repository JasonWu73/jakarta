package net.wuxianjie.springbootweb.oplog;

import net.wuxianjie.springbootweb.oplog.dto.GetOpLogRequest;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志相关的 SQL 语句。
 *
 * @author 吴仙杰
 */
@Mapper
public interface OpLogMapper {

  /**
   * 新增操作日志记录。
   *
   * @param opLog 需要新增的日志
   * @return 新增记录数
   */
  int insert(OpLog opLog);

  /**
   * 根据查询条件获取操作日志分页列表。
   *
   * @param pagination 查询分页参数
   * @param request 查询参数
   * @return 过滤后的操作日志分页列表
   */
  List<OpLog> selectByQueryOrderByUpdatedAtDesc(
    @Param("p") PaginationParam pagination,
    @Param("q") GetOpLogRequest request);

  /**
   * 根据查询条件获取操作日志总条数。
   *
   * @param request 查询参数
   * @return 过滤后的总条数
   */
  long countByQuery(@Param("q") final GetOpLogRequest request);
}
