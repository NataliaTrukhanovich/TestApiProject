package Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class House {
    private int id;
    private int floorCount;
    private List<ParkingPlaces> parkingPlaces;
    private double price;
    @JsonIgnore
    private List<User> lodgers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House)) return false;

        House house = (House) o;

        if (getFloorCount() != house.getFloorCount()) return false;
        if (Double.compare(house.getPrice(), getPrice()) != 0) return false;
        return getParkingPlaces().equals(house.getParkingPlaces());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFloorCount();
        result = 31 * result + getParkingPlaces().hashCode();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}


