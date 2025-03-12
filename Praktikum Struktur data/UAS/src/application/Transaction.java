package application;

import javafx.beans.property.*;

public class Transaction {
    private IntegerProperty transactionId;
    private IntegerProperty customerId;
    private IntegerProperty carId;
    private ObjectProperty<java.sql.Date> saleDate;
    private DoubleProperty salePrice;

    public Transaction(int transactionId, int customerId, int carId, java.sql.Date saleDate, double salePrice) {
        this.transactionId = new SimpleIntegerProperty(transactionId);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.carId = new SimpleIntegerProperty(carId);
        this.saleDate = new SimpleObjectProperty<>(saleDate);
        this.salePrice = new SimpleDoubleProperty(salePrice);
    }

    // Getters and setters
    public int getTransactionId() {
        return transactionId.get();
    }

    public IntegerProperty transactionIdProperty() {
        return transactionId;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public int getCarId() {
        return carId.get();
    }

    public IntegerProperty carIdProperty() {
        return carId;
    }

    public java.sql.Date getSaleDate() {
        return saleDate.get();
    }

    public ObjectProperty<java.sql.Date> saleDateProperty() {
        return saleDate;
    }

    public double getSalePrice() {
        return salePrice.get();
    }

    public DoubleProperty salePriceProperty() {
        return salePrice;
    }
}
