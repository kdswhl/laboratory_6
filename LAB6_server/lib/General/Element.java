package lib.General;


public abstract class Element implements Comparable<Element>, Validatable {
    public Element() {
    }

    public abstract int getId();
}