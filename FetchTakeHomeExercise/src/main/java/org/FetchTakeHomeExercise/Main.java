package org.FetchTakeHomeExercise;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static int count = 0;
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("http://sdetchallenge.fetch.com/");

        //Initialize first weighing
        int result=findFakeBar(driver, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7)));
        driver.findElement(By.id("coin_" + result)).click();

        //Output results to console
        Alert alert = driver.switchTo().alert();
        System.out.println("Alert Message: " + alert.getText());
        alert.dismiss();
        System.out.println("Found the fake bar: " + result);
        List<WebElement> steps = driver.findElements(By.tagName("li"));
        System.out.println("Steps taken to find Bar: " + count);
        for (WebElement step:steps) {
            System.out.println(step.getText());
        }

        driver.quit();
    }

    public static int findFakeBar(WebDriver driver, ArrayList<Integer> bars){

        if (bars.size() == 1){
            return bars.get(0);
        }else{
            ArrayList<Integer> leftHalf = new ArrayList<Integer>();
            ArrayList<Integer> rightHalf = new ArrayList<Integer>();

            for(int i = 0; i < bars.size(); i++){
                leftHalf.add(bars.get(i++));
                rightHalf.add(bars.get(i));
            }
            driver.findElement(By.xpath("//button[contains(text(), 'Reset')]")).click();

            for(int i = 0; i < leftHalf.size(); i++){
                driver.findElement(By.id("left_" + i)).sendKeys(leftHalf.get(i).toString());
                driver.findElement(By.id("right_" + i)).sendKeys(rightHalf.get(i).toString());
            }
            switch(getWeightResults(driver)){
                case "<":
                    bars = leftHalf;
                    break;
                case ">":
                    bars = rightHalf;
                    break;
                default:
                    bars = new ArrayList<Integer>();
                    bars.add(8);
                    break;
            }
            return findFakeBar(driver, bars);
        }
    }

    public static String getWeightResults(WebDriver driver){
        driver.findElement(By.id("weigh")).click();

        count++;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.numberOfElementsToBe(By.tagName("li"), count));

        WebElement resultButton = driver.findElement(By.xpath("//*[@class='result']/button"));
        return resultButton.getText();
    }
}