package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class FIORIAutomation {

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static String parentWinHandle;
	public static ExtentTest test;
	public static ExtentReports report;
	public static Properties prop;


	@BeforeClass
	public static void invokeBrowser() throws Exception{
		initialiseConfig();
		report = new ExtentReports(System.getProperty("user.dir")+"\\FIORIProjectResults.html");		
		System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"\\libs\\geckodriver.exe");
		driver = new FirefoxDriver();		
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 400);
		startTest("Login to WEBIDE Project");
		try {
			driver.get(prop.getProperty("LocalURL"));
			test.log(LogStatus.INFO, "Should Open FIORI PROJECT", "Successfully Opened FIORI PRoject");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='j_username']")));
			WebElement username = driver.findElement(By.xpath("//*[@id='j_username']"));
			WebElement password = driver.findElement(By.xpath("//*[@id='j_password']"));
			WebElement loginButton = driver.findElement(By.xpath("//*[@id='logOnFormSubmit']"));
			try {
				if(username.isDisplayed()&&password.isDisplayed()&&loginButton.isDisplayed()){
					highLighterMethod(driver, username);
					username.sendKeys(prop.getProperty("Username"));
					test.log(LogStatus.PASS, "Should Enter UserName", "Entered Username successfully");
					highLighterMethod(driver, password);					
					password.sendKeys(prop.getProperty("Password"));
					test.log(LogStatus.PASS, "Should Enter Password", "Entered Password successfully");
					highLighterMethod(driver, loginButton);
					loginButton.click();
					test.log(LogStatus.PASS, "Should click on Login button", "Clicked on Login Button successfully");
				}else{
					test.log(LogStatus.FAIL, "Should display Username, Password and Login Button", "Failed to display Username, Password and Login Button");
				}
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "Should display Username, Password and Login Button", "Failed to display Username, Password and Login Button due to:: "+e);
			}		

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Should Open FIORI PROJECT", "Failed to Opened FIORI PRoject due to:: "+e);
		}	
		report.endTest(test);
	}

	@Test(priority=1)
	public static void validateGraphData() throws Exception{
		parentWinHandle = driver.getWindowHandle();
		wait = new WebDriverWait(driver, 800);	
		startTest("Validate Graph Data");
		JavascriptExecutor js =((JavascriptExecutor)driver);
		js.executeScript("window.scrollBy(0, 200)");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li//span[@class='sapUiTreeNodeContent' and text()='StackedBar']")));
		Thread.sleep(5000);
		WebElement stackedBar = driver.findElement(By.xpath("//li//span[@class='sapUiTreeNodeContent' and text()='StackedBar']"));
		js.executeScript("arguments[0].scrollIntoView(true)", stackedBar);
		highLighterMethod(driver, stackedBar);
		stackedBar.click();		
		test.log(LogStatus.PASS, "Should click on Stacked Bar", "Successfully Clicked on Stacked Bar");
		Thread.sleep(7000);
		WebElement run = driver.findElement(By.xpath("//*[@id='__toolbar1-applicationToolbar-preview.run']"));
		highLighterMethod(driver, run);
		run.click();
		test.log(LogStatus.PASS, "Should click on Execute Graph button", "Successfully Clicked on Graph button");
		Thread.sleep(7000);
		Set<String> winHandles = driver.getWindowHandles();
		for(String handle: winHandles){
			if(!handle.equals(parentWinHandle)){
				driver.switchTo().window(handle);
				Thread.sleep(3000);
				System.out.println(driver.getTitle());
				test.log(LogStatus.PASS, "Should Switch to Graph Window", "Successfully Switched to graph window "+driver.getTitle());
			}
		}
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='ui5-viz-controls-viz-frame']")));
		Thread.sleep(3000);		

		int graphCount = driver.findElements(By.xpath("//*[name()='g' and @class='v-datapoint v-morphable-datapoint v-datapoint-default']")).size();
		for (int i = 1; i<=graphCount; i++) {
			WebElement graph1 = driver.findElement(By.xpath("(//*[name()='g' and @class='v-datapoint v-morphable-datapoint v-datapoint-default'])["+i+"]"));		
			Actions builder = new Actions(driver);
			builder.click(graph1).build().perform();
			Thread.sleep(1000);
			WebElement Year = driver.findElement(By.xpath("//*[@class='v-tooltip-dimension-measure']/tr[1]/td[2]"));			
			highLighterMethod(driver, Year);
			String year = Year.getText();
			WebElement Item = driver.findElement(By.xpath("//*[@class='v-tooltip-dimension-measure']/tr[2]/td[1]"));
			WebElement ItemData = driver.findElement(By.xpath("//*[@class='v-tooltip-dimension-measure']/tr[2]/td[2]"));
			highLighterMethod(driver, Item);
			highLighterMethod(driver, ItemData);
			String item = Item.getText();
			String itemData = ItemData.getText();
			if(prop.getProperty("Year").contains(year)&&prop.getProperty("Itemvalues").contains(itemData)){
				test.log(LogStatus.PASS, "DEV Year =" +year, "Captured Year = "+year);
				test.log(LogStatus.PASS, "Dev data = "+itemData, "Expected "+item+" Value = "+itemData);
			}else{
				test.log(LogStatus.FAIL, "DEV Year =" +year, "Captured Year = "+year);
				test.log(LogStatus.FAIL, "Dev data = "+itemData, "Expected "+item+" Value = "+itemData);
			}
			System.out.println(year);
			System.out.println(itemData);
			WebElement graphTitle = driver.findElement(By.xpath("//*[name()='g']/following::*[text()='Resource Analysis']"));
			builder.click(graphTitle).build().perform();
			Thread.sleep(1000);
		}	
		driver.close();
		test.log(LogStatus.INFO, "Should close Graph window", "Closed Graph window successfuly");
		Thread.sleep(3000);
		driver.switchTo().window(parentWinHandle);		
		Thread.sleep(3000);
		test.log(LogStatus.PASS, "Should Switch to Parent Window", "Successfully Switched to Parent window "+driver.getTitle());
		report.endTest(test);
	}


	public static void highLighterMethod(WebDriver driver, WebElement element) throws Exception{
		Thread.sleep(500);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", element);
	}

	public static void startTest(String description){
		test = report.startTest(description);
	}

	public static void initialiseConfig(){
		File file = new File(System.getProperty("user.dir")+"\\FIORIConfig.properties");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		prop = new Properties();
		prop.getProperty(",","");
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void closeInstance(){
		startTest("Close the Instance");
		if(driver!=null){
			driver.quit();
			test.log(LogStatus.PASS, "Should close browser Instance", "Closed browser instance successfuly");
		}else{
			test.log(LogStatus.FAIL, "Should close browser Instance", "Failed to close browser instance");
		}
		report.endTest(test);
		report.flush();

	}
}
