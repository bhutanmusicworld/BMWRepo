package com.bmusic.bmusicworld.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmusic.bmusicworld.R;
import com.bmusic.bmusicworld.base.BaseFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongInfoFragment extends BaseFragment {


    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.listtitle)
    TextView title;
    @BindView(R.id.selected_track_image)
    ImageView selectedTrackImage;
    @BindView(R.id.selected_track_title)
    TextView selectedTrackTitle;
    @BindView(R.id.player_control)
    ImageView playerControl;
    @BindView(R.id.play)
    LinearLayout play;
    @BindView(R.id.songtitle)
    TextView songtitle;
    @BindView(R.id.album)
    TextView album;
    @BindView(R.id.musicby)
    TextView musicby;
    @BindView(R.id.releasedate)
    TextView release;
    @BindView(R.id.lable)
    TextView lable;
    @BindView(R.id.main_audio_view)
    LinearLayout mainAudioView;
    Unbinder unbinder;

    String msicby,relase,ttle,lab,albmtitle,img;
    public static SongInfoFragment newInstance(Bundle bundle) {

        SongInfoFragment newFragment = new SongInfoFragment();
        newFragment.setArguments(bundle);

        return newFragment;
    }
    public static SongInfoFragment newInstance() {
        return new SongInfoFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle !=null)
        {
            msicby=   bundle.getString("smusic");
            relase=   bundle.getString("srelease");
            ttle=   bundle.getString("stitle");
            lab=   bundle.getString("slable");
            albmtitle=   bundle.getString("albumtitle");
            img=   bundle.getString("img");

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.song_information, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (albmtitle!=null)
            title.setText(albmtitle);
        musicby.setText(msicby);
        release.setText(relase);
        lable.setText(lab);
        Picasso.with(getActivity()).load(img).into(imageView);
        songtitle.setText(ttle);
        return view;
    }

    @Override
    protected String getTitle() {
        return "Song Information";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
