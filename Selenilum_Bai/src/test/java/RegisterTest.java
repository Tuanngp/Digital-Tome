import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import java.time.Duration;

public class RegisterTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void fillRegistrationForm(String username, String email, String password) {
        driver.get("http://localhost:8080/register");

        WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
        usernameInput.sendKeys(username);

        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        emailInput.sendKeys(email);

        WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordInput.sendKeys(password);

        WebElement registerButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='btn btn-primary w-100 me-2']")));
        registerButton.click();
    }

    @Test
    public void testUsernameIsExisting() {
        try {
            fillRegistrationForm("Nguyen Quanghau", "haunqde1710605@fpt.edu.vn", "hau123");

            WebElement alertNotify = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='alert alert-danger']")));
            String alertText = alertNotify.getText();

            Assertions.assertEquals("Username already exists. Please use a different username.", alertText);
        } catch (NoSuchElementException e) {
            Assertions.fail("Element not found: " + e.getMessage());
        }
    }

    @Test
    public void testEmailIsExisting() {
        try {
            fillRegistrationForm("QuangHau", "quanghau6622@gmail.com", "hau123");

            WebElement alertNotify = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='alert alert-danger']")));
            String alertText = alertNotify.getText();

            Assertions.assertEquals("Email already exists. Please use a different email.", alertText);
        } catch (NoSuchElementException e) {
            Assertions.fail("Element not found: " + e.getMessage());
        }
    }

    @Test
    public void testWrongOTP() {
        try {
            fillRegistrationForm("QuangHau", "haunqde170605@fpt.edu.vn", "hau123");

            wait.until(ExpectedConditions.urlContains("/otp"));

            WebElement otpInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp")));
            otpInput.sendKeys("dâsdasdasda");

            WebElement verifyButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifyButton")));
            verifyButton.click();

            WebElement errorNotify = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='error']")));
            String alertText = errorNotify.getText();

            Assertions.assertEquals("Invalid OTP! Please try again.", alertText);
        } catch (NoSuchElementException e) {
            Assertions.fail("Element not found: " + e.getMessage());
        }
    }

    @Test
    public void testShowTokenExpiredAndDisableOtpInput() {
        try {
            fillRegistrationForm("QuangHau", "haunqde170605@fpt.edu.vn", "hau123");

            wait.until(ExpectedConditions.urlContains("/otp"));

            WebElement verifyButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("verifyButton")));
            verifyButton.click();

            wait.withTimeout(Duration.ofSeconds(61));

            WebElement errorNotify = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='error-red']")));
            String alertText = errorNotify.getText();

            WebElement otpInput = driver.findElement(By.id("otp"));
            boolean isDisabled = otpInput.getAttribute("disabled") != null;

            Assertions.assertEquals("Token expired", alertText);
            Assertions.assertTrue(isDisabled, "The OTP input field should be disabled.");
        } catch (NoSuchElementException e) {
            Assertions.fail("Element not found: " + e.getMessage());
        }
    }


}
