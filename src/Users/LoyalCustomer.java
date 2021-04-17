package Users;

public class LoyalCustomer extends User{
    public float bonus =0;
    public float bonusPoint=0;

    public LoyalCustomer(String name, String surname, int id, String username, String password, String address, String phone) {
        super(name, surname, id, username, password, address, phone);
    }

    public void CalculateBonus()
    {
        System.out.println("CalculateBonus of loyal customer has been asked");
        while(bonusPoint>=100)
        {
            bonus += 10;
            bonusPoint-=100;
        }
    }
}
