package Users;

public class Manager extends User{
    public String internalPhone;
    public int pin;
    public float baseSalary;
    public float bonus;
    public float bonusPoint;

    public Manager(String name, String surname, int id, String username, String password, String address, String phone, String internalPhone, int pin, float baseSalary) {
        super(name, surname, id, username, password, address, phone);
        this.internalPhone = internalPhone;
        this.pin = pin;
        this.baseSalary = baseSalary;
        bonus = 0;
        bonusPoint = 0;
    }

    public void CalculateBonus()
    {
        while(bonusPoint>=100)
        {
            bonus += 50;
            bonusPoint -= 100;
        }
    }
}
