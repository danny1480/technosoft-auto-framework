package framework.webPages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import stepdefinition.SharedSD;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DarkSkyPage extends BasePage{

    private By signUpButton = By.xpath("//a[@class='button filled']");
    private By darkskyApi = By.xpath("//div[@class='inner']//a[contains(text(),'Dark Sky API')]");
    private By timelineLink = By.xpath("//body[@class='forecast']/div[@class='center']/a[1]/span[3]/span[1]/*[1]");
    private By registerbutton = By.xpath("//button[contains(text(),'Register')]");
    private By errorMessage = By.xpath("//input[@name='_csrf']");
    private By timeline = By.xpath("//div[@id='timeline']//div[@class='hours']/descendant::span//span");
    private By temptimeline = By.xpath("//div[@id='timeline']//div[@class='temps']/descendant::span//span");
    private By currentTemp = By.xpath("//span[@class='summary swap']");
    private By minFromTimeline = By.xpath("//*[@id='week']/a[1]/span[2]/span[1]");
    private By maxFromTimeline = By.xpath("//[@id='week']/a[1]/span[2]/span[3]");

    public void clickOnSignupButton() { clickOn(signUpButton);}
    public void clickOnTimelineLink() { clickOn(timelineLink);}
    public void clickOnDarkskyAPI() { clickOn(darkskyApi);}
    public void clickOnRegisterButton() { clickOn(registerbutton);}

    private int currentTempInt = 0;
    ArrayList<Integer> dailyTempList = new ArrayList<>();
    int maxTempInt =0;
    int minTempInt =0;

    public String getErrorMessage() {
        return getTextFromElement(errorMessage);
    }

    public void scrollPageAction () {
        JavascriptExecutor jsScrollBy = (JavascriptExecutor)SharedSD.getDriver();
        jsScrollBy.executeScript("scrollBy(0,700);");
    }

    public ArrayList<String>getHoursFromCalendar() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ha");
        ArrayList<String> hoursArrayCalendar = new ArrayList<>();
        hoursArrayCalendar.add(0, sdf.format(calendar.getTime()).toLowerCase());
        for (int i =1; i<12; i++) {
            calendar.add(Calendar.HOUR, 2);
            String time = sdf.format(calendar.getTime()).toLowerCase();
            hoursArrayCalendar.add(time);
        }
        return hoursArrayCalendar;
    }

    public ArrayList<String> getHoursFromDarkSky () {
        List<WebElement> timeElement = webAction(timeline).findElements(timeline);
        ArrayList<String> hoursArrayDarksky = new ArrayList<>();
        for (WebElement h : timeElement){
            hoursArrayDarksky.add(h.getText());
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ha");
        hoursArrayDarksky.set(0, sdf.format(calendar.getTime()).toLowerCase());
        return hoursArrayDarksky;
    }

    public ArrayList<Integer> getDailyTempList () throws ParseException {
        List<WebElement> dailyTemps = webAction(temptimeline).findElements(temptimeline);
        for (WebElement temp : dailyTemps) {
            String tempStr = temp.getText();
            int tempDaily = ((Number) NumberFormat.getInstance().parse(tempStr)).intValue();
            dailyTempList.add(tempDaily);
        }
        return dailyTempList;
    }

    public int getCurrentTemp () throws ParseException {
        String currentTempStr = webAction(currentTemp).findElement(currentTemp).getText();
        return currentTempInt = ((Number) NumberFormat.getInstance().parse(currentTempStr)).intValue();
    }

    public boolean verifyCurrentTempWithDailyTimeline () {
        return  (currentTempInt <= maxTempInt && currentTempInt >= minTempInt);
    }

    public boolean verifyTodaysDisplayedTemp () {
        int minTempDisplayed = Integer.parseInt(getTextFromElement(minFromTimeline).replace("°",""));
        int maxTempDisplayed = Integer.parseInt(getTextFromElement(maxFromTimeline).replace("°",""));
        int minTempToday = Collections.min(dailyTempList);
        int maxTempToday = Collections.max(dailyTempList);
        return  (minTempDisplayed==minTempToday && maxTempDisplayed==maxTempToday);

    }

}
