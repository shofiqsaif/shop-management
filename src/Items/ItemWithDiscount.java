package Items;

public class ItemWithDiscount extends Item {
    public float discount;
    public ItemWithDiscount(String name, int id, float price,float discount) {
        super(name, id, price);
        this.discount = discount;
    }

    public float CalculatePrice(int n)
    {
        return price*n;
    }
    public float CalculateDiscount(int n)
    {
        var singleDiscount = price*(discount/100);
        return singleDiscount*n;
    }
}
