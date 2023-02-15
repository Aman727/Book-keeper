import java.util.Date;

public class Transaction {
	
	private Date date;
	private String details;
	private String type;
	private Double in;
	private Double out;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getIn() {
		return in;
	}
	public void setIn(Double in) {
		this.in = in;
	}
	public Double getOut() {
		return out;
	}
	public void setOut(Double out) {
		this.out = out;
	}

	
	

	
}
