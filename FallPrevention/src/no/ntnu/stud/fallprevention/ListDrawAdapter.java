package no.ntnu.stud.fallprevention;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListDrawAdapter extends ArrayAdapter<String> {

	Context context;
	String[] values;

	public ListDrawAdapter(Context context, String[] values) {
		super(context, R.layout.events, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.events, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(values[position]);

		// Change icon based on name
		String s = values[position];

		if (s.equals("Legen din er fornøgd :)")) {
			imageView.setImageResource(R.drawable.halo);
		} else if (s.equals("Skjerp deg!")) {
			imageView.setImageResource(R.drawable.sleep);
		} else {
			imageView.setImageResource(R.drawable.smiley_icon);
		}

		return rowView;
	}
}
