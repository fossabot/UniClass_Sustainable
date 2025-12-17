package it.unisa.uniclass.testing.integration;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.util.*;

public class LoginTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void tC1ValidLogin() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacomoporetti@unisa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2222WxY$"); // ggignore
  }
  @Test
  public void tC2NotPresentPassword() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacomoporetti@unisa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2222WxY$p"); // ggignore
  }
  @Test
  public void tC3NotValidPassword() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacomoporetti@unisa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2222WxYS"); // ggignore
    driver.findElement(By.cssSelector(".logreg")).click();
  }
  @Test
  public void tC4NotPresentEmail() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacomporetti@unisa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2222WxY$"); // ggignore
    driver.findElement(By.cssSelector(".logreg")).click();
  }
  @Test
  public void tC5formatEmailOnlyValid() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacomo@unisa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2212"); // ggignore
    driver.findElement(By.cssSelector(".logreg")).click();
  }
  @Test
  public void tC6formatPasswordOnlyValid() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacunsa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("2212XyA$"); // ggignore
    driver.findElement(By.cssSelector(".logreg")).click();
  }
  @Test
  public void tC7NothingValid() {
    driver.get("http://localhost:8080/UniClass-Dependability/Home");
    driver.findElement(By.cssSelector("span > a > img")).click();
    driver.findElement(By.id("email")).click();
    driver.findElement(By.id("email")).sendKeys("giacunsa.it");
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).click(); // ggignore
    driver.findElement(By.id("password")).sendKeys("221"); // ggignore
    driver.findElement(By.cssSelector(".logreg")).click();
  }
}
