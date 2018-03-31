package edu.bruguerolle.rocher.fanny.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base activity providing functions to open gallery OR camera at the same time.
 * Handles intent result and saves
 */
public abstract class ImagePickerBaseActivity extends AppCompatActivity {

    /**
     * The result of the image picker intent is stored as a Uri
     */
    protected Uri imageUri;
    /**
     * Temporary file used for camera
     */
    protected File tmpFile = null;

    private String fileName;
    /**
     * ImageView layout where you want the image to be displayed
     */
    private ImageView imageView;

    /**
     * The EXTRA_CODE when the image is chosen
     */
    public final int PICKER_RESULT_CODE  = 1;
    private final int PERMISSION_REQUEST = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCameraOrGalleryIntent();
        }
    }

    public abstract void onImagePicked(Uri uri);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICKER_RESULT_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }

                if (isCamera) {
                    imageUri = Uri.fromFile(tmpFile);
                } else {
                    imageUri = data == null ? null : data.getData();
                }
                imageView.setImageURI(imageUri);
                onImagePicked(imageUri);
            }
        }
    }

    /**
     * Starts an intent with both camera and gallery
     * @param imageViewId The layout ID of the ImageView you want to display the image on
     */
    public void startCameraOrGalleryIntent(int imageViewId, String fileName) {
        imageView = findViewById(imageViewId);
        this.fileName = fileName;
        this.startCameraOrGalleryIntent();
    }

    private void startCameraOrGalleryIntent() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSION_REQUEST);
        }
        else {

            tmpFile = tmpFile == null ? getNewExternalStorageFile("FANNY") : tmpFile;

            // Camera.
            final List<Intent> cameraIntents = new ArrayList<>();
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            final PackageManager packageManager = getPackageManager();
            final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

            for(ResolveInfo res : listCam) {
                final String packageName = res.activityInfo.packageName;
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
                cameraIntents.add(intent);
            }

            // Filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of filesystem options.
            final Intent pickIntent = Intent.createChooser(galleryIntent, "Select Source");

            // Add the camera options.
            pickIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
            startActivityForResult(pickIntent, PICKER_RESULT_CODE);
        }
    }

    /**
     *  Checks if external storage is available to write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     *  Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getNewExternalStorageFile(String albumName) {
        File file = null;
        if(isExternalStorageReadable() && isExternalStorageWritable()) {
            // Get the directory for the user's public pictures directory.
            File directory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), albumName);
            if (!directory.mkdirs()) {
                System.err.println("Directory not created");
            }
            file = new File(directory, fileName);
        } else {
            try {
                file = File.createTempFile("picture", "jpg", getExternalCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
