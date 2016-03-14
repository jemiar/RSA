
public class HugeInteger {
	
	public int[] digits;
	
	public HugeInteger(){
		digits = new int[100];
	}
	
	public HugeInteger(HugeInteger i){
		digits = new int[100];
		for(int n = 0; n < 100; n++){
			this.digits[n] = i.digits[n];
		}
	}
	
	public HugeInteger(String s){
		digits = new int[100];
		for(int n = 0; n < s.length(); n++){
			this.digits[n] = s.charAt(s.length() - 1 - n) - '0';
		}
	}
	
	public void assign(HugeInteger i){
		for(int n = 0; n < 100; n++){
			this.digits[n] = i.digits[n];
		}
	}
	
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
	
	public HugeInteger modulus(HugeInteger i){
		HugeInteger quotient = new HugeInteger(this.divide(i));
//		System.out.println("quotient:");
//		for(int j = 0; j < 100; j++)
//			System.out.print(this.digits[j]);
//		System.out.println("");
		HugeInteger product = new HugeInteger(quotient.multiply(i));
		return this.subtract(product);
	}
	
	public boolean equalTo(HugeInteger i){
		for(int n = 0; n < 100; n++)
			if(this.digits[n] != i.digits[n])
				return false;
		return true;
	}
	
	public boolean greaterThan(HugeInteger i){
		for(int n = 99; n >= 0; n--){
			if(this.digits[n] > i.digits[n])
				return true;
			if(this.digits[n] < i.digits[n])
				return false;
		}
		return false;
	}
	
	public boolean lessThan(HugeInteger i){
		for(int n = 99; n >= 0; n--){
			if(this.digits[n] < i.digits[n])
				return true;
			if(this.digits[n] > i.digits[n])
				return false;
		}
		return false;
	}
	
	public HugeInteger GCD(HugeInteger i){
		HugeInteger zero = new HugeInteger();
		if(i.equalTo(zero))
			return this;
		else{
			HugeInteger a = new HugeInteger(this);
			HugeInteger b = new HugeInteger(i);
			while(b.greaterThan(zero)){
				HugeInteger remain = new HugeInteger(a.modulus(b));
//				System.out.println("remain:");
//				for(int j = 0; j < 100; j++)
//					System.out.print(remain.digits[j]);
//				System.out.println("");
				a.assign(b);
				b.assign(remain);
//				System.out.println("a:");
//				for(int j = 0; j < 100; j++)
//					System.out.print(a.digits[j]);
//				System.out.println("");
//				System.out.println("b:");
//				for(int j = 0; j < 100; j++)
//					System.out.print(b.digits[j]);
//				System.out.println("");
			}
			return a;
		}
	}
	
}
