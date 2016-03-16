//CS 342 - SPRING 2016
//Project 3
//Group 55
//Member : Hoang Minh Huynh Nguyen (hhuynh20)
//Member : Kevin Molina (kmolin2)

//Class: HugeInteger.java
//Responsibility: used to create huge integer number

public class HugeInteger {
	
	public int[] digits;
	
	public HugeInteger(){
		digits = new int[100]; //array to store digits
	}
	
	//Constructor, parameter is a HugeInteger object
	public HugeInteger(HugeInteger i){
		digits = new int[100];
		for(int n = 0; n < 100; n++){
			this.digits[n] = i.digits[n];
		}
	}
	
	//Constructor, parameter is a string representation of huge integer number
	public HugeInteger(String s){
		digits = new int[100];
		for(int n = 0; n < s.length(); n++){
			this.digits[n] = s.charAt(s.length() - 1 - n) - '0';
		}
	}
	
	//Assign function, used for copy
	public void assign(HugeInteger i){
		for(int n = 0; n < 100; n++){
			this.digits[n] = i.digits[n];
		}
	}
	
	//Addition function
	public HugeInteger add(HugeInteger i){
		int carry = 0;
		for(int n = 0; n < 100; n++){
			if((this.digits[n] + i.digits[n] + carry) < 10){
				this.digits[n] += i.digits[n] + carry;
				carry = 0;
			} else{
				this.digits[n] += i.digits[n] + carry - 10;
				carry = 1;
			}
		}
		return this;
	}
	
	//Subtraction function: a - b, if b > a, just return a. In this program we don't need to handle negative number
	public HugeInteger subtract(HugeInteger i){
		if(this.lessThan(i))
			return this;
		else{
			int borrow = 0;
			for(int n = 0; n < 100; n++){
				if(this.digits[n] >= (i.digits[n] + borrow)){
					this.digits[n] -= (i.digits[n] + borrow);
					borrow = 0;
				} else{
					this.digits[n] -= (i.digits[n] + borrow - 10);
					borrow = 1;
				}
			}
		}
		return this;
	}
	
	//Multiplication helper function, multiply a HugeInteger and a single digit integer
	public HugeInteger multiplyUnit(int i, int index){
		HugeInteger zero = new HugeInteger();
		if(i == 0)
			return zero;
		else{
			HugeInteger temp = new HugeInteger();
			for(int j = 0; j < i; j++){
				temp.add(this);
			}
			HugeInteger result = new HugeInteger();
			for(int j = 0; j < 50; j++){
				result.digits[j+index] = temp.digits[j];
			}
			return result;
		}
	}
	
	//Multiplication function, using long multiplication algorithm
	public HugeInteger multiply(HugeInteger i){
		HugeInteger zero = new HugeInteger();
		if(this.equalTo(zero) || i.equalTo(zero))
			return zero;
		else{
			HugeInteger result = new HugeInteger();
			for(int j = 0; j < 20; j++){
				HugeInteger temp = new HugeInteger(this.multiplyUnit(i.digits[j], j));
				result.add(temp);
			}
			return result;
		}
	}
	
	//Division function, using long division algorithm
	public HugeInteger divide(HugeInteger i){
		HugeInteger zero = new HugeInteger();
		if(i.equalTo(zero))
			return this;
		if(this.lessThan(i))
			return zero;
		else{
			StringBuilder n = new StringBuilder();
			StringBuilder q = new StringBuilder();
			for(int j = 50; j >= 0; j--){
				n.append(this.digits[j]);
				HugeInteger temp = new HugeInteger(n.toString());
				if(!temp.lessThan(i)){
					int found = 0;
					for(int k = 1; k <= 10; k++){
						if(i.multiplyUnit(k, 0).greaterThan(temp)){
							found = k - 1;
							break;
						}
					}
					StringBuilder s = new StringBuilder();
					s.append(found);
					q.append(found);
					HugeInteger pro = new HugeInteger(i.multiply(new HugeInteger(s.toString())));
					HugeInteger diff = new HugeInteger(temp.subtract(pro));
					StringBuilder x = new StringBuilder();
					for(int l = 50; l >= 0; l--)
						x.append(diff.digits[l]);
					int id = 0;
					for(int l = 0; l < x.length(); l++){
						if(x.charAt(l) != '0'){
							id = l;
							break;
						}
					}
					if(id != 0){
						n.delete(0, n.length());
						n.append(x.substring(id, x.length()));
					}else{
						n.delete(0, n.length());
					}
				}else{
					q.append(0);
				}
			}
			return new HugeInteger(q.toString());
		}
	}
	
	//Modulo function
	public HugeInteger modulus(HugeInteger i){
		HugeInteger quotient = new HugeInteger(this.divide(i));
		HugeInteger product = new HugeInteger(quotient.multiply(i));
		HugeInteger mod = new HugeInteger(this);
		return mod.subtract(product);
	}
	
	//Power function, a^b
	public HugeInteger power(int i){
		HugeInteger result = new HugeInteger("1");
		for(int j = 0; j < i; j++)
			result.assign(this.multiply(result));
		return result;
	}
	
	//Relational function: equal
	public boolean equalTo(HugeInteger i){
		for(int n = 0; n < 100; n++)
			if(this.digits[n] != i.digits[n])
				return false;
		return true;
	}
	
	//Relational function: greater than
	public boolean greaterThan(HugeInteger i){
		for(int n = 99; n >= 0; n--){
			if(this.digits[n] > i.digits[n])
				return true;
			if(this.digits[n] < i.digits[n])
				return false;
		}
		return false;
	}
	
	//Relational function: less than
	public boolean lessThan(HugeInteger i){
		for(int n = 99; n >= 0; n--){
			if(this.digits[n] < i.digits[n])
				return true;
			if(this.digits[n] > i.digits[n])
				return false;
		}
		return false;
	}
	
	//Function used to find the greatest common divisor, using Euclidian algorithm
	public HugeInteger GCD(HugeInteger i){
		HugeInteger zero = new HugeInteger();
		if(i.equalTo(zero))
			return this;
		else{
			HugeInteger a = new HugeInteger(this);
			HugeInteger b = new HugeInteger(i);
			while(b.greaterThan(zero)){
				HugeInteger remain = new HugeInteger(a.modulus(b));

				a.assign(b);
				b.assign(remain);
			}
			return a;
		}
	}
	
}
//end of HugeInteger class
