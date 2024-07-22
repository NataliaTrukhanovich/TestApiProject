package Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPlaces {

private int id;
@JsonProperty("isCovered")
private boolean isCovered;
@JsonProperty("isWarm")
private boolean isWarm;
private int placesCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingPlaces)) return false;

        ParkingPlaces that = (ParkingPlaces) o;

        if (isCovered() != that.isCovered()) return false;
        if (isWarm() != that.isWarm()) return false;
        return getPlacesCount() == that.getPlacesCount();
    }

    @Override
    public int hashCode() {
        int result = (isCovered() ? 1 : 0);
        result = 31 * result + (isWarm() ? 1 : 0);
        result = 31 * result + getPlacesCount();
        return result;
    }
}
