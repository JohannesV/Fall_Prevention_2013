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

public class ListDrawAdapter extends ArrayAdapter<String> {

	Context context;
	List<Event> events;

	private static List<String> eventToStringList(List<Event> events) {
		List<String> strings = new ArrayList<String>();
		for (Event e : events) {
			strings.add(e.getTitle());
		}
		return strings;
	}

	public ListDrawAdapter(Context context, List<Event> events) {
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
		} else {
			imageView.setImageResource(R.drawable.smiley_icon);
		}

		return rowView;
	}
}
