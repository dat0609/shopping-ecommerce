package com.shopme.checkout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckoutInfo {

	private float productCost;
	private float productTotal;
	private float shippingCostTotal;
	private float paymentTotal;
	private int deliverDays;
	private boolean codSuported;

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}

	public float getShippingCostTotal() {
		return shippingCostTotal;
	}

	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}

	public float getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, deliverDays);
		
		return calendar.getTime();
	}

	public boolean isCodSuported() {
		return codSuported;
	}

	public void setCodSuported(boolean codSuported) {
		this.codSuported = codSuported;
	}
	
	public String  paymentTotal4PayPal() {
		DecimalFormat format = new DecimalFormat("###,###.##");
		
		return format.format(paymentTotal);
	}

}
