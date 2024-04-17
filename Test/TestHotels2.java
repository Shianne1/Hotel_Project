import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class TestHotels2 {
    private static WebDriver driver;
    private static final String HOTEL_URL = "https://www.getaroom.com/";
    //private static final String HOTEL_URL = "https://www.getaroom.com/search?amenities=&destination=Austin&page=1&per_page=25&rinfo=%5B%5B18%5D%5D&sort_order=position&hide_unavailable=true&check_in=2024-05-01&check_out=2024-05-02&property_name=";
    private static Connection connection;
    private static String DB_URL = "jdbc:sqlite:hotelcheckin.sqlite";

    private static List<LocalDate> hotelDates = new ArrayList<>();

    @BeforeClass
    public static void setUp() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        driver = new FirefoxDriver();
        LocalDate startDate = LocalDate.of(2024,5,1);
        LocalDate endDate = LocalDate.of(2024,5,2);
        while(startDate.isBefore(endDate)){
            hotelDates.add(startDate);
            startDate = startDate.plusDays(1);
        }
    }


    @Test
    @Parameters({"Atlanta","Orlando","Sacramento","Miami","Austin"})
    public void testHotelPrices(String location) {
        driver.get(HOTEL_URL);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        for (int i = 0; i < hotelDates.size(); i++) {
            //driver.get(HOTEL_URL);
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            // input of location
            WebElement inputCity = driver.findElement(By.id("destination"));
            inputCity.clear();
            inputCity.sendKeys(location);
            inputCity.submit();

            // date of stay
            WebElement checkIn = driver.findElement(By.id("check_in"));
            checkIn.clear();
            checkIn.sendKeys(hotelDates.get(i).toString());
            checkIn.submit();
            WebElement checkOut = driver.findElement(By.id("check_out"));
            checkOut.clear();
            checkOut.sendKeys(hotelDates.get(i).plusDays(1).toString());
            checkOut.submit();

            WebElement searchHotels = driver.findElement(By.id("enter-travel-dates"));
            searchHotels.click();

            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));

            //testHotelCards("Holiday Inn Express", checkIn, checkOut);

            //driver.getCurrentUrl();
            //WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
            //wait.until(ExpectedConditions.urlMatches(driver.getCurrentUrl()));



            // search hotel name
            WebElement inputHotel = driver.findElement(By.id("hotelName"));
            inputHotel.clear();
            inputHotel.sendKeys("Holiday Inn");
            inputHotel.submit();

            driver.getCurrentUrl();
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));


            List<WebElement> cards = driver.findElements(By.className("hotel-card"));
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
            wait.until(ExpectedConditions.urlContains("Holiday"));

            for (int k = 0; k < cards.size(); k++) {
                //WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
                //wait.until(ExpectedConditions.urlContains("Holiday"));


                WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-body > div > section > div > div > div > div > div > div.results-wrapper > div.results-list-wrapper > ul > li:nth-child(1) > div > div.details > div.title > button")));
                //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                //WebElement city = current.findElement(By.className("city"));
                String cityName = city.getText();

                WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-body > div > section > div > div > div > div > div > div.results-wrapper > div.results-list-wrapper > ul > li:nth-child(1) > div > div.details > div.title > div.name > a")));
                //WebElement title = current.findElement(By.className("name"));
                String hotelName = title.getText();

                //WebElement priceInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("amount")));
                WebElement priceInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-body > div > section > div > div > div > div > div > div.results-wrapper > div.results-list-wrapper > ul > li:nth-child(1) > div > div.details > div.pricing > div.price > span.amount")));
                //WebElement priceInfo = current.findElement(By.className("amount"));
                String price = priceInfo.getText();

                String timeStamp = (new Date()).toString();
                //insertHotelPricesToDatabase(title.getText(),location, checkIn.getText(), checkOut.getText(), priceInfo.getText(), timeStamp);

                System.out.println(hotelName);
                System.out.println(cityName);
                System.out.println(hotelDates.get(i).toString());
                System.out.println(hotelDates.get(i).plusDays(1));
                System.out.println(price);
                System.out.println(timeStamp);
                System.out.println();
            }




        }
    }
}