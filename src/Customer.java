public class Customer
{
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int points;

    public Customer(int newId,
                    String newFirstName,
                    String newLastName,
                    String newPhone,
                    String newEmail,
                    int newPoints)
    {
        id = newId;
        firstName = newFirstName;
        lastName = newLastName;
        phone = newPhone;
        email = newEmail;
        points = newPoints;
    }

    // Accessor methods
    public int getId()
    {
        return id;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getPhone()
    {
        return phone;
    }
    public String getEmail()
    {
        return email;
    }
    public int getPoints()
    {
        return points;
    }
    // Mutator methods
    public void setId(int newId)
    {
        id = newId;
    }
    public void setFirstName(String newFirstName)
    {
        firstName = newFirstName.toUpperCase();
    }
    public void setLastName(String newLastName)
    {
        lastName = newLastName.toUpperCase();
    }
    public void setPhone(String newPhone)
    {
        phone = newPhone;
    }
    public void setEmail(String newEmail)
    {
        email = newEmail;
    }
    public void setPoints(int newPoints)
    {
        points = newPoints;
    }
    // Methods to check if fields are formatted correctly and valid
    public static boolean checkName(String nameCheck)
    {
        return (nameCheck.length() >= 2 && nameCheck.chars().allMatch(Character::isLetter));
    }
    public static boolean checkPhone(String phoneCheck)
    {
        return (phoneCheck.length() == 10 && phoneCheck.chars().allMatch(Character::isDigit) && phoneCheck.charAt(0) != '0');
    }
    public static boolean checkEmail(String emailCheck)
    {
        return (emailCheck.endsWith(".com")
                && emailCheck.contains("@")
                && emailCheck.length() > 6
                && !emailCheck.startsWith("@"));
    }
    public static boolean checkPoints(int pointsCheck)
    {
        return (pointsCheck > 0);
    }
}