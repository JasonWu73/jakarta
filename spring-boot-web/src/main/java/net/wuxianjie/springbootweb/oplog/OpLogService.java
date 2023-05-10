package net.wuxianjie.springbootweb.oplog;

import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.dto.GetOpLogRequest;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import net.wuxianjie.springbootweb.shared.util.StringUtils;
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
   * 获取操作日志列表。
   *
   * @param pagination 分页请求参数
   * @param request 请求参数
   * @return 操作日志分页列表
   */
  public ResponseEntity<PaginationResult<OpLog>> getLogs(
    final PaginationParam pagination,
    final GetOpLogRequest request
  ) {
    // 设置模糊查询参数
    request.setClientIp(StringUtils.toNullableLikeValue(request.getClientIp()));
    request.setMessage(StringUtils.toNullableLikeValue(request.getMessage()));

    // 查询数据库获取列表数据
    final List<OpLog> logs = opLogMapper.selectByQuery(pagination, request);

    // 查询数据库获取总条目数
    final long total = opLogMapper.countByQuery(request);

    // 构造分页结果
    return ResponseEntity.ok(new PaginationResult<>(
      pagination.getPageNumber(),
      pagination.getPageSize(),
      total,
      logs
    ));
  }
}
