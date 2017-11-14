package com.atahani.retrofit_sample.models;

import java.util.List;

/**
 * Created by m.hosein on 11/10/2017.
 */

public class ProductModel {
    public int Id;
    public String Name;
    public int UnitPrice;
    public int UnitsInStock;
    public int SupplierId;
    public SupplierModel Supplier;
    public List<Order_DetailsModel> Order_Details;
}
