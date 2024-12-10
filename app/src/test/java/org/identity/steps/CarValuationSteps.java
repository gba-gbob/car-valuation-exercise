package org.identity.steps;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.identity.domain.CarInfo;
import org.identity.pageObjects.BuyAnyCarHomePage;
import org.identity.pageObjects.BuyAnyCarSearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class CarValuationSteps {

    private static Logger logger = LoggerFactory.getLogger(CarValuationSteps.class);
    private List<String> carRegistrationInput = new ArrayList<>();
    private List<CarInfo> searchResults = new ArrayList<>();
    private final WebDriver driver;

    public CarValuationSteps(WebDriver driver) {
        this.driver = driver;
    }

    @Before
    public void beforeScenario() {
        carRegistrationInput.clear();
        searchResults.clear();
    }


    @Given("Bob reads car registration numbers from {string}")
    public void bob_reads_car_registration_numbers_from(String inputFileName) throws IOException {
        Path path = Paths.get(getPath(inputFileName).getPath());
        String join = String.join(System.lineSeparator(), Files.readAllLines(path));

        this.carRegistrationInput = Pattern.compile("[A-Z]{2}\\s*\\d{2}\\s*[A-Z]{3}")
                .matcher(join)
                .results()
                .map(s -> s.group().replaceAll("\\s+", ""))
                .sorted()
                .toList();
        logger.info("{} contains following car registration numbers: {}", inputFileName, this.carRegistrationInput);
    }


    @When("Bob performs valuation search for each car")
    public void bob_performs_valuation_search_for_each_car() {

        BuyAnyCarSearchResultsPage buyAnyCarSearchResultsPage = new BuyAnyCarSearchResultsPage(driver);
        carRegistrationInput.forEach(carReg -> {
                    logger.info("Searching car info for registration number: {}", carReg);
                    new BuyAnyCarHomePage(driver).navigateTo()
                            .enterRegistration(carReg)
                            .enterMileage(getRandomInRange(0, 999999))
                            .clickGetValuation();
                    searchResults.add(buyAnyCarSearchResultsPage.getSearchResult());

                }
        );
    }

    @Then("Bob finds results matching {string}")
    public void bob_finds_results_matching(String resultsFileName) throws URISyntaxException, IOException {
        File file = new File(getPath(resultsFileName).toURI());
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.typedSchemaFor(CarInfo.class).withHeader();

        List<CarInfo> expectedCarInfos;
        try (MappingIterator<CarInfo> carInfoMappingIterator
                     = new CsvMapper().readerFor(CarInfo.class).with(schema).readValues(file)) {

            expectedCarInfos = carInfoMappingIterator.readAll();
        }

        expectedCarInfos.sort(Comparator.comparing(CarInfo::getVariantReg));

        assertThat(searchResults, is(equalTo(expectedCarInfos)));
        // can be done without sorting but error message will not be as clear
        // assertThat(searchResults, Matchers.containsInAnyOrder(expectedCarInfos.toArray()));


    }

    private URL getPath(String fileName) {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new RuntimeException("Unable to find resource: " + fileName);
        }
        return resource;

    }

    private int getRandomInRange(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt(min, max + 1);
    }

}
