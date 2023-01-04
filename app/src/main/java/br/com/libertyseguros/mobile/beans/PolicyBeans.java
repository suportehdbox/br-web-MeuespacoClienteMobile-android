package br.com.libertyseguros.mobile.beans;


public class PolicyBeans {
    private Insurances insurances[];

    public Insurances[] getInsurances() {
        return insurances;
    }

    public void setInsurances(Insurances[] insurances) {
        this.insurances = insurances;
    }

    public void addInsurances(Insurances[] insurancesNew){

        if(insurances == null){
            insurances = new Insurances[0];
        }

        Insurances insurancesAdd[] = new Insurances[insurances.length + insurancesNew.length];

        for(int ind = 0; ind < insurances.length; ind++){
            insurancesAdd[ind] = insurances[ind];
        }

        for(int indj = 0; indj < insurancesNew.length; indj++){
            insurancesAdd[insurances.length + indj] = insurancesNew[indj];
        }

        setInsurances(insurancesAdd);
    }


    public Insurances getInsurancesInstance(){
        return new Insurances();
    }

    public class Insurances{
        private SalesmanBeans broker;

        private ClaimBeans claimBeans;

        private int code;

        private String cliCode;

        private String ciaCode;

        public String getCiaCode() {
            return ciaCode;
        }

        public void setCiaCode(String ciaCode) {
            this.ciaCode = ciaCode;
        }

        private String contract;

        private String description;

        private int issuance;

        private String policy;

        private String branch;

        private InsuranceStatusBeans insuranceStatus;

        public InsuranceStatusBeans getInsuranceStatus() {
            return insuranceStatus;
        }

        public void setInsuranceStatus(InsuranceStatusBeans insuranceStatus) {
            this.insuranceStatus = insuranceStatus;
        }

        public SalesmanBeans getBroker() {
            return broker;
        }

        public void setBroker(SalesmanBeans broker) {
            this.broker = broker;
        }

        public ClaimBeans getClaimBeans() {
            return claimBeans;
        }

        public void setClaimBeans(ClaimBeans claimBeans) {
            this.claimBeans = claimBeans;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getIssuance() {
            return issuance;
        }

        public void setIssuance(int issuance) {
            this.issuance = issuance;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String policyBranch) {
            this.branch = policyBranch;
        }

        public String getCliCode() {
            return cliCode;
        }

        public void setCliCode(String cliCode) {
            this.cliCode = cliCode;
        }

    }
}
