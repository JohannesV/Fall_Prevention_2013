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
	List<Contact> contacts;

	private static List<String> contactToStringList(List<Contact> contacts) {
		List<String> strings = new ArrayList<String>();
		for (Contact c : contacts) {
			strings.add(c.toString());
		}
		return strings;
	}

	public EditContactAdapter(Context context, List<Contact> contacts) {
		super(context, R.layout.entry_danger_event, contactToStringList(contacts));
		
		this.context = context;
		this.contacts = contacts;
	} 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.entry_danger_event, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(contacts.get(position).toString());
		ToggleButton toggleButton = (ToggleButton) rowView.findViewById(R.id.toggleButton1);
		toggleButton.setSelected(false);



		return rowView;
	}
}