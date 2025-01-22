package mavenPackage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TeslaStockTest2 {

	public static WebElement waitForElement(WebDriver driver, By by, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
	
    public static void main(String[] args) {
    
        // Initialize the WebDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\arunk\\Downloads\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Set implicit wait for elements

		try {
            //Step 1: Open Yahoo Finance Homepage
            driver.get("https://finance.yahoo.com/");
            
            driver.manage().window().maximize(); //Maximize window

            //Step 2: Search for 'TSLA'
            WebElement searchBox = driver.findElement(By.id("ybar-sbq"));
            searchBox.sendKeys("TSLA");

            // Wait for the autosuggest to appear and verify the first entry
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement firstSuggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@data-id='result-quotes-0'][@title='Tesla, Inc.']")));

            if (firstSuggestion.getAttribute("title").equals("Tesla, Inc."))
            {
                System.out.println("First autosuggested entry is correct: Tesla, Inc.");
            } else {
                System.out.println("First autosuggested entry is incorrect!");
                driver.quit();
                return;
            }

            //Step 3: Click on the first entry
            firstSuggestion.click();

            //Step 4: Verify stock price is > $200
            WebElement stockPriceElement = waitForElement(driver, By.xpath("//span[@data-testid='qsp-price']"), 10);
            String stockPriceText = stockPriceElement.getText().replace(",", "").replace("$", "");
            double stockPrice = Double.parseDouble(stockPriceText);

            if (stockPrice > 200) {
                System.out.println("Stock price is greater than $200: " + stockPrice);
            } else {
                System.out.println("Stock price is less than or equal to $200: " + stockPrice);
                driver.quit();
                return;
            }

            //Step 5: Capture Previous Close and Volume details
            WebElement previousCloseElement = driver.findElement(By.xpath("//*[@data-field='regularMarketPreviousClose']"));
            WebElement volumeElement = driver.findElement(By.xpath("//*[@data-field='regularMarketVolume']"));

            String previousClose = previousCloseElement.getText();
            String volume = volumeElement.getText();

            System.out.println("Previous Close: " + previousClose);
            System.out.println("Volume: " + volume);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); //Close Browser
        }
    }
}