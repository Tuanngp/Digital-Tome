
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleSearchPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.name("q");
    private By searchButton = By.name("btnK");

    // Constructor
    public GoogleSearchPage(WebDriver driver) {
        this.driver = driver;
    }

    // Page actions
    public void enterSearchTerm(String searchTerm) {
        WebElement searchBoxElement = driver.findElement(searchBox);
        searchBoxElement.sendKeys(searchTerm);
    }

    public void clickSearchButton() {
        WebElement searchBoxElement = driver.findElement(searchBox);
        searchBoxElement.submit();  // Submit the form since the search button might not be clickable initially
    }
}
