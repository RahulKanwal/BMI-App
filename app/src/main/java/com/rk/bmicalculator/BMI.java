package com.rk.bmicalculator;


public class BMI {
    private String bmi;
    private String t;
    private String res;
    private double w;

    public BMI() {
    }

    public BMI(String t, double w, String bmi, String res) {
        this.bmi = bmi;
        this.t = t;
        this.res = res;
        this.w = w;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    @Override
    public String toString() {
        return "Date and Time:" + t + "\nWeight:" + w + "\nBMI:" + bmi + "\nResult:" + res;
    }
}
