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
package ntnu.stud.valens.demonstration;

/**
 * Contains constants used elsewhere in the application
 * 
 * @author Johannes
 */
public class Constants {
    /**
     * Definition of what is a significant change in step count, measures as the
     * ratio of STEPS_NOW / STEPS_BEFORE
     */
    public static final double GOOD_STEP_CHANGE = 1.10;
    public static final double BAD_STEP_CHANGE = 0.9;
    /**
     * Definition of what is a good amount of steps regardless of what has been
     * done before
     */
    public static final int GOOD_STEPS_NUMBER = 3000;
    public static final int BAD_STEPS_NUMBER = 1000;

    public static final double BAD_SPEED_NUMBER = 3500;
    public static final double BAD_VARI_NUMBER = 4000;
}
