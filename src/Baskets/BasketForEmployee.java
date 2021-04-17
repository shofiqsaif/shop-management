package Baskets;

public class BasketForEmployee extends StoreBasket{
    public float howMuchBonusUsed = 0;
    public BasketForEmployee(int basketId, int userId, String addressOfShop, String nameOfCashier, float vat) {
        super(basketId, userId, addressOfShop, nameOfCashier, vat);
    }

    public float CalculateTotalAmount()
    {
        return super.CalculateTotalAmount();
    }
    public float CalculateNetAmount()
    {
        return super.CalculateNetAmount();
    }

    public float CalculateFinalAmount(float bonus)
    {
        float amount = CalculateNetAmount();
        amount -= amount * (extraDiscount/100);

        if(bonus<amount)
        {
            amount -= bonus;
            howMuchBonusUsed = bonus;
        }
        else
        {
            amount = 0;
            howMuchBonusUsed = amount;
        }
        return amount;
    }

    public int howMuchBonusPointToGive()
    {
        return (int) (finalAmount/15);
    }

    public float HowMuchBonusUsed()
    {
        return howMuchBonusUsed;
    }
}
