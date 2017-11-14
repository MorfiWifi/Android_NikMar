package com.atahani.retrofit_sample.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User Model
 */
public class UserModel {
    public String Id;
    public String UserName;
    public List<RoleModel> Roles;
    public int Salary;
    public int FinalSalary;
    public int AbsentDays;
    public int OverTime;
    public int Benefits;

}
