package pl.wro.mm.materialweather.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by noiser on 11.06.15.
 */
@Table(name = "forecast")
public class MainForecast extends Model {

    @Column(name = "unix_date")
    long date;
    @Column(name = "condition_id")
    int conditionId;
    @Column(name = "city_id")
    int cityId;
    @Column(name = "city_name")
    String cityName;
    @Column(name = "min_temp")
    String dayTemperature;
    @Column(name = "max_temp")
    String nightTemperature;
    @Column(name = "day_of_week")
    String dayOfWeek;
    @Column(name = "hum")
    String humidity;
    @Column(name = "cloudiness")
    String cludiness;
    @Column(name = "rain")
    String rainVolume;
    @Column(name = "pressure")
    String pressure;
    @Column(name = "wind_speed")
    String windSpeed;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature(String dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public String getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(String nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCludiness() {
        return cludiness;
    }

    public void setCludiness(String cludiness) {
        this.cludiness = cludiness;
    }

    public String getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(String rainVolume) {
        this.rainVolume = rainVolume;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
