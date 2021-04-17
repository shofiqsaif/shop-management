package Items;

public class ItemTakeItAll extends ItemWithDiscount{
    public int minimumTake;

    public ItemTakeItAll(String name, int id, float price, float discount, int minimumTake) {
        super(name, id, price, discount);
        this.minimumTake = minimumTake;
    }

    public float CalculatePrice(int n)
    {
        return price * n;
    }

    public float CalculateDiscount(int n)
    {
        if(n < minimumTake)
        {
            return 0;
        }
        else
        {
            var singleDiscount = price * (discount/100);
            return singleDiscount * n;
        }
    }
}
