package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.waitAtMost;
import static org.awaitility.Durations.ONE_MINUTE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {
    private final static String firstName = "checkFirstName";
    private final static String lastName = "checkLastName";
    private final static String userName = "root";
    private final static String password = "password";
    private final static String noteTitle = "Super test title";
    private final static String noteDescription = "Super test description";
    private final static String credURL = "example.com";

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(2)
    void getSignupPage() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    @Order(3)
    void getUnauthorizedHomePage() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(4)
    void getUnauthorizedResultPage() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get("http://localhost:" + this.port + "/result");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(5)
    void newUserAccessTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        // signup
        driver.get("http://localhost:" + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(firstName);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(lastName);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();

        //login
        driver.get("http://localhost:" + this.port + "/login");
        inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        //logout
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        Assertions.assertEquals("Login", driver.getTitle());

        //Try accessing homepage
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(6)
    void noteCreationTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        //added note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newNote = driver.findElement(By.id("newnote"));
        wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.sendKeys(noteDescription);
        WebElement savechanges = driver.findElement(By.id("save-changes"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        //check for note
        driver.get("http://localhost:" + this.port + "/home");
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
        boolean created = false;
        for (WebElement element : notesList) {
            if (element.getAttribute("innerHTML").equals(noteTitle)) {
                created = true;
                break;
            }
        }
        Assertions.assertTrue(created);
    }

    @Test
    @Order(7)
    void noteUpdatingTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String newNoteTitle = "new note title";
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        //update note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (WebElement element : notesList) {
            editElement = element.findElement(By.name("edit"));
            if (editElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        WebElement notetitle = driver.findElement(By.id("note-title"));
        wait.until(ExpectedConditions.elementToBeClickable(notetitle));
        notetitle.clear();
        notetitle.sendKeys(newNoteTitle);
        WebElement savechanges = driver.findElement(By.id("save-changes"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        //check the updated note
        driver.get("http://localhost:" + this.port + "/home");
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        notesTable = driver.findElement(By.id("userTable"));
        notesList = notesTable.findElements(By.tagName("th"));
        boolean edited = false;
        for (WebElement element : notesList) {
            if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
                edited = true;
                break;
            }
        }
        Assertions.assertTrue(edited);
    }

    @Test
    @Order(8)
    void noteDeletionTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        for (WebElement element : notesList) {
            deleteElement = element.findElement(By.name("delete"));
            if (deleteElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
        Assertions.assertEquals("Result", driver.getTitle());
    }

    @Test
    @Order(9)
    void credentialCreationTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newCred = driver.findElement(By.id("newcred"));
        wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credURL);
        WebElement credUsername = driver.findElement(By.id("credential-username"));
        credUsername.sendKeys(userName);
        WebElement credPassword = driver.findElement(By.id("credential-password"));
        credPassword.sendKeys(password);
        WebElement submit = driver.findElement(By.id("save-credential"));
        submit.click();
        Assertions.assertEquals("Result", driver.getTitle());

        //check for credential
        driver.get("http://localhost:" + this.port + "/home");
        credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        boolean created = false;
        for (WebElement element : credsList) {
            if (element.getAttribute("innerHTML").equals(userName)) {
                created = true;
                break;
            }
        }
        Assertions.assertTrue(created);
    }

    @Test
    @Order(10)
    void credentialUpdatingTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String newCredUsername = "newUser";
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        waitAtMost(ONE_MINUTE).untilAsserted(() -> assertThat(driver.getTitle()).isEqualTo("Home"));

        //update credential
        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (WebElement element : credsList) {
            editElement = element.findElement(By.name("editCred"));
            if (editElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        WebElement credUsername = driver.findElement(By.id("credential-username"));
        wait.until(ExpectedConditions.elementToBeClickable(credUsername));
        credUsername.clear();
        credUsername.sendKeys(newCredUsername);
        WebElement savechanges = driver.findElement(By.id("save-credential"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        //check the updated note
        driver.get("http://localhost:" + this.port + "/home");
        credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        credsTable = driver.findElement(By.id("credentialTable"));
        credsList = credsTable.findElements(By.tagName("td"));
        boolean edited = false;
        for (WebElement element : credsList) {
            if (element.getAttribute("innerHTML").equals(newCredUsername)) {
                edited = true;
                break;
            }
        }
        Assertions.assertTrue(edited);
    }

    @Test
    @Order(11)
    void credentialDeletionTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        //login
        driver.get("http://localhost:" + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(userName);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(password);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        for (WebElement element : credsList) {
            deleteElement = element.findElement(By.name("delete"));
            if (deleteElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
        Assertions.assertEquals("Result", driver.getTitle());
    }
}
