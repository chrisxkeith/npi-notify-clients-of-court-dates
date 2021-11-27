// Partially generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.util.*;
public class ScrapeDataTest {
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
  public void scrapeData() {
    driver.get("https://gscivildata.shelbycountytn.gov/pls/gnweb/ck_public_qry_cpty.cp_personcase_setup_idx");
    driver.switchTo().frame(1);
    driver.findElement(By.name("partial_ind")).click();
    driver.findElement(By.name("last_name")).click();
    driver.findElement(By.name("last_name")).sendKeys("A");
    driver.findElement(By.name("begin_date")).click();
    driver.findElement(By.name("begin_date")).sendKeys("01-NOV-2021");
    driver.findElement(By.cssSelector("tr:nth-child(7) > td:nth-child(1)")).click();
    driver.findElement(By.name("end_date")).click();
    driver.findElement(By.name("end_date")).sendKeys("14-NOV-2021");
    driver.findElement(By.name("case_type")).click();
    {
      WebElement dropdown = driver.findElement(By.name("case_type"));
      dropdown.findElement(By.xpath("//option[. = '16 - FED - OTHER']")).click();
    }
    driver.findElement(By.cssSelector("input:nth-child(4)")).click();
    vars.put("ID", driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(1)")).getText());
    driver.findElement(By.linkText("2109558")).click();
    vars.put("courtDate", driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(2)")).getText());
    vars.put("room", driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(3)")).getText());
    vars.put("location", driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(4)")).getText());
  }
}
