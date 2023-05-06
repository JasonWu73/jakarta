package net.wuxianje;

public class PrivateDefaultInterfaceMethod {

  public static void main(final String[] args) {
    final CalculatorTester calculator = new CalculatorTester();
    System.out.println(calculator.doSum());
  }

  static class CalculatorTester implements Calculator {
  }

  interface Calculator {

    default int doSum() {
      return sum(10, 10);
    }

    private int sum(final int num1, final int num2) {
      return num1 + num2;
    }
  }
}
