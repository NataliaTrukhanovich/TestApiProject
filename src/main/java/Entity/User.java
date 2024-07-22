package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int age;
    private  String firstName;
    private int id;
    private double money;
    private String secondName;
    private String sex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getAge() != user.getAge()) return false;
        if (Double.compare(user.getMoney(), getMoney()) != 0) return false;
        if (!getFirstName().equals(user.getFirstName())) return false;
        if (!getSecondName().equals(user.getSecondName())) return false;
        return getSex().equals(user.getSex());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getAge();
        result = 31 * result + getFirstName().hashCode();
        temp = Double.doubleToLongBits(getMoney());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getSecondName().hashCode();
        result = 31 * result + getSex().hashCode();
        return result;
    }
}
