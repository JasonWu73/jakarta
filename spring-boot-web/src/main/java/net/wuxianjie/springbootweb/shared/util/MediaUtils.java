package net.wuxianjie.springbootweb.shared.util;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.File;

/**
 * 媒体文件相关工具类。
 *
 * <p>需添加 {@code ws.schild:jave-all-deps} 依赖才可使用。
 *
 * <p>若系统未配置 ffmpeg，则将提取包中的 ffmpeg 至系统临时目录（如 C:\Windows\Temp\jave）。
 *
 * @author 吴仙杰
 **/
@Slf4j
public class MediaUtils {

  static {
    // 使用本工具类前，需要先完成初始化（拷贝 ffmpeg 至系统临时目录），否则会在首次执行音视频处理时出现失败
    final DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
    final String exePath = locator.getExecutablePath();

    log.info("使用的 ffmpeg 版本 [{}]", exePath);
  }

  /**
   * 获取视频或音频文件的播放时长，单位：秒。
   *
   * @param media 视频或音频文件
   * @return 播放时长（单位：秒），若无法获取，则返回 0
   */
  public static long getTimeLen(final File media) {
    final MultimediaObject multimediaObject = new MultimediaObject(media);
    final MultimediaInfo info;

    try {
      info = multimediaObject.getInfo();
    } catch (Exception e) {
      log.warn("无法读取文件时长 [{}]", media.getAbsolutePath(), e);
      return 0;
    }

    return info.getDuration() / 1000;
  }

  /**
   * 视频文件转 MP3 文件。
   *
   * @param originalVideo 原始视频文件
   * @param targetMp3 目标 MP3 文件
   * @return MP3 文件是否转换成功
   * @see <a href="https://github.com/a-schild/jave2/wiki/Examples">Examples · a-schild/jave2 Wiki</a>
   */
  public static boolean toMp3(final File originalVideo, final File targetMp3) {
    try {
      // 需要转换的音频属性
      final AudioAttributes audio = new AudioAttributes();
      // 音频编码器：MP3
      audio.setCodec("libmp3lame");
      // 比特率：128 kbit/s
      audio.setBitRate(128_000);
      // 采样率：44100 Hz
      audio.setSamplingRate(44_100);
      // 声道：立体声
      audio.setChannels(2);

      // 编码属性
      final EncodingAttributes attrs = new EncodingAttributes();
      // 设置输出格式
      attrs.setOutputFormat("mp3");
      // 设置音频参数
      attrs.setAudioAttributes(audio);

      // 开始编码
      final Encoder encoder = new Encoder();
      encoder.encode(new MultimediaObject(originalVideo), targetMp3, attrs);
    } catch (Exception e) {
      log.error(
        "视频文件转 MP3 失败 [{} -> {}]",
        originalVideo.getAbsolutePath(),
        targetMp3.getAbsolutePath(),
        e
      );
      return false;
    }

    return true;
  }
}
