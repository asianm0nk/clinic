package org.example;

public class Appointment {
    private int id;
    private int patientId;
    private int staffId;
    private String date;
    private String reason;

    public Appointment(int id, int patientId, int staffId, String date, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.staffId = staffId;
        this.date = date;
        this.reason = reason;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getStaffId() { return staffId; }
    public String getDate() { return date; }
    public String getReason() { return reason; }

    public void setDate(String date) { this.date = date; }
    public void setReason(String reason) { this.reason = reason; }
}
