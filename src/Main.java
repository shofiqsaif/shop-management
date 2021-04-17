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



        SetLanguagePreference();

        currentManager = UnlockSoftware();

        SimulateClearScreen();
        print(r.getString("wish")+"\n");

        var loginType = InputValidLoginOption();
        println(""+loginType);
        if(loginType == 1)
        {
            println(r.getString("askLoginInfo"));
            currentCashier = HandleCashierLogin();

            println(r.getString("wish") + " : " +currentCashier.name);
            while(true)
            {
                SimulateClearScreen();
                if(openedBasket != null)
                {
                    DisplayCurrentBasketInfo();
                }
                else println(r.getString("noBasketOpen"));
                println("\n");
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
            println("Nothing Special on Manager side is implemented.");
        }

        //PresentItemList(ITEMS);



    }

    private static void ProcessCurrentBasket()
    {
        SimulateClearScreen();
        var basketType = GetObjectType(openedBasket);
        var ownerIdx = GetIdxOfCustomerWithCertainId(openedBasket.userId);
        if(basketType.equals("StoreBasket"))
        {
            println(r.getString("basketPrecessed"));
            WaitForInput();
        }
        else
        {
            println(r.getString("basketPrecessed"));
            DisplayCurrentBasketInfo();

            var owner = CUSTOMERS.get(ownerIdx);
            //println("This is "+ owner.name);

            var bonusUsed = openedBasket.HowMuchBonusUsed();
            var bonusPointToGive = openedBasket.HowMuchBonusPointToGive(owner.bonus);
            //println("How much Bonus used "+ bonusUsed);
            //println("How much Bonus Point to give "+ bonusPointToGive);
            //println("Bonus: " + owner.bonus +"  Bonus Point: "+ owner.bonusPoint);
            //println("Bonus: " + owner.bonus +"  Bonus Point: "+ owner.bonusPoint);
            owner.bonus -= bonusUsed;
            owner.bonusPoint += bonusPointToGive;

            if(GetObjectType(owner).equals("Cashier") || GetObjectType(owner).equals("Manager"))
            {
                while(owner.bonusPoint >= 100)
                {
                    owner.bonusPoint -=100;
                    owner.bonus +=50;
                }
            }
            else{
                while(owner.bonusPoint >= 100)
                {
                    owner.bonusPoint -=100;
                    owner.bonus +=10;
                }
            }

            //println("Bonus: " + owner.bonus +"  Bonus Point: "+ owner.bonusPoint);

            WaitForInput();

            BasketServed.add(openedBasket);
            var i = GetIdxOfBasketWithCertainId(openedBasket.basketId);
            BasketBeingServed.remove(i);
            openedBasket = null;
        }
    }
    private static int GetIdxOfBasketWithCertainId(int id)
    {
        int idx = 0;
        for(var b: BasketBeingServed)
        {
            if(b.basketId == idx) return idx;
        }
        return -1;
    }
    private static int GetIdxOfCustomerWithCertainId(int id){
        int idx = 0;
        for(var c : CUSTOMERS)
        {
            if (c.id == id) return idx;
            idx++;
        }
        return -1;
    }
    private  static void WaitForInput()
    {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    private static void ChangeOpenedBasket() {
        println(r.getString("thisBasketCurrentlyServed"));
        for(var b : BasketBeingServed)
        {
            println(""+ b.basketId);
        }
        print(r.getString("whatBasketToOpen"));
        var basIdx = InputNumber();

        for (var b: BasketBeingServed)
        {
            if(b.basketId == basIdx)
            {
                openedBasket = b;
                return ;
            }
        }

        print(r.getString("basketNotExist"));
        WaitForInput();

    }

    private static void AddChristmasDiscount() {
        openedBasket.extraDiscount = 10;
    }

    private static void PrintInfoOfCertainBasket() {

        SimulateClearScreen();
        print(r.getString("whichBasketDetails"));
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

        if(sb == null) println(r.getString("basketNotExist"));
        else
        {
            var backup = openedBasket;
            openedBasket = sb;
//            println(r.getString("basketId") + openedBasket.basketId);
//            println(r.getString("customerId")+ openedBasket.userId);
//            println(r.getString("cashierName") + openedBasket.nameOfCashier);
//            println(r.getString("addressOfShop") + openedBasket.addressOfShop);
            println(r.getString("currentlyOpen"));
            DisplayCurrentBasketInfo();
            println("\n");
            openedBasket = backup;
        }

        println("Press any button/enter to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }

    private static void RemoveItemFromBasket() {
        if(openedBasket == null)
        {
            print(r.getString("canNotAddRemoveItemAsNoBasketOpen"));
            WaitForInput();
            return;
        }

        SimulateClearScreen();
        DisplayCurrentBasketInfo();
        print(r.getString("whichItemToRemove"));
        var remIdx = InputNumber();

        if(remIdx >= openedBasket.listOfItems.size())
        {
            print(r.getString("selectValidItem"));
            WaitForInput();
            RemoveItemFromBasket();
        }
        else
        {
            openedBasket.listOfItems.remove(remIdx);
            openedBasket.itemCounts.remove(remIdx);
        }

    }

    private static void AddItemToBasket() {

        if(openedBasket == null)
        {
            print(r.getString("canNotAddItemAsNoBasketOpen"));
            WaitForInput();
            return;
        }

        PresentItemList(ITEMS);
        print(r.getString("whichItemToAdd"));
        var itemIdx = InputNumber();
        print(r.getString("howManyToAdd"));
        var howMany = InputNumber();

        if(itemIdx>=ITEMS.size())
        {
            SimulateClearScreen();
            print(r.getString("selectValidItem") + "\n");
            AddItemToBasket();
        }
        else
        {
            openedBasket.listOfItems.add(ITEMS.get(itemIdx));
            openedBasket.itemCounts.add(howMany);
        }

    }

    private static User FindCustomerBasedOnId(int id)
    {
        for(var c : CUSTOMERS)
        {
            if(c.id == id) return c;
        }
        return null;
    }
    private static void DisplayCurrentBasketInfo() {
        var basket = openedBasket;
        var items = basket.listOfItems;
        var owner = FindCustomerBasedOnId(basket.userId);

        println(r.getString("basketId") + openedBasket.basketId);
        println(r.getString("customerId")+ openedBasket.userId);
        println(r.getString("cashierName") + openedBasket.nameOfCashier);
        println(r.getString("addressOfShop") + openedBasket.addressOfShop);

        println(r.getString("thisIsBasket") +  openedBasket.basketId + "  "+ r.getString("whichisofType") + openedBasket.getClass().getTypeName());
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

        print("\n");
        println(r.getString("totalAmount") + " " + basket.CalculateTotalAmount());
        println(r.getString("netAmount") + " " + basket.CalculateNetAmount());
        println(r.getString("customerBonus") + " " + owner.bonus);
        println(r.getString("finalAmount") +" " + + basket.CalculateFinalAmount(owner.bonus));


    }

    private static void AddNewBasket() {
        print(r.getString("EnterTheUserIdThisBasketWillBelongTo"));
        var uid = InputNumber();
        print(r.getString("EnterVatToBasket"));
        var vat = InputValidVat();

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

    private static float InputValidVat() {
        var n = InputFloat();
        if(n>0) return n;
        else
        {
            print(r.getString("inputValidVat"));
            return InputValidVat();
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
            print(r.getString("askManagerPin"));
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
        println ("Apa pilihan bahasa anda?: \n1. Bahasa Inggeris \n2.Malay \n");
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
                println(r.getString("PleaseInputValidLoginOption"));
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
        print(r.getString("enterUsername"));
        var username = sc.nextLine();
        print(r.getString("enterPass"));
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
        println(r.getString("NoValidCashierFound"));
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
