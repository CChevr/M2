package fr.uge.tp2.models;

public class Pharmacy {
    private final int id;
    private final String name;
    private final String address;
    private final String department;
    private final String region;

    public Pharmacy(int id, String name, String address, String department, String region) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.department = department;
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
