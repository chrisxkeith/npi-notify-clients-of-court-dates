// Partially generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrapeDataTest {
  class ClientRowData {
    String clientFirstName = "";
    String clientLastName = "";
    String clientAddress = "";
    String caseNumber = "";
    String courtDate = "";
    String room = "";
    String location = "";
    String landlord = "";
    String trimString(String s) {
      return s.trim().replace(",", "").replace("\n", " ");
    }
    void print() {
      System.out.println(
        this.trimString(caseNumber) + ',' +
        this.trimString(clientLastName) + ',' +
        this.trimString(clientFirstName) + ',' +
        this.trimString(clientAddress) + ',' +
        this.trimString(courtDate) + ',' +
        this.trimString(location) + ',' +
        this.trimString(room) + ',' +
        this.trimString(location));
    }
}
/*  private void doSleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      // TO DO Auto-generated catch block
      e.printStackTrace();
    }
  } */
  void fillInCourtInfo(ClientRowData crd) {
    if (!crd.caseNumber.isEmpty()) {
      driver.findElement(By.linkText(crd.caseNumber)).click();
      try {
        driver.switchTo().frame(1);
        crd.courtDate = driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(2)")).getText();
        crd.room = driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(3)")).getText();
        crd.location = driver.findElement(By.cssSelector("a:nth-child(5) td:nth-child(4)")).getText();
        crd.print();
        this.caseCount++;
      } catch(Throwable e) {
        if (this.debug) {
          System.out.println("crd.caseNumber exception: " + e.toString());
        }
        // Assume no (or incomplete) case event history. Continue to the next row in the case list.
      }              
      this.js.executeScript("window.history.go(-1)");
    }
  }
  void fillInDefendantInfo(Integer rowNumber) {
    ClientRowData crd = new ClientRowData();      
    final String NAME_CORPORATION_COLUMN = "2";
    final String defendant_name = driver.findElement(By.cssSelector(
            "tr:nth-child(" + rowNumber.toString() + ") > td:nth-child(" + NAME_CORPORATION_COLUMN + ")")).getText();
    if (defendant_name.contains(",")) {
      String[] names = defendant_name.split(","); // last, first
      crd.clientLastName = names[0];
      crd.clientFirstName = names[1];
    } else {
      crd.clientLastName = defendant_name;
    }
    final String ADDRESS_COLUMN = "3";
    final String cellText = driver.findElement(By.cssSelector(
            "tr:nth-child(" + rowNumber.toString() + ") > td:nth-child(" + ADDRESS_COLUMN + ")")).getText();    
    String[] chunks = cellText.split("Case:");
    crd.clientAddress = chunks[0];
    Pattern pattern = Pattern.compile("\\d\\d\\d\\d\\d\\d\\d", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(cellText);
    if (matcher.find()) {
      crd.caseNumber = matcher.group();
    }
    this.fillInCourtInfo(crd);
    if (this.debug) {
      crd.print();
    }
  }
  void scrapeOnePage() {
    boolean switchFrame = true;
    while (true) {
      for (Integer rowNumber = 2; rowNumber < 22; rowNumber++) {
        try {
          if (switchFrame) {
            driver.switchTo().frame(1);
          } else {
            switchFrame = true;
          }
          // Firefox fails here with "NoSuchWindowError: Browsing context has been discarded"
          final String PARTY_TYPE_COLUMN = "4";
          final String party_type = driver.findElement(By.cssSelector("tr:nth-child(" + rowNumber.toString() + ") > td:nth-child(" + PARTY_TYPE_COLUMN + ")")).getText();
          if (party_type.equals("DEFENDANT")) {
            fillInDefendantInfo(rowNumber);
          } else {
            switchFrame = false;
          }
        } catch(Throwable t) {
          // Assume we ran out of rows on this page.
          return;
        }
      }
      try {
        driver.findElement(By.linkText("Next->")).click();
      } catch(Throwable t) {
          // Assume we ran out of clients beginning with 'firstLetter'.
          return;
      }
      switchFrame = false;
    }
  }
  private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY");
  String getFirstDate() {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);
    return df.format(c.getTime());
  }
  String getLastDate() {
    return df.format(Calendar.getInstance().getTime());
  }
  void scrapeByFirstLetter(String firstLetter) {
    driver.get("https://gscivildata.shelbycountytn.gov/pls/gnweb/ck_public_qry_cpty.cp_personcase_setup_idx");
    driver.switchTo().frame(1);
    driver.findElement(By.name("partial_ind")).click();
    driver.findElement(By.name("last_name")).click();
    driver.findElement(By.name("last_name")).sendKeys(firstLetter);
    driver.findElement(By.name("begin_date")).click();
    driver.findElement(By.name("begin_date")).sendKeys(this.getFirstDate());
    driver.findElement(By.cssSelector("tr:nth-child(7) > td:nth-child(1)")).click();
    driver.findElement(By.name("end_date")).click();
    driver.findElement(By.name("end_date")).sendKeys(this.getLastDate());
    driver.findElement(By.name("case_type")).click();

    driver.findElement(By.cssSelector("option:nth-child(17)")).click();
    driver.findElement(By.cssSelector("input:nth-child(4)")).click();
    this.scrapeOnePage();
  }
  private boolean debug = false;
  private int caseCount = 0;
  private WebDriver driver;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void scrapeData() {
    Instant start = Instant.now();
    for (char c = 'A'; c <= 'Z'; c++) {
      this.scrapeByFirstLetter(Character.toString(c));
    }
    if (debug) {
      Long timeElapsed = Duration.between(start, Instant.now()).toMillis() / 1000;
      System.out.println("caseCount: " + caseCount);
      System.out.println("duration in seconds: " + timeElapsed.toString());
    }
  }
}
