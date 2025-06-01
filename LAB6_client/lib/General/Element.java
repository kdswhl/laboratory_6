package lib.General;


import java.io.Serializable;

public abstract class Element implements Comparable<Element>, Validatable, Serializable {
    public Element() {
    }

    public abstract int getId();
}