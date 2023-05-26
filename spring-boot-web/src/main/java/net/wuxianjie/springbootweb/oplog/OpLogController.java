package net.wuxianjie.springbootweb.oplog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.wuxianjie.springbootweb.oplog.dto.GetOpLogRequest;
import net.wuxianjie.springbootweb.shared.pagination.PaginationParam;
import net.wuxianjie.springbootweb.shared.pagination.PaginationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志 REST API。
 *
 * @author 吴仙杰
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OpLogController {

  private final OpLogService opLogService;

  /**
   * 获取操作日志分页列表。
   *
   * @param pag 分页参数
   * @param query 查询参数
   * @return 操作日志分页列表
   */
  @GetMapping("/op-logs")
  @PreAuthorize("hasAuthority('op_log_view')")
  public ResponseEntity<PaginationResult<OpLog>> getLogs(
    @Valid final PaginationParam pag,
    @Valid final GetOpLogRequest query
  ) {
    return opLogService.getLogs(pag, query);
  }
}
