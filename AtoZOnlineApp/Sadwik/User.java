package AtoZOnlineApp.Sadwik;

public class User {
    private String name;
    private String mobileNumber;
    private String address;
    private String email;

    public User(String name, String mobileNumber, String address, String email) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
