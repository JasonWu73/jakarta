package net.wuxianje;

public class PatternMatching {

  public static void makeNoise(final Animal animal) {
    if (animal instanceof Dog) {
      ((Dog) animal).bark();
      return;
    }

    if (animal instanceof Cat) {
      ((Cat) animal).meow();
    }
  }

  public static void makeNoiseNewWay(final Animal animal) {
    if (animal instanceof Dog dog) {
      dog.bark();
      return;
    }

    if (animal instanceof Cat cat) {
      cat.meow();
    }
  }

  public static void main(final String[] args) {
    final Dog dog = new Dog();
    makeNoise(dog);
    makeNoiseNewWay(dog);

    final Cat cat = new Cat();
    makeNoise(cat);
    makeNoiseNewWay(cat);
  }

  static class Dog extends Animal {

    void bark() {
      System.out.println("arf arf...");
    }
  }

  static class Cat extends Animal {

    void meow() {
      System.out.println("meow meow...");
    }
  }

  static class Animal {
  }
}
