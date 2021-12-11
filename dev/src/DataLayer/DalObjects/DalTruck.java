package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.Truck;

public class DalTruck {
    private int licenseNum;
    private String model;
    private double netoWeight;
    private double maxWeight;

    public DalTruck(){ }
    public DalTruck(Truck truck){
        this.licenseNum = truck.getLicenseNum();
        this.model = truck.getModel();
        this.netoWeight = truck.getNetoWeight();
        this.maxWeight = truck.getMaxWeight();
    }
    public DalTruck(int licenseNum, String model, double netoWeight, double maxWeight) {
        this.licenseNum = licenseNum;
        this.model = model;
        this.netoWeight = netoWeight;
        this.maxWeight = maxWeight;
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
