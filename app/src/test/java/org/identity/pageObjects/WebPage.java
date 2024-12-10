package org.identity.pageObjects;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebPage {
    public static final int DEFAULT_IMPLICIT_WAIT = 5;
    protected final WebDriver driver;
    protected WebDriverWait twoSecondWait;
    protected WebDriverWait tenSecondWait;

    public WebPage(WebDriver driver) {
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_IMPLICIT_WAIT));
        driver.manage().window().setSize(new Dimension(1280, 800));
        this.twoSecondWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        this.tenSecondWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void disableImplicitWait(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }

    protected void enableImplicitWait(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_IMPLICIT_WAIT));
    }

}
