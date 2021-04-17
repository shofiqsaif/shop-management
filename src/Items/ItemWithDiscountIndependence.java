package Items;

public class ItemWithDiscountIndependence extends ItemWithDiscount{
      public float discountIndependence;

      public ItemWithDiscountIndependence(String name, int id, float price, float discount, float discountIndependence) {
            super(name, id, price, discount);
            this.discountIndependence = discountIndependence;
      }

      public float CalculatePrice(int n)
      {
            return price * n;
      }
      public float CalculateDiscount(int n)
      {
            var singleDiscount = price* ((discount+discountIndependence)/100);
            return singleDiscount*n;
      }
}
