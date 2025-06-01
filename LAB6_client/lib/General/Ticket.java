package lib.General;


import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class Ticket extends Element implements Validatable, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Double price; //Поле может быть null, Значение поля должно быть больше 0
    private String comment; //Длина строки не должна быть больше 380, Поле может быть null
    private TicketType type; //Поле не может быть null
    private Venue venue; //Поле не может быть null

    public Ticket(Integer id, String name, Coordinates coordinates,
                  LocalDate creationDate, Double price, String comment, TicketType type, Venue venue) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.comment = comment;
        this.type = type;
        this.venue = venue;
    }

    public Ticket(Integer id, String name, Coordinates coordinates,
                  Double price, String comment, TicketType type, Venue venue) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.price = price;
        this.comment = comment;
        this.type = type;
        this.venue = venue;
    }



    public static Ticket fromArray(String[] a){
        if (a.length!=8){
            return null;
        }
        Integer id; //0 Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        String name; //1 Поле не может быть null, Строка не может быть пустой
        Coordinates coordinates; //2 Поле не может быть null
        LocalDate creationDate; //3 Поле не может быть null, Значение этого поля должно генерироваться автоматически
        Double price; //4 Поле может быть null, Значение поля должно быть больше 0
        String comment; //5 Длина строки не должна быть больше 380, Поле может быть null
        TicketType type; //6 Поле не может быть null
        Venue venue; //7

        try{
            try{ id = Integer.parseInt(a[0]);} catch (NumberFormatException e) {id = null;}
            name = a[1];
            try {coordinates = new Coordinates(a[2]);}catch (NumberFormatException e){coordinates = new Coordinates(0.0,0);}
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                creationDate = LocalDate.parse(a[3], formatter);
            } catch (DateTimeException e){creationDate = null;};
            try { price = Double.parseDouble(a[4]);} catch (NumberFormatException e){price = null;}
            comment = a[5];
            try { type = TicketType.valueOf(a[6]);} catch (NullPointerException | IllegalArgumentException  e) { type = null; }


            venue = new Venue(a[7]);
            if (venue.getCapacity()==-1 || venue.getAddress().getStreet()==null  ){
                return null;
            }
            return new Ticket(id,name,coordinates,creationDate,price,comment,type,venue);

        } catch (ArrayIndexOutOfBoundsException e){}
        return null;
    }

    public static String[] toArray(Ticket e){
        var list = new ArrayList<String>();
        list.add(Integer.valueOf(e.getId()).toString());
        list.add(e.getName());
        list.add(e.getCoordinates().toString());
        list.add(e.getCreationDate().format(DateTimeFormatter.ISO_DATE));
        list.add(e.getPrice()!=null?Double.valueOf(e.getPrice()).toString():"null");
        list.add(e.getComment());
        list.add(e.getType().toString());
        list.add(e.getVenue().toString(true));
        return list.toArray(new String[0]);
    }





    @Override
    public String toString() {
        return "Ticket {id: " + id + ", " +
                "name: " + "\""+name + "\", " +
                "coordinates: " + coordinates + ", " +
                "creationDate: " + creationDate.format(DateTimeFormatter.ISO_DATE) + ", " +
                "price: " + price + ", " +
                "comment: " + "\"" +comment + "\", " +
                "type: " +type + ", "+
                venue.toString() +
                "}";
    }

    public boolean validate() {
        if (venue.getCapacity()==-1) return false;
        if (venue.getAddress()==null) return false;
        if (id <= 0|| id==null) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (price!=null && price<=0) return false;
        if (comment.length()>380) return false;
        if (type==null) return false;
        if (venue==null) return false;
        return true;
    }

    public int compareTo(Element var1) {
        return (int)(this.id - var1.getId());
    }


    public int getId() {return id;}
    public String getName() {return name;}
    public Coordinates getCoordinates() {return coordinates;}
    public Double getPrice() {return price;}
    public String getComment(){ return comment;}
    public LocalDate getCreationDate(){return  creationDate;}
    public TicketType getType(){return type;}
    public Venue getVenue(){ return venue;}

    public void setId(int id){this.id = id;}




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket that = (Ticket) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, price, comment, type, venue);
    }


}
