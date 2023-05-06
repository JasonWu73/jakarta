package net.wuxianje;

public class SwitchExpression {

  public static void main(final String[] args) {
    final int code = 3;

    oldSwitchStatement(code);
    System.out.println();

    newSwitchStatement(code);
    System.out.println();

    final int yieldCode = switchYield(code);
    System.out.println("返回 Code: " + yieldCode);
  }

  public static void oldSwitchStatement(final int code) {
    switch (code) {
      case 0:
        System.out.println(code + " is stdin");
        break;
      case 1:
        System.out.println(code + " is stdout");
        break;
      case 2:
        System.out.println(code + " is stderr");
        break;
      default:
        System.out.println("未知 Code: " + code);
    }
  }

  public static void newSwitchStatement(final int code) {
    switch (code) {
      case 0 -> System.out.println(code + " is stdin");
      case 1 -> System.out.println(code + " is stdout");
      case 2 -> System.out.println(code + " is stderr");
      default -> System.out.println("未知 Code: " + code);
    }
  }

  public static int switchYield(final int code) {
    return switch (code) {
      case 0 -> {
        System.out.println(code + " is stdin");
        yield 90;
      }
      case 1 -> {
        System.out.println(code + " is stdout");
        yield 91;
      }
      case 2 -> {
        System.out.println(code + " is stderr");
        yield 92;
      }
      default -> {
        System.out.println("未知 Code: " + code);
        yield Integer.parseInt("9" + code);
      }
    };
  }
}
