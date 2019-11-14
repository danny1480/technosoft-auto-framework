package framework.webPages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import stepdefinition.SharedSD;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class hotelsPage extends BasePage{

    private By reservationbutton = By.id("qf-0q-compact-occupancy");
    private By widgetbutton = By.xpath("//button[@class='cta widget-overlay-close']");
    private By moreoptions = By.xpath("//select[@id='qf-0q-compact-occupancy']//option[@data-more-options='true']");
    private By roomsdropdown = By.xpath("//select[@id='qf-0q-rooms']");
    private By roomsDisplayed = By.xpath("//div[contains(@class,'widget-query-group widget-query-rooms widget-query-rooms-multiple')]//span[@class='widget-query-room-options-label']");
    private By dealOfDay = By.xpath("/html[1]/body[1]/div[3]/main[1]/div[2]/div[2]/div[1]/section[2]/div[1]/ol[1]/li[1]/article[1]/section[1]/aside[1]/div[1]/a[1]/ins[1]");
    private By checkbox = By.xpath("//input[@id='f-star-rating-5']");
    private By hotelList = By.xpath("//span[@class='star-rating-text star-rating-text-strong'][contains(text(),'5-star')]");
    private By hotelradius = By.xpath("//a[@class='property-name-link' and //li//article[1]//section[1]//div[1]//div[1]//div[1]//div[1]//ul[1]//li[1]]");
    private By objectAtTwoMileRadius = By.xpath("//li[@class='hotel secret-price-badge-available family-friendly']//li[contains(text(),'1.9 miles to City center')]");

    public void clickOnReservationButton() { clickOn(reservationbutton);}
    public void clickOnWidgetButton() { clickOn(widgetbutton);}
    public void clickOnMoreOptions() {clickOn(moreoptions);}
    public void clickOnCheckBox() {clickOn(checkbox);}


    public void scrollPageAction () {
        JavascriptExecutor jsScrollBy = (JavascriptExecutor) SharedSD.getDriver();
        jsScrollBy.executeScript("scrollBy(0,300);");
    }

    public void scrollSearchPage () {
        JavascriptExecutor jsScrollBy = (JavascriptExecutor) SharedSD.getDriver();
        for (int second = 0;; second++) {
            if(second >=10){
                break;
            }
            WebElement objectAt2mile = webAction(objectAtTwoMileRadius).findElement(objectAtTwoMileRadius);
            jsScrollBy.executeScript("window.scrollBy(0,800)", objectAt2mile);
        }
    }

    public void scrollSearchPage2mileRadius () {
        JavascriptExecutor jsScrollBy = (JavascriptExecutor) SharedSD.getDriver();
        for (int second = 0;; second++) {
            if(second >=10){
                break;
            }
            jsScrollBy.executeScript("window.scrollBy(0,800)", "1.9 mile to City center");
        }
    }

    public boolean getHotelsList () {
        List<WebElement> list = webAction(hotelList).findElements(hotelList);
        list.toString().contains("3-star");
        return false;
    }

    public ArrayList<String> getRoomsDisplayed () {
        List<WebElement> roomDisplayed = webAction(roomsDisplayed).findElements(roomsDisplayed);
        ArrayList<String> roomsArray = new ArrayList<>();
        for (WebElement room : roomDisplayed) {
            roomsArray.add(room.getText());
        }
        return roomsArray;
    }

    public void selectFromDropdown(ArrayList<String>select_rooms) {
        WebElement room = webAction(roomsdropdown).findElement(roomsdropdown);
        Select selectRoom = new Select(room);
        selectRoom.selectByValue(String.valueOf(select_rooms));
    }

    public boolean getDealOfDay() throws ParseException {
        String expextedDealStr = dealOfDay.toString().replace("$", "");
        int expectedDealInt = NumberFormat.getInstance().parse(expextedDealStr).intValue();
        int budgetLimit = 200;
        if (expectedDealInt <= budgetLimit) {
            System.out.println("test passed!");
        }
        return true;
        }


    public ArrayList<String> getHotelWithin2mile () {
        scrollSearchPage2mileRadius();
        List<WebElement> hotelWithin2mile = webAction(hotelradius).findElements(hotelradius);
        ArrayList<String> hotelList2mile = new ArrayList<>();
        for (WebElement hotel : hotelWithin2mile) {
            String hotelStr = hotel.getText();
            hotelList2mile.add(hotelStr);
        }
        return hotelList2mile;
    }

}