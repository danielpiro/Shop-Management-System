package Bussiness_Layer.Location;

public abstract class Location {
    private int amount;
    private int aisleNum;
    private int shelfNum;
    private String locType;

    public Location(String locType, int aisle, int shelf) {
        //this.amount = amount;
        this.amount = 0;
        this.locType = locType;
        this.aisleNum = aisle;
        this.shelfNum = shelf;
    }

    public String getLocType()
    {
        return locType;
    }

    public void AddAmount(int amount) {
        this.amount = this.amount + amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount()
    {
        return amount;
    }

    public boolean isEqual(Location loc)
    {
        return locType.equals(loc.locType)&& aisleNum == loc.getAisleNum() && shelfNum == loc.getShelfNum();
    }

    @Override
    public String toString()
    {
        return amount+" in "+locType+" on aisle "+getAisleNum() +" shelf "+getShelfNum() +"\n";
    }

    public int getAisleNum() {
        return aisleNum;
    }

    public int getShelfNum() {
        return shelfNum;
    }
}
