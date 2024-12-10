package org.identity.pageObjects;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BuyAnyCarHomePage extends WebPage {

    private static final String URL = "https://www.webuyanycar.com";


    @FindBy(id = "onetrust-accept-btn-handler")
    private WebElement acceptCookiesButton;

    @FindBy(id = "vehicleReg")
    private WebElement enterYourReg;

    @FindBy(id = "Mileage")
    private WebElement mileage;

    @FindBy(id = "btn-go")
    private WebElement getValuationButton;

    public BuyAnyCarHomePage(WebDriver driver ) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public BuyAnyCarHomePage navigateTo() {
        driver.navigate().to(URL);
        try {
            disableImplicitWait();
            twoSecondWait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
            enableImplicitWait();
            acceptCookiesButton.click();
        } catch (TimeoutException e) {
            //not displayed, no action is required
        }
        return this;
    }

    public BuyAnyCarHomePage enterRegistration(String reg) {
        enterYourReg.clear();
        enterYourReg.sendKeys(reg);
        return this;
    }

    public BuyAnyCarHomePage enterMileage(int miles) {
        mileage.clear();
        mileage.sendKeys(String.valueOf(miles));
        return this;
    }

    public BuyAnyCarHomePage clickGetValuation() {
        getValuationButton.click();
        return this;
    }
}


