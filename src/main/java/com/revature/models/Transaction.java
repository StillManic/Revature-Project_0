package com.revature.models;

public class Transaction {
	private Integer id;
	private Account source;
	private String type;
	private Float amount;
	private Account receiver;
	
	public Transaction() {}
	
	public Transaction(Account source, String type, Float amount) {
		this.source = source;
		this.type = type;
		this.amount = amount;
	}
	
	public Transaction(Account source, String type, Float amount, Account receiver) {
		this.source = source;
		this.type = type;
		this.amount = amount;
		this.receiver = receiver;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getSource() {
		return source;
	}

	public void setSource(Account source) {
		this.source = source;
	}
	
	public Account getReceiver() {
		return this.receiver;
	}
	
	public void setReceiver(Account receiver) {
		this.receiver = receiver;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null) return false;
		} else if (!amount.equals(other.amount)) return false;
		
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		
		if (receiver == null) {
			if (other.receiver != null) return false;
		} else if (!receiver.equals(other.receiver)) return false;
		
		if (source == null) {
			if (other.source != null) return false;
		} else if (!source.equals(other.source)) return false;
		
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", source=" + source + ", type=" + type + ", amount=" + amount + ", receiver=" + receiver + "]";
	}

	
}
