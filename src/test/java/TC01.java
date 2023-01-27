import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TC01 {

  WebDriver driver;

  @BeforeTest
  public void setupBrowser() {
    // It will launch and close the browser after execution.
    driver = WebDriverManager.chromedriver().create();
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void validateMovieDetails() throws Exception {
    String movieToSearch = "Pushpa: The Rise";
    String imdbReleaseDate = "";
    String imdbOriginCountry = "";
    String wikiReleaseDate = "";
    String wikiOriginCountry = "";

    // Getting Wiki's details.
    driver.get("https://en.wikipedia.org/wiki/Wiki");
    WebElement wikiSearchBar = driver.findElement(By.xpath("//input[@name='search']"));
    locateAndHighLightElement(driver, wikiSearchBar);
    wikiSearchBar.sendKeys(movieToSearch);
    WebElement wikiSearchBtn = driver.findElement(By.xpath("//form[@id='searchform']/div/button"));
    locateAndHighLightElement(driver, wikiSearchBtn);
    wikiSearchBtn.click();
    WebElement wikiReleaseDateElement = driver.findElement(By.xpath("//div[text()='Release date']//following::li[1]"));
    locateAndHighLightElement(driver, wikiReleaseDateElement);
    wikiReleaseDate = wikiReleaseDateElement.getText();
    WebElement wikiOriginCountryElement = driver.findElement(By.xpath("//th[text()='Country']/following-sibling::td"));
    locateAndHighLightElement(driver, wikiOriginCountryElement);
    wikiOriginCountry = wikiOriginCountryElement.getText();

    // Getting IMDB details.
    driver.get("https://www.imdb.com");
    WebElement imdbSearchBar = driver.findElement(By.xpath("//input[@name='q']"));
    locateAndHighLightElement(driver, imdbSearchBar);
    imdbSearchBar.sendKeys(movieToSearch );
    WebElement imdbSearchBtn = driver.findElement(By.xpath("//button[@id='suggestion-search-button']"));
    locateAndHighLightElement(driver, imdbSearchBtn);
    imdbSearchBtn.click();
    WebElement imdbMovieLink = driver.findElement(By.xpath("(//*[contains(text(), '"+ movieToSearch +"')])[1]"));
    locateAndHighLightElement(driver, imdbMovieLink);
    imdbMovieLink.click();
    WebElement imdbReleaseDateElement = driver.findElement(By.xpath("//*[text()='Release date']//following::ul[1]/li/a"));
    locateAndHighLightElement(driver, imdbReleaseDateElement);
    imdbReleaseDate = imdbReleaseDateElement.getText();
    WebElement imdbOriginCountryElement = driver.findElement(By.xpath("//*[text()='Country of origin']//following::ul[1]/li/a"));
    locateAndHighLightElement(driver, imdbOriginCountryElement);
    imdbOriginCountry = imdbOriginCountryElement.getText();

    // Asserting both details.
    Assert.assertEquals(imdbOriginCountry.trim(), wikiOriginCountry.trim());
    String[] strArr = imdbReleaseDate.split(" ");
    String[] strArr2 = strArr[1].split("\\,");
    imdbReleaseDate = strArr2[0] + strArr[0] + strArr[2];
    Assert.assertEquals(imdbReleaseDate.trim(), wikiReleaseDate.trim().replace(" ", ""));
  }

  public static void locateAndHighLightElement(WebDriver driver, WebElement e) throws Exception {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
    Thread.sleep(1000);
    ((JavascriptExecutor) driver).executeScript("arguments[0].style.background='yellow';", e);
  }
}
