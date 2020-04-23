package br.com.libertyseguros.mobile.beans;


public class VehicleAccidentStatusBeans {

    private Claims claims[];

    public Claims[] getClaims() {
        return claims;
    }

    public void setClaims(Claims[] claims) {
        this.claims = claims;
    }

    public class Claims{
        private String date;

        private String number;

        private String policy;

        private String status;

        private int type;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
