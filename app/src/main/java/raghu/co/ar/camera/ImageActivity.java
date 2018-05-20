package raghu.co.ar.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import raghu.co.ar.R;
import raghu.co.ar.utils.FileUtils;

public class ImageActivity extends AppCompatActivity {

    private static final String KEY_BITMAP_ID = "bitmap_img";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image)
    ImageView imageView;

    public static void start(Activity context, Uri bitmap) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(KEY_BITMAP_ID, bitmap.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String uri = getIntent().getStringExtra(KEY_BITMAP_ID);
        imageView.setImageURI(FileUtils.getPhotoUri(FileUtils.filename, this));
    }
}
