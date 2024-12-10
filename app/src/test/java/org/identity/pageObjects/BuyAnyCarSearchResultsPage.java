package org.identity.pageObjects;

import org.identity.domain.CarInfo;
import org.identity.steps.CarValuationSteps;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BuyAnyCarSearchResultsPage extends WebPage {

    private static Logger logger = LoggerFactory.getLogger(BuyAnyCarSearchResultsPage.class);
    public static final String CAR_DETAILS_TABLE_ROWS = "div[class*='d-table-row']";

    private By notFoundDisplay = By.cssSelector("h1[class^=text-focus]");

    @FindBy(css = "vehicle-details div[class^='details-vrm']")
    private List<WebElement> regNumbers;


    @FindBy(css = CAR_DETAILS_TABLE_ROWS)
    private List<WebElement> carDetailsTableRows;

    By carDetailsTableRowsSelector = By.cssSelector(CAR_DETAILS_TABLE_ROWS);

    public BuyAnyCarSearchResultsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public CarInfo getSearchResult() {

        CarInfo carInfo = new CarInfo();
        TimeoutException timeoutException = null;
        try {
            tenSecondWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(carDetailsTableRowsSelector, 2));
        } catch (TimeoutException e) {
           timeoutException = e;
        }

        try {
            WebElement notFound = driver.findElement(notFoundDisplay);
            if(notFound.isDisplayed() && notFound.getText().contains("we couldn't find your car")) {
                logger.error("Unable to find car details");
                return carInfo;
            }
        } catch (NotFoundException e) {
            throw  timeoutException == null ? e : timeoutException;
        }


        //there are two sets of elements with same ids in the page source thus filtering on visible ones
        carDetailsTableRows.stream().filter(WebElement::isDisplayed).forEach(element -> {
            String text = element.getText();
            if (text.contains("Manufacturer")) {
                carInfo.setMake(text.split(":")[1].trim());
            } else if (text.contains("Model")) {
                carInfo.setModel(text.split(":")[1].trim());
            } else if (text.contains("Year")) {
                carInfo.setYear(text.split(":")[1].trim());
            }
        });
        Optional<WebElement> first = regNumbers.stream()
                .filter(WebElement::isDisplayed).findFirst();

        first.ifPresentOrElse(
                regNumber -> carInfo.setVariantReg(regNumber.getText()),
                () -> {
                    throw new RuntimeException("Unable to find any displayed reg number");
                });
        return carInfo;
    }
}


