package pl.wro.mm.materialweather.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by noiser on 08.06.15.
 */
@Table(name = "weather")
public class MainWeather extends Model {

    @Column(name = "description")
    String description;
    @Column(name = "city_name")
    String cityName;
    @Column(name = "temp")
    String temp;
    @Column(name = "pressure")
    String pressure;
    @Column(name = "conditionid")
    int conditionID;
    @Column(name = "cityID")
    int cityID;

    public MainWeather( ) {

    }

    public MainWeather(MainWeather mainWeather) {
        this.description = mainWeather.description;
        this.cityName = mainWeather.cityName;
        this.temp = mainWeather.temp;
        this.pressure = mainWeather.pressure;
        this.conditionID = mainWeather.conditionID;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public int getConditionID() {
        return conditionID;
    }

    public void setConditionID(int conditionID) {
        this.conditionID = conditionID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
