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

  List<OpLog> selectByQueryOrderByReqTimeDescLimit(
    @Param("p") PaginationParam pag,
    @Param("q") GetOpLogRequest query
  );

  long countByQuery(@Param("q") GetOpLogRequest query);

  int insert(OpLog opLog);
}
