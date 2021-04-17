package Users;

public class Cashier extends User{
    public String internalPhone;
    public float baseSalary;
    public float hourOfWork;
    public float bonus;
    public float bonusPoint;

    public Cashier(String name, String surname, int id, String username, String password, String address, String phone, String internalPhone, float baseSalary, float hourOfWork) {
        super(name, surname, id, username, password, address, phone);
        this.internalPhone = internalPhone;
        this.baseSalary = baseSalary;
        this.hourOfWork = hourOfWork;

        bonus = 0;
        bonusPoint = 0;
    }


    public void CalculateBonus()
    {
        while(bonusPoint>=100)
        {
            bonus+=50;
            bonusPoint-=100;
        }
    }
}
