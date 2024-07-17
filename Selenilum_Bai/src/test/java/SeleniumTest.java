import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumTest {
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

    @Test
    public void testLoginSuccess() {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameField.sendKeys("duclkde170045@fpt.edu.vn");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("123");

        WebElement loginForm = driver.findElement(By.id("login"));
        loginForm.submit();

        wait.until(ExpectedConditions.urlContains("/index"));
    }



    @ParameterizedTest
    @CsvSource({
            "admin@gmail.com, 123",
            "invalid-email,123",
            ", 123",
            "admin@gmail.com, ",
            ", ",
    })
    public void testLoginFailure(String username, String password) {
        driver.get("http://localhost:8080/login");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameField.clear();
        if (username != null && !username.isEmpty()) {
            usernameField.sendKeys(username);
        }


        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        if (password != null && !password.isEmpty()) {
            passwordField.sendKeys(password);
        }

        // Tìm nút submit
        WebElement loginButton = driver.findElement(By.cssSelector("button.btn-primary"));


        boolean invalidInput = (username == null || username.isEmpty() || password == null || password.isEmpty() || !username.contains("@"));
        if (invalidInput) {
            try {
                loginButton.click();
                // Nếu có thông báo lỗi ở client-side (input:invalid) thì kiểm tra thông báo đó
                WebElement errorMsg = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input:invalid")));

                Assertions.assertTrue(errorMsg.isDisplayed(), "Form should not be submitted for invalid inputs");
            } catch (NoSuchElementException e) {
            }
        } else {
            loginButton.click();
            wait.until(ExpectedConditions.urlContains("/login?error"));
        }
    }
}