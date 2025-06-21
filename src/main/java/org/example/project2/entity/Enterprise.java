package org.example.project2.entity;

//used for User
public class Enterprise {
    private Long id;
    private String name;
    private String licenseNumber;
    private String contactPerson;
    private String contactPhone;
    private Integer audiStatus;
    public Enterprise(Long id,String name,String licenseNumber,String contactPerson,String contactPhone, Integer audiStatus){
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.audiStatus = audiStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getAudiStatus() {
        return audiStatus;
    }

    public void setAudiStatus(Integer audiStatus) {
        this.audiStatus = audiStatus;
    }
}
