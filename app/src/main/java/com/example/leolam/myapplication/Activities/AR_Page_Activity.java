package com.example.leolam.myapplication.Activities;

import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;



import com.example.leolam.myapplication.R;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


import java.util.List;
import java.util.concurrent.CompletableFuture;


import uk.co.appoly.arcorelocation.LocationScene;

public class AR_Page_Activity extends AppCompatActivity {

    private ArFragment fragment;
    //private ArSceneView arSceneView;

    private ModelRenderable sphereRenderable;
    private ModelRenderable andyRenderable;
    private ModelRenderable exampleLayoutRenderable;


    // True once scene is loaded
    private boolean hasFinishedLoading = false;

    // True once the scene has been placed.
    private boolean hasPlacedSolarSystem = false;

    private GestureDetector gestureDetector;
    private Snackbar loadingMessageSnackbar = null;

    private LocationScene locationScene;
    private boolean installRequested;

    private static final String TAG = "AR_Page_Activity";
    private BaseArFragment arFragment;

    private final Paint paint = new Paint();
    private boolean enabled;

    private PointerDrawable pointer = new PointerDrawable();
    private boolean isTracking;
    private boolean isHitting;
    private CompletableFuture<ModelRenderable> future;
    private ModelRenderable redSphereRenderable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar__page);

        //rSceneView = findViewById(R.id.ar_scene_view);

        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


        fragment.getArSceneView().getScene().setOnUpdateListener(frameTime -> {
            fragment.onUpdate(frameTime);
            onUpdate();
        });


        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
                            redSphereRenderable =
                                    ShapeFactory.makeSphere(0.1f, new Vector3(0.0f, 0.15f, 0.0f), material); });


        Node node = new Node();

        ModelRenderable.builder()
                .setSource(this, Uri.parse("sphere.sfb"))
                .build()
                .thenAccept(renderable -> redSphereRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            node.setRenderable(sphereRenderable);
                            node.setLocalPosition(new Vector3(0.5f, 0f, 0f));
                            return null;
                        });


        node.setParent(fragment.getArSceneView().getScene());
        node.setRenderable(sphereRenderable);
        node.setLocalPosition(new Vector3(0.5f, 0f, 0f));


        initializeGallery();

    }


    private void onUpdate() {
        boolean trackingChanged = updateTracking();
        View contentView = findViewById(android.R.id.content);
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer);
            } else {
                contentView.getOverlay().remove(pointer);
            }
            contentView.invalidate();
        }

        if (isTracking) {
            boolean hitTestChanged = updateHitTest();
            if (hitTestChanged) {
                pointer.setEnabled(isHitting);
                contentView.invalidate();
            }
        }
    }

    private boolean updateTracking() {
        Frame frame = fragment.getArSceneView().getArFrame();
        boolean wasTracking = isTracking;
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() == TrackingState.TRACKING;
        return isTracking != wasTracking;
    }

    private boolean updateHitTest() {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        boolean wasHitting = isHitting;
        isHitting = false;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    isHitting = true;
                    break;
                }
            }
        }
        return wasHitting != isHitting;
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth()/2, vw.getHeight()/2);
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView atkins = new ImageView(this);
        atkins.setImageResource(R.drawable.atkinsuncsign);
        atkins.setContentDescription("atkins");
        atkins.setOnClickListener(view ->{addObject(Uri.parse("AtkinsSign.sfb"));});
        gallery.addView(atkins);

        ImageView cone = new ImageView(this);
        cone.setImageResource(R.drawable.coneuncsign);
        cone.setContentDescription("cone");
        cone.setOnClickListener(view ->{addObject(Uri.parse("ConeSign.sfb"));});
        gallery.addView(cone);

        ImageView sac = new ImageView(this);
        sac.setImageResource(R.drawable.sacuncsign);
        sac.setContentDescription("sac");
        sac.setOnClickListener(view ->{addObject(Uri.parse("SACsign.sfb"));});
        gallery.addView(sac);


        ImageView igloo = new ImageView(this);
        igloo.setImageResource(R.drawable.igloo_thumb);
        igloo.setContentDescription("igloo");
        igloo.setOnClickListener(view ->{addObject(Uri.parse("igloo.sfb"));});
        gallery.addView(igloo);

        ImageView sphere = new ImageView(this);
        sphere.setImageResource(R.drawable.yellowballs);
        sphere.setContentDescription("sphere");
        sphere.setOnClickListener(view ->{addObject(Uri.parse("sphere.sfb"));});
        gallery.addView(sphere);
    }

    private void addObject(Uri model) {
        Frame frame = fragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage())
                                    .setTitle("Codelab error!");
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


}
