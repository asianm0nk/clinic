package org.example;

public class Patient {
    private int id;
    private String name;
    private String birthDate;
    private String phone;

    public Patient(int id, String name, String birthDate, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setPhone(String phone) { this.phone = phone; }
}
