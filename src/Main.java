import Items.*;
import Users.User;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    private static String lang;
    private static String country;
    private static Locale l;
    private static ResourceBundle r;

    private static ArrayList<Item> ITEMS = new ArrayList<>();
    private static ArrayList<User> EMPLOYEES = new ArrayList<>();
    private static ArrayList<User> CUSTOMERS = new ArrayList<>();

    private static int itemId = 0;
    private static int userId = 0;

    public static void main(String[] args) {

        lang = "en";
        country = "US";
        l = new Locale(lang, country);
        r = ResourceBundle.getBundle("Properties/Bundle", l);

        print(r.getString("wish")+"\n");

        PopulateItem();
        PresentItemList(ITEMS);

        println("" + InputNumberString());

    }




    public static void print(String s)
    {
        System.out.print(s);
    }
    public static void println(String s)
    {
        System.out.println(s);
    }
    public static int InputNumber(){

        while(true)
        {
            try {
                Scanner sc = new Scanner(System.in);
                return sc.nextInt();
            }
            catch (Exception e)
            {
                println(r.getString("askValidNumber"));
            }

        }
    }
    public static String InputString() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public static String InputNumberString(){

        while (true)
        {
            try
            {
               Scanner sc = new Scanner(System.in);
               return sc.nextBigInteger().toString();
            }
            catch(Exception e)
            {
                println(r.getString("askValidNumber"));
            }
        }
    }


    public static void PopulateItem()
    {
        ITEMS.add(new Item("Pancake",itemId++,8));
        ITEMS.add(new Item("Chicken",itemId++,10));
        ITEMS.add(new ItemWithDiscount("Icecreem",itemId++,5,15));
        ITEMS.add(new ItemWithDiscount("Cheese",itemId++,4,10));
        ITEMS.add(new ItemWithDiscountIndependence("Apple",itemId++,4,10,5));
        ITEMS.add(new ItemWithDiscountIndependence("Orange",itemId++,5,15,5));
        ITEMS.add(new BuyMorePayLess("Soap",itemId++,4,10));
        ITEMS.add(new ItemTakeItAll("EGG",itemId++,3,20,12));
    }
    public static void PresentItemList(ArrayList<Item> itemList)
    {
        print(FormatString(r.getString("index"),1) + FormatString(r.getString("item"),11) +
                FormatString(r.getString("price"),15)+
                        FormatString(r.getString("discount"),10) +
                        FormatString(r.getString("type"),40) + "\n"
                );

        for (int i = 0; i < ITEMS.size() ; i++)
        {
            var currentItem = ITEMS.get(i);
            print(FormatString(Integer.toString(i),1) + FormatString(currentItem.name,15) +
                    FormatString(Float.toString(currentItem.price),15)+
                    FormatString(Float.toString(currentItem.CalculateDiscount(1)),10) +
                    FormatString(currentItem.getClass().getTypeName().split("\\.")[1],40) + "\n"
            );
        }
    }
    public static String FormatString(String s,int pad)
    {
        return String.format("%"+pad+"s", s);
    }
}
