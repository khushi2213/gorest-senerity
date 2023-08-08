package com.gorest.crudtest;

import com.gorest.steps.GoRestSteps;
import com.gorest.testbase.TestBase;
import com.gorest.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class UserCRUDTest extends TestBase {
    static String name = "sita" + TestUtils.getRandomValue();
    static String gender = "female";
    static String email = "sita.ram" + TestUtils.getRandomValue() + "@email.com";
    static String status = "active";
    static int id;
    @Steps
    GoRestSteps goRestSteps;

    @Title("This method will get all users records")
    @Test
    public void test001() {
        goRestSteps.getUserIDs();
    }

    @Title("This method will create a new users record and verify it by its ID")
    @Test
    public void test002() {
        ValidatableResponse getId = goRestSteps.createUserRecord(name, email, gender, status);
        id = getId.extract().path("id");
        ValidatableResponse response = goRestSteps.getUserIDs();
        ArrayList<?> userId = response.extract().path("id");
        Assert.assertTrue(userId.contains(id));
    }

    @Title("This method will update the existing record")
    @Test
    public void test003() {
        status = "inactive";
        goRestSteps.userRecordUpdate(id, name, email, gender, status);
        ValidatableResponse response = goRestSteps.getSingleUser(id).statusCode(200);
        HashMap<String, ?> userRecord = response.extract().path("");
        Assert.assertThat(userRecord, hasValue("inactive"));
    }

    @Title("This method will delete the existing record")
    @Test
    public void test004() {
        goRestSteps.deleteUserRecord(id).statusCode(204);
        goRestSteps.getSingleUser(id).statusCode(404);
    }
}
