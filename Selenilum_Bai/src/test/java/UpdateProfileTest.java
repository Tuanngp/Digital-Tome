import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateProfileTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test invalid name field")
    public void testInvalidNameField() {
        login();
        navigateToProfile();
        fillProfileFormInvalidName();
        submitForm();
        assertNameFieldValidation();
    }

    @Test
    @DisplayName("Test invalid phone field")
    public void testInvalidPhoneField() {
        login();
        navigateToProfile();
        fillProfileFormInvalidPhone();
        submitForm();
        assertPhoneFieldValidation();
    }

    @Test
    @DisplayName("Test invalid date of birth field")
    public void testInvalidDateOfBirthField() {
        login();
        navigateToProfile();
        fillProfileFormValidDateOfBirth();
        submitForm();
        assertDateOfBirthValidation();
    }

    private void login() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameField.sendKeys("duclkde170045@fpt.edu.vn");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("123");

        WebElement loginForm = driver.findElement(By.id("login"));
        loginForm.submit();

        wait.until(ExpectedConditions.urlContains("/index"));
    }

    private void navigateToProfile() {
        WebElement profileLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@class, 'nav-link')]/div[contains(@class, 'profile-avatar-container')]")));
        profileLink.click();

        WebElement profileMenuItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".dropdown-menu a[href*='/profile']")));
        profileMenuItem.click();
    }

    private void fillProfileFormInvalidName() {
        WebElement fullNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("inputName")));
        fullNameField.clear();
        fullNameField.sendKeys("12345"); // Invalid name input
    }

    private void fillProfileFormInvalidPhone() {
        WebElement phoneField = driver.findElement(By.id("phone"));
        phoneField.clear();
        phoneField.sendKeys("123"); // Invalid phone number input
    }

    private void fillProfileFormValidDateOfBirth() {
        WebElement dateOfBirthField = driver.findElement(By.id("birthday"));
        dateOfBirthField.clear();
        dateOfBirthField.sendKeys("1949-01-01");
    }


    private void submitForm() {
        try {
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("save-profile")));
            saveButton.click(); // Click vào nút Save

            // Chờ cho form được submit thành công và trang được load lại
            wait.until(ExpectedConditions.urlContains("/profile")); // Kiểm tra URL có chứa /profile

        } catch (TimeoutException e) {
            throw new TimeoutException("Timeout waiting for save button to be clickable or profile page to load.");
        }
    }

    private void assertNameFieldValidation() {
        try {
            WebElement nameErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#inputName + .alert.alert-danger")));
            assertEquals("Name should contain only alphabetic characters and spaces", nameErrorElement.getText());
        } catch (TimeoutException e) {
            fail("Timeout waiting for name error message");
        }
    }

    private void assertPhoneFieldValidation() {
        try {
            WebElement phoneErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#phone + .alert.alert-danger")));
            assertEquals("Phone number must be between 8 and 11 digits", phoneErrorElement.getText());
        } catch (TimeoutException e) {
            fail("Timeout waiting for phone error message");
        }
    }

    private void assertDateOfBirthValidation() {
        try {
            WebElement dateOfBirthErrorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#birthday + .alert.alert-danger")));
            assertEquals("Date of birth must be greater than 1950 and less than current date", dateOfBirthErrorElement.getText());
        } catch (TimeoutException e) {
            fail("Timeout waiting for date of birth error message");
        }
    }
}
