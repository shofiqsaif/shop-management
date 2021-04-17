import Baskets.BasketForEmployee;
import Baskets.BasketForLoyalCustomers;
import Baskets.StoreBasket;
import Items.*;
import Users.*;

import javax.security.auth.callback.TextInputCallback;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {

    private static String lang;
    private static String country;
    private static Locale l;
    private static ResourceBundle r;

    public static ArrayList<Item> ITEMS = new ArrayList<>();
    public static ArrayList<User> EMPLOYEES = new ArrayList<>();
    public static ArrayList<User> CUSTOMERS = new ArrayList<>();
    public static ArrayList<StoreBasket> BasketServed = new ArrayList<>();
    public static ArrayList<StoreBasket> BasketBeingServed = new ArrayList<>();

    private static int itemId = 0;
    private static int userId = 0;
    private static int basketId = 0;

    private static Manager currentManager = null;
    private static Cashier currentCashier = null;
    private static StoreBasket openedBasket =null;

    private static String addressOfShop = "Singapore";

    public static void main(String[] args) {
        PopulateItem();
        PopulateEmployee();
        PopulateCustomers();

        //TODO Uncomment below lone in final code
        //SetLanguagePreference();
        lang = "en";
        country = "US";
        l = new Locale(lang, country);
        r = ResourceBundle.getBundle("Properties/Bundle", l);
        //TODO Remove 4 hardcoded line above

        //TODO below line will be active in final
        //currentManager = UnlockSoftware();

        SimulateClearScreen();
        print(r.getString("wish")+"\n");

        var loginType = InputValidLoginOption();
        println(""+loginType);
        if(loginType == 1)
        {
            println("Enter your login info : ");
            currentCashier = HandleCashierLogin();

            println(r.getString("wish") + " : " +currentCashier.name);
            while(true)
            {
                SimulateClearScreen();
                if(openedBasket != null)
                {
                    DisplayCurrentBasketInfo();
                }
                else println("NO BASKET OPENED.");
                DisplayCashierMenu();
                var function = InputNumber();
                if(function == 1)
                {
                    AddNewBasket();
                }
                else if(function == 2)
                {
                    AddItemToBasket();
                }
                else if(function == 3)
                {
                    RemoveItemFromBasket();
                }
                else if(function == 4)
                {
                    PrintInfoOfCertainBasket();
                }
                else if(function == 5)
                {
                    AddChristmasDiscount();
                }
                else if(function == 6)
                {
                    ChangeOpenedBasket();
                }
                else if(function == 7)
                {
                    ProcessCurrentBasket();
                }
                else if(function == 8)
                {
                    return;
                }
            }
        }
        //TODO Currently not handling Manager Login and Operations
        else if(loginType == 2)
        {
            println("Hat be.");
        }

        //PresentItemList(ITEMS);



    }

    private static void ProcessCurrentBasket() {
    }

    private  static void WaitForInput()
    {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    private static void ChangeOpenedBasket() {
        println("These are the baskets currently on being served : ");
        for(var b : BasketBeingServed)
        {
            println(""+ b.basketId);
        }
        print("Enter the basket id you want to open : ");
        var basIdx = InputNumber();

        for (var b: BasketBeingServed)
        {
            if(b.basketId == basIdx)
            {
                openedBasket = b;
                return ;
            }
        }

        print("Your given basket does not exist or are not being served... Press anything to continue...");
        WaitForInput();

    }

    private static void AddChristmasDiscount() {
        openedBasket.extraDiscount = 10;
    }

    private static void PrintInfoOfCertainBasket() {

        SimulateClearScreen();
        print("Enter the ID of the basket you wanna see details about: ");
        var id = InputNumber();

        StoreBasket sb = null;
        for(var b : BasketServed)
        {
            if(b.basketId == id)
            {
                sb = b;
                break;
            }
        }
        if(sb == null)
        {
            for(var b : BasketBeingServed)
            {
                if(b.basketId == id)
                {
                    sb = b;
                    break;
                }
            }
        }

        if(sb == null) println("BASKET DOES NOT EXIST!!!");
        else
        {
            var backup = openedBasket;
            openedBasket = sb;
            println("Basket Id: " + openedBasket.basketId);
            println("Customet Id : "+ openedBasket.userId);
            println("Cashier Name: " + openedBasket.nameOfCashier);
            println("Address of Shop : " + openedBasket.addressOfShop);
            DisplayCurrentBasketInfo();

            openedBasket = backup;
        }

        println("Press any button/enter to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    private static void RemoveItemFromBasket() {
        SimulateClearScreen();
        DisplayCurrentBasketInfo();
        print("Which Item you want to remove: ");
        var remIdx = InputNumber();

        openedBasket.listOfItems.remove(remIdx);
        openedBasket.itemCounts.remove(remIdx);
    }

    private static void AddItemToBasket() {
        PresentItemList(ITEMS);
        print("Which item you want to add to the list: ");
        var itemIdx = InputNumber();
        print("How many item of this type you want to add: ");
        var howMany = InputNumber();

        openedBasket.listOfItems.add(ITEMS.get(itemIdx));
        openedBasket.itemCounts.add(howMany);
    }

    private static void DisplayCurrentBasketInfo() {
        var basket = openedBasket;
        var items = basket.listOfItems;
        print(FormatString(r.getString("index"),1) + FormatString(r.getString("item"),11) +
                FormatString(r.getString("count"),10)+
                FormatString(r.getString("price"),15)+
                FormatString(r.getString("discount"),10) +
                FormatString(r.getString("type"),40) + "\n"
        );

        for (int i = 0; i < items.size() ; i++)
        {
            var currentItem = items.get(i);
            var n = basket.itemCounts.get(i);
            print(FormatString(Integer.toString(i),1) + FormatString(currentItem.name,15) +
                    FormatString(""+n,10)+
                    FormatString(Float.toString(currentItem.CalculatePrice(n)),15)+
                    FormatString(Float.toString(currentItem.CalculateDiscount(n)),10) +
                    FormatString(currentItem.getClass().getTypeName().split("\\.")[1],40) + "\n"
            );
        }

        println("Total Amount : " + basket.CalculateTotalAmount());
        println("Net Amount : " + basket.CalculateNetAmount());
        println("Final Amount (After Extra Discount and Bonus Point) : " + basket.CalculateFinalAmount());


    }

    private static void AddNewBasket() {
        print("Enter the userID this basket will belong to : ");
        var uid = InputNumber();
        print("Enter the vat to this basket (percent in integer) : ");
        var vat = InputFloat();

        var customerType = FindCustomerTypeBasedOnId(uid);
        if(customerType.equals("SimpleCustomer"))
        {
            var basket = new StoreBasket(basketId++,uid,addressOfShop,currentCashier.name,vat);
            BasketBeingServed.add(basket);
            openedBasket = basket;
        }
        else if(customerType.equals("LoyalCustomer"))
        {
            var basket = new BasketForLoyalCustomers(basketId++,uid,addressOfShop,currentCashier.name,vat);
            BasketBeingServed.add(basket);
            openedBasket = basket;
        }
        else
        {
            var basket = new BasketForEmployee(basketId++,uid,addressOfShop,currentCashier.name,vat);
            BasketBeingServed.add(basket);
            openedBasket = basket;
        }

    }

    private static String FindCustomerTypeBasedOnId(int id)
    {
        for(var c: CUSTOMERS)
        {
            if(c.id == id)
            {
                return GetObjectType(c);
            }
        }
        return null;
    }

    private static String GetObjectType(Object o)
    {
        return o.getClass().getTypeName().split("\\.")[1];
    }




    private static Manager UnlockSoftware() {
        while(true)
        {
            print("Please enter your managerial PIN to unlock the software: ");
            var pin = InputNumber();
            for(var e : EMPLOYEES)
            {
                var employeeType = e.getClass().getTypeName().split("\\.")[1];
                if (employeeType.equals("Manager"))
                {
                    Manager m = (Manager) e;
                    if(m.pin == pin)
                    {
                        return m;
                    }
                }
            }
        }
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
    private static float InputFloat() {
        while(true)
        {
            try {
                Scanner sc = new Scanner(System.in);
                return sc.nextFloat();
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
    public static String FormatString(String s,int pad)
    {
        return String.format("%"+pad+"s", s);
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
    public static void PopulateEmployee()
    {
        var m = new Manager("manager1","man1",userId++,"man","1122",
                "NYC","123456", "123456",7777,30000);
        var c1 = new Cashier("cashier1","cash1",userId++,"c1","c111","IN","dummy1",
                "internal1",20000,8);
        var c2 = new Cashier("cashier2","cash2",userId++,"c2","c222","IN","dummy2",
                "internal2",21000,8);
        var c3 = new Cashier("cashier3","cash3",userId++,"c3","c333","IN","dummy3",
                "internal3",22000,8);
        var c4 = new Cashier("cashier4","cash4",userId++,"c4","c444","IN","dummy4",
                "internal4",21000,8);
        var c5 = new Cashier("cashier5","cash5",userId++,"c5","c555","IN","dummy5",
                "internal5",20000,8);

        EMPLOYEES.add(m);
        EMPLOYEES.add(c1);
        EMPLOYEES.add(c2);
        EMPLOYEES.add(c3);
        EMPLOYEES.add(c4);
        EMPLOYEES.add(c5);

        CUSTOMERS.add(m);
        CUSTOMERS.add(c1);
        CUSTOMERS.add(c2);
        CUSTOMERS.add(c3);
        CUSTOMERS.add(c4);
        CUSTOMERS.add(c5);

    }
    public static void PopulateCustomers()
    {
        var c1 = new SimpleCustomer("simpleCustomer1","simple1",userId++,"sc1",
                "sc111","PAK","dummy");
        var c2 = new SimpleCustomer("simpleCustomer2","simple2",userId++,"sc2",
                "sc222","PAK","dummy");
        var c3 = new LoyalCustomer("LoyalCustomer1","loyal1",userId++,"lc1",
                "lc111","PAK","dummy");
        var c4 = new LoyalCustomer("LoyalCustomer2","loyal2",userId++,"lc2",
                "lc222","PAK","dummy");

        CUSTOMERS.add(c1);
        CUSTOMERS.add(c2);
        CUSTOMERS.add(c3);
        CUSTOMERS.add(c4);
    }

    public static void SetLanguagePreference()
    {
        println("What's your language preference ? : \n1. English \n2.Malay");
        var langIdx = InputNumber();
        if(langIdx == 1)
        {
            lang = "en";
            country = "US";
        }
        else if(langIdx ==2)
        {
            lang = "malay";
            country = "SG";
        }

        l = new Locale(lang, country);
        r = ResourceBundle.getBundle("Properties/Bundle", l);
    }

    private static int InputValidLoginOption() {
        while(true)
        {
            println(r.getString("pleaseLogin"));
            var employeeChoice = InputNumber();
            if(employeeChoice>2 || employeeChoice<1)
            {
                SimulateClearScreen();
                println("Please Input valid login option...");
            }
            else if(employeeChoice == 1)
            {
                return 1;
            }
            else return 2;
        }
    }
    public static Cashier HandleCashierLogin()
    {
        Scanner sc = new Scanner(System.in);
        print("Enter Username: ");
        var username = sc.nextLine();
        print("Enter Password: ");
        var password = sc.nextLine();

        for(var e : EMPLOYEES)
        {
            if(e.getClass().getTypeName().split("\\.")[1].equals("Cashier"))
            {
                if(e.username.equals(username) && e.password.equals(password))
                {
                    return (Cashier) e;
                }
            }
        }
        println("No Valid Cashire found...");
        return HandleCashierLogin();
    }

    private static void SimulateClearScreen()
    {
        for(int i=0;i<75;i++)
        {
            println("");
        }
    }

    private static void DisplayCashierMenu()
    {
        println(r.getString("cashierMenu"));
    }
}
