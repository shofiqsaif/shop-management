package Items;

public class Item {
    public String name;
    public int id;
    public float price;

    public Item(String name, int id, float price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }


    public float CalculatePrice(int n)
    {
        return price*n;
    }
    public float CalculateDiscount(int n)
    {
        return 0;
    }
}
