package csci498.jshaw.lunchlist;

import java.util.Date;

public class Restaurant {
  private String name="";
  private String address="";
  private String type="";
  private int date;
  
public int getDate() {
	return date;
}

public void setDate(int date) {
	this.date = date;
}

public String getName() {
    return(name);
  }

public void setName(String name) {
    this.name=name;
  }

public String getAddress() {
    return(address);
  }

public void setAddress(String address) {
    this.address=address;
  }

public String getType() {
	  	return(type);
	}

public void setType(String type) {
		this.type=type;
	}

public String toString() {
	  return(getName());
}


}