package net.wuxianje;

import java.io.Closeable;
import java.io.IOException;

public class TryWithResource {

  public static void main(final String[] args) {
    closeResourceByManually();
    System.out.println();
    closeResourceAutomatically();
  }

  public static void closeResourceByManually() {
    final ResourceCustomer customer = new ResourceCustomer();

    try {
      customer.doSomething();
    } finally {
      try {
        customer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void closeResourceAutomatically() {
    try (final ResourceCustomer customer = new ResourceCustomer()) {
      customer.doSomething();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static class ResourceCustomer implements Closeable {

    public void doSomething() {
      System.out.println("执行资源消耗操作");
    }

    @Override
    public void close() throws IOException {
      System.out.println("关闭资源占用");
    }
  }
}
