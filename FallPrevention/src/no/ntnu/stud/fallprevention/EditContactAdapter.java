package no.ntnu.stud.fallprevention;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EditContactAdapter extends ArrayAdapter<String> {

	Context context;
	List<Event> events;

	private static List<String> eventToStringList(List<Event> events) {
		List<String> strings = new ArrayList<String>();
		for (Event e : events) {
			strings.add(e.getTitle());
		}
		return strings;
	}

	public EditContactAdapter(Context context, List<Event> events) {
		super(context, R.layout.activity_danger_events, eventToStringList(events));
		
		this.context = context;
		this.events = events;
	} 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.activity_danger_events, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(events.get(position).getTitle());
		ToggleButton toggleButton = (ToggleButton) rowView.findViewById(R.id.toggleButton1);
		toggleButton.setSelected(false);



		return rowView;
	}
}