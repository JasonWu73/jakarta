package net.wuxianje;

public class SealedClass {

  // 'Dog' is not allowed in the sealed hierarchy
  // public static final class Dog extends Animal {}

  public static final class Cat extends Animal {
  }

  public static sealed class Animal permits Cat {
  }
}
