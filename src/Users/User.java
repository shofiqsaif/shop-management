package Users;

public class User {
    public String name;
    public String surname;
    public int id;
    public String username;
    public String password;
    public String address;
    public String phone;

    public User(String name, String surname, int id, String username, String password, String address, String phone) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }
}
