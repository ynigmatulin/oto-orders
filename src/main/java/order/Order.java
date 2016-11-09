package order;

import org.springframework.data.annotation.Id;

public class Order {

	@Id private String id;

    private String productId;
	private int amount;
	private double totalSum;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getTotalSum()
	{
		return totalSum;
	}

	public void setTotalSum(double totalSum)
	{
		this.totalSum = totalSum;
	}
}
