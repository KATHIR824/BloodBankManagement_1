package model;

public class BloodRequest {
    private int requestId;
    private String patientName;
    private String bloodGroup;
    private int unitsRequired;
    private String contact;
    private String requestDate;
    private String status;

    public BloodRequest() {}

    public BloodRequest(int requestId, String patientName, String bloodGroup, int unitsRequired,
                         String contact, String requestDate, String status) {
        this.requestId = requestId;
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.unitsRequired = unitsRequired;
        this.contact = contact;
        this.requestDate = requestDate;
        this.status = status;
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getUnitsRequired() { return unitsRequired; }
    public void setUnitsRequired(int unitsRequired) { this.unitsRequired = unitsRequired; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
