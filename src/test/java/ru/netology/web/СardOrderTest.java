package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class СardOrderTest {
    WebDriver driver;

    @BeforeAll
    static void setUp1() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp2() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldCheckCardOrder() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Маслова-Маслова Ольга");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79867281447");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }


    @Test
    void shouldCheckInvalidName() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Maslova Olga4/");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79874563625");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldCheckNameWithoutValue() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79874563625");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldCheckPhoneWithInvalidValue() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Маслова Ольга");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("454545FF");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldCheckPhoneWithoutValue() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Маслова Ольга");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldCheckFormWithoutAgreement() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Маслова Ольга");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79867281447");

        driver.findElement(By.className("checkbox__box"));
        driver.findElement(By.tagName("button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй",
                text.trim());
    }
}