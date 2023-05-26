package net.wuxianjie.springbootweb.oplog;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.dto.GetOpLogRequest;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.util.StrUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志相关处理。
 *
 * @author 吴仙杰
 */
@Service
@RequiredArgsConstructor
public class OpLogService {

  private final OpLogMapper opLogMapper;

  /**
   * 获取操作日志分页列表。
   *
   * @param pag 分页参数
   * @param query 查询参数
   * @return 操作日志分页列表
   */
  public ResponseEntity<PaginationResult<OpLog>> getLogs(final PaginationParam pag, final GetOpLogRequest query) {
    // 设置模糊查询参数
    setFuzzyQueryParams(query);

    // 检索数据库获取数据列表
    final List<OpLog> list = opLogMapper.selectByQueryOrderByRequestTimeDescLimit(pag, query);

    // 检索数据库获取总数
    final long total = opLogMapper.countByQuery(query);

    // 构造分页查询结果
    return ResponseEntity.ok(new PaginationResult<>(
      pag.getPageNum(),
      pag.getPageSize(),
      total,
      list
    ));
  }

  private void setFuzzyQueryParams(final GetOpLogRequest query) {
    query.setUsername(StrUtils.toNullableLikeValue(query.getUsername()));
    query.setClientIp(StrUtils.toNullableLikeValue(query.getClientIp()));
    query.setMessage(StrUtils.toNullableLikeValue(query.getMessage()));
  }
}
