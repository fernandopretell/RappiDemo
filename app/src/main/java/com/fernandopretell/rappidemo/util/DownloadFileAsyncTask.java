package com.fernandopretell.rappidemo.util;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadFileAsyncTask extends AsyncTask<InputStream, Void, Boolean> {

    private final String appDirectoryName = "RappiDemo";
    private final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);
    private final String filename;
    private final String TAG = DownloadFileAsyncTask.class.getSimpleName();

    public DownloadFileAsyncTask(String name) {
        filename = name;
    }

    @Override
    protected Boolean doInBackground(InputStream... params) {
        InputStream inputStream = params[0];
        File file = new File(imageRoot, filename);

        boolean success = true;
        if (!imageRoot.exists()) {
            success = imageRoot.mkdirs();
        }

        if(!file.exists()){
            if(success){
                OutputStream output = null;
                try {
                    output = new FileOutputStream(file);

                    byte[] buffer = new byte[1024]; // or other buffer size
                    int read;

                    Log.d(TAG, "Attempting to write to: " + imageRoot + "/" + filename);
                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                        Log.v(TAG, "Writing to buffer to output stream.");
                    }
                    Log.d(TAG, "Flushing output stream.");
                    output.flush();
                    Log.d(TAG, "Output flushed.");
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                            Log.d(TAG, "Output stream closed sucessfully.");
                        }
                        else{
                            Log.d(TAG, "Output stream is null");
                        }
                    } catch (IOException e){
                        Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }




        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        Log.d(TAG, "Download success: " + result);
        // TODO: show a snackbar or a toast
    }


}
