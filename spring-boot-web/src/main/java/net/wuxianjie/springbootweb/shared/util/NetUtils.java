package net.wuxianjie.springbootweb.shared.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Optional;

/**
 * 网络相关工具类。
 *
 * @author 吴仙杰
 */
public class NetUtils {

  /**
   * 校验 IP（支持 IPv4 和 IPv6）地址是否合法。
   *
   * @param ip 需要校验的 IP 地址
   * @return IP 地址是否合法
   */
  public static boolean isValidIp(final String ip) {
    return Validator.isIpv4(ip) || Validator.isIpv6(ip);
  }

  /**
   * 校验端口号是否合法。
   *
   * @param port 需要校验的端口号
   * @return 端口号是否合法
   */
  public static boolean isValidPort(final int port) {
    return NetUtil.isValidPort(port);
  }

  /**
   * 发送 TCP 请求。
   *
   * <p>提示：利用 {@link HexUtil} 工具类可轻松实现十六进制字符串数据处理。
   *
   * @param ip TCP 服务端 IP
   * @param port TCP 服务端端口
   * @param data 要发送的数据
   * @return TCP 服务端的响应结果
   */
  public static Optional<byte[]> sendTcp(
    final String ip,
    final int port,
    final byte[] data
  ) {
    try (
      // 创建 TCP 客户端，连接 TCP 服务端
      final Socket client = new Socket(ip, port);

      // 创建客户端输出流，用于发送数据
      final DataOutputStream out = new DataOutputStream(client.getOutputStream());

      // 创建客户端输入流，用于接收数据
      final DataInputStream in = new DataInputStream(client.getInputStream());
    ) {

      // 向服务端发送数据
      out.write(data);
      out.flush();

      // 读取服务端返回数据
      final byte[] readBuffer = new byte[1024];
      final int readLen = in.read(readBuffer);
      if (readLen > 0) {
        return Optional.of(ArrayUtil.sub(readBuffer, 0, readLen));
      }

      return Optional.empty();
    } catch (IOException e) {
      throw new RuntimeException(
        StrUtil.format("发送 TCP 请求失败 [ip={};port={};hexData={}]", ip, port, HexUtil.encodeHexStr(data)),
        e
      );
    }
  }

  /**
   * 发送 UDP 请求。
   *
   * <p>提示：利用 {@link HexUtil} 工具类可轻松实现十六进制字符串数据处理。
   *
   * <p>注意：UDP 是无连接的，故请求的服务端口可能不是服务端返回数据的端口。
   *
   * @param ip UDP 服务端 IP
   * @param port UDP 服务端端口
   * @param data 要发送的数据
   * @return UDP 服务端的响应结果
   */
  public static Optional<byte[]> sendUdp(
    final String ip,
    final int port,
    final byte[] data
  ) {
    // 创建 UDP 客户端，连接 UDP 服务端
    try (final DatagramSocket client = new DatagramSocket()) {

      // 设置最大 1 秒的等待响应时间
      client.setSoTimeout(1_000);

      // 向服务端发送数据
      final InetAddress inetAddr = InetAddress.getByName(ip);
      final DatagramPacket reqPkt = new DatagramPacket(data, data.length, inetAddr, port);
      client.send(reqPkt);

      // 读取服务端返回数据
      final byte[] readBuffer = new byte[1024];
      final DatagramPacket respPkt = new DatagramPacket(readBuffer, readBuffer.length);
      client.receive(respPkt);

      return Optional.of(ArrayUtil.sub(readBuffer, respPkt.getOffset(), respPkt.getLength()));
    } catch (IOException e) {
      if (e instanceof SocketTimeoutException) {
        return Optional.empty();
      }

      throw new RuntimeException(
        StrUtil.format("发送 UDP 请求失败 [ip={};port={};hexData={}]", ip, port, HexUtil.encodeHexStr(data)),
        e
      );
    }
  }
}
