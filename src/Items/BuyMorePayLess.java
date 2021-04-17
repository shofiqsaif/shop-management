package Items;

public class BuyMorePayLess extends Item{
    public int maxBuy;

    public BuyMorePayLess(String name, int id, float price, int maxBuy) {
        super(name, id, price);
        this.maxBuy = maxBuy;
    }

    public float CalculatePrice(int n)
    {
        return  price * n;
    }

    public float CalculateDiscount(int n)
    {
        int freeCount = 0;
        while(n>=3)
        {
            if(n>=5)
            {
                n-=5;
                freeCount+=2;
            }
            else
            {
                n-=3;
                freeCount++;
            }
        }

        return price * freeCount;

    }
}
