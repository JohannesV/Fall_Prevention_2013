package no.ntnu.stud.fallprevention;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class EditContactAdapter extends ArrayAdapter<String> {

	Context context;
	List<String> alarms;

	public EditContactAdapter(Context context, List<String> alarms) {
		super(context, R.layout.entry_danger_event, alarms);
		
		this.context = context;
		this.alarms = alarms;
	} 

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