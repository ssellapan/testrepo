/*
 *  (c) Polopoly AB (publ).
 *  This software is protected by copyright law and international copyright
 *  treaties as well as other intellectual property laws and treaties.
 *  All title and rights in and to this software and any copies thereof
 *  are the sole property of Polopoly AB (publ).
 *  Polopoly is a registered trademark of Polopoly AB (publ).
 */

package com.atex.plugins.helloworld.pagelayout;

import static junit.framework.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.atex.plugins.test.GuiDriver;

public class HelloWorldPageLayoutIT {

    protected WebDriver webDriver;
    protected GuiDriver robot;

    private static Logger LOG = Logger.getLogger(HelloWorldPageLayoutIT.class.getName());

    @Before
    public void login() {
        String port = System.getProperty("jetty.port", "8080");
        webDriver = GuiDriver.getHeadlessWebDriver();
        robot = new GuiDriver(webDriver, "http://localhost:" + port);

        // login to polopoly
        assertTrue("Failed to login", robot.login());
        robot.unlockAllContents();
    }

    @Test(timeout = 300000)
    public void helloworldPageLayoutPluginIsInstalled() {
        // search helloworld page layout plugin
        robot.search("com.atex.plugins.helloworld.pagelayout.HelloWorldPageLayout").switchToWork();
        String xpathExpName = "//fieldset[@class='field']/input";
        String valueName = "Hello World Page Layout";
        assertTrue("Cant find template name via text box", isElementPresent(xpathExpName, valueName));
        robot.logout();
    }

    private boolean isElementPresent(String xpathExpression, String value) {
        WebElement element = webDriver.findElement(By.xpath(xpathExpression));
        if (value.equals(element.getAttribute("value"))) {
            return true;
        }
        return false;
    }

  @Test
  public void testCreateSiteWithHelloWorldPageLayout() {
      robot
          .search("p.siteengine.Sites.d")
          .switchToWork();
      robot
          .click("//fieldset[@class='contentCreator field structureContentCreator']//button")
          
          .type("//fieldset[@class='field name']/input", "Blank Hello World Site")
          .select("//div[@class='tabbedPaneBox editorarea']/div[@class='ajax_placeHolder']/div/select", "Hello World Page Layout")
          .openTab("Advanced")
          .type("//fieldset[@class='field id']/input", "blank-hello-world-site.d")
          .clickInsert()
          .clickSaveAndView();
      robot.logout();
      robot.get("/blank-hello-world-site.d")
      .waitForElement("//div/div[@class='borderLayoutColumn mainColumn']/div[@class='pageLayoutTitle']");
      WebElement element = webDriver.findElement(
              By.xpath("//div/div[@class='borderLayoutColumn mainColumn']/div[@class='pageLayoutTitle']"));
      assertTrue("Hello World".equals(element.getText()));
  }

    @After
    public void after() {
        webDriver.quit();
    }
}
