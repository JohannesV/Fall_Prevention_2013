package no.ntnu.stud.fallprevention;

/**
 * Names the risk states
 * 
 * @author Tayfun
 * 
 */
public enum RiskStatus {
	BAD_JOB, NOT_SO_OK_JOB, OK_JOB, GOOD_JOB, VERY_GOOD_JOB;
	public RiskStatus getByNumber(int i) {
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
			return OK_JOB;
		}

	}
}