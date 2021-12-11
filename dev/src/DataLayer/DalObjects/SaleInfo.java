package DataLayer.DalObjects;

import java.util.Date;

public class SaleInfo {
    private int _id;
    private int _amount;
    private double _priceBought;
    private double _priceSold;
    private Date date;
    //private Supplier supplier   //when we will work with supplier if at all


    public SaleInfo(int id, int _amount, double _priceBought, double _priceSold, Date date) {
        this._amount = _amount;
        this._priceBought = _priceBought;
        this._priceSold = _priceSold;
        this._id = id;
        this.date = date;
    }

    //returns item's id of sale
    public int getSaleID()    {
        return this._id;
    }

    public int get_amount() {
        return _amount;
    }

    public double get_priceBought() {
        return _priceBought;
    }

    public double get_priceSold() {
        return _priceSold;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        double profit = _amount*(_priceSold-_priceBought);
        return "SaleInfo:" +
                "amount=" + _amount +
                ", price Bought=" + _priceBought +
                ", price Sold=" + _priceSold +
                "\nProfit from this sale is: "+profit+"\n";
    }
}
