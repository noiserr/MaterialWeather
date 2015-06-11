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
    String minTemperature;
    @Column(name = "max_temp")
    String maxTemperature;

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

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
