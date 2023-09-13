package faker;

import com.github.javafaker.Faker;
import pojos.UserPOJO;

public class FakerUser {

    static Faker faker = new Faker();

    public static UserPOJO fakeUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6,10);
        String name = faker.name().fullName();
        return new UserPOJO(email, password, name);
    }

    public  static  UserPOJO fakeUserWithoutPassword() {
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        return new UserPOJO(email, name);
    }

    public  static  UserPOJO fakeUserWithoutName() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(6,10);
        return new UserPOJO(email, password);
    }

    public  static  UserPOJO fakeUserWithoutEmail() {
        String password = faker.internet().password(6,10);
        String name = faker.name().fullName();
        return new UserPOJO(password, name);
    }
}
