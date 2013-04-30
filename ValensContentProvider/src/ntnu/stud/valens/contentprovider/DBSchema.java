package ntnu.stud.valens.contentprovider;

import android.provider.BaseColumns;

public class DBSchema {
	
	/**
	 * Table schema of raw_steps.
	 * @author fiLLLip
	 *
	 */
	public static abstract class RawSteps implements BaseColumns {
		public static final String TABLE_NAME = "raw_steps";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_SOURCE = "source";
	}

	/**
	 * Table schema of steps.
	 * @author fiLLLip
	 *
	 */
	public static abstract class Steps implements BaseColumns {
		public static final String TABLE_NAME = "steps";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
	}
	
	/**
	 * Table schema of gaits.
	 * @author fiLLLip
	 *
	 */
	public static abstract class Gaits implements BaseColumns {
		public static final String TABLE_NAME = "gaits";
		public static final String COLUMN_NAME_SPEED = "speed";
		public static final String COLUMN_NAME_VARIABILITY = "variability";
		public static final String COLUMN_NAME_START = "start";
		public static final String COLUMN_NAME_STOP = "stop";
	}

	/**
	 * Table schema of tests.
	 * @author fiLLLip
	 *
	 */
	public static abstract class Tests implements BaseColumns {
		public static final String TABLE_NAME = "tests";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_TYPE_CODE_KEY = "type_code_key";
		public static final String COLUMN_NAME_SCORE = "score";
	}

	/**
	 * Table schema of test_types.
	 * @author fiLLLip
	 *
	 */
	public static abstract class TestTypes implements BaseColumns {
		public static final String TABLE_NAME = "test_types";
		public static final String COLUMN_NAME_CODE_KEY = "code_key";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_SCORE_DESCRIPTION = "score_desc";
	}
}
