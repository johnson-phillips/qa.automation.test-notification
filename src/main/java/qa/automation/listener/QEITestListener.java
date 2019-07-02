package qa.automation.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import qa.automation.core.Api;
import qa.automation.notification.NotificationData;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import java.util.UUID;

/**
 * Created by johnson_phillips on 8/16/17.
 */
public class QEITestListener extends TestListenerAdapter {
    ObjectMapper mapper = new ObjectMapper();
    public static String reportingURL = System.getProperty("testnotificationurl");
    public static boolean sendNotification = Boolean.parseBoolean(System.getProperty("sendNotification"));
    static
    {
        System.setProperty("suiteid", UUID.randomUUID().toString());
    }

    @Override
    public void onStart(ITestContext iTestContext)  {

        try {
            System.out.println("\r\n" + "reportingURL:" + reportingURL + "\r\n");
            System.out.println("\r\n" + "sendNotificationFlag:" + sendNotification + "\r\n");
            System.out.println("\r\n" + "Execution ID:" + System.getProperty("suiteid") + "\r\n");
            System.out.println(iTestContext.getName() + " onStart");
            System.out.println("Total Tests:" + iTestContext.getAllTestMethods().length);
            System.out.println("Total Failed:" + iTestContext.getFailedTests().size());
            System.out.println("Total Pass:" + iTestContext.getPassedTests().size());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        try {
            NotificationData notificationData = new NotificationData(iTestResult);
            if(sendNotification) {
                System.out.print("Test Start Notification Sent" +"\r\n");
                Api.postRequests(reportingURL,mapper.writeValueAsString(notificationData));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void onTestSuccess(ITestResult iTestResult){
        try {
            NotificationData notificationData = new NotificationData(iTestResult);
            if(sendNotification) {
                System.out.print("Test Success Notification Sent" +"\r\n");
                Api.postRequests(reportingURL, mapper.writeValueAsString(notificationData));
            }
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }
    }
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try {
            NotificationData notificationData = new NotificationData(iTestResult);
            notificationData.test_results = iTestResult.getThrowable();
//            System.out.print(mapper.writeValueAsString(notificationData) +"\r\n");
            if(sendNotification) {
                System.out.print("Test Failure Notification Sent" +"\r\n");
                Api.postRequests(reportingURL, mapper.writeValueAsString(notificationData));
            }
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }

    }
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        try {
            NotificationData notificationData = new NotificationData(iTestResult);
//            System.out.print(mapper.writeValueAsString(notificationData) +"\r\n");
            if(sendNotification) {
                System.out.print("Test Skipped Notification Sent" +"\r\n");
                Api.postRequests(reportingURL, mapper.writeValueAsString(notificationData));
            }
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }
    }

    public void onFinish(ITestContext iTestContext) {
        try {
            System.out.println(iTestContext.getName() + " onFinish");
            System.out.println("Total Tests:" + iTestContext.getAllTestMethods().length);
            System.out.println("Total Failed:" + iTestContext.getFailedTests().size());
            System.out.println("Total Pass:" + iTestContext.getPassedTests().size());
        }
        catch (Exception ex)
        {

        }

    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

        try {
            NotificationData notificationData = new NotificationData(iTestResult);
//            System.out.print(mapper.writeValueAsString(notificationData) +"\r\n");
            if(sendNotification) {
                System.out.print("Test Failed But Within Success Percentage Notification Sent" +"\r\n");
                Api.postRequests(reportingURL, mapper.writeValueAsString(notificationData));
            }
        }
        catch (Exception ex)
        {
            System.out.print(ex.getMessage());
        }

    }
}
