package sk.upjs.ics.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.ecotastic.android.camerautil.lib.CameraIntentHelperActivity;
import de.ecotastic.android.camerautil.util.BitmapHelper;

public class TakePhotoActivity extends CameraIntentHelperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startCameraIntent();
    }
//zxczxc

    @Override
    protected void onPhotoUriFound() {
        super.onPhotoUriFound();

        //Delete photo in second location (if applicable)
        if (this.preDefinedCameraUri != null && !this.preDefinedCameraUri.equals(this.photoUri)) {
            BitmapHelper.deleteImageWithUriIfExists(this.preDefinedCameraUri, this);
        }
        //Delete photo in third location (if applicable)
        if (this.photoUriIn3rdLocation != null) {
            BitmapHelper.deleteImageWithUriIfExists(this.photoUriIn3rdLocation, this);
        }
        finishWithResult(photoUri.toString());
    }

    private void finishWithResult(String uri)
    {
        Bundle conData = new Bundle();
        conData.putString("param_result", uri);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
