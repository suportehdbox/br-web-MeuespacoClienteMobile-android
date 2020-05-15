package br.com.libertyseguros.mobile.beans;


public class PolicyBeansV2 {


    private InsurancesV2 insurances[];

    public InsurancesV2[] getInsurances() {
        return insurances;
    }

    public void setInsurances(InsurancesV2[] insurances) {
        this.insurances = insurances;
    }

    public void addInsurances(InsurancesV2[] insurancesNew){

        if(insurances == null){
            insurances = new InsurancesV2[0];
        }

        InsurancesV2 insurancesAdd[] = new InsurancesV2[insurances.length + insurancesNew.length];

        for(int ind = 0; ind < insurances.length; ind++){
            insurancesAdd[ind] = insurances[ind];
        }

        for(int indj = 0; indj < insurancesNew.length; indj++){
            insurancesAdd[insurances.length + indj] = insurancesNew[indj];
        }

        setInsurances(insurancesAdd);
    }


    public InsurancesV2 getInsurancesInstance(){
        return new InsurancesV2();
    }

    public class InsurancesV2{
        private SalesmanBeans broker;
        private String cliCode;
        private String ciaCode;
        private String contract;
        private String policy;
        private String branch;
        private String dataEndPolicy;
        private String dataStartPolicy;
        private int issuingAgency;
        private boolean allowPHS;

        private InsuranceStatusBeans insuranceStatus;

        public InsuranceStatusBeans getInsuranceStatus() {
            return insuranceStatus;
        }

        public void setInsuranceStatus(InsuranceStatusBeans insuranceStatus) {
            this.insuranceStatus = insuranceStatus;
        }

        private int[] issuances;

        public SalesmanBeans getBroker() {
            return broker;
        }

        public void setBroker(SalesmanBeans broker) {
            this.broker = broker;
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

        private PolicyItem itens[];

        public PolicyItem[] getItens() {
            return itens;
        }

        public void setItens(PolicyItem[] itens) {
            this.itens = itens;
        }


        private PaymentBeans payments[];

        public PaymentBeans[] getPayments() {
            return payments;
        }

        public void setPayments(PaymentBeans[] payments) {
            this.payments = payments;
        }
        public String getCliCode() {
            return cliCode;
        }

        public void setCliCode(String cliCode) {
            this.cliCode = cliCode;
        }

        public String getCiaCode() {
            return ciaCode;
        }

        public void setCiaCode(String ciaCode) {
            this.ciaCode = ciaCode;
        }

        public int[] getIssuances() {
            return issuances;
        }

        public void setIssuances(int[] issuances) {
            this.issuances = issuances;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public String getDataEndPolicy() {
            return dataEndPolicy;
        }

        public void setDataEndPolicy(String dataEndPolicy) {
            this.dataEndPolicy = dataEndPolicy;
        }

        public String getDataStartPolicy() {
            return dataStartPolicy;
        }

        public void setDataStartPolicy(String dataStartPolicy) {
            this.dataStartPolicy = dataStartPolicy;
        }

        public int getIssuingAgency() {
            return issuingAgency;
        }

        public void setIssuingAgency(int issuingAgency) {
            this.issuingAgency = issuingAgency;
        }

        public boolean isAllowPHS() {
            return allowPHS;
        }

        public void setAllowPHS(boolean allowPHS) {
            this.allowPHS = allowPHS;
        }
    }


    public class PolicyItem{

        private int code;

        private String description;
        private String licensePlate;
        private ClaimBeans claim;

        public int getIssuance() {
            return issuance;
        }

        public void setIssuance(int issuance) {
            this.issuance = issuance;
        }

        private int issuance;

        private InsuranceCoverages insuranceCoverages[];

        public InsuranceCoverages[] getInsuranceCoverages() {
            return insuranceCoverages;
        }

        public void setInsuranceCoverages(InsuranceCoverages[] insuranceCoverages) {
            this.insuranceCoverages = insuranceCoverages;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContract() {
            return description;
        }

        public void setContract(String description) {
            this.description = description;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ClaimBeans getClaim() {
            return claim;
        }

        public void setClaim(ClaimBeans claim) {
            this.claim = claim;
        }


    }
}
