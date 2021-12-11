package Facade_Layer.FacadeObjects;


import Bussiness_Layer.TransportationPackage.Truck;

public class TruckFacade {

    public int licenseNum;
    public String model;
    public double netoWeight;
    public double maxWeight;

    public TruckFacade(int licenseNum, String model, double netoWeight, double maxWeight) {
        this.licenseNum = licenseNum;
        this.model = model;
        this.netoWeight = netoWeight;
        this.maxWeight = maxWeight;
    }

    public TruckFacade(Truck truck){
        this.licenseNum=truck.getLicenseNum();
        this.model=truck.getModel();
        this.netoWeight=truck.getNetoWeight();
        this.maxWeight=truck.getMaxWeight();
    }

    @Override
    public String toString(){
        return "license num: "+ licenseNum + '\t'+"model: " + model +'\t'+"neto Weight: "+netoWeight+'\t'+"max weight: "+maxWeight+'\n';
    }
}
