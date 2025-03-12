package application;

import javafx.beans.property.*;

public class Car {
    private IntegerProperty carId;
    private StringProperty make;
    private StringProperty model;
    private IntegerProperty year;
    private DoubleProperty price;
    private StringProperty color;
    private IntegerProperty mileage;
    private BooleanProperty isSold;

    public Car(int carId, String make, String model, int year, double price, String color, int mileage, boolean isSold) {
        this.carId = new SimpleIntegerProperty(carId);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.color = new SimpleStringProperty(color);
        this.mileage = new SimpleIntegerProperty(mileage);
        this.isSold = new SimpleBooleanProperty(isSold);
    }

    public int getCarId() { return carId.get(); }
    public IntegerProperty carIdProperty() { return carId; }
    public String getMake() { return make.get(); }
    public StringProperty makeProperty() { return make; }
    public String getModel() { return model.get(); }
    public StringProperty modelProperty() { return model; }
    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }
    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }
    public String getColor() { return color.get(); }
    public StringProperty colorProperty() { return color; }
    public int getMileage() { return mileage.get(); }
    public IntegerProperty mileageProperty() { return mileage; }
    public boolean isSold() { return isSold.get(); }
    public BooleanProperty isSoldProperty() { return isSold; }
}
