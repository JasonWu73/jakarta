package net.wuxianjie.springbootweb.oplog;

import org.apache.ibatis.annotations.Mapper;

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
}
