package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    private int id;
    private String engineType;
    private String mark;
    private String model;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;

        Car car = (Car) o;

        if (Double.compare(car.getPrice(), getPrice()) != 0) return false;
        if (!getEngineType().equals(car.getEngineType())) return false;
        if (!getMark().equals(car.getMark())) return false;
        return getModel().equals(car.getModel());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getEngineType().hashCode();
        result = 31 * result + getMark().hashCode();
        result = 31 * result + getModel().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
