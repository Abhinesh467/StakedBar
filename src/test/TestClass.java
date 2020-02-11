package test;

import java.awt.Robot;
import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestClass {

	public static WebDriver driver;
	public static WiniumDriverService service;
	public static DesktopOptions options;
	public static void main(String[] args) throws Exception{
		driverInvoke();
		firstTest();
		stopServices();
	}


	@BeforeClass
	public static void driverInvoke() throws Exception{
		options = new DesktopOptions();
		options.setApplicationPath("C:\\Program Files (x86)\\SAP\\FrontEnd\\SapGui\\saplogon.exe");

		File driverPath = new File(System.getProperty("user.dir")+"\\Winium.Desktop.Driver.exe");
		WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(driverPath).usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
		try {				
			service.start();
		} catch (IOException e) {
			System.out.println("Exception while starting WINIUM service");
			e.printStackTrace();
		}
		driver = new WiniumDriver(service,options);
		Thread.sleep(5000);
	}

	@Test
	public static void firstTest() throws Exception{
		System.out.println("First Test");
		System.setProperty("webdriver.gecko.driver","C:\\SAP Automation\\SAPTransport\\libs\\geckodriver.exe");
		Actions actions = new Actions(driver);	
		actions.doubleClick(driver.findElement(By.name("ND5"))).build().perform();		
		Thread.sleep(2000);
		driver.findElement(By.id("100")).sendKeys("AJAY");
		Robot r = new Robot();
		r.keyPress(java.awt.event.KeyEvent.VK_DOWN);
		r.keyRelease(java.awt.event.KeyEvent.VK_DOWN);
		driver.findElement(By.id("100")).sendKeys("Tomala@2020");
		r.keyPress(java.awt.event.KeyEvent.VK_ENTER);
		r.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
	}

	@AfterClass
	public static void stopServices() throws Exception{
		try {
			service.stop();
		} catch (Exception e) {
			System.out.println("Exception while stoping WINIUM service");
			e.printStackTrace();
		}

	}
}
