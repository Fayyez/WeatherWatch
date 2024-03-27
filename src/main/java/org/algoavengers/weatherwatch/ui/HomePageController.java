package org.algoavengers.weatherwatch.ui;

//fxml imports
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Accordion;
//models imports
import javafx.stage.Stage;
import org.algoavengers.weatherwatch.models.*;
import org.algoavengers.weatherwatch.services.WeatherWatchService;
import org.algoavengers.weatherwatch.utils.DTConverter;

import static org.algoavengers.weatherwatch.utils.DTConverter.getDate;


public class HomePageController {
    // assets
    private String assetsPath = "src/main/resources/org/algoavengers/weatherwatch/assets/";
    // service
    WeatherWatchService wws = new WeatherWatchService();
    // Weather Data from backend
    private WeatherData currentWeather;
    private LocationData currentLocation;
    private LocationData[] savedLocations;
    private WeatherData[] forecast;
    private APData currentAPData;
    // Accordion
    @FXML
    private Accordion accordion;
    // Text Fields
    @FXML
    private TextField searchByNameField;
    @FXML
    private TextField searchByCoordsField;
    // Pane
    @FXML
    private Pane mainPane;
    // Buttons
    @FXML
    private Button saveButton;
    @FXML
    private Button SL1;
    @FXML
    private Button SL2;
    @FXML
    private Button SL3;
    @FXML
    private Button SL4;
    @FXML
    private Button SL5;
    // images
    @FXML
    private ImageView mainWeatherIcon;
    @FXML
    private ImageView FmainIcon1;
    @FXML
    private ImageView FmainIcon2;
    @FXML
    private ImageView FmainIcon3;
    @FXML
    private ImageView FmainIcon4;
    @FXML
    private ImageView FmainIcon5;
    // Labels
    @FXML
    private Label locationName;
    @FXML
    private Label tempLabel;
    @FXML
    private Label todayDate;
    @FXML
    private Label feelsLikeTemp;
    @FXML
    private Label windLabel;
    @FXML
    private Label maxTempLabel;
    @FXML
    private Label sunriseTime;
    @FXML
    private Label visibilityLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label minTempLabel;
    @FXML
    private Label sunsetTime;
    @FXML
    private Label pressureLabel;
    @FXML
    private Label mainWeather;
    @FXML
    private Label description;
    // Air Quality Labels
    @FXML
    private Label aqiValue;
    @FXML
    private Label COLevel;
    @FXML
    private Label NOLevel;
    @FXML
    private Label NO2Level;
    @FXML
    private Label sulphurLevel;
    @FXML
    private Label pm2Level;
    @FXML
    private Label pm10Level;
    // Forecast Labels
    @FXML
    private Label Fdate1;
    @FXML
    private Label Fdate2;
    @FXML
    private Label Fdate3;
    @FXML
    private Label Fdate4;
    @FXML
    private Label Fdate5;
    @FXML
    private Label Ftemp1;
    @FXML
    private Label Ftemp2;
    @FXML
    private Label Ftemp3;
    @FXML
    private Label Ftemp4;
    @FXML
    private Label Ftemp5;
    @FXML
    private Label Fmain1;
    @FXML
    private Label Fmain2;
    @FXML
    private Label Fmain3;
    @FXML
    private Label Fmain4;
    @FXML
    private Label Fmain5;
    @FXML
    private Label Fdescription1;
    @FXML
    private Label Fdescription2;
    @FXML
    private Label Fdescription3;
    @FXML
    private Label Fdescription4;
    @FXML
    private Label Fdescription5;
    @FXML
    private Label FmaxTemp1;
    @FXML
    private Label FmaxTemp2;
    @FXML
    private Label FmaxTemp3;
    @FXML
    private Label FmaxTemp4;
    @FXML
    private Label FmaxTemp5;
    @FXML
    private Label FminTemp1;
    @FXML
    private Label FminTemp2;
    @FXML
    private Label FminTemp3;
    @FXML
    private Label FminTemp4;
    @FXML
    private Label FminTemp5;
    @FXML
    private Label Fhumidity1;
    @FXML
    private Label Fhumidity2;
    @FXML
    private Label Fhumidity3;
    @FXML
    private Label Fhumidity4;
    @FXML
    private Label Fhumidity5;


    // Rectangles
    @FXML
    private Rectangle aqiStatus;

    @FXML
    public void initialize() {
        // set svae locations

        // set all event listeners here
        searchByNameField.setOnAction(event -> {
            String text = searchByNameField.getText();
            System.out.println(text);
            try {
                currentLocation = wws.cityToCoords(text);
                Object[] data = wws.fetchData(currentLocation);
                setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
                displayData();
            } catch (Exception e) {
                System.out.println("Error fetching data: " + e.getMessage());
            }
            searchByNameField.clear();

        });

        searchByCoordsField.setOnAction(event -> {
            String text = searchByCoordsField.getText();
            System.out.println(text);
            // split text into (float) lat, lon
            String[] coords = text.split(", ");
            System.out.println(Float.parseFloat(coords[0]));
            System.out.println(Float.parseFloat(coords[1]));
            try {
                currentLocation = wws.coordsToCity(Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
                Object[] data = wws.fetchData(currentLocation);
                setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
                displayData();
            } catch (NumberFormatException e) {
                System.out.println("Error parsing coordinates: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error fetching data: " + e.getMessage());
            }
            searchByCoordsField.clear();
        });

        saveButton.setOnAction(event -> {
            wws.saveLocation(currentLocation);
            displaySavedLocation();
        });
        // default actions
        // setting saved locations
        displaySavedLocation();
    }

    public void displayData() {
        // main card info
        if(currentLocation.city.length()>0)
            locationName.setText(currentLocation.city + ", " + currentLocation.country);
        else locationName.setText(currentLocation.lat + ", " + currentLocation.lon);
        tempLabel.setText(currentWeather.temp + "°C");
        todayDate.setText(currentWeather.dt);
        feelsLikeTemp.setText(currentWeather.feelsLike + "°C");
        mainWeather.setText(currentWeather.main);
        // set png image on main pane here
        description.setText(currentWeather.description);
        windLabel.setText(currentWeather.windSpeed + "m/s");
        maxTempLabel.setText(currentWeather.tempMax + "°C");
        sunriseTime.setText(DTConverter.getTime(currentWeather.sunrise));
        visibilityLabel.setText(currentWeather.visibility + "m");
        humidityLabel.setText(currentWeather.humidity + "%");
        minTempLabel.setText(currentWeather.tempMin + "°C");
        sunsetTime.setText(DTConverter.getTime(currentWeather.sunset));
        pressureLabel.setText(currentWeather.pressure + "hPa");
        mainWeatherIcon.setImage(loadImageFromAssets(currentWeather.icon));
        //implement background images here
        String bkgPNG = "";
        if(currentWeather.main.equals("Drizzle") || currentWeather.main.equals("Rain"))
            bkgPNG = "rain.jpg";
        else if(currentWeather.main.equals("Snow"))
            bkgPNG = "snow.jpeg";
        else if(currentWeather.main.equals("Clear"))
            bkgPNG = "clear.jpeg";
        else if(currentWeather.main.equals("Clouds"))
            bkgPNG = "clouds.jpg";
        else if (currentWeather.main.equals("Thunderstorm") || currentWeather.main.equals("Tornado"))
            bkgPNG = "thunderstorm.jpg";
        else if (currentWeather.main.equals("Smoke") || currentWeather.main.equals("Haze") || currentWeather.main.equals("Dust") || currentWeather.main.equals("Fog"))
            bkgPNG = "haze.jpeg";

        if (bkgPNG.length() > 0) {
            String imagePath = getClass().getResource("/assets/" + bkgPNG).toExternalForm();
            mainPane.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover;");
        }

        // air quality info
        aqiValue.setText(currentAPData.aqi + "");
        COLevel.setText(currentAPData.co + " µg/m³");
        NOLevel.setText(currentAPData.no + " µg/m³");
        NO2Level.setText(currentAPData.no2 + " µg/m³");
        sulphurLevel.setText(currentAPData.so2 + " µg/m³");
        pm2Level.setText(currentAPData.pm2_5 + " µg/m³");
        pm10Level.setText(currentAPData.pm10 + " µg/m³");
        switch (currentAPData.aqi) {
            case 1:
                aqiStatus.setFill(Color.GREEN);
                break;
            case 2:
                aqiStatus.setFill(Color.YELLOW);
                break;
            case 3:
                aqiStatus.setFill(Color.ORANGE);
                break;
            case 4:
                aqiStatus.setFill(Color.DARKORANGE);
                break;
            case 5:
                aqiStatus.setFill(Color.RED);
                break;
            default:
                aqiStatus.setFill(Color.BLACK);
        }


        // forecast
        loadForecastCards();
    }
    public void displaySavedLocation() {
        savedLocations = wws.getSavedLocations();
        int size = savedLocations.length;
        Button[] SLButtons = new Button[]{SL1, SL2, SL3, SL4, SL5};
        for(int i = 0; i < size; i++) {
            if (savedLocations[i].city.length() > 0)
                SLButtons[i].setText(savedLocations[i].city + ", " + savedLocations[i].country);
            else SLButtons[i].setText("(" + savedLocations[i].lat + ", " + savedLocations[i].lon + ")");
            SLButtons[i].setDisable(false);
        }
    }

    public void getSavedLocationData1() {
        try {
            currentLocation = savedLocations[0];
            Object[] data = wws.fetchData(currentLocation);
            setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
            displayData();
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }
    public void getSavedLocationData2() {
        try {
            currentLocation = savedLocations[1];
            Object[] data = wws.fetchData(currentLocation);
            setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
            displayData();
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }
    public void getSavedLocationData3() {
        try {
            currentLocation = savedLocations[2];
            Object[] data = wws.fetchData(currentLocation);
            setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
            displayData();
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }
    public void getSavedLocationData4() {
        try {
            currentLocation = savedLocations[3];
            Object[] data = wws.fetchData(currentLocation);
            setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
            displayData();
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }
    public void getSavedLocationData5() {
        try {
            currentLocation = savedLocations[4];
            Object[] data = wws.fetchData(currentLocation);
            setAllData((LocationData) data[0], (WeatherData) data[1], (APData) data[2], (WeatherData[]) data[3]);
            displayData();
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    //setters
    public void setCurrentWeather(WeatherData currentWeather) {
        this.currentWeather = currentWeather;
    }
    public  void setCurrentLocation(LocationData currentLocation) {
        this.currentLocation = currentLocation;
    }
    public void setCurrentAPData(APData currentAPData) {
        this.currentAPData = currentAPData;
    }
    public void setForecast(WeatherData[] forecast) {
        this.forecast = forecast;
    }
    public void setAllData(LocationData loc, WeatherData weather, APData apData, WeatherData[] forecast) {
        setCurrentLocation(loc);
        setCurrentWeather(weather);
        setCurrentAPData(apData);
        setForecast(forecast);
    }

    // others
    public void loadForecastCards() {
        Label[] Fdates = new Label[5];
        Label[] Ftemps = new Label[5];
        Label[] Fmains = new Label[5];
        ImageView[] FmainIcons = new ImageView[5];
        Label[] Fdescriptions = new Label[5];
        Label[] FmaxTemps = new Label[5];
        Label[] FminTemps = new Label[5];
        Label[] Fhumidities = new Label[5];
        // push all fxml elements in the relevant forecast arrays
        Fdates[0] = Fdate1; Fdates[1] = Fdate2; Fdates[2] = Fdate3; Fdates[3] = Fdate4; Fdates[4] = Fdate5;
        Ftemps[0] = Ftemp1; Ftemps[1] = Ftemp2; Ftemps[2] = Ftemp3; Ftemps[3] = Ftemp4; Ftemps[4] = Ftemp5;
        Fmains[0] = Fmain1; Fmains[1] = Fmain2; Fmains[2] = Fmain3; Fmains[3] = Fmain4; Fmains[4] = Fmain5;
        FmainIcons[0] = FmainIcon1; FmainIcons[1] = FmainIcon2; FmainIcons[2] = FmainIcon3; FmainIcons[3] = FmainIcon4; FmainIcons[4] = FmainIcon5;
        Fdescriptions[0] = Fdescription1; Fdescriptions[1] = Fdescription2; Fdescriptions[2] = Fdescription3; Fdescriptions[3] = Fdescription4; Fdescriptions[4] = Fdescription5;
        FmaxTemps[0] = FmaxTemp1; FmaxTemps[1] = FmaxTemp2; FmaxTemps[2] = FmaxTemp3; FmaxTemps[3] = FmaxTemp4; FmaxTemps[4] = FmaxTemp5;
        FminTemps[0] = FminTemp1; FminTemps[1] = FminTemp2; FminTemps[2] = FminTemp3; FminTemps[3] = FminTemp4; FminTemps[4] = FminTemp5;
        Fhumidities[0] = Fhumidity1; Fhumidities[1] = Fhumidity2; Fhumidities[2] = Fhumidity3; Fhumidities[3] = Fhumidity4; Fhumidities[4] = Fhumidity5;
        for(int currentIndex = 0; currentIndex < 5; currentIndex++) {
            Fdates[currentIndex].setText(getDate(forecast[currentIndex].dt));
            Ftemps[currentIndex].setText(forecast[currentIndex].temp + "°C");
            Fmains[currentIndex].setText(forecast[currentIndex].main);
            Fdescriptions[currentIndex].setText(forecast[currentIndex].description);
            FmaxTemps[currentIndex].setText(forecast[currentIndex].tempMax + "°C");
            FminTemps[currentIndex].setText(forecast[currentIndex].tempMin + "°C");
            Fhumidities[currentIndex].setText(forecast[currentIndex].humidity + "%");
            FmainIcons[currentIndex].setImage(loadImageFromAssets(forecast[currentIndex].icon));
        }
    }
    public Image loadImageFromAssets(String iconName) {
        return new Image("file:" + assetsPath + iconName + ".png");
    }
}