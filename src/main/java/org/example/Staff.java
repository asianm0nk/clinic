package org.example;

public class Staff {
    private int id;
    private String name;
    private String role;
    private int hospitalId;
    private String username;
    private String password;

    public Staff(int id, String name, String role, int hospitalId, String username, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.hospitalId = hospitalId;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getHospitalId() { return hospitalId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
