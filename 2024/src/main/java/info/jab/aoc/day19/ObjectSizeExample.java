package info.jab.aoc.day19;

import org.openjdk.jol.info.ClassLayout;

//Temporal class
public class ObjectSizeExample {
    static class MyClass {
        int id;
        String name;
    }

    public static void main(String[] args) {
        MyClass myObject = new MyClass();
        System.out.println(ClassLayout.parseInstance(myObject).toPrintable());
    }
}
