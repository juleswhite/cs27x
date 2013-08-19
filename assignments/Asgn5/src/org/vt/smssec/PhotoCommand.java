package org.vt.smssec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

public class PhotoCommand {

	private class UploadTask extends AsyncTask<HttpUriRequest, Long, Long> {

		@Override
		protected Long doInBackground(HttpUriRequest... params) {

			try {
				HttpClient client = new DefaultHttpClient();
				client.execute(params[0]);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Long(0);
		}

	}

	private Camera camera_;
	private Context context_;
	private int previousVolume_;

	public PhotoCommand(Context ctx) {
		context_ = ctx;
	}

	public void speak() {
		camera_ = Camera.open();
		setAudioVolume(0);
		getPicture();
	}

	private void uploadPhotoToServer(File photo) {


		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context_);

		String server = prefs.getString("PhotoServer", null);
		boolean enabled = prefs.getBoolean("EnableImageUploads", true);
		boolean deluploads = prefs.getBoolean("DeleteImageUploads", true);

		String url = null; // change me
		boolean uploadEnabled = false; // change me

		if (uploadEnabled) {
			HttpPost request = new HttpPost(url);

			// File uploads must use a multipart request. This
			// code creates the basic multipart request and
			// and adds the file to it.
			MultipartEntity entity = new MultipartEntity();
			try {
				entity.addPart("thefile", new InputStreamBody(
						new FileInputStream(photo), photo.getName()));
			} catch (Exception e) {
				return;
			}

			request.setEntity(entity);
			UploadTask t = new UploadTask();
			t.execute(request);
		}
	}

	public void setAudioVolume(int volume) {
		// Get the AudioManager
		AudioManager audioManager = (AudioManager) context_
				.getSystemService(Context.AUDIO_SERVICE);
		// Set the volume of played media to maximum.
		previousVolume_ = audioManager
				.getStreamVolume(AudioManager.STREAM_SYSTEM);

		audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
	}

	public void getPicture() {
		camera_.takePicture(new Camera.ShutterCallback() {

			@Override
			public void onShutter() {
			}
		}, new PostViewCallback(), new JpegPictureCallback());
	}

	private final class PostViewCallback implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

		}

	}

	private final class JpegPictureCallback implements PictureCallback {

		public JpegPictureCallback() {

		}

		public void onPictureTaken(byte[] jpegData,
				android.hardware.Camera camera) {

			File f = getImageFile();
			try {
				FileOutputStream fout = new FileOutputStream(f);
				fout.write(jpegData);
				fout.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			setAudioVolume(previousVolume_);

			uploadPhotoToServer(f);

		}
	};

	public File getImageFile() {
		String imageFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ System.currentTimeMillis()
				+ "_"
				+ Math.random()
				+ ".tmp";

		File imageFile = new File(imageFilePath);
		return imageFile;
	}

}
