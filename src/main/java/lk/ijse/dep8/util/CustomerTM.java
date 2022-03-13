package lk.ijse.dep8.util;

public class CustomerTM {
    private String id;
    private String name;
    private String address;
    private String picture;

    public CustomerTM() {
    }

    public CustomerTM(String id, String name, String address, String picture) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "CustomerTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
