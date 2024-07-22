package Utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface ConfigProvider {
    Config config = readConfig();

    static Config readConfig(){
        return ConfigFactory.load("testApi.conf");
    }

    String LOGIN = readConfig().getString("endpoint.login");
    String CREATE_USER = readConfig().getString("endpoint.createUser");
    String CREATE_CAR = readConfig().getString("endpoint.createCar");
    String CREATE_HOUSE = readConfig().getString("endpoint.createHouse");
    String BUY_CAR = readConfig().getString("endpoint.buyCar");
    String BUY_HOUSE = readConfig().getString(("endpoint.settleHouse"));
}
