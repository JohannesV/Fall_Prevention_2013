/*******************************************************************************
 * Licensed to UbiCollab.org under one or more contributor
 * license agreements.  See the NOTICE file distributed 
 * with this work for additional information regarding
 * copyright ownership. UbiCollab.org licenses this file
 * to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
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
