package com.dev.praful.trackyouremployee;

public class Employeedetails {
        String username;
        String password;
        String bloodgroup;
        String phone;
        String pick_up_address;
        String employee_Id;
        String addresslat, addresslng;

        public Employeedetails(String username, String password, String bloodgroup, String phone, String pick_up_address, String employee_Id, String addresslat, String addresslng, String id) {
            this.username = username;
            this.password = password;
            this.bloodgroup = bloodgroup;
            this.phone = phone;
            this.pick_up_address = pick_up_address;
            this.employee_Id = employee_Id;
            this.addresslat = addresslat;
            this.addresslng = addresslng;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        String id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBloodgroup() {
            return bloodgroup;
        }

        public void setBloodgroup(String bloodgroup) {
            this.bloodgroup = bloodgroup;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPick_up_address() {
            return pick_up_address;
        }

        public void setPick_up_address(String pick_up_address) {
            this.pick_up_address = pick_up_address;
        }


        public Employeedetails() {

        }

        public String getEmployee_Id() {
            return employee_Id;
        }

        public void setEmployee_Id(String employee_Id) {
            this.employee_Id = employee_Id;
        }

        public String getAddresslat() {
            return addresslat;
        }

        public void setAddresslat(String addresslat) {
            this.addresslat = addresslat;
        }

        public String getAddresslng() {
            return addresslng;
        }

        public void setAddresslng(String addresslng) {
            this.addresslng = addresslng;
        }
}
