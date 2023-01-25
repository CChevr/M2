package fr.uge.tp4.models;

public class Pharmacy {
    private final int id;
    private String name;
    private String address;
    private String department;
    private String region;

    public Pharmacy(int id, String name, String address, String department, String region) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.department = department;
        this.region = region;
    }

    public Pharmacy(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDepartment() {
        return department;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", department='" + department + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
