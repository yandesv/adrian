package application;

import javafx.beans.property.*;

public class Customer {
    private IntegerProperty customerId;
    private StringProperty name;
    private StringProperty address;
    private StringProperty phoneNumber;
    private StringProperty email;

    public Customer(int customerId, String name, String address, String phoneNumber, String email) {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }
}
