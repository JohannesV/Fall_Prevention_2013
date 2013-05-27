package ntnu.stud.valens.demonstration.datastructures;

/**
 * Names the risk states
 * 
 * @author Filip, Johannes
 * 
 */
public enum RiskStatus {
	BAD_JOB(1), NOT_SO_OK_JOB(2), OK_JOB(3), GOOD_JOB(4), VERY_GOOD_JOB(5);
	private int code;
	
	RiskStatus(int i){
		code=i;
	}
	public int getCode(){
		return code;
		
	}
	public RiskStatus getStatus(int i){
		switch (i) {
		case 1:
			return BAD_JOB;
		case 2:
			return NOT_SO_OK_JOB;
		case 3:
			return OK_JOB;
		case 4:
			return GOOD_JOB;
		case 5:
			return VERY_GOOD_JOB;

		default:
			break;
		}
		if(i>5){
			return VERY_GOOD_JOB;
		}else{
			return BAD_JOB;
		}
	}
}