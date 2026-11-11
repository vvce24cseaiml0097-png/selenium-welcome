package com.example;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WelcomePageTest {

    private static HttpServer server;
    private static String baseUrl;
    private WebDriver driver;

    @BeforeAll
    static void startServer() throws Exception {
        server = WelcomeServer.start(0); // random free port
        baseUrl = "http://localhost:" + server.getAddress().getPort() + "/";
    }

    @AfterAll
    static void stopServer() {
        server.stop(0);
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1280,800");

        // Used only inside Docker (environment variables are set in the Dockerfile)
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.isBlank()) {
            options.setBinary(chromeBin);
        }
        String driverPath = System.getenv("CHROMEDRIVER_PATH");
        if (driverPath != null && !driverPath.isBlank()) {
            driver = new ChromeDriver(
                new ChromeDriverService.Builder().usingDriverExecutable(new File(driverPath)).build(), options);
        } else {
            driver = new ChromeDriver(options); // locally, Selenium Manager downloads the driver
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void welcomePageHasExpectedTitleAndHeading() {
        driver.get(baseUrl);
        assertEquals("Welcome Page", driver.getTitle());
        assertEquals("Welcome to Selenium Project", driver.findElement(By.id("welcome-heading")).getText());
    }

    @Test
    void clickingButtonShowsGreeting() {
        driver.get(baseUrl);
        driver.findElement(By.id("greet-btn")).click();
        assertEquals("Hello, Tester!", driver.findElement(By.id("message")).getText());
    }
}