# car-valuation-exercise

### Run tests commands 
Run `./gradlew testCucumberChrome` to execute tests against chrome browser  
Run `./gradlew testCucumberFirefox`  to execute tests against firefox browser 

### Test reports 
Can be found under target/cucumber-report.html

### Known issues 
Firefox browser fails to launch on Ubuntu 22.04 - known issue and requires geko driver installation via snap   
Input files contains unrecognised number plates what results in test failure  

### Supported browsers 
Ensure browser is installed and use system property `browser` to set driver         
       chrome = ChromeDriver  
       chromium =  ChromiumDriver  
       firefox = FirefoxDriver  
       edge = EdgeDriver    
       safari SafariDriver  
 