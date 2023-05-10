package net.wuxianjie.springbootweb.shared.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Optional;

/**
 * 字符串相关工具类。
 *
 * @author 吴仙杰
 */
public class StringUtils {

  /**
   * 按如下规则生成数据库 LIKE 值：
   *
   * <ul>
   *   <li>
   *     当 {@code value} 不为空时，则将字符串中的空白字符替换为 {@code %}，例如：
   *     <pre>{@code "KeyOne    KeyTwo" -> "%KeyOne%KeyTwo%"}</pre>
   *   </li>
   *   <li>当 {@code value} 为空时，则返回 {@code null}</li>
   * </ul>
   *
   * @param value 需要转换的原字符串
   * @return <pre>{@code "KeyOne    KeyTwo" -> "%KeyOne%KeyTwo%"}</pre>
   * 或 {@code null}
   */
  public static String toNullableLikeValue(final String value) {
    return Optional.ofNullable(StrUtil.trimToNull(value))
      .map(v -> "%" + v.replaceAll(" +", "%") + "%")
      .orElse(null);
  }

  /**
   * 获取机器码。
   *
   * <p>将 MAC 地址转换为十六进制大写字符串作为机器码。
   *
   * <p>可通过 {@link #toMacAddress(String)} 方法将机器码重新解析为 MAC 地址。
   *
   * @return 机器码
   */
  public static Optional<String> getMachineCode() {
    return Optional.ofNullable(NetUtil.getLocalMacAddress())
      .map(mac -> HexUtil.encodeHexStr(StrUtil.bytes(mac), false));
  }

  /**
   * 将通过 {@link #getMachineCode()} 方法得到的机器码转为 MAC 地址。
   *
   * @param machineCode 机器码
   * @return MAC 地址
   * @throws IllegalArgumentException 当机器码传入为空时抛出
   */
  public static String toMacAddress(final String machineCode) {
    return HexUtil.decodeHexStr(machineCode);
  }
}
