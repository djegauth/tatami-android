package tatami.android.widget;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import tatami.android.R;
import tatami.android.model.Status;
import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.fedorvlasov.lazylist.ImageLoader;

/**
 * <p>
 * Own implementation of {@link ArrayAdapter} in order to handle {@link Status}.
 * </p>
 * 
 * <p>
 * Please use {@link StatusesAdapter}{@link #add(Status)} or
 * {@link StatusesAdapter}{@link #addAll(java.util.Collection)} in order to
 * populate {@link Status}.
 * </p>
 * 
 * @author pariviere
 */
public class StatusesAdapter extends ArrayAdapter<Status> {
	private ImageLoader imageLoader;
	private HtmlSpanner htmlSpanner;

	private static class ViewHolder {
		public ImageView avatar;
		public TextView status;
		public TextView info;
	}

	public StatusesAdapter(Activity context) {
		super(context, R.layout.list_status);
		imageLoader = new ImageLoader(context);
		htmlSpanner = new HtmlSpanner();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_status, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
			viewHolder.status = (TextView) rowView.findViewById(R.id.status);
			viewHolder.info = (TextView) rowView.findViewById(R.id.info);

			rowView.setTag(viewHolder);
		}

		Status status = getItem(position);
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();

		buildStatusTextView(viewHolder.status, status);
		buildAvatarTextView(viewHolder.avatar, status);
		buildInfoTextView(viewHolder.info, status);

		return rowView;
	}

	private TextView buildInfoTextView(TextView infoTextView, Status status) {

		infoTextView.setTextSize(12);

		String html = status.getFirstName() + " "
				+ status.getLastName();
		
		infoTextView.setText(html);

		return infoTextView;
	}

	private ImageView buildAvatarTextView(ImageView avatarImageView,
			Status status) {

		String gravatar = status.getGravatar();

		String url = "http://www.gravatar.com/avatar/" + gravatar
				+ "?s=80&d=mm";

		imageLoader.DisplayImage(url, avatarImageView);

		return avatarImageView;
	}

	private TextView buildStatusTextView(TextView statusTextView, Status status) {
		statusTextView.setTextSize(14);

		String html = status.getHtmlContent();

		SpannableStringBuilder ssb = (SpannableStringBuilder) htmlSpanner
				.fromHtml(html);

		UsernameDecorator.getInstance().decorate(ssb);
		TagDecorator.getInstance().decorate(ssb);

		statusTextView.setText(ssb, BufferType.SPANNABLE);

		return statusTextView;
	}

}