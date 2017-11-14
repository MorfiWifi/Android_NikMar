package com.atahani.retrofit_sample.models;

import java.util.Date;
import java.util.List;

/**
 * Created by m.hosein on 11/10/2017.
 */

public class OrderModel {
    public int Id;
    public Date OrderDate;
    public Date RequiredDate;
    public int CustomerId;
    public int ContractId;
    public List<Order_DetailsModel> Order_Details;
    public CustomerModel Customer;
    public ContractModel Contract;


}
