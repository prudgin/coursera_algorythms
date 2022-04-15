import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        class Parent {
            @Override
            public String toString() {
                return "this is Parent";
            }
        }

        class Child extends Parent {
        }

        class C<T extends Parent> {
            C(T a) {
                System.out.println(a);
            }
        }

        Parent parent = new Parent();
        Child child = new Child();
        C<Child> c = new C<>(child); // Ok
        C<Parent> d = new C<>(parent); // Ok
        C<Parent> e = new C<>(child); // Compile error - Cannot infer arguments
        System.out.println(null == null);

    }
}
