package Baskets;

import Items.Item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StoreBasket {
    public int basketId;
    public int userId;
    public ArrayList<Item> listOfItems;
    public String dateTime;
    public String addressOfShop;
    public String nameOfCashier;
    public float vat;
    public float totalAmount;
    public float netAmount;

    public ArrayList<Integer> itemCounts;
    public float extraDiscount;
    public float bonusPoints;
    public float finalAmount;

    public StoreBasket(int basketId, int userId, String addressOfShop, String nameOfCashier,float vat) {
        this.basketId = basketId;
        this.userId = userId;
        this.addressOfShop = addressOfShop;
        this.nameOfCashier = nameOfCashier;
        this.vat = vat;

        listOfItems = new ArrayList<>();
        dateTime = getCurrentDateTime();
        totalAmount = 0;
        netAmount = 0;
        itemCounts = new ArrayList<>();
        extraDiscount = 0;
        bonusPoints = 0;
        finalAmount = 0;

    }

    public float CalculateTotalAmount()
    {
        float amount = 0;
        for(int i = 0; i < listOfItems.size(); i++)
        {
            amount += listOfItems.get(i).CalculatePrice(itemCounts.get(i));
        }
        totalAmount = amount;
        return amount;
    }
    public float CalculateNetAmount()
    {
        float amount = 0;
        for(int i = 0; i < listOfItems.size(); i++)
        {
            amount += listOfItems.get(i).CalculatePrice(itemCounts.get(i)) - listOfItems.get(i).CalculateDiscount(itemCounts.get(i));
        }
        amount-= amount*(vat/100);
        netAmount = amount;
        return amount;
    }

    public float CalculateFinalAmount()
    {
        float amount = CalculateNetAmount();
        amount -= amount * (extraDiscount/100);
        finalAmount = amount;
        return amount;
    }

    public static String getCurrentDateTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        //System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return myDateObj.format(myFormatObj);
        //String formattedDate = myDateObj.format(myFormatObj);
        //System.out.println("After formatting: " + formattedDate);
        //return formattedDate;
    }
}


