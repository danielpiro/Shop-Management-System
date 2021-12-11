package Bussiness_Layer.TransportationPackage;

import DataLayer.DalObjects.DalTruck;

public class Truck {
    private int licenseNum;
    private String model;
    private double netoWeight;
    private double maxWeight;

    public Truck(int licenseNum, String model, double netoWeight, double maxWeight) {
        this.licenseNum = licenseNum;
        this.model = model;
        this.netoWeight = netoWeight;
        this.maxWeight = maxWeight;
    }
    public Truck(DalTruck dalTruck) {
        this.licenseNum = dalTruck.getLicenseNum();
        this.model = dalTruck.getModel();
        this.netoWeight = dalTruck.getNetoWeight();
        this.maxWeight = dalTruck.getMaxWeight();
    }

    public int getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(int licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getNetoWeight() {
        return netoWeight;
    }

    public void setNetoWeight(double netoWeight) {
        this.netoWeight = netoWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }
}
