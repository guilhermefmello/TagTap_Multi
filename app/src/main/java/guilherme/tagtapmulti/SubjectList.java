package guilherme.tagtapmulti;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Destino Dublin on 02-Jul-17.
 */

public class SubjectList extends ArrayAdapter<Subject> {

    private Activity context;
    List<Subject> subjects;

    public SubjectList(Activity context, List<Subject> subjects) {
        super(context, R.layout.layout_subject_list, subjects);
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_subject_list, null, true);
        TextView textViewSubject = (TextView) listViewItem.findViewById(R.id.textViewSubject);
        TextView textViewRating = (TextView) listViewItem.findViewById(R.id.textViewRating);

        Subject subject = subjects.get(position);
        textViewSubject.setText("Subject: " + subject.getSubject());
        textViewRating.setText("Priority: " + String.valueOf( subject.getPriority()));
        return listViewItem;
    }
}
