package com.opensymphony.able.service;

import com.opensymphony.able.IntegrationTest;
import com.opensymphony.able.model.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.List;


/**
 * @author Nicholas Hill <a href="mailto:nick.hill@gmail.com">nick.hill@gmail.com</a>
 * @author <a href="mailto:jhouse@revolition.net">James House</a>
 */
public class UserServiceTest extends IntegrationTest {
    @Test
    public void creatingAUserCausesADatabaseRowToBeInserted() {
        int before = getCount("SELECT COUNT(*) FROM users;");
        User user = createDefaultUser();
        int after = getCount("SELECT COUNT(*) FROM users;");

        Assert.assertEquals(user.getId(), 1, "We expected a user ID of 1");
        Assert.assertEquals(after - before, 1);
    }

    @Test
    public void creatingAUserSetsTheIDOnTheUserObject() {
        User user = createDefaultUser();
        Assert.assertEquals(user.getId(), user.getId(), "The id of the user object does not match that returned" +
                "by the create user method.");
    }

    @Test
    public void creatingAUserAndGettingAUserByIdProducesTheSameUsername() {
        createDefaultUser();
        User byId = userService.findById(1);
        Assert.assertEquals(byId.getUsername(), "niko2416");
    }

    @Test
    public void creatingAUserWithANullUsernameCausesAException() {
        try {
            createUser(null, "nick.hill@gmail.com", "Nick Hill", "password");
            Assert.fail("Exception should have been thrown due to null username.");
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void creatingAUserWithANullEmailCausesAException() {
        try {
            createUser("niko2416", null, "Nick Hill", "password");
            Assert.fail("Exception should have been thrown due to null email address.");
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void creatingAUserWithAUsernameThatAlreadyExistsCausesAException() {
        createUser("niko2416", "nick.hill@gmail.com", "Nick Hill", "password");
        try {
            createUser("niko2416", "plightbo@gmail.com", "Patrick Lightbody", "password");
            Assert.fail("Exception should have been thrown due to duplicate usernames.");
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void creatingAUserWithAnEmailThatAlreadyExistsCausesAnException() {
        createUser("niko2416", "nick.hill@gmail.com", "Nick Hill", "password");
        try {
            createUser("plightbo", "nick.hill@gmail.com", "Patrick Lightbody", "password");
            Assert.fail("Exception should have been thrown due to duplicate email addresses.");
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void gettingAUserByUpdateTimeProducesTheCorrectUserObjects() {

    	// setup some dates...
    	Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        cal.set(Calendar.YEAR, 2002);
        java.util.Date date1 = cal.getTime(); 

        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.YEAR, 2002);
        java.util.Date date2 = cal.getTime(); 

        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        cal.set(Calendar.YEAR, 2002);
        java.util.Date date3 = cal.getTime(); 

        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 18);
        cal.set(Calendar.YEAR, 2002);
        java.util.Date date4 = cal.getTime(); 

        User user1 = createUser("niko2416", "nick.hill@gmail.com", "Nick Hill", "password", date2, date2);
        List<User> users = userService.findByUpdatedAfterDate(date3);
        Assert.assertEquals(users.size(), 0, "Found unexpected record, no records should be later than " + date3);
        
        users = userService.findByUpdatedAfterDate(date1);
        Assert.assertEquals(users.size(), 1, "Expected 1 record later than " + date1 + ", but found " + users.size());
        
        User user2 = createUser("niko24163", "nick.hill@gmail.com3", "Nick Hill3", "password", date4, date4);
        users = userService.findByUpdatedAfterDate(date3);
        Assert.assertEquals(users.size(), 1, "Expected 1 record later than " + date3 + ", but found " + users.size());

        users = userService.findByUpdatedAfterDate(date1);
        Assert.assertEquals(users.size(), 2, "Expected 2 records later than " + date1 + ", but found " + users.size());
        
    }

    @Test
    public void gettingAUserByUsernameProducesTheCorrectUserObject() {
        User user = createDefaultUser();
        User result = userService.findByUsername("niko2416");
        Assert.assertEquals(result.getId(), user.getId());
    }

    @Test
    public void gettingAUserByEmailProducesTheCorrectUserObject() {
        User user = createDefaultUser();
        User result = userService.findByEmail("nick.hill@gmail.com");
        Assert.assertEquals(result.getId(), user.getId());
    }

    @Test
    public void updatingAUserCausesUserInformationToBeSaved() {
        User user = createDefaultUser();
        user.setName("John Doe");
        user.setEmail("john.doe@email.com");
        user.setPasswordHash("newpass");
        userService.save(user);

        User byId = userService.findById(1);
        Assert.assertEquals(byId.getName(), user.getName());
        Assert.assertEquals(byId.getEmail(), user.getEmail());
    }

    @Test
    public void updatingAUserEmailToAnEmailThatAlreadyExistsCausesAnException() {
        User user = createDefaultUser();
        createUser("plightbo", "plightbo@gmail.com", "Patrick Lightbody", "password");
        user.setEmail("plightbo@gmail.com");
        try {
            userService.save(user);
            Assert.fail("Exception should have been thrown due to duplicate email addresses.");
        } catch (Exception e) {
            // this is expected
        }
    }

    @Test
    public void removingAUserCausesADatabaseRowToBeDeleted() {
        User user = createDefaultUser();
        int before = getCount("SELECT COUNT(*) FROM users;");
        userService.remove(user.getId());
        int after = getCount("SELECT COUNT(*) FROM users;");

        Assert.assertEquals(after - before, -1);
    }

    @Test
    public void newPasswordsAreGeneratedAndEmailedWhenForgotten() throws MailException {
        User user = createDefaultUser();

        final User original = user;
        final String[] args = new String[3];
        userService.setMailService(new MailService() {
            public void send(String to, String subject, String body) {
                args[0] = to;
                args[1] = subject;
                args[2] = body;
            }
        });

        userService.forgotPassword(user.getUsername());

        user = userService.findByEmail(user.getEmail());
        String oldPassword = original.getPasswordHash();
        String newPassword = user.getPasswordHash();

        Assert.assertFalse(oldPassword.equals(newPassword));

        Assert.assertEquals(args[0], original.getEmail());
        Assert.assertEquals(args[1], "Juice: Reset Password");
    }

    @Test
    public void changingAPasswordDoesNotUpdateOtherFields() {
        User user = createDefaultUser();

        String original = user.getPasswordHash();
        user.setName("THIS SHOULD NOT CHANGE");
        userService.changePassword(user, "new!@#$");

        user = userService.findByEmail(user.getEmail());
        Assert.assertFalse(user.getName().equals("THIS SHOULD NOT CHANGE"));
        Assert.assertFalse(user.getPasswordHash().equals(original));
    }

    @Test
    public void updatingUserDoesNotChangePassword() {
        User user = createDefaultUser();
        user.setName("SOMETHING ELSE");
        user.setPasswordHash("THIS SHOULD NOT CHANGE");

        userService.save(user);

        user = userService.findByEmail(user.getEmail());
        Assert.assertEquals("SOMETHING ELSE", user.getName());
        Assert.assertFalse("THIS SHOULD NOT CHANGE".equals(user.getPasswordHash()));
    }

    @Test
    public void selectingUsersByCaseInSensitiveUsernameWillReturnThatUser() {
        User user = createDefaultSecondUser();
        userService.save(user);

        User temp = userService.findByUsernameIgnoreCase("tRaVHoAng");

        Assert.assertEquals(user, temp);

        temp = userService.findByUsernameIgnoreCase("TRAVHOANG");
        Assert.assertEquals(user, temp);

        temp = userService.findByUsernameIgnoreCase("travHOANG");

        Assert.assertEquals(user, temp);
    }
}
