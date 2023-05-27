package net.wuxianjie.springbootweb.shared.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

/**
 * 文件相关工具类。
 *
 * @author 吴仙杰
 **/
@Slf4j
public class FileUtils {

  /**
   * 获取当前运行程序所在目录的绝对路径，结尾拥有文件分隔符。
   *
   * @param clazz 作为 Jar 路径判断的类，传入调用本方法的类对象即可
   * @return 程序所在目录的绝对路径，结尾拥有文件分隔符
   */
  public static String getAppDirPath(final Class<?> clazz) {
    return new ApplicationHome(clazz).getDir().getAbsolutePath() + File.separator;
  }

  /**
   * 以 {@code filePath} 的结尾路径分隔符 ({@value StrUtil#SLASH} 或 {@value StrUtil#BACKSLASH}) 作为分隔符拼接多个路径。
   *
   * <p>当 {@code filePath} 不以 {@value StrUtil#SLASH} 或 {@value StrUtil#BACKSLASH} 结尾时，则会使用当前系统的文件路径分隔符进行拼接。
   *
   * @param filePath 起始文件路径
   * @param names 需要合并至 {@code filePath} 的目录或文件名
   * @return 拼接后的文件路径
   */
  public static String join(final String filePath, final String... names) {
    if (StrUtil.endWith(filePath, StrUtil.SLASH)) {
      return StrUtil.join(StrUtil.SLASH, StrUtil.removeSuffix(filePath, StrUtil.SLASH), names);
    }

    if (StrUtil.endWith(filePath, StrUtil.BACKSLASH)) {
      return StrUtil.join(StrUtil.BACKSLASH, StrUtil.removeSuffix(filePath, StrUtil.BACKSLASH), names);
    }

    return StrUtil.join(File.separator, filePath, names);
  }

  /**
   * 判断上传的文件是否为 MP3。
   *
   * @param file HTTP 请求上传的文件
   * @return 上传的文件是否为 MP3
   */
  public static boolean isMp3(final MultipartFile file) {
    final String contentType = getContentType(file);
    final boolean isMp3 = StrUtil.equalsAnyIgnoreCase(contentType, "audio/mpeg");

    if (!isMp3) {
      log.warn("非 MP3 MIME-Type [{}]", contentType);
    }

    return isMp3;
  }

  /**
   * 判断上传的文件是否为 MP4。
   *
   * @param file HTTP 请求上传的文件
   * @return 上传的文件是否为 MP4
   */
  public static boolean isMp4(final MultipartFile file) {
    final String contentType = getContentType(file);
    final boolean isMp4 = StrUtil.equalsAnyIgnoreCase(contentType, "video/mp4");

    if (!isMp4) {
      log.warn("非 MP4 MIME-Type [{}]", contentType);
    }

    return isMp4;
  }

  private static String getContentType(final MultipartFile file) {
    return Optional.ofNullable(file.getContentType()).orElse("");
  }
}
