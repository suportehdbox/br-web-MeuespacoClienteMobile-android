package br.com.libertyseguros.mobile.beans;



public class HomeBeans {
    private String policy;

    private String dataStartPolicy;

    private String dataEndPolicy;

    private String contract;

    private String issuance;

    private String ciaCode;

    private String branch;

    private String cifCode;

    private String description;

    private String licensePlate;

    private PaymentBeans payment;

    private ClaimBeans claim;

    private String cliCode;

    public String getCliCode() {
        return cliCode;
    }

    public void setCliCode(String cliCode) {
        this.cliCode = cliCode;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getDataStartPolicy() {
        return dataStartPolicy;
    }

    public void setDataStartPolicy(String dataStartPolicy) {
        this.dataStartPolicy = dataStartPolicy;
    }

    public String getDataEndPolicy() {
        return dataEndPolicy;
    }

    public void setDataEndPolicy(String dataEndPolicy) {
        this.dataEndPolicy = dataEndPolicy;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getIssuance() {
        return issuance;
    }

    public void setIssuance(String issuance) {
        this.issuance = issuance;
    }

    public String getCiaCode() {
        return ciaCode;
    }

    public void setCiaCode(String ciaCode) {
        this.ciaCode = ciaCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public PaymentBeans getPayment() {
        return payment;
    }

    public void setPayment(PaymentBeans payment) {
        this.payment = payment;
    }

    public ClaimBeans getClaim() {
        return claim;
    }

    public void setClaim(ClaimBeans claim) {
        this.claim = claim;
    }

    public String getCifCode() {
        return cifCode;
    }

    public void setCifCode(String cifCode) {
        this.cifCode = cifCode;
    }
}
