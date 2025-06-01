package lib.General;


import java.io.Serializable;

public class Coordinates implements Validatable, Serializable {
    private Double x; //Поле не может быть null
    private int y; //Максимальное значение поля: 857

    public Coordinates(Double x, int y){
        this.x = x;
        this.y = y;

    }

    public int getY(){
        return y;
    }
    public Coordinates (String s) {
        try {
            try { this.x = Double.parseDouble(s.split(";")[0]); } catch (NumberFormatException e) { }
            try { this.y = Integer.parseInt(s.split(";")[1]); }catch (NumberFormatException e) { }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }

    public boolean validate() {
        if(x==null) return false;
        if(y>875) return false;
        return true;
    }
}
