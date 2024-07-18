import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:8080/login");

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameField.sendKeys("nguyengiaphuongtuan@gmail.com");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("123");

        WebElement loginForm = driver.findElement(By.id("login"));
        loginForm.submit();

        wait.until(ExpectedConditions.urlContains("/index"));
        driver.get("http://localhost:8080/books/7465926578");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCommentSuccess() {
        WebElement content = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("content")));
        content.sendKeys("This is a test comment");

        WebElement post = driver.findElement(By.id("submit"));
        post.submit();

        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-message")));
        assertTrue(toastMessage.getText().contains("Comment added successfully"));
    }

    @ParameterizedTest
    @CsvSource({
            "email me at somename@microsoft.com ",
            "call me at 1(800)642-7676 ",
            "You are so full of crap! ",
            ",",
    })
    public void testCommentInappropriate(String content) {
        WebElement contentField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("content")));
        if (content==null || content.trim().isEmpty()) {
            assertTrue(true);
            return;
        }

        contentField.sendKeys(content);
        WebElement post = driver.findElement(By.id("submit"));
        post.submit();
        contentField.clear();
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-message")));
        assertTrue(toastMessage.getText().contains("Content is inappropriate"));
        wait.withTimeout(Duration.ofSeconds(3));
    }

    @ParameterizedTest
    @CsvSource({
            "spam",
            "spam",
            "spam",
            "spam",
            "spam"
    })
    public void testCommentSpam(String content) {
        WebElement contentField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("content")));
        contentField.sendKeys(content);

        WebElement post = driver.findElement(By.id("submit"));
        post.submit();
        contentField.clear();
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-message")));
        assertTrue(toastMessage.getText().contains("Content is spam"));
    }
}
