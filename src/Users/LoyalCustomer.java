package Users;

public class LoyalCustomer extends User{
    public float bonus;
    public float bonusPoint;

    public LoyalCustomer(String name, String surname, int id, String username, String password, String address, String phone) {
        super(name, surname, id, username, password, address, phone);
        bonus = 0;
        bonusPoint = 0;
    }

    public void CalculateBonus()
    {
        while(bonusPoint>=100)
        {
            bonus += 10;
            bonusPoint-=100;
        }
    }
}
