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
package ntnu.stud.valens.demonstration.listadapters;

import java.util.List;

import ntnu.stud.valens.demonstration.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
/**
 * This adapter is used to set the layout for a list in ContactPerson
 * 
 * @author Elias
 *
 */
public class EditContactAdapter extends ArrayAdapter<String> {

	Context context;
	List<String> alarms;

	public EditContactAdapter(Context context, List<String> alarms) {
		super(context, R.layout.entry_danger_event, alarms);
		
		this.context = context;
		this.alarms = alarms;
	} 
	/**
	 * Define the layout of the List in ContactPerson
	 * @param: int position
	 * @param: View convertView
	 * @param: ViewGroup parent
	 * @return: View rowView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.entry_danger_event, parent, false);
		
		CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
		checkBox.setText(alarms.get(position));
		
		return rowView;
	}
}
