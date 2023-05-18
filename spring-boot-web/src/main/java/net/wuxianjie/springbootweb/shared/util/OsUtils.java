package net.wuxianjie.springbootweb.shared.util;

/**
 * 与操作系统相关的工具类。
 *
 * @author 吴仙杰
 */
public class OsUtils {


  /**
   * 判断当前系统是否为 Linux。
   *
   * @return 是否为 Linux
   */
  public static boolean isLinux() {
    return System.getProperty("os.name").toLowerCase().contains("linux");
  }

  /**
   * 判断当前系统是否为 Windows。
   *
   * @return 是否为 Windows
   */
  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  /**
   * 当前系统是否为 Mac OS。
   *
   * @return 是否为 Mac OS
   */
  public static boolean isMacOs() {
    return System.getProperty("os.name").toLowerCase().contains("mac");
  }
}
