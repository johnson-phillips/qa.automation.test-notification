package qa.automation.notification;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;

/**
 * Created by johnson_phillips on 8/17/17.
 */
public class NotificationData {

    public	String	build_number;
    public String job_name;
    public String Environment;
    public String ProductID;
    public String test_phase;
    public String status;
    public String test_name;
    public String test_description;
    public String test_method_name;
    public String test_class_name;
    public String event_time;
    public Throwable test_results;
    public String id;
    public String event_type = "TestNotification";
    public HashMap<String,Object> annotations = new HashMap<String, Object>();


    public NotificationData(ITestResult iTestResult) throws Exception
    {
        Annotation annotation = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
        for(Method m:annotation.getClass().getDeclaredMethods())
        {
            if(m.getParameterCount() == 0) {
                Object o = m.invoke(annotation);
                annotations.put(m.getName(),o);
            }
        }
        test_description = System.getProperty("suiteid");
        build_number = System.getProperty("BUILD_NUMBER");
        job_name = System.getProperty("JOB_BASE_NAME");
        ProductID = System.getProperty("ProductID");
        Environment = System.getProperty("Environment");
        event_time = Instant.now().toString();
        test_phase = (GetStatus(iTestResult).equals("STARTED"))?GetStatus(iTestResult):"FINALIZED";
        status = GetStatus(iTestResult);
        test_name = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName();
        //test_description = iTestResult.getMethod().getDescription();
        test_method_name = iTestResult.getName();
        test_class_name = iTestResult.getTestClass().getName();
    }

    public String GetStatus(ITestResult iTestResult) throws Exception
    {
        for(Field f:ITestResult.class.getDeclaredFields())
        {
            if(f.get(iTestResult).equals(iTestResult.getStatus()))
            {
                return f.getName();
            }
        }

        return "";
    }


}
