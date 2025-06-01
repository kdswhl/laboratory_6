package lib.General;

import java.io.Serializable;

public class Venue implements Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long capacity; //Значение поля должно быть больше 0
    private Address address; //Поле не может быть null

    public Venue(Integer id, String name, long capacity, Address address) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }

    public Venue(String s){
        if (s.split(" _ ").length!=4){
            this.address = null;
            this.id = null;
            this.name = null;
            this.capacity = -1;
        }
        else {
            try {
                this.id = Integer.parseInt(s.split(" _ ")[0]);
            } catch (NumberFormatException e) {
                return;
            }
            this.name = s.split(" _ ")[1];

            try {
                this.capacity = Long.parseLong(s.split(" _ ")[2]);
            } catch (NumberFormatException e) {
                return;
            }
            this.address = new Address(s.split(" _ ")[3]);
        }
    }


    @Override
    public String toString() {
        return "Venue {id:" + id + " _ " +
                "name:" + name + " _ " +
                "capacity:" + capacity + " _ " +
                address.toString() + "}";
    }

    public String toString(boolean a){
        return id+" _ " + name + " _ " + capacity + " _ " +
                address.toString(true);
    }

    public Address getAddress(){
        return address;
    }

    public Integer getId(){ return this.id;}
    public long getCapacity(){return capacity;}



    public boolean equals(Venue v){
        if (this.id == v.id
         //&& this.name == v.name && this.address == v.address && this.capacity == v.capacity
        ){
            return true;
        }
        return false;
    }

}


