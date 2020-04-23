package br.com.libertyseguros.mobile.beans;

public class VehicleAccidentSendBeans {

    private String branch;

    private String policy= "";

    private String claimType= "1";

    private String licensePlate= "";

    private String claimDateTime= "";

    private String description= "";

    private String itemCode= "";

    private String contractCode= "";

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    private  String contract;

    private String issueCode= "";

    private String issuance;

    public String getIssuance() {
        return issuance;
    }

    public void setIssuance(String issuance) {
        this.issuance = issuance;
    }

    private String ciaCode= "";

    private String issuingAgency= "";

    private String userName= "";

    private String userEmail= "";

    private String userPhone= "";

    private String userIsDriver= "";

    private String addressLine1= "";

    private String addressLine2= "";

    private String number= "";

    private String addressSupport= "";

    private String city= "";

    private String district= "";

    private String driverName= "";

    private String driverBirthDate= "";

    private String driverPhone = "";

    private int state;

    public CitiesBeans getCityBeans() {
        return cityBeans;
    }

    public void setCityBeans(CitiesBeans cityBeans) {
        this.cityBeans = cityBeans;
    }

    private CitiesBeans cityBeans;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private InsuranceStatusBeans insuranceStatus;

    public InsuranceStatusBeans getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(InsuranceStatusBeans insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getClaimDateTime() {
        return claimDateTime;
    }

    public void setClaimDateTime(String claimDateTime) {
        this.claimDateTime = claimDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(String issueCode) {
        this.issueCode = issueCode;
    }

    public String getCiaCode() {
        return ciaCode;
    }

    public void setCiaCode(String ciaCode) {
        this.ciaCode = ciaCode;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public void setIssuingAgency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserIsDriver() {
        return userIsDriver;
    }

    public void setUserIsDriver(String userIsDriver) {
        this.userIsDriver = userIsDriver;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddressSupport() {
        return addressSupport;
    }

    public void setAddressSupport(String addressSupport) {
        this.addressSupport = addressSupport;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverBirthDate() {
        return driverBirthDate;
    }

    public void setDriverBirthDate(String driverBirthDate) {
        this.driverBirthDate = driverBirthDate;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
