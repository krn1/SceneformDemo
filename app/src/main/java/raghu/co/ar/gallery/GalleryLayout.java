package raghu.co.ar.gallery;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import raghu.co.ar.R;
import raghu.co.ar.utils.ViewUtils;

public class GalleryLayout extends LinearLayout {

    private static int IMAGE_SIZE = 200;
    private static int IMAGE_MARGIN_SIZE = 5;

    private ArFragment fragment = null;
    private AppCompatActivity activity = null;

    public GalleryLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void addTo(ArFragment fragment) {
        this.fragment = fragment;
        this.activity = (AppCompatActivity) getContext();
    }

    // region private
    private void init() {
        addImage(R.drawable.emoji_smile, "emoji_smile_anim_a.sfb");
        addImage(R.drawable.wow, "emoji_wow_anim.sfb");
        addImage(R.drawable.star, "object_star_anim.sfb");
        addImage(R.drawable.heart, "emoji_heart_anim.sfb");
        addImage(R.drawable.cloud, "object_cloud_anim.sfb");
        addImage(R.drawable.pumpkin, "pumpkinman_anim.sfb");
        addImage(R.drawable.icecream, "icecreamman_anim_a.sfb");
    }

    private void addObject(Uri model) {
        Frame frame = fragment.getArSceneView().getArFrame();
        Point pt = ViewUtils.getScreenCenter(activity);
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if ((trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose()))) {
                    placeObject(fragment, hit.createAnchor(), model);
                    break;

                }
            }
        }
    }

    private void placeObject(ArFragment fragment, Anchor anchor, Uri model) {
        CompletableFuture<Void> renderableFuture =
                ModelRenderable.builder()
                        .setSource(fragment.getContext(), model)
                        .build()
                        .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                        .exceptionally((throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(throwable.getMessage())
                                    .setTitle("Oops error!");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return null;
                        }));
    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    private void addImage(int resId, String imageUrl) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(IMAGE_SIZE, IMAGE_SIZE);
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
        layoutParams.bottomMargin = IMAGE_MARGIN_SIZE;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(resId);
        imageView.setContentDescription(imageUrl);
        imageView.setOnClickListener(view -> {
            addObject(Uri.parse(imageUrl));
        });

        addView(imageView);
    }
    // endregion
}
