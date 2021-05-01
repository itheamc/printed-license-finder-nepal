package com.itheamc.licensefinder.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.licensefinder.R;
import com.itheamc.licensefinder.databinding.FragmentLicenseBinding;
import com.itheamc.licensefinder.models.Datas;
import com.itheamc.licensefinder.models.License;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;


public class LicenseFragment extends Fragment {
    private static final String TAG = "LicenseFragment";
    private FragmentLicenseBinding licenseBinding;
    private ExecutorService executorService;
    private Handler handler;
    private NavController navController;

    public LicenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        licenseBinding = FragmentLicenseBinding.inflate(inflater, container, false);
        return licenseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        executorService = Executors.newFixedThreadPool(4);
        handler = HandlerCompat.createAsync(Looper.getMainLooper());

        licenseBinding.progressBar.setVisibility(View.VISIBLE);
        fetchUserData("01-10-00648118", "", "2051-10-02");
    }

    private void fetchUserData(String lcn, String rfn, String dob) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Cloud Testing
                DesiredCapabilities capabilities = new DesiredCapabilities();
//                capabilities.setCapability("platform", "Windows 10");
//                capabilities.setCapability("browserName", "Chrome");
//                capabilities.setCapability("version", "90.0"); // If this cap isn't specified, it will just get the any available one
//                capabilities.setCapability("resolution","1280x800");

                // For MacOs
                capabilities.setCapability("platform", "MacOS Big sur");
                capabilities.setCapability("browserName", "Firefox");
                capabilities.setCapability("version", "87.0"); // If this cap isn't specified, it will just get the any available one
                capabilities.setCapability("resolution","1024x768");
                capabilities.setCapability("build", "First Test");
                capabilities.setCapability("name", "Sample Test");
                capabilities.setCapability("network", true); // To enable network logs
                capabilities.setCapability("visual", true); // To enable step by step screenshot
                capabilities.setCapability("video", true); // To enable video recording
                capabilities.setCapability("console", true); // To capture console logs

                try {
                    @SuppressLint("AuthLeak") WebDriver driver= new RemoteWebDriver(new URL("https://itheamc:XeDBYstR8ZW8fRff5bRjV8iEDhxbSTu1xJyPo1J5mchgRyWtcH@hub.lambdatest.com/wd/hub"), capabilities);

                    // Creating the javascript executor to play with the elements attributes
                    JavascriptExecutor js = (JavascriptExecutor) driver;

                    // Creating the instance of the WebDriveWait object
                    WebDriverWait wait = new WebDriverWait(driver, 10);

                    // Calling driver to visit on the target website
                    driver.get("https://onlineedlreg.dotm.gov.np/checkStatus");

                    //Web Element
                    WebElement referenceNumberRadioBtn = wait.until(presenceOfElementLocated(By.id("appEntry_appStatusref")));
                    WebElement licenseNumberRadioBtn = wait.until(presenceOfElementLocated(By.id("appEntry_appStatuslic")));
                    WebElement textBox = null;

                    // Selecting the radio button as per user inputs
                    if (!lcn.isEmpty()) {
                        licenseNumberRadioBtn.click();
                        Thread.sleep(1000);
                        textBox = wait.until(presenceOfElementLocated(By.cssSelector("input[name='licenseNo']")));
                        textBox.clear();
                        textBox.sendKeys(lcn);
                    } else if (!rfn.isEmpty()) {
                        referenceNumberRadioBtn.click();
                        Thread.sleep(1000);
                        textBox = wait.until(presenceOfElementLocated(By.cssSelector("input[name='referenceNo']")));
                        textBox.clear();
                        textBox.sendKeys(rfn);
                    } else {
                        driver.close();
                        throw new Exception("Something Went Wrong With The Provided Details");
                    }

                    // Targeting the date of birth input text
                    WebElement dateOfBirthBox = wait.until(presenceOfElementLocated(By.id("imagePicker")));
                    js.executeScript("document.getElementById('imagePicker').removeAttribute('readonly')");
                    Thread.sleep(1000);
                    dateOfBirthBox.clear();
                    dateOfBirthBox.sendKeys(dob);

                    // Target the calendar image
                    WebElement calendarIcon = wait.until(elementToBeClickable(By.cssSelector("img[alt='Popup']")));
                    calendarIcon.click();

                    // Targeting the bs date table
                    List<WebElement> tables = wait.until(presenceOfAllElementsLocatedBy(By.tagName("table")));

                    // Targeting the all day of the month
                    List<WebElement> tableData = tables.get(tables.size() - 1).findElements(By.tagName("td"));

                    // comparing the date of birth day with the day and targeting the anchor tag to click on it
                    // It will change the ad date as per bs
                    Thread.sleep(500);
                    for (WebElement we: tableData) {
                        if (!we.getText().trim().isEmpty() && Integer.parseInt(dob.substring(8)) == Integer.parseInt(we.getText())) {
                            we.findElement(By.tagName("a")).click();
                            Thread.sleep(1000);
                            break;
                        }
                    }

                    // Finally clicking on the check status button
                    WebElement checkStatusBtn = wait.until(elementToBeClickable(By.id("appEntry_search")));
                    checkStatusBtn.click();

                    // Finally clicking on the select button
                    Thread.sleep(1000);
                    WebElement selectButton = wait.until(elementToBeClickable(By.cssSelector("input[name='select']")));
                    selectButton.click();


                    // Creating the new Datas object to store users information
                    List<Datas> userData = new ArrayList<>();

                    // First getting image url
                    Thread.sleep(1000);
                    WebElement userImage = wait.until(presenceOfElementLocated(By.cssSelector("td[rowspan='5']"))).findElement(By.tagName("img"));
                    String imageUrl = "https://onlineedlreg.dotm.gov.np/" + userImage.getAttribute("src");

                    // Removing the image td from the table
                    js.executeScript("document.querySelector(\"td[rowspan='5']\").remove()");

                    // Playing with final data
                    Thread.sleep(1000);
                    List<WebElement> dataTables = wait.until(presenceOfAllElementsLocatedBy(By.className("datagrid")));

                    for (int i = 0; i < dataTables.size()-1; i++) {
                        if (i < dataTables.size()-3) {
                            List<WebElement> tds = dataTables.get(i).findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("td"));
                            int j = 0;
                            while (j < tds.size()) {
                                if ((j+1)%2 == 0) {
                                    userData.add(new Datas(
                                            tds.get(j-1).getText(),
                                            tds.get(j).getText()
                                    ));
                                }
                                j++;
                            }
                        } else {
                            if (i == 3) {
                                continue;
                            }
                            List<WebElement> trs = dataTables.get(i).findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
                            List<WebElement> heading = trs.get(0).findElements(By.tagName("th"));
                            List<WebElement> values = trs.get(1).findElements(By.tagName("td"));
                            for (int k = 0; k < heading.size(); k++) {
                                userData.add(new Datas(
                                        heading.get(k).getText(),
                                        values.get(k).getText()
                                ));
                            }

                        }

                    }

                    driver.close();
                    notifySuccess(userData, imageUrl);

                } catch (Exception e) {
                    notifyFailure(e.getMessage());
                }

            }
        });
    }

    // Function to notify success
    private void notifySuccess(List<Datas> userData, String imageUrl) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                License license = new License();
                license.set_image(imageUrl);
                for (int i = 0; i < userData.size(); i++) {
                    if (i == 0) license.set_citizenship_no(userData.get(i).getValue());
                    else if(i == 2) license.set_license_no(userData.get(i).getValue());
                    else if (i == 3) license.set_name(userData.get(i).getValue());
                    else if (i == 6) license.set_dob_ad(convertMonth(userData.get(i).getValue()));
                    else if (i == 7) license.set_blood_group(userData.get(i).getValue());
                    else if (i == 9) license.set_ward_number(userData.get(i).getValue());
                    else if (i == 10) license.set_tole(userData.get(i).getValue());
                    else if (i == 12) license.set_zone(userData.get(i).getValue());
                    else if (i == 13) license.set_district(userData.get(i).getValue());
                    else if (i == 14) license.set_municipality(userData.get(i).getValue());
                    else if (i == 16) license.set_issue_date(convertMonth(userData.get(i).getValue()));
                    else if (i == 17) license.set_expiry_date(convertMonth(userData.get(i).getValue()));
                    else if (i == 18) license.set_category(userData.get(i).getValue().split("-")[0]);
                    else if (i == 19) license.set_issuer(userData.get(i).getValue());
                }

                licenseBinding.progressBar.setVisibility(View.GONE);
                licenseBinding.constraintLayout.setVisibility(View.VISIBLE);
                licenseBinding.setLicense(license);
                Log.d(TAG, "run: " + license.toString());
            }
        });

    }

    // Function to notify failure
    private void notifyFailure(String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                licenseBinding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Unable to get License Info", Toast.LENGTH_LONG).show();
                Log.d(TAG, "run: " + message);
                try {
                    Thread.sleep(1000);
                    navController.navigate(R.id.action_licenseFragment_to_detailsFragment);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    // Function to convert months in number
    private String convertMonth(String date) {
        String [] strings = date.split("-");
        String month = strings[1];
        switch (month) {
            case "JAN":
                return strings[0] + "-01-" + strings[2].substring(0, 2);
            case "FEB":
                return strings[0] + "-02-" + strings[2].substring(0, 2);
            case "MAR":
                return strings[0] + "-03-" + strings[2].substring(0, 2);
            case "APR":
                return strings[0] + "-04-" + strings[2].substring(0, 2);
            case "MAY":
                return strings[0] + "-05-" + strings[2].substring(0, 2);
            case "JUN":
                return strings[0] + "-06-" + strings[2].substring(0, 2);
            case "JUL":
                return strings[0] + "-07-" + strings[2].substring(0, 2);
            case "AUG":
                return strings[0] + "-08-" + strings[2].substring(0, 2);
            case "SEP":
                return strings[0] + "-09-" + strings[2].substring(0, 2);
            case "OCT":
                return strings[0] + "-10-" + strings[2].substring(0, 2);
            case "NOV":
                return strings[0] + "-11-" + strings[2].substring(0, 2);
            case "DEC":
                return strings[0] + "-12-" + strings[2].substring(0, 2);
            default:
                return "0000-00-00";
        }
    }
}