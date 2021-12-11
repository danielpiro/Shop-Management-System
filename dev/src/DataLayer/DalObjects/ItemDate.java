package DataLayer.DalObjects;
import java.util.Date;

public class ItemDate {

    private int itemID;
    private int amount;
    private Date date;
    public ItemDate(Date date, int itemID, int amount)
    {
        this.itemID = itemID;
        this.date = date;
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount)
    {
        this.amount = this.amount + amount;
    }
    public Date getDate() {
        return date;
    }
}
