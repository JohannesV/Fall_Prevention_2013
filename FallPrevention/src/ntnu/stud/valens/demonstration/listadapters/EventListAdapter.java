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


import java.util.ArrayList;
import java.util.List;

import ntnu.stud.valens.demonstration.R;
import ntnu.stud.valens.demonstration.datastructures.Event;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  This adapter is used to set the layout for a list in EventList
 *  
 * @author Elias
 *
 */
public class EventListAdapter extends ArrayAdapter<String> {

	Context context;
	List<Event> events;
	/**
	 * 
	 * @param events: List for accessing the events
	 * @return the new version of the list.
	 */
	private static List<String> eventToStringList(List<Event> events) {
		List<String> strings = new ArrayList<String>();
		for (Event e : events) {
			strings.add(e.getTitle());
		}
		return strings;
	}

	public EventListAdapter(Context context, List<Event> events) {
		super(context, R.layout.entry_eventlist, eventToStringList(events));
		
		this.context = context;
		this.events = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.entry_eventlist, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(events.get(position).getTitle());

		// Set the correct icon
		String s = events.get(position).getIcon();

		if (s.equals("halo")) {
			imageView.setImageResource(R.drawable.halo);
		} else if (s.equals("sleep")) {
			imageView.setImageResource(R.drawable.sleep);
		} else if (s.equals("warning")) {
            imageView.setImageResource(R.drawable.warning);
        }
		else {
			imageView.setImageResource(R.drawable.smiley_icon);
		}

		return rowView;
	}
}
