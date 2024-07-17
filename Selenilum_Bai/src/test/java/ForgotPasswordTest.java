import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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

public class ForgotPasswordTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Thiết lập WebDriver (giả sử bạn sử dụng ChromeDriver)
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "duclkde170045@fpt.edu.vn",     // Test với email đã đăng ký
            "unregistered@example.com",   // Test với email chưa đăng ký
            "invalid-email"               // Test với email không đúng định dạng
    })
    public void testForgotPassword(String email) {
        driver.get("http://localhost:8080/login");

        // Nhấp vào liên kết "Forgot Password"
        WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot Password")));
        forgotPasswordLink.click();

        // Kiểm tra xem điều hướng đến trang quên mật khẩu
        wait.until(ExpectedConditions.urlContains("/forgotPassword"));

        // Nhập email
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        emailField.clear();
        emailField.sendKeys(email);

        // Nhấn nút "Reset Password"
        WebElement resetPasswordButton = driver.findElement(By.cssSelector("button[type='submit']"));
        resetPasswordButton.click();

        if (email.equals("duclkde170045@fpt.edu.vn")) {
            // Đợi và kiểm tra xem có điều hướng đến trang nhập OTP
            wait.until(ExpectedConditions.urlContains("/sendEmail"));
        } else if (email.equals("unregistered@example.com")) {
            // Đợi và kiểm tra xem có thông báo lỗi không tìm thấy email không
            WebElement errorMsg = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".text-danger")));
            Assertions.assertTrue(errorMsg.isDisplayed(), "Error message should be displayed for unregistered email");
        } else {
            // Đợi và kiểm tra xem có thông báo lỗi định dạng email không
            try {
                WebElement invalidEmailMsg = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input:invalid")));
                Assertions.assertTrue(invalidEmailMsg.isDisplayed(), "Error message should be displayed for invalid email format");
            } catch (NoSuchElementException e) {
                // Nếu không có thông báo lỗi, kiểm tra xem form có được submit không
                WebElement form = driver.findElement(By.id("forgotPassword"));
                Assertions.assertTrue(form.isDisplayed(), "Form should not be submitted for invalid email format");
            }
        }
    }
}
